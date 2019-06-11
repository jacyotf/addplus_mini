package com.addplus.server.web.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AESDemoContraller {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
