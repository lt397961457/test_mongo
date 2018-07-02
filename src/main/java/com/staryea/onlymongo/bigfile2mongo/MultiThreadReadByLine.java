package com.staryea.onlymongo.bigfile2mongo;

public class MultiThreadReadByLine {
    public static void main(String[] args){
        MultiThreadReadFile fileReader = new MultiThreadReadFile("C:\\Users\\LiTao\\Desktop\\view_20180623.log",1024,3,null);
        fileReader.startRead();
    }
}