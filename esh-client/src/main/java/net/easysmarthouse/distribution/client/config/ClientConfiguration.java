package net.easysmarthouse.distribution.client.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ClientConfiguration {

    @Bean(name = "HazelcastClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        //unlimited connections (try to reconnect)
        clientConfig.getNetworkConfig().setConnectionAttemptLimit(0);
        return clientConfig;
    }

    @Bean(name = "ClientInstance", destroyMethod = "shutdown")
    public HazelcastInstance clientInstance(ClientConfig clientConfig) throws Exception {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

}
