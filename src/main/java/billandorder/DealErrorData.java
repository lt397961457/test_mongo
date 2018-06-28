package billandorder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealErrorData {
    private static Map<String,String> acountItemMap = new HashMap<String,String>();

    public static void main(String[] args) throws IOException {
//        0	iTV影视_点播包月费
//        1	iTV影视_按次点播费
//        10	iTV生活_点播包月费
//        11	iTV生活_按次点播费
//        110	ITV应用商店应用按次下载
//        113	ITV应用商店应用产品包按次订购
//        114	ITV应用商店应用产品包包月订购
//        280002	包年月日
//        4	iTV游戏_游戏充值
//        71	天翼视讯按次产品
//        80	酒店按次产品
//        9	iTV游戏_包月费
//        72	天翼视讯包月产品
//        111	ITV应用商店应用内按次订购
        acountItemMap.put("0","iTV影视_点播包月费");
        acountItemMap.put("1","iTV影视_按次点播费");
        acountItemMap.put("10","iTV生活_点播包月费");
        acountItemMap.put("11","iTV生活_按次点播费");
        acountItemMap.put("110","ITV应用商店应用按次下载");
        acountItemMap.put("113","ITV应用商店应用产品包按次订购");
        acountItemMap.put("114","ITV应用商店应用产品包包月订购");
        acountItemMap.put("280002","包年月日");
        acountItemMap.put("4","iTV游戏_游戏充值");
        acountItemMap.put("71","天翼视讯按次产品");
        acountItemMap.put("80","酒店按次产品");
        acountItemMap.put("9","iTV游戏_包月费");
        acountItemMap.put("72","天翼视讯包月产品");
        acountItemMap.put("111","ITV应用商店应用内按次订购");

        //原始文件路径
        String sourceRootDir = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理20180529服务器格式错误数据\\file_cdr";
        File sourceRootDirFile = new File(sourceRootDir);
        //输出路径
        String targetRootDir = "E:\\mywork\\应用商城-数据采集与生成\\给应用商城生成文件需求\\处理20180529服务器格式错误数据\\output\\file_cdr\\";
        File tagetRootDirFile = new File(targetRootDir);

        File[] sourceDirs = sourceRootDirFile.listFiles(); //028 0282 ....
        for(File sourceSubFile : sourceDirs){
            String subFileName = sourceSubFile.getName();
            String newSubFileName = targetRootDir + subFileName;
            File newSubFile = new File(newSubFileName);
            if(!newSubFile.exists()){
                newSubFile.mkdir();
            }

            File[] ppvDirs = sourceSubFile.listFiles();  //ppv
            for(File ppvdir:ppvDirs){
                String ppvFileName = ppvdir.getName();

                if(ppvFileName.contains("ppv")){
                    String newppvFileName= newSubFileName +"\\"+ppvFileName;
                    File newPPVFile = new File(newppvFileName);
                    if(!newPPVFile.exists()){
                        newPPVFile.mkdir();
                    }
                    File[] needDealFiles =  ppvdir.listFiles();
                    for(File file  : needDealFiles){
                        String fileName = file.getName();

                        //c处理一个文件
                        System.out.println( newppvFileName + "\\" + fileName);
                        String line = null;
                        InputStreamReader fileStream = new InputStreamReader(new FileInputStream(file), "UTF-8");
                        BufferedReader fileBuffer = new BufferedReader(fileStream);
                        List<String> oneFile = new ArrayList<String>();
                        while((line = fileBuffer.readLine()) != null){
                            if(line.indexOf("|")>0){
                                String newLine = dealFileHandler(line);
                                oneFile.add(newLine);
                            }
                        }

                        String newFileAName = newppvFileName + "\\" + fileName;
                        File newFile = new File(newFileAName);
                        if(newFile.exists()){
                            newFile.delete();
                        }

                        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8");
                        BufferedWriter bw = new BufferedWriter(writer);
                        //新写入一个文件
                        for(String newLine : oneFile){
                            bw.write(newLine);
                            bw.newLine();
                        }
                        bw.close();
                        writer.close();
                    }
                }
            }


        }
    }

    private static String dealFileHandler(String source){
        //一共15列0-14
        //倒数第二列 复制添加到行首
        //倒数第二列和倒数第一列对调
        //在倒数第4列后面添加一列，为账目项名称，根据前一列的账目项ID获取
//        String[] fieldsValue = new String[15];
        String[] fieldsValue = source.split("\\|");

//        if(fieldsValue.length<15){
            String[] fieldsValue2 =  new String[15];
            for(int i=0;i<15;i++){
                if(fieldsValue.length<15){
                    if(i==14){
                        fieldsValue2[i] = "";
                    }else {
                        fieldsValue2[i] = fieldsValue[i];
                    }
                }else {
                    fieldsValue2[i] = fieldsValue[i];
                }
            }
//        }

        String[] newFiedsValue =new String[fieldsValue2.length+2];
        String countName = acountItemMap.get(fieldsValue2[10]);
        if(null == countName && "".equals(countName)){
            System.out.println(fieldsValue2[9]);
        }

        try {
            newFiedsValue[0]=fieldsValue2[13];
            newFiedsValue[1]=fieldsValue2[0];
            newFiedsValue[2]=fieldsValue2[1];
            newFiedsValue[3]=fieldsValue2[2];
            newFiedsValue[4]=fieldsValue2[3];
            newFiedsValue[5]=fieldsValue2[4];
            newFiedsValue[6]=fieldsValue2[5];
            newFiedsValue[7]=fieldsValue2[6];
            newFiedsValue[8]=fieldsValue2[7];
            newFiedsValue[9]=fieldsValue2[8];
            newFiedsValue[10]=fieldsValue2[9];

            newFiedsValue[11]=fieldsValue2[10];
            newFiedsValue[12]=countName; //账目项名称
            newFiedsValue[13]=fieldsValue2[11];
            newFiedsValue[14]=fieldsValue2[12];
            newFiedsValue[15]=fieldsValue2[14];
            newFiedsValue[16]=fieldsValue2[13];
        }catch (Exception e){
            e.printStackTrace();
        }

        String newLine = "";
        for(String value :newFiedsValue){
            newLine =newLine+"|"+ value;
        }

        return  newLine.substring(1,newLine.length());
    }
}
