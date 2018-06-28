package com.staryea.listenfilealter;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class FileMonitor {
      
    private FileAlterationMonitor monitor = null;
      
    private String filePath;  
    private FileAlterationListener alterationListener;
    private long intervalTime = 10000L;  
      
    public FileMonitor(long interval) throws Exception {    
        monitor = new FileAlterationMonitor(interval);    
    }  
    public FileMonitor() throws Exception {    
        monitor = new FileAlterationMonitor(intervalTime);    
    }  
      
    public void monitor(String path, FileAlterationListener listener) {    
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);    
        observer.addListener(listener);    
    }  
    public void monitor() {    
        FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));    
        monitor.addObserver(observer);    
        observer.addListener(alterationListener);    
    }  
    public void stop() throws Exception{    
        monitor.stop();    
    }    
    public void start() throws Exception {    
        monitor.start();    
    }  
      
      
  
    public String getFilePath() {  
        return filePath;  
    }  
  
    public void setFilePath(String filePath) {  
        this.filePath = filePath;  
    }  
  
    public FileAlterationListener getAlterationListener() {  
        return alterationListener;  
    }  
  
    public void setAlterationListener(FileAlterationListener alterationListener) {  
        this.alterationListener = alterationListener;  
    }  
    public long getIntervalTime() {  
        return intervalTime;  
    }  
    public void setIntervalTime(long intervalTime) {  
        this.intervalTime = intervalTime;  
    }  
      
}  