package com.greatmrpark.helper;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class HelperApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
        new SpringApplicationBuilder()
        .bannerMode(Banner.Mode.CONSOLE)
        .sources(HelperApplication.class)
        .run(args);
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HelperApplication.class);
    }
    
}
