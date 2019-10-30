package com.symverse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/keystore")
public class KeyStoreConvertController {

	static final Logger logger = LoggerFactory.getLogger(KeyStoreConvertController.class);

	@Value("${spring.application.name}")
	String serviceMode;

    @GetMapping("/convert")
    public String homePage(Model model) {

        model.addAttribute("appName", serviceMode);
        return "keystore/convert";
    }
}
