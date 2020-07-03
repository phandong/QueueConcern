package com.common.queue.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerManager {

	private static final String KAFKA_SERVER_URL = "localhost:9092";
	private KafkaProducer<String, CommonMessage> producer;
	private String topic;

	public KafkaProducerManager() {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "com.common.queue.kafka.KafkaMessageSerializer");
		this.producer = new KafkaProducer<>(props);
		
	}

	public static void sendMessage() {

		ExecutorService executor = Executors.newFixedThreadPool(3);

		final List<ProducerWorker> producerWorkers = new ArrayList<ProducerWorker>();
		for (int i = 0; i < 1; i++) {
			ProducerWorker producerWorker = new ProducerWorker(KAFKA_SERVER_URL, "topic1");
			producerWorkers.add(producerWorker);
			executor.submit(producerWorker);
		}
	}
	

	public  void sendMessage(String topic, String message) {
		CommonMessage mes = new CommonMessage();
		mes.setContent(message);
		for(int i = 0; i < 1000000; i++) {
			this.producer.send(new ProducerRecord<String,CommonMessage>(topic, mes), new Callback() {
				@Override
				public void onCompletion(RecordMetadata arg0, Exception arg1) {
					// TODO Auto-generated method stub
					System.out.println("Send to topic: "+arg0.topic());
				}
			});
		}
		producer.flush();
	}
	
	public static void main(String[] args) {
		KafkaProducerManager managerWrite = new KafkaProducerManager();
		managerWrite.sendMessage("topic1","Fdfs");
		//KafkaConsumerManager managerRead = new KafkaConsumerManager();
		//managerRead.readMessage();
	}

	
}
