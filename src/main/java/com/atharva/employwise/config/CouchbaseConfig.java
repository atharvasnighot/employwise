package com.atharva.employwise.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CouchbaseConfig {

    @Value("${spring.couchbase.bootstrap-hosts}")
    private String connectionString;

    @Value("${spring.couchbase.bucket.user}")
    private String username;

    @Value("${spring.couchbase.bucket.password}")
    private String password;

    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    @Bean
    public Cluster couchbaseCluster() {
        log.debug("Connecting to Couchbase cluster at {}", connectionString);
        return Cluster.connect(connectionString, username, password);
    }

    @Bean
    public Bucket couchbaseBucket(Cluster cluster) {
        log.debug("Opening bucket {}", bucketName);
        return cluster.bucket(bucketName);
    }

}
