package com.pro.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.pro.repo")
public class AppConfig {
    // Other bean definitions
}
