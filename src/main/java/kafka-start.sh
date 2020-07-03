#!/bin/bash

echo hello world 

cd /Users/dongphan/Documents/server/kafka_2.11-2.2.0/
echo Start ZooKeeper
sh bin/zookeeper-server-start.sh config/zookeeper.properties

cd /Users/dongphan/Documents/server/kafka_2.11-2.2.0/
echo Start Kafka
sh bin/kafka-server-start.sh config/server.properties 