package com.example.postgres.config;

import org.jooq.ExecuteListenerProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.postgres.listener.LoggingExecuteListener;

@Configuration
public class RootConfig {

    @Bean
    public ExecuteListenerProvider executeListenerProvider() {
        return new DefaultExecuteListenerProvider(new LoggingExecuteListener());
    }

}
