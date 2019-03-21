package com.thor.core.service.tool;

import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class ToolService {

    public String[] listTimeZone() {
        return TimeZone.getAvailableIDs();
    }
}
