package fr.lmarchau.proto.fees.config;

import fr.lmarchau.proto.fees.repository.IpStackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfiguration {

    @Bean
    public IpStackRepository ipStackRepository(RestTemplateBuilder restTemplateBuilder, @Value("${app.ipstack.api.uri}") String apiUri, @Value("${app.ipstack.api.key}") String apiKey) {
        return new IpStackRepository(restTemplateBuilder, apiUri, apiKey);
    }
}
