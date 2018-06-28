package billandorder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 迪科遗留文件：读取order 文件生成1、2号文件
 */
public class ToCreateOrder1_2_jf {
    public static void main(String[] args) throws IOException, ParseException {
        String targetRootDir = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理迪科遗留数据脚本与原始数据\\order_output";
        File rootfileDir = new File(targetRootDir);
        if(!rootfileDir.exists()){
            rootfileDir.mkdir();
        }

        String orderFilePath = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理迪科遗留数据脚本与原始数据\\v_all_order_relationorder_test.file";
        File orderFile = new File(orderFilePath);
        InputStreamReader orderFileStream = new InputStreamReader(new FileInputStream(orderFile), "GBK");
        BufferedReader orderFileBuffer = new BufferedReader(orderFileStream);


        //order1 输出的目标路径
        String order1FileName =rootfileDir + "\\order1.txt";
        File order1File = new File(order1FileName);
        if(order1File.exists()){
            order1File.delete();
        }
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(order1File),"GBK");
        BufferedWriter bw = new BufferedWriter(writer);
        //order2 输出的目标路径
        String order2FileName =rootfileDir + "\\order2.txt";
        File order2File = new File(order2FileName);
        if(order2File.exists()){
            order2File.delete();
        }
        OutputStreamWriter writer2 = new OutputStreamWriter(new FileOutputStream(order2File),"GBK");
        BufferedWriter bw2 = new BufferedWriter(writer2);

        String line = null;
        Map<String,List<String>> dateMap = new HashMap<String, List<String>>();
        while((line = orderFileBuffer.readLine()) != null){
            if(line.indexOf("#")>0){
                String[] fieldsValue = line.split("\\|#\\|");

                /** 1号文件
                 订购实列数据: 订购实列ID（OFFER_INST_ID-4）|客户订单号（CUST_ORDER_ID-36->4）|ITV账号（ACC_NBR-17）|用户名称（PROD_INST_NAME-18）
                 |订购时间（EFF_DATE-9）|订购生效时间订购时间（EFF_DATE-9|订购失效时间（EXP_DATE-10）|销售品ID（OFFER_ID -5）|销售品名称（OFFER_NAME-20）
                 |收费金额（PRICING_VALUE-26 ->27）|销售品计费模式（固定：按次）|地市编码（LATN_ID-7 前面拼接10）|地市名称（空）|订购渠道编码（固定：104）
                 |订购渠道名称（固定：应用商城）|订购状态（STATUS_CD-8 固定：有效）
                 */

                /** 2号文件：
                 * 订购关系信息：订购关系ID（OFFER_INST_ID-4）|订购实列ID（OFFER_INST_ID-4）|客户订单号（CUST_ORDER_ID-36->4）|ITV账号（ACC_NBR-17）
                 * |订购时间（EFF_DATE-9）|订购生效时间（EFF_DATE-9）|订购失效时间（EXP_DATE-10）|销售品ID（OFFER_ID -5）|销售品名称（OFFER_NAME-20）
                 * |应用ID（PRODUCT_NBR-21-22）|应用名称（PRODUCT_NAME-22-23）|地市编码（LATN_ID-7）|地市名称|订购渠道编码|订购渠道名称|订购状态（STATUS_CD-8）
                 *
                 *
                 */
                String newLine_order1 = fieldsValue[3].trim()+"|"+fieldsValue[3].trim()+"|"+fieldsValue[16].trim()+"|"+fieldsValue[17].trim()+"|"
                        +fieldsValue[8].trim()+"|"+fieldsValue[8].trim()+"|"+fieldsValue[9].trim()+"|"+fieldsValue[4].trim()+"|"+fieldsValue[19].trim()
                        +"|"+fieldsValue[26].trim()+"|"+"按次"+"|"+fieldsValue[6].trim()+"|" + "" +"|"+"104"+"|"
                        +"应用商城"+"|"+"有效";
                String newLine_order2 = fieldsValue[3].trim()+"|" +fieldsValue[3].trim()+"|"+fieldsValue[3].trim()+"|"+fieldsValue[16].trim()+"|"
                        +fieldsValue[8].trim()+"|"+fieldsValue[8].trim()+"|"+fieldsValue[9].trim()+"|"+fieldsValue[4].trim()+"|"+fieldsValue[19].trim()+"|"
                        +fieldsValue[21].trim()+"|"+fieldsValue[22].trim()+"|"+fieldsValue[6].trim()+"|"+""+"|"+"104"+"|"+"应用商城"+"|"+"有效";

                bw.write(newLine_order1);
                bw.newLine();

                bw2.write(newLine_order2);
                bw2.newLine();
            }
        }

        bw.close();
        writer.close();

        bw2.close();
        writer2.close();

        orderFileBuffer.close();
        orderFileStream.close();

    }
}
