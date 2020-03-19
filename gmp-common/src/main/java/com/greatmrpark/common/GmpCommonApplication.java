package com.greatmrpark.common;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.greatmrpark.common.utils.CustomKeyGenerator;

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

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    /**
     * 캐시복합키생성기
     * @return
     */
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
