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

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

@Controller
public class WebViewController {
    
    @FXML
    private WebView webView;

    @FXML
    private void initialize() {
        WebEngine engine = webView.getEngine();
        engine.load("http://localhost:9999/apilist/index.html");
    }
}
