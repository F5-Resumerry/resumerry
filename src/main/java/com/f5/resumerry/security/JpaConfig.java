package com.f5.resumerry.security;

import com.f5.resumerry.Member.service.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
}

