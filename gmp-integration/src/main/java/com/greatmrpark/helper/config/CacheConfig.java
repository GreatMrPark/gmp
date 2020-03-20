package com.greatmrpark.helper.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.greatmrpark.common.utils.CustomKeyGenerator;


/**
 * <p>
 * <pre>
 * 
 * CacheConfig.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 6. 14.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 6. 14.
 * @version 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {
   
    /**
     * 캐시복합키생성기
     * @return
     */
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }    
}
