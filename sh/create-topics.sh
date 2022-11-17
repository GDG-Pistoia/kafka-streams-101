#!/usr/bin/env bash

./bin/kafka-topics.sh --create --partitions 6 --topic stocks --bootstrap-server localhost:9092
./bin/kafka-topics.sh --create --partitions 6 --topic logarithmic-returns --bootstrap-server localhost:9092