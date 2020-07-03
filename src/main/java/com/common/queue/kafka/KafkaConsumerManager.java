package com.common.queue.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerManager {

	private static final String KAFKA_SERVER_URL = "localhost:9092";

	private Consumer<String, CommonMessage> consumer; 
	public KafkaConsumerManager() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test1");
		props.put("enable.auto.commit", "true");
		//props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "com.common.queue.kafka.KafkaMessageDeserializer");
		this.consumer = new KafkaConsumer<>(props);
	}

	public void readMessage(String topic) {
		
		consumer.subscribe(Arrays.asList(topic));
		System.out.println("*******start  reading***********");
		while (true) {
			ConsumerRecords<String, CommonMessage> records = consumer.poll(Duration.ofSeconds(600));
			//consumer.committed(records.()
			System.out.println("Size of message read:" + records.count());
			for (ConsumerRecord<String, CommonMessage> record : records) {
				System.out.println(String.format("*****Message Read: offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value().getContent()));
			}
				
		}
	}
	
	public class ConsumerWorker implements Runnable {
		
		private final KafkaConsumer<String, KafkaMessageManager> consumer;
		private final List<String> topics;
		private final int id;
		
		private AtomicInteger count;

		public ConsumerWorker(int id, String groupId, List<String> topics, ConcurrentHashMap c, AtomicInteger count) {
			this.id = id;
			this.topics = topics;
			
			this.count = count;
			Properties props = new Properties();
			props.put("bootstrap.servers", "localhost:9092");
			props.put("group.id", groupId);
			props.put("enable.auto.commit", "false");
			props.put("auto.commit.interval.ms", "1000");
			props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
			props.put("value.deserializer", "com.simplelibrary.framework.KafkaMessageDeserializer");
			consumer = new KafkaConsumer<String, KafkaMessageManager>(props);
		}

		@Override
		public void run() {
			try {
				consumer.subscribe(topics);

				while (true) {
					ConsumerRecords<String, KafkaMessageManager> records = consumer.poll(Long.MAX_VALUE);
					this.count.addAndGet(records.count());
					for (ConsumerRecord<String, KafkaMessageManager> record : records) {
						System.out.printf("offset = %d,Partition = %d,  key = %s, value = %s%n", record.offset(),record.partition(), record.key(), record.value().toString());
						
					}
					System.out.println("*********************");
					System.out.println("********"+this.count.get()+"*************");
					System.out.println("*********************");
				}
			} catch (WakeupException e) {
				// ignore for shutdown
			} finally {
				consumer.close();
			}
		}

		public void shutdown() {
			consumer.wakeup();
		}

	}



}
