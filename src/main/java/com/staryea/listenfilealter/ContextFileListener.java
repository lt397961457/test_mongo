package com.staryea.listenfilealter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextFileListener implements ApplicationListener<ContextRefreshedEvent> {
  
    @Autowired
    private FileMonitor fileMonitor;

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
            try {
                fileMonitor.monitor();
                fileMonitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//
//            try {
//                fileMonitor.monitor();
//                fileMonitor.start();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//    }
  
}  