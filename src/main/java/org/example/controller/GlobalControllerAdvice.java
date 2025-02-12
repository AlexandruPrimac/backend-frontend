package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final Environment env;

    @Autowired
    public GlobalControllerAdvice(Environment env) {
        this.env = env;
    }

    @ModelAttribute("activeProfile")
    public String getActiveProfile() {
        String[] activeProfiles = env.getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : "default";
    }
}