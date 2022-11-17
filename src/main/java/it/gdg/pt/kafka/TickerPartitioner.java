package it.gdg.pt.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.streams.processor.StreamPartitioner;

import java.util.List;
import java.util.Map;

@Slf4j
public class TickerPartitioner implements Partitioner, StreamPartitioner<String, Object> {

    private final DefaultPartitioner defaultPartitioner = new DefaultPartitioner();
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        if (key instanceof String) {
            String k = (String) key;
            final int partition = partition(topic, k, value, partitions.size());
            return partition;
        } else {
            log.warn("warn=usingDefaultPartitioner topic={} keyClass=\"{}\" key={}",
                    topic, key == null ? null : key.getClass().getSimpleName(), key);
            return defaultPartitioner.partition(topic, key, keyBytes, value, valueBytes, cluster);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {

    }

    @Override
    public Integer partition(String topic, String key, Object value, int numPartitions) {
        return key.split("-")[0].hashCode() % numPartitions;
    }
}
