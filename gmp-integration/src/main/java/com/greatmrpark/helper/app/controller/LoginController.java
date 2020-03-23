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
 *  greatmrpark 2020. 3. 20.
 *
 */	
package com.greatmrpark.helper.app.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

    private Gson gson = new GsonBuilder().create();
    
    private Stage primaryStage;

    @FXML
    private TextField txtID;

    @FXML
    private PasswordField txtPWD;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnCancel;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    @FXML
    public void clickLogin(ActionEvent event) throws IOException {
        
        String id  = txtID.getText().toString();
        String pwd = txtPWD.getText().toString();

        log.debug("아이디 : {}", id);
        log.debug("비밀번호 : {}", pwd);
        
        // FXML에 의한 Pane 생성
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/app/DialogView.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Scene 생성
        Scene scene = new Scene(page);
        
        // dialog Stage 생성
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Login Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        
        dialogStage.setScene(scene);
        
        // 다이아로그 Controller에 data 전달
        DialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPerson(id, pwd);
        
        dialogStage.showAndWait();        
    }

    @FXML
    public void clickCancel(ActionEvent event) {
        System.exit(0);
    }
}
