package com.addplus.server.web.serviceimpl.web.demomodule;


import com.addplus.server.api.service.web.authoritymodule.HelloWebService;
import org.springframework.stereotype.Service;

@Service
public class HelloWebServiceImpl implements HelloWebService {


    @Override
    public String sayHelloWeb(String name) {
        return "Hello " + name;
    }
}
