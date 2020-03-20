package com.greatmrpark.web;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.greatmrpark.app.GmpAppApplication;

@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class GmpWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
        new SpringApplicationBuilder().bannerMode(Banner.Mode.CONSOLE)
        .sources(GmpAppApplication.class, GmpWebApplication.class)
        .run(args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GmpAppApplication.class, GmpWebApplication.class);
    }
    
}
