package com.staryea.onlymongo.v2;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test {
    @org.junit.Test
    public void test1(){
        RequestData data = new RequestData();
        data.setCardNo("aaaaaANO");
        data.setCardPwd("bbbbbBPWD");
        data.setFirstDate(new Date());
        ObjectId objectId = new ObjectId();
        System.out.println(objectId.toString());
        data.setId(objectId);
        Document document = data.toDocument();
        MongoDbUtils.insertOne("student", document);

    }

    @org.junit.Test
    public void test2(){
        Date start = new Date();
        for(int i=0;i<=10000;i++){
            RequestData data = new RequestData();
            data.setCardNo("aaaaaANO"+i);
            data.setCardPwd("bbbbbBPWD"+i);
            data.setFirstDate(new Date());
            ObjectId objectId = new ObjectId();
//            System.out.println(objectId.toString());
            data.setId(objectId);
            Document document = data.toDocument();
            MongoDbUtils.insertOne("student", document);
        }
        System.out.println("消耗时间："+(new Date().getTime() - start.getTime())); //59986
    }

    @org.junit.Test
    public void test3(){
        Date start = new Date();

        List<Document> documents = new ArrayList<Document>();
        for(int i=0;i<=1000000;i++){
            RequestData data = new RequestData();
            data.setCardNo("aaaaaANO"+i);
            data.setCardPwd("bbbbbBPWD"+i);
            data.setFirstDate(new Date());
            ObjectId objectId = new ObjectId();
//            System.out.println(objectId.toString());
            data.setId(objectId);
            Document document = data.toDocument();
            documents.add(document);
        }
        MongoDbUtils.insertMany("student2", documents);
        System.out.println("消耗时间："+(new Date().getTime() - start.getTime()));
    }

    @org.junit.Test
    public void test4(){
        Date date = new Date();
        date.setHours(-1);
        Bson filter = Filters.and(Filters.lte("firstDate",new Date()),Filters.gte("firstDate",date));
        MongoCursor<Document> cursor = MongoDbUtils.findByFilter("student2",filter);

//        while (cursor.hasNext()){
//            Document document = cursor.next();
//            System.out.println(document.toString());
//        }
    }

}
