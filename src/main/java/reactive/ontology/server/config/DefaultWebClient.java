package reactive.ontology.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class DefaultWebClient {

    @Value("${webclient.timeout}")
    private Integer timeout;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofMillis(timeout));
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return WebClient.builder()
            .uriBuilderFactory(factory)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}
