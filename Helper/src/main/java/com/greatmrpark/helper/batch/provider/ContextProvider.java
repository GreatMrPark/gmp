package com.greatmrpark.helper.batch.provider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * <p>
 * <pre>
 * 
 * ContextProvider.java
 * 개정이력(Modification Information)·
 * 수정일   수정자    수정내용
 * ------------------------------------
 * 2019. 7. 1.    greatmrpark     최초작성
 * </pre>
 *  
 * @author greatmrpark
 * @since 2019. 7. 1.
 * @version 1.0.0
 */
@Component
public class ContextProvider implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      CONTEXT = applicationContext;
    }

    /**
     * Get a Spring bean by type.
     **/
    public static <T> T getBean(Class<T> beanClass) {
      return CONTEXT.getBean(beanClass);
    }

    /**
     * Get a Spring bean by name.
     **/
    public static Object getBean(String beanName) {
      return CONTEXT.getBean(beanName);
    }
}
