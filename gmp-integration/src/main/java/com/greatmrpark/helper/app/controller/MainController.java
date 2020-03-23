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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@Controller
public class MainController {
    
    private Boolean b = false;
    
    @FXML
    private Button btn;
    
    @FXML
    public void btnClick(ActionEvent event) {
        if(b) {
            btn.setText("You Clicked Me.");
            b = true;
        }
        else {
            btn.setText("Click Me Please!");
            b = false;
        }
    }
}
