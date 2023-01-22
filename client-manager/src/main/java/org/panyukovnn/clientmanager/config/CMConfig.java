package org.panyukovnn.clientmanager.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CMConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean
    public IMap<String, String> lockMap(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("LOCK_MAP");
    }
}
