package com.staryea.onlymongo.bigfile2mongo;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadReadFile {
    private MongoTemplate mongoTemplate;
    private int threadNum;
    private int bufSize;
    private String filePath;
    private ExecutorService threadPool;
//    private DataProcessHandler dataProcessHandler;
    public MultiThreadReadFile(String filePath,int bufSize,int threadNum,MongoTemplate mongoTemplate){
        this.threadNum = threadNum;
        this.bufSize = bufSize;
        this.filePath = filePath;
        this.threadPool = Executors.newFixedThreadPool(threadNum);
        this.mongoTemplate=mongoTemplate;
    }

    /**
     * 启动多线程读取文件
     */
    public void startRead(){
        FileChannel infile = null;
        try {
            RandomAccessFile raf = new RandomAccessFile(filePath,"r");
            infile = raf.getChannel();
            long size = infile.size();
            long subSize = size/threadNum;
            for(int i = 0; i < threadNum; i++){
                long startIndex = i*subSize;
                if(size%threadNum > 0 && i == threadNum - 1){
                    subSize += size%threadNum;
                }
                RandomAccessFile accessFile = new RandomAccessFile(filePath,"r");
                FileChannel inch = accessFile.getChannel();
                threadPool.execute(new MultiThreadReader(inch,startIndex,subSize,mongoTemplate));
            }
            threadPool.shutdown();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(infile != null){
                    infile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    /**
//     * 注册数据处理接口
//     * @param dataHandler
//     */
//    public void registerHanlder(DataProcessHandler dataHandler){
//        this.dataProcessHandler = dataHandler;
//    }


    /**
     * 多线程按行读取文件具体实现类
     * @author zyh
     *
     */
    public class MultiThreadReader implements Runnable{
        private MongoTemplate mongoTemplate;
        private FileChannel channel;
        private long startIndex;
        private long rSize;

        public MultiThreadReader(FileChannel channel,long startIndex,long rSize,MongoTemplate mongoTemplate){
            this.mongoTemplate =mongoTemplate;
            this.channel = channel;
            this.startIndex = startIndex > 0?startIndex - 1:startIndex;
            this.rSize = rSize;
        }

        public void run(){
            readByLine();
        }

        /**
         * 按行读取文件实现逻辑
         * @return
         */
        public void readByLine(){
            DataProcessHandler dataProcessHandler2 = new FileLineDataHandler(mongoTemplate);
            try {
                ByteBuffer rbuf = ByteBuffer.allocate(bufSize);
                channel.position(startIndex);//设置读取文件的起始位置
                long endIndex = startIndex + rSize;//读取文件数据的结束位置
                byte[] temp = new byte[0];//用来缓存上次读取剩下的部分
                int LF = "\n".getBytes()[0];//换行符
                boolean isEnd = false;//用于判断数据是否读取完
                boolean isWholeLine = false;//用于判断第一行读取到的是否是完整的一行
                long lineCount = 0;//行数统计
                long endLineIndex = startIndex;//当前处理字节所在位置
                while(channel.read(rbuf) != -1 && !isEnd){
                    int position = rbuf.position();  //获取buff 被读取过一次之后 的数据位置
                    byte[] rbyte = new byte[position];
                    rbuf.flip(); //切换成读模式，position变为0，但是limit 会变成 转换前position 的位置
                    rbuf.get(rbyte); //将数据读取到byte数组中
                    int startnum = 0;//每行的起始位置下标，相对于当前所读取到的byte数组
                    //判断是否有换行符
                    //如果读取到最后一行不是完整的一行时，则继续往后读取直至读取到完整的一行才结束
                    for(int i = 0; i < rbyte.length; i++){
                        endLineIndex++;
                        if(rbyte[i] == LF){//若存在换行符
                            if(channel.position() == startIndex){//若该数据片段第一个字节为换行符，说明第一行读取到的是完整的一行
                                isWholeLine = true;
                                startnum = i + 1;
                            }else{
                                byte[] line = new byte[temp.length + i - startnum + 1];
                                System.arraycopy(temp, 0, line, 0, temp.length);
                                System.arraycopy(rbyte, startnum, line, temp.length, i - startnum + 1);
                                startnum = i + 1;
                                lineCount++;
                                temp = new byte[0];
                                //处理数据
                                if(startIndex != 0){//如果不是第一个数据段
                                    if(lineCount == 1){
                                        if(isWholeLine){//当且仅当第一行为完整行时才处理
                                            dataProcessHandler2.process(line);
                                        }
                                    }else{
                                        dataProcessHandler2.process(line);
                                    }
                                }else{
                                    dataProcessHandler2.process(line);
                                }
                                //结束读取的判断
                                if(endLineIndex >= endIndex){
                                    isEnd = true;
                                    break;
                                }
                            }
                        }
                    }
                    if(!isEnd && startnum < rbyte.length){//说明rbyte最后还剩不完整的一行
                        byte[] temp2 = new byte[temp.length + rbyte.length - startnum];
                        System.arraycopy(temp, 0, temp2, 0, temp.length);
                        System.arraycopy(rbyte, startnum, temp2, temp.length, rbyte.length - startnum);
                        temp = temp2;
                    }
                    rbuf.clear();
                    if(dataProcessHandler2.getDataListSize() > 100000){
                        boolean result = dataProcessHandler2.TransDataToDB("table_test");
                        System.out.println("table_" +"===>"+result);
                        dataProcessHandler2.clearDataList();
                    }
                }
                //兼容最后一行没有换行的情况
                if(temp.length > 0){
                    if(dataProcessHandler2 != null){
                        dataProcessHandler2.process(temp);
                    }
                }

                boolean result = dataProcessHandler2.TransDataToDB("table_test");
                System.out.println("table_" +"===>"+result);
                dataProcessHandler2.clearDataList();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getThreadNum() {
        return threadNum;
    }

    public String getFilePath() {
        return filePath;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }
    public int getBufSize() {
        return bufSize;
    }
}
