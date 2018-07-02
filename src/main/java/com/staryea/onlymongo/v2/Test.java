package com.staryea.onlymongo.v2;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.staryea.entity.HvLog;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
        for(int i=0;i<=10;i++){
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
        MongoDbUtils.insertMany("student3", documents);
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
    @org.junit.Test
    public void test5_1(){
        Document document = new Document();
        document.put("userId","userId");
        document.put("contentId","userId");
        document.put("serviceType","userId");
        document.put("beginTime","userId");
        document.put("endTime","userId");
        MongoDbUtils.insertOne("sss",document);

    }
    @org.junit.Test
    public void test5() throws IOException, InterruptedException, ExecutionException {
        Long startTime = new Date().getTime();

        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService service = Executors.newFixedThreadPool(10);

        LineNumberReader reader = null;
        reader = new LineNumberReader(new FileReader("C:\\Users\\LiTao\\Desktop\\view_20180623.log"));
        String lineRead = "";
        int count = 1;
        int tableCount = 0;
        List<Document> list = new ArrayList<Document>();
        List<Future> futures = new ArrayList<Future>();
        while ((lineRead = reader.readLine()) != null) {
            lineRead = new String(lineRead.getBytes(), "gbk");
            //根据获取数组
            String[] array = lineRead.split("\\|");
            Document document = new Document();
            document.put("userId",array[0]);
            document.put("contentId",array[1]);
            document.put("serviceType",array[2]);
            document.put("beginTime",array[3]);
            document.put("endTime",array[4]);


            list.add(document);

            if (count == 100000) {

                count=0;
                tableCount ++;
                System.out.println("开始插入表："+tableCount);
                FutureTask task = new FutureTask(list,"table_"+tableCount);
                Future<Boolean> f = service.submit(task);
                futures.add(f);

            }
            if(futures!=null&&futures.size()==5){

                for(Future<Boolean> f : futures){

                        Boolean result = f.get();
                        System.out.println("Future结果："+result);


                }
                futures.clear();
            }
            count++;
        }
        reader.close();
        System.out.println("************************"+(new Date().getTime() - startTime)+"************************");
    }

    public class FutureTask implements Callable<Boolean>{
        private List<Document> list;
        private String tableName;

        public FutureTask(List<Document> list, String tableName) {
            this.list = list;
            this.tableName = tableName;
        }

        public Boolean call() throws Exception {

            System.out.println("=================================");
            MongoDbUtils.insertMany(tableName, list);

            System.out.println("++++++++++++++++++++++++++++++++");
            return new Boolean(Boolean.TRUE);
        }
    }

    public class Task implements Runnable{
        private List<Document> list;
        private String tableName;
        private CountDownLatch countDownLatch;

        public Task( List<Document> list, String tableName,CountDownLatch countDownLatch) {
            this.list = list;
            this.tableName = tableName;
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            System.out.println("=================================");
            countDownLatch.countDown();
            MongoDbUtils.insertMany(tableName, list);

            System.out.println("++++++++++++++++++++++++++++++++"+countDownLatch.getCount());
        }
    }
}
