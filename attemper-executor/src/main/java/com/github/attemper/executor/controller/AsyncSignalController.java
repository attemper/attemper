package com.github.attemper.executor.controller;

import com.github.attemper.executor.service.core.AsyncSignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * async job call this controller to notify the waited execution id
 */
@RestController
public class AsyncSignalController {

    @Autowired
    private AsyncSignalService service;

    @PostMapping("signal")
    public String signal(@RequestBody Map<String, Object> map) {
        return service.signal(map);
    }
}
