package billandorder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(System.getProperty("file.encoding"));
        File excelFile = new File("E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\mytest.xlsx");
        File resultExcelFile = new File("E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\mytest_result.xlsx");

        File billFile = new File("E:\\mywork\\应用商城-数据采集与生成\\迪科平台订购及话单数据\\生成的文件\\bill7.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(billFile), "GBK");
        BufferedReader orderFileBuffer = new BufferedReader(isr);


        //order1.txt中的 第7字段：销售品ID 第9字段：销售品单价 单位分
        int count =1;
        String line = null;
        Map<Integer,Map<String,IdAndPrice>> productMap = new HashMap<Integer,Map<String,IdAndPrice>>();
        while((line = orderFileBuffer.readLine()) != null){
            String[] fieldsValue = line.split("\\|");
            if("".equals(fieldsValue[3].trim())){
                continue;
            }
//            Integer productId = Integer.valueOf(fieldsValue[3].trim());
            String dateStr =  fieldsValue[8].trim();
            //2018-02-28 11:54:28
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date orderDate = simpleDateFormat.parse(dateStr);

            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMM");
            String yearAndMoth = targetFormat.format(orderDate);
            if("201812".equals(yearAndMoth)){
                System.out.println(count + "-----:"+dateStr);
                System.out.println(count + "-----:"+yearAndMoth);
            }
            String productPrice =  fieldsValue[11].trim();
            String appId = fieldsValue[6].trim() ;
            String appName=fieldsValue[7].trim();
            String productName =fieldsValue[4].trim();
            Integer productId = Integer.valueOf(fieldsValue[3].trim());

//            Integer subKey = productId;
            Integer subKey = Integer.valueOf(appId);
            if(productMap.get(subKey) == null){

                Map<String,IdAndPrice> byMonth = new HashMap<String, IdAndPrice>();
                productMap.put(subKey,byMonth);

            }
            if(productMap.containsKey(subKey)){
                Map<String,IdAndPrice> byMonth =productMap.get(subKey);
                if(byMonth.get(yearAndMoth) == null){
                    IdAndPrice idAndPrice = new IdAndPrice();
                    idAndPrice.setSum(0);
                    idAndPrice.setPrice(0);
                    idAndPrice.setAppId(appId);
                    idAndPrice.setAppName(appName);
                    idAndPrice.setProductName(productName);
                    idAndPrice.setProductId(productId);
                    byMonth.put(yearAndMoth,idAndPrice);
                }
                if(byMonth.containsKey(yearAndMoth)){
                    IdAndPrice idAndPrice = byMonth.get(yearAndMoth);
                    if(productPrice != null && !"".equals(productPrice) ){
                        idAndPrice.setPrice(Integer.parseInt(productPrice));
                    }
                    idAndPrice.setSum(idAndPrice.getSum()+1);
                }
            }
        }


        InputStream resultExcelIs= new FileInputStream(resultExcelFile);
        XSSFWorkbook resultWorkbook = new XSSFWorkbook(resultExcelIs);
        XSSFSheet resultSheet = resultWorkbook.getSheetAt(0);

        int rowno = 1;
        for(Integer key : productMap.keySet()){

            Map<String,IdAndPrice> monthMap = productMap.get(key);
            for(String month : monthMap.keySet()){
                XSSFRow hssfRow = resultSheet.createRow(rowno);
                IdAndPrice idAndPrice = monthMap.get(month);
                Integer price = idAndPrice.getPrice();
                Integer sum = idAndPrice.getSum();
                Integer productId = idAndPrice.getProductId();
                Integer sumMoneyByMonth = price * sum/100;


                XSSFCell monthCell = hssfRow.createCell(0);
                XSSFCell appIdCell = hssfRow.createCell(1);
                XSSFCell appNameCell = hssfRow.createCell(2);
                XSSFCell proIdCell = hssfRow.createCell(3);
                XSSFCell proNameCell = hssfRow.createCell(4);
                XSSFCell sumMoneyCell = hssfRow.createCell(5);
                XSSFCell orderCountCell = hssfRow.createCell(6);
                monthCell.setCellValue(month);
                proIdCell.setCellValue(productId);
                sumMoneyCell.setCellValue(sumMoneyByMonth);
                orderCountCell.setCellValue(sum);
                appIdCell.setCellValue(idAndPrice.getAppId());

                XSSFCellStyle stringCellStyle = resultWorkbook.createCellStyle();


                String appName=new String(idAndPrice.getAppName());
                appNameCell.setCellValue(appName);

//                appNameCell.setEncoding(HSSFCell.ENCODING_UTF_16); //设置边个格式，防止中文乱码
                String proName=new String(idAndPrice.getProductName());
                proNameCell.setCellValue(proName);
                rowno ++;
            }
        }
        FileOutputStream fout = new FileOutputStream("E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\mytest_result4.xlsx");
        resultWorkbook.write(fout);
        fout.close();


//        for(int rowNum = 2;rowNum<=resultSheet.getLastRowNum();rowNum++){
//            System.out.println(rowNum);
//            XSSFRow hssfRow = resultSheet.getRow(rowNum);
//
//            XSSFCell orderDateCell = hssfRow.getCell(4);
//            XSSFCell productIdCell = hssfRow.getCell(7);
//            XSSFCell sumMoneyCell = hssfRow.getCell(9);
//            XSSFCell countCell = hssfRow.getCell(12);
//            if(productIdCell.getCellType() == Cell.CELL_TYPE_STRING){
//                continue;
//            }
//
//
//
//            Integer productId = (new Double(productIdCell.getNumericCellValue())).intValue();
//            IdAndPrice idAndPrice = idAndPriceMap.get(productId);
//            if(idAndPrice!=null){
//                Integer sum = idAndPrice.getSum();
//                Integer privce = idAndPrice.getPrice();
//                Integer sumMoney = sum.intValue() * privce.intValue();
//                if(countCell == null){
//                    countCell = hssfRow.createCell(12);
//                }
//                countCell.setCellValue(sum);
//                if(sumMoneyCell == null){
//                    sumMoneyCell = hssfRow.createCell(9);
//                }
//                sumMoneyCell.setCellValue(sumMoney/100);
//            }
//        }
//        FileOutputStream fout = new FileOutputStream("E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\mytest_result.xlsx");
//        workbook.write(fout);
//        fout.close();
    }
}
