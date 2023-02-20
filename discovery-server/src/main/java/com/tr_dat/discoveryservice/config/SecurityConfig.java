package com.tr_dat.discoveryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {

  @Value("${eureka.username}")
  private String username;
  @Value("${eureka.password}")
  private String password;

  @Bean
  public InMemoryUserDetailsManager userDetailService() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    String encodedPassword = encoder.encode(password);

    UserDetails user = User.withUsername(username)
      .password(encodedPassword)
      .authorities("USER")
      .build();

    return new InMemoryUserDetailsManager(user);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((authz) -> authz
        .anyRequest().authenticated()
      ).csrf().disable()
      .httpBasic();
    return http.build();
  }
}
