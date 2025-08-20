package com.practice.expense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.practice.expense.filter.AuthFilter;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}
	
	//Registering the AuthFilter to intercept requests to /api/expenses/*
	// and check for valid JWT token in the Authorization header
	
	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/expenses/*");
		return registrationBean;
	}
	
	// Registering CORS filter to allow cross-origin requests
	// This is useful for development purposes when the frontend and backend are on different ports
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		System.out.println("CORS Filter is being registered");
		FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>();
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		source.registerCorsConfiguration("/**", config);
		registrationBean.setFilter(new CorsFilter(source));
		registrationBean.setOrder(0);
		return registrationBean;
	}
	//
	@Bean
	public Docket swaggerConfiguration() {
		// Return a prepared Docket instance
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
//				.paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("com.practice.expense"))
				.build();
	}

	// Spring Security configuration to allow access to certain endpoints without authentication
		
	@Configuration
	public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeRequests()
        .antMatchers(
            "/actuator/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/webjars/**","/api/**"
        ).permitAll()
        .anyRequest().authenticated()
        .and().csrf().disable();
    }
}

}
