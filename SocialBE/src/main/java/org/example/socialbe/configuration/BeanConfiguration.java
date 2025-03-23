package org.example.socialbe.configuration;

import org.example.socialbe.exception.RestTemplateResponseErrorHandler;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class BeanConfiguration {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${timeout.in.seconds:120}")
    private Integer timeoutInSeconds;

    @Bean
    public RestTemplate restTemplate() {
        Duration timeout = Duration.ofMillis(timeoutInSeconds * 1000);

        RestTemplate restTemplate = this.restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .setConnectTimeout(timeout)
                .setReadTimeout(timeout)
                .requestFactory(CustomClientHttpRequestFactory.class)
                .build();

        return restTemplate;
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000", "http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowCredentials(true);
        }
    }

    @Bean
    public ModelMapper defaultMapper() {
        ModelMapper m = new ModelMapper();
        m.getConfiguration()
                .setCollectionsMergeEnabled(false)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return m;
    }

}
