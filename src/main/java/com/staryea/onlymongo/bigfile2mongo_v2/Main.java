package com.staryea.onlymongo.bigfile2mongo_v2;

public class Main {

	public static void main(String[] args) {
		BigFileReader.Builder builder = new BigFileReader.Builder("C:\\Users\\LiTao\\Desktop\\aaa.txt",new IHandle() {

			public void handle(String line) {
				System.out.println(line);
//				increat();
			}
		});
		builder.withTreahdSize(10)
			   .withCharset("gbk")
			   .withBufferSize(1024*1024);
		BigFileReader bigFileReader = builder.build();
		bigFileReader.start();
	}
	
}
