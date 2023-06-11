package com.deliverengine.deliver.helper;

import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

public class MvcResultParser<T> {

    private MvcResultParser() {
    }

    public static <T> T resultAsObject(MvcResult mvcResult, Class<T> clazz) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        return new ObjectMapper().readValue(content, clazz);
    }
}
