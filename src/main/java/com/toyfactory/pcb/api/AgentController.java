package com.toyfactory.pcb.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toyfactory.pcb.model.Version;

@RestController
public class AgentController {

    @RequestMapping("/version")
    public Version version(@RequestParam(value="name", defaultValue="World") String name) {
        return new Version("20161205");
    }
}
