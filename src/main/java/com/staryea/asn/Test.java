package com.staryea.asn;


//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.*;
//import org.springframework.util.SocketUtils;

import java.io.*;
import java.util.*;

public class Test {
    private List<String[]> m_asnFileContent = new ArrayList();
    public static void main(String[] args) throws Exception {

        /**
         * 解析ASN1话单的入口
         */
        Test t = new Test();
//        t.parseAsnFile("C:\\Users\\LiTao\\Desktop\\xxiangjia\\MediaX-CDR\\b00004303.dat");

        //b00004270 type =5

        t.parseAsnFile("C:\\Users\\LiTao\\Desktop\\xxiangjia\\现网数据\\403处理数据\\b00004276.dat");
//        t.parseAsnFile("C:\\Users\\LiTao\\Desktop\\xxiangjia\\多方通话\\b00000006.dat");

    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }
    public int parseAsnFile(String fileName)
    {
        /**
         * 定义ASN1文件中关键tag的数据类型
         *
         * 1、AsnTagType=204,200:INTEGER|204,292,4:INTEGER|204,292,6:INTEGER|204,290:INTEGER|204,10:TETSTRING|204,11:TETSTRING|204,15:INTEGER|204,3:INTEGER
         */
        this.m_asnFileContent.clear();
        Map fieldMap = new HashMap();
        fieldMap.put("204,200", "INTEGER");
        fieldMap.put("204,292,4", "INTEGER");
        fieldMap.put("204,292,5", "INTEGER");
        fieldMap.put("204,292,6", "INTEGER");
        fieldMap.put("204,290", "INTEGER");
        fieldMap.put("204,10", "TETSTRING");
        fieldMap.put("204,11", "TETSTRING");


        ASN1InputStream asn1InputStream = null;
        try
        {
            //我们所需要的tag列表，正常是在配置文件中读取的
//            String asnTags = LoadSysData.getServerConfig().getValue("AsnTags");
            String asnTags = "204,7,0|204,7,1|204,200|204,292,0|204,6,1|204,11|204,10|204,290";
              if (asnTags == null) {
                  return -1;
              }
              String[] needReadField = asnTags.split("\\|");

              // 过滤tag，当一个话单文件中，204,290 这个tag所对应的值为8的数据，才是我们所需要的
            String asnFilterTag = "204,290";
            if (asnFilterTag == null) {
                  asnFilterTag = "";
            }

            String asnFilterValue = "8";
            if (asnFilterValue == null) {
                asnFilterValue = "";
            }

            File sourceFile = new File(fileName);

            /**
             * 将原始的话单文件中的 去掉我们不需要的数据（例如头信息），保留我们需要的转换成一个临时文件
             */
            File tmpFile = prepareFormatHuawei(sourceFile);

            byte[] byteContents = FileUtils.readFileToByteArray(tmpFile);
            /**
             * 将临时文件读入Bouncycastle 工具提供的流中
             */
            asn1InputStream = new ASN1InputStream(new ByteArrayInputStream(byteContents));
            ASN1Primitive obj = null;
            int i = 0;

            /**
             * 一个话单文件中会有多条数据
             */
            while ((obj = asn1InputStream.readObject()) != null) {
                i++;

                System.out.println("================record[" + i + "] Map result========================");
                //将一条记录使用Bouncycastle工具转换成一个map
                ParseAsnToMap parse = new ParseAsnToMap(fieldMap);
                Map<String,String> result = parse.dumpAsMap(obj);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"+result.size()+">>>>>>>>>>>>>>>>>>>>>>>>>>");
                for(Map.Entry entry : result.entrySet()){
                    System.out.println(entry.getKey() + " : "+entry.getValue() );
                }
                System.out.println("*******************************************************************");

                /**
                 * 获取Map中我们需要的字段，并将所有的值保存到数组中，数组中就是我们需要入库的最终数据
                 */
                String[] params = new String[needReadField.length];
                int index = 0;
                String filterValue = "";
                for (String fieldKey : needReadField) {
                    if ((fieldKey.equals(asnFilterTag)) && (!"".equals(asnFilterTag))) {
                        filterValue = (String)result.get(fieldKey);
                    }
                    System.out.println("key[" + fieldKey + "]   value[" + (String)result.get(fieldKey) + "]");
                    params[(index++)] = ((String)result.get(fieldKey));
                }

                //将每条记录关键数据保存到集合中
                if (asnFilterValue.equals(filterValue)) {
                    this.m_asnFileContent.add(params);
                }
            }
            System.out.println("入库的数据内容=========================================================================》》》》》》》》》");
            for(String [] onerecord :m_asnFileContent ){
                for(String value: onerecord ){
                    System.out.print(value + "|");
                }
                System.out.println();
            }
            if (tmpFile.exists())
                tmpFile.delete();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (asn1InputStream != null)
                    asn1InputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        try
        {
            if (asn1InputStream != null)
                asn1InputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

    public static File prepareFormatHuawei(File sourceFile)
            throws IOException
    {
        String tmpFileName = sourceFile.getAbsolutePath() + ".mid";
        File tmpFile = new File(tmpFileName);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        tmpFile.createNewFile();

        FileOutputStream out = new FileOutputStream(tmpFileName);

        byte[] srcContents = FileUtils.readFileToByteArray(sourceFile);
        /**
         * 文件的前50个byte是一个话单文件的头信息，比如该文件一个有多少条记录。
         */
        byte[] dstContents = new byte[srcContents.length - 50];
        for (int i = 0; i < srcContents.length - 50; i++) {
            dstContents[i] = srcContents[(i + 50)];
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dstContents));
        byte[] recordHead = new byte[2];
        int recordLen = 0;

        while (in.available() > 0)
        {
            if (in.read(recordHead) != -1) {
                recordLen = byteArrayToShort(recordHead);
            }

            in.read(recordHead);

            byte[] record = new byte[recordLen];
            if (in.read(record) != -1) {
                out.write(record);
            }
        }

        in.close();
        out.close();
        return tmpFile;
    }
    public static int byteArrayToShort(byte[] b)
    {
        int value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (1 - i) * 8;
            value += ((b[i] & 0xFF) << shift);
        }
        return value;
    }
}
