package br.com.finnet.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application-messages.yml", encoding = "UTF-8")
@ConfigurationProperties
@Getter
@Setter
public class ApplicationMessages {

    private String existingPayment;

    private String notFoundPayment;

}
