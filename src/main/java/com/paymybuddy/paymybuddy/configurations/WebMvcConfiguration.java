package com.paymybuddy.paymybuddy.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * This class extends the {@link WebMvcConfigurer} interface to define methods to customise the configuration for Spring
 * MVC enabled via the {@link EnableWebMvc} annotation.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * Defines the resources to be served to a static path.
     *
     * @param registry
     *         create resources handlers.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * Defines the view to be served to a static path.
     *
     * @param registry
     *         assists with the registration of simple automated controllers pre-configured with status code and/or a
     *         view.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/").setViewName("login");
    }
}