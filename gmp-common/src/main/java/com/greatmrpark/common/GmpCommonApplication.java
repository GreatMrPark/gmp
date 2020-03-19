package com.greatmrpark.common;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories("com.greatmrpark.common.repository")
@EntityScan("com.greatmrpark.common.model")
@EnableTransactionManagement
@SpringBootApplication
public class GmpCommonApplication {

	public static void main(String[] args) {
        new SpringApplicationBuilder()
        .bannerMode(Banner.Mode.CONSOLE)
        .sources(GmpCommonApplication.class)
        .run(args);
	}
}
