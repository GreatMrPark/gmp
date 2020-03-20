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

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DialogController {

    private Gson gson = new GsonBuilder().create();
    
    private Stage dialogStage;

    @FXML
    private TextArea txtArea;
    
    @FXML
    private Button btnConfirm;
    
    @FXML
    public void clickConfirm(ActionEvent event) {
        dialogStage.close();
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    public void setPerson(String id, String pwd) {
        String value = "";
        value += "아이디 : " + id;
        value += "\n";
        value += "비밀번호 : " + pwd;
        txtArea.setText(value);
    }
}
