package com.staryea.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class TestCommand1 extends HystrixCommand<String> {

    private final String name;
    protected TestCommand1(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("FirstGroup"));
        this.name = name;
    }

    protected String run() throws Exception {
        Thread.sleep(10000);
        return "this is success!";
    }

    @Override
    protected String getFallback() {
        return"this is fallback msg!!!";
    }

}
