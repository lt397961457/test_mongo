package com.staryea.controller;

import com.staryea.entity.Name;
import com.staryea.entity.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

@Controller
@RequestMapping(value = "/mongo")
public class FirstController {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取数据库的下面的所有表名称
     * @return
     */
    @RequestMapping(value = "/test2", method = {RequestMethod.GET})
    @ResponseBody
    public String test2(){
        Set<String> names = mongoTemplate.getCollectionNames();
        for(String name : names){
            System.out.println(name);
        }
        return "this is a test2";
    }

    /**
     * 利用实体映射，插入数据
     * @return
     */
    @RequestMapping(value = "/test3", method = {RequestMethod.GET})
    @ResponseBody
    public String test3(){
        People people = new People();
        Name name = new Name();
        name.setFirstName("ma");
        name.setLastName("yun");

        people.setName(name);
        people.setAddr("CD");
        people.setSchool("BJDX");
        mongoTemplate.insert(people,"people");
        return "this is a test3";
    }

    /**
     * 不使用映射，直接用Map插入数据
     * @return
     */
    @RequestMapping(value = "/test4", method = {RequestMethod.GET})
    @ResponseBody
    public String test4(){

        Map<Object,Object> peopleMap = new HashMap<Object, Object>();
        peopleMap.put("age",20);
        peopleMap.put("shcool","shdx222333");
        peopleMap.put("addr","BJ");
        peopleMap.put("age",20);

        mongoTemplate.insert(peopleMap,"people");
        return "this is a test3";
    }

    /**
     * 复杂Map插入数据
     * @return
     */
    @RequestMapping(value = "/test5", method = {RequestMethod.GET})
    @ResponseBody
    public String test5(){
        Name name = new Name();
        name.setFirstName("ma");
        name.setLastName("yun");

        Map<Object,Object> peopleMap = new HashMap<Object, Object>();
        peopleMap.put("age",20);
        peopleMap.put("shcool","shdx");
        peopleMap.put("addr","BJ");
        peopleMap.put("name",name);

        mongoTemplate.insert(peopleMap,"people");
        return "this is a test3";
    }

    /**
     * 查询所有，返回Map结果
     * @return
     */
    @RequestMapping(value = "/test6", method = {RequestMethod.GET})
    @ResponseBody
    public List<Map> test6(){
        List<Map> result = mongoTemplate.findAll(Map.class,"people");;
        return result;
    }

    /**
     * 查询所有返回实体结果
     * @return
     */
    @RequestMapping(value = "/test7", method = {RequestMethod.GET})
    @ResponseBody
    public List<People> test7(){
        return mongoTemplate.findAll(People.class,"people");
    }

    public static void main(String[] args) throws Exception {
        InetAddress address = getLocalHostLANAddress();
        address.getHostAddress();
        System.out.println( address.getHostAddress());

        InetAddress address2 = InetAddress.getLocalHost();
        String localIP = address2.getHostAddress();
        System.out.println( localIP);
    }
    public static InetAddress getLocalHostLANAddress() throws Exception {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
