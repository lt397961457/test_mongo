package com.staryea.onlymongo.bigfile2mongo;

import com.mongodb.DBCollection;
import com.staryea.onlymongo.v2.MongoDbUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileLineDataHandler implements DataProcessHandler {
    private MongoTemplate mongoTemplate;
    private String encode = "GBK";
    private List<Document> list = new ArrayList<Document>();
    private List<TableEntity> entities = new ArrayList<TableEntity>();

    public FileLineDataHandler(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public FileLineDataHandler() {
    }

    public void process(byte[] data) {
        try {
//            System.out.println(new String(data,encode));
                String line = new String(data,encode);
                String[] array = line.split("\\|");
                Document document = new Document();
                document.put("userId",array[0]);
                document.put("contentId",array[1]);
                document.put("serviceType",array[2]);
                document.put("beginTime",array[3]);
                document.put("endTime",array[4]);
                list.add(document);


                TableEntity entity = new TableEntity();
                entity.setUserId(array[0]);
                entity.setContentId(array[1]);
                entity.setServiceType(array[2]);
                entity.setBeginTime(array[3]);
                entity.setEndTime(array[4]);
                entities.add(entity);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
}

    public Boolean TransDataToDB(String tableName) {
        System.out.println("=================================");
        if(mongoTemplate!=null){
             mongoTemplate.insertAll(entities);
        }  else {
             MongoDbUtils.insertMany(tableName, list);
        }

        System.out.println("++++++++++++++++++++++++++++++++");
        return new Boolean(Boolean.TRUE);
    }

    public int getDataListSize(){
        if(mongoTemplate!=null){
            return entities.size();
        }
        return  list.size();
    }

    public void clearDataList() {
        entities.clear();
        list.clear();
    }

    public List<Document> getList() {
        return list;
    }

    public void setList(List<Document> list) {
        this.list = list;
    }

    public List<TableEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<TableEntity> entities) {
        this.entities = entities;
    }
}