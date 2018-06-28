package com.staryea.listenfilealter;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class FileListener implements FileAlterationListener {
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        System.out.println("start====");
    }

    public void onDirectoryCreate(File file) {
        System.out.println("onDirectoryCreate====");
    }

    public void onDirectoryChange(File file) {
        System.out.println("onDirectoryChange====");
    }

    public void onDirectoryDelete(File file) {
        System.out.println("onDirectoryDelete====");
    }

    public void onFileCreate(File file) {
        System.out.println("onFileCreate====");
    }

    public void onFileChange(File file) {
        System.out.println("onFileChange====");
    }

    public void onFileDelete(File file) {
        System.out.println("onFileDelete====");
    }

    public void onStop(FileAlterationObserver fileAlterationObserver) {
        System.out.println("onStop====");
    }

//    @Override
//    public void onStart(FileAlterationObserver observer) {
//        // TODO Auto-generated method stub
//        System.out.println("onStart()");
//
//    }
//
//    @Override
//    public void onDirectoryCreate(File directory) {
//        // TODO Auto-generated method stub
//        System.out.println("onDirectoryCreate()");
//    }
//
//    @Override
//    public void onDirectoryChange(File directory) {
//        // TODO Auto-generated method stub
//        System.out.println("onDirectoryChange()");
//    }
//
//    @Override
//    public void onDirectoryDelete(File directory) {
//        // TODO Auto-generated method stub
//        System.out.println("onDirectoryDelete()");
//    }
//
//    @Override
//    public void onFileCreate(File file) {
//        // TODO Auto-generated method stub
//        System.out.println("onFileCreate()");
//    }
//
//    @Override
//    public void onFileChange(File file) {
//        // TODO Auto-generated method stub
//        System.out.println("onFileChange()");
//    }
//
//    @Override
//    public void onFileDelete(File file) {
//        // TODO Auto-generated method stub
//        System.out.println("onFileDelete()");
//    }
//
//    @Override
//    public void onStop(FileAlterationObserver observer) {
//        // TODO Auto-generated method stub
//        System.out.println("onStop()");
//    }
//
}  