package com.fitness.controller;

import com.fitness.controller.main.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statics")
public class StaticsCont extends Attributes {
    @GetMapping
    public String Statics(Model model) {
        AddAttributesStatics(model);
        return "statics";
    }
}
