/*
 *  Copyright (c) 2020 Great Mr. Park
 *  All right reserved.
 *  This software is the confidential and proprietary information of Great Mr. Park.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with Great Mr. Park.
 *
 *  Revision History
 *  Author Date Description
 *  ------------------ -------------- ------------------
 *  greatmrpark 2020. 3. 18.
 *
 */	
package com.greatmrpark.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.greatmrpark.helper.app.controller.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@EnableJpaRepositories("com.greatmrpark.common.repository")
@EntityScan("com.greatmrpark.common.model")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class GmpHelperApplication extends Application {
    
    private ConfigurableApplicationContext springContext;
    private LoginController controller;
    private Parent root;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {

        springContext = SpringApplication.run(GmpHelperApplication.class);
        controller = springContext.getBean(LoginController.class);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/fxml/LoginView.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        
        root = fxmlLoader.load();
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("GMPHelper For Dev Of Great Mr. Park.");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
