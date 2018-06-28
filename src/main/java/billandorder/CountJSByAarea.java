package billandorder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountJSByAarea {
    /**
     * 计算所有正式环境上JS类型文件 按天订购总价
     * @param args
     */
    public static void main(String[] args) throws IOException {
        Map<String,Integer> rsultMap = new HashMap<String,Integer>();
        String JSRootFilePath = "E:\\mywork\\应用商城-数据采集与生成\\按天计算JS文件订购总金额\\file_cdr";
        File JSRootFile = new File(JSRootFilePath);
        File[] areaFiles =  JSRootFile.listFiles();
        Map<String,List<String>> dayBatchFiles = new HashMap<String, List<String>>();
        for(File areafile : areaFiles){
            String areafileName = areafile.getAbsolutePath();
            String ppvFileName = areafileName +"\\ppv";

            File ppvFile = new File(ppvFileName);
            File[] dayLiFiles = ppvFile.listFiles();

            for(File dayliFile : dayLiFiles){
                String dayliFileName = dayliFile.getName();
                if(dayliFileName.startsWith("ppv_")){
                    String keyName = dayliFileName.substring(4,12);

                    if(!dayBatchFiles.containsKey(keyName)){
                        List<String> dayFiles = new ArrayList<String>();
                        dayFiles.add(dayliFile.getAbsolutePath());
                        dayBatchFiles.put(keyName,dayFiles);
                    }else {
                        dayBatchFiles.get(keyName).add(dayliFile.getAbsolutePath());
                    }

                }

            }
        }

        //打印每天 所有区域的文件名
        for(String day:dayBatchFiles.keySet()){
            System.out.println("=============="+day+"================");
            for (String filePath : dayBatchFiles.get(day)){
                System.out.println(filePath);
            }
        }

        for(String day:dayBatchFiles.keySet() ){
            int daySum = 0;
            for(String filePath :  dayBatchFiles.get(day)){
                File oneFile = new File(filePath);
                InputStreamReader oneFileStream = new InputStreamReader(new FileInputStream(oneFile), "utf-8");
                BufferedReader orderFileBuffer = new BufferedReader(oneFileStream);

                String line = null;
                while((line = orderFileBuffer.readLine()) != null){
                    String[] values = line.split("\\|");
                    try {
                        daySum += Integer.parseInt(values[13]);
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                    }

                }
            }
            rsultMap.put(day,daySum);
        }

        for(String day : rsultMap.keySet()){
            System.out.println(day + "============"+rsultMap.get(day));
        }

    }
}
