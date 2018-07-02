package com.staryea;

import com.staryea.entity.Friut;
import com.staryea.entity.Name;
import com.staryea.entity.People;
import com.staryea.onlymongo.bigfile2mongo.MultiThreadReadFile;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test {
    @Autowired
    private MongoTemplate mongoTemplate;

    // 使用spring整合的话, 就直接注入就可以了, 这是测试uanjing
    @Before
    public void testBefore() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        mongoTemplate = (MongoTemplate) context.getBean("mongoTemplate");
    }

    @org.junit.Test
    public void test1(){
        Set<String> names = mongoTemplate.getCollectionNames();
        for(String name : names){
            System.out.println(name);
        }
    }
    @org.junit.Test
    public void test2(){
        People people = new People();
        Name name = new Name();
        name.setFirstName("ma");
        name.setLastName("yun2");

        people.setName(name);
        people.setAddr("CD");
        people.setSchool("BJDX");
        people.setId("lllllOOOOO");
        people.setAge(15);
        mongoTemplate.insert(people,"people");
    }
    @org.junit.Test
    public void test3(){
        List<People> list = mongoTemplate.findAll(People.class,"people");
        for(People people : list){
            System.out.println(people);
        }
    }
    @org.junit.Test
    public void test4(){
        List<Map> list = mongoTemplate.findAll(Map.class,"friut");
        for(Map Map : list){
            System.out.println(Map);
        }
    }


    /**
     * 查询
     */
    @org.junit.Test
    public void test5(){

        // criteria.and("name").is("cuichongfei");等于
        // List<String> interests = new ArrayList<String>();
        // interests.add("study");
        // interests.add("linux");
        // criteria.and("interest").in(interests); in查询
        // criteria.and("home.address").is("henan"); 内嵌文档查询
        // criteria.and("").exists(false); 列存在
        // criteria.and("").lte(); 小于等于
        // criteria.and("").regex(""); 正则表达式
        // criteria.and("").ne(""); 不等于
        // 多条件查询
        // criteria.orOperator(Criteria.where("key1").is("0"),Criteria.where("key1").is(null));

        Query query = new Query();
        Criteria criteria = Criteria.where("age").gte(13);
        query.addCriteria(criteria);

        // 排序查询sort方法,按照age降序排列
        query.with(new Sort(Sort.Direction.DESC,"age"));

        // 指定字段查询,只查询age和name两个字段
        // query.fields().include("age").include("name");
        // List<User> userList3 = mongoTemplate.find(query, User.class);
        // printList(userList3);

        // 分页查询
        // query.skip(2).limit(3);
        // List<User> userList4 = mongoTemplate.find(query, User.class);
        // printList(userList4);

        // 查询所有
        // printList(mongoTemplate.findAll(User.class));

        // 统计数据量
        // System.out.println(mongoTemplate.count(query, User.class));



        List<People> list = mongoTemplate.find(query, People.class);
        for(People people : list){
            System.out.println(people);
        }
    }

    /**
     * 更新
     */
    @org.junit.Test
    public void test6(){
        // update(query,update,class)
        // Query query:需要更新哪些用户,查询参数
        // Update update:操作符,需要对数据做什么更新
        // Class class:实体类

        // 更新age大于24的用户信息
        Query query = new Query();
        Criteria criteria = Criteria.where("age").gt(13);
        query.addCriteria(criteria);

        Update update = new Update();
        // age值加2
        update.inc("age", 2);
        // update.set("name", "zhangsan"); 直接赋值
        // update.unset("name"); 删去字段
        // update.push("interest", "java"); 把java追加到interest里面,interest一定得是数组
        // update.pushAll("interest", new String[]{".net","mq"})
        // 用法同push,只是pushAll一定可以追加多个值到一个数组字段内
        // update.pull("interest", "study"); 作用和push相反,从interest字段中删除一个等于value的值
        // update.pullAll("interest", new String[]{"sing","dota"})作用和pushAll相反
        // update.addToSet("interest", "study") 把一个值添加到数组字段中,而且只有当这个值不在数组内的时候才增加
        // update.rename("oldName", "newName") 字段重命名

        // 只更新第一条记录,age加2,name值更新为zhangsan
        mongoTemplate.updateFirst(query, new Update().inc("age", 2).set("name", "zhangsan"), People.class);

        // 批量更新,更新所有查询到的数据
//        mongoTemplate.updateMulti(query, update, People.class);
    }

    /**
     * 测试删除数据
     */
    @Ignore
    @org.junit.Test
    public void testRemoveUser() {
        Query query = new Query();
        // query.addCriteria(where("age").gt(22));
        Criteria criteria = Criteria.where("age").gt(18);
        // 删除年龄大于22岁的用户
        query.addCriteria(criteria);
        mongoTemplate.remove(query, People.class);
    }

    public void printList(List<People> userList) {
        for (People people : userList) {
            System.out.println(people);
        }
    }

    /**
     * 数组文档指定元素更新
     */
    @org.junit.Test
    public void testUpdateDocument() {
        //查找friut表中 friut数组中包含“aaaaaa”的数据，并将“aaaaaa” 改为 “aaaa2”
        Update update = new Update();
        update.set("friut.$", "aaaa2");
        mongoTemplate.updateMulti(new Query(Criteria.where("friut").is("aaaaaa")), update, Friut.class);

        // 批量更新所有的内嵌文档
//        update.set("sonModelList.$.state", 2);
//        mongoTemplate.updateMulti(new Query(Criteria.where("sessionId").is("cf23e870-2c5a-4d1f-9652-6fa1793dc8be")),
//                new Update().set("state", 2), CatchThirteenthModel.class);
//        mongoTemplate.updateMulti(new Query(Criteria.where("sonModelList.parentGeoNum").is("532568523000512512")),
//                new Update().set("state", 2), CatchFifteenthModel.class);
        List<Map> find = mongoTemplate.find(new Query(Criteria.where("sonModelList.parentGeoNum").is("532568523000512512")), Map.class);
        System.out.println("");
    }

    /**
     * 测试findAndModify方法
     */
    @org.junit.Test
    public void testFindAndModify() {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is("readTest"));
        Friut model = mongoTemplate.findAndModify(new Query(criteria), new Update().set("friut", new String [] {"www2w","xxxx"}).set("endTime", new Date()), new FindAndModifyOptions().returnNew(true), Friut.class);

        System.out.println(model);
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        MongoTemplate mongoTemplate2 = (MongoTemplate) context.getBean("mongoTemplate");
        MultiThreadReadFile fileReader = new MultiThreadReadFile("C:\\Users\\LiTao\\Desktop\\view_20180623.log",1024,3,mongoTemplate2);
        fileReader.startRead();
    }
    @org.junit.Test
    public  void testSocketTimeOut(){

        MultiThreadReadFile fileReader = new MultiThreadReadFile("C:\\Users\\LiTao\\Desktop\\view_20180623.log",1024,3,mongoTemplate);
        fileReader.startRead();
    }
}
