package com.nlmk.adp.config;

import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Конфигурация для Spring Security.
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,
        securedEnabled = true)
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
@ConditionalOnProperty(value = "keycloak.enabled", havingValue = "true")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .and()
                .formLogin().permitAll()
                ;
    }

}
