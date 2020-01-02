package com.pawban.communicator.config;

import com.pawban.communicator.converter.StringToAccessRequestRoleConverter;
import com.pawban.communicator.converter.StringToAccessRequestStatusConverter;
import com.pawban.communicator.converter.StringToChatRoomStatusConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableScheduling
@Configuration
@Getter
public class CoreConfiguration implements WebMvcConfigurer {

    @Value("${communicator.app.restricted.usernames}")
    private List<String> restrictedUserNames;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToChatRoomStatusConverter());
        registry.addConverter(new StringToAccessRequestRoleConverter());
        registry.addConverter(new StringToAccessRequestStatusConverter());
    }

}
