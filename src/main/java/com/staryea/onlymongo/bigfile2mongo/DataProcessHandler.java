package com.staryea.onlymongo.bigfile2mongo;

public interface DataProcessHandler {
    public void process(byte[] data);
    public Boolean TransDataToDB(String tableName);
    public int getDataListSize();
    public void clearDataList();
}