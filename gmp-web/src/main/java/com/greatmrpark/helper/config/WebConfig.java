package com.greatmrpark.helper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport{
    
    @Value("${gmp.file.crawler.contents}")
    private String crawlerContents;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:static/");
        registry.addResourceHandler("/crawler/contents/**").addResourceLocations("file://"+crawlerContents+"/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry ) {
        registry.addViewController( "/" ).setViewName( "redirect:/html/dashboard.html" );
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers( registry );
    }
}
