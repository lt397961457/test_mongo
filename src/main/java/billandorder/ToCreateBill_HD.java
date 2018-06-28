package billandorder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 迪科遗留文件：读取bill 文件生成 按天拆分
 */
public class ToCreateBill_HD {
    public static void main(String[] args) throws IOException, ParseException {
        String targetRootDir = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理迪科遗留数据脚本与原始数据\\bill_output";
        File rootfileDir = new File(targetRootDir);
        if(!rootfileDir.exists()){
            rootfileDir.mkdir();
        }

        String billFilePath = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理迪科遗留数据脚本与原始数据\\app_bill_pay_info_test.file";
        File billFile = new File(billFilePath);
        InputStreamReader billFileStream = new InputStreamReader(new FileInputStream(billFile), "GBK");
        BufferedReader orderFileBuffer = new BufferedReader(billFileStream);


        String line = null;

        Map<String,List<String>> dateMap = new HashMap<String, List<String>>();
        while((line = orderFileBuffer.readLine()) != null){
            if(line.indexOf("#")>0){
                String[] fieldsValue = line.split("\\|#\\|");
                String timeStr =  fieldsValue[23].trim();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date orderDate = simpleDateFormat.parse(timeStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(orderDate);
                int lastDayofMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMM");
                String dateStr = targetFormat.format(orderDate)+String.valueOf(lastDayofMonth);

                /**
                 * 话单信息： 流水号（BILL_PAY_INFO_ID-13）| ITV账号（ITV账号-16 null）|宽带账号（ITV账号-16）|地区CODE（AREA_CODE-18 拼接10）
                 * |销售外部ID（OFFER_ID -17 ->8）|销售品名称（OFFER_NAME-20）|服务编码（默认0）|节目编码（PROGRAM_NO-22）
                 * |节目名称（PROGRAM_NAME-23）|开始时间（START_TIME-24）|结束时间（END_TIME-25）
                 * |账目类型ID（帐目类型ID-11）|账目类型名称(帐目类型名称-12)|金额（CHARGE-27）|免费金额（DISCOUNT_FEE-28）
                 * |用户ID（USER_ID-30）（ITV账号-16）|客户订单号（BILL_PAY_INFO_ID -13）
                 */
                String newLine = fieldsValue[12].trim()+"|"+fieldsValue[15].trim()+"|"+fieldsValue[15].trim()+"|"+fieldsValue[17].trim()+"|"
                        +fieldsValue[7].trim()+"|"+fieldsValue[19].trim()+"|"+"0"+"|"+fieldsValue[21].trim()+"|"
                        +fieldsValue[22].trim()+"|"+fieldsValue[23].trim()+"|"+fieldsValue[24].trim()+"|"
                        +fieldsValue[10].trim()+"|"+fieldsValue[11].trim()+"|"+fieldsValue[26].trim()+"|"+fieldsValue[27].trim()+"|"
                        +fieldsValue[15].trim()+"|"+fieldsValue[12].trim();
                List<String> dateList =dateMap.get(dateStr);
                if(dateList == null){
                    dateList = new ArrayList<String>();
                    dateMap.put(dateStr,dateList);
                }
                dateList.add(newLine);
            }
        }
        orderFileBuffer.close();
        billFileStream.close();
        //按天生成文件
        int count = 0; //计算总量
        for(String key : dateMap.keySet()){
            //ppv_20180529_028_JS_20180529005143_3_2001.DAT
            String dayFileName = "ppv_"+key+"_028_JS_"+key+"000000_3_9999.DAT";
            String fullDayFileName = rootfileDir +"\\"+dayFileName;
            File fullDayFile = new File(fullDayFileName);
            if(fullDayFile.exists()){
                fullDayFile.delete();
            }
            List<String> dayRecords = dateMap.get(key);


            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fullDayFile),"GBK");
            BufferedWriter bw = new BufferedWriter(writer);

            for(String record : dayRecords){
                bw.write(record);
                bw.newLine();
            }
            bw.close();
            writer.close();
            count += dayRecords.size();
        }
        System.out.println("总数量："+count);
    }
}
