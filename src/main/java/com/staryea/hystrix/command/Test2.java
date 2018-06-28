package com.staryea.hystrix.command;

import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Test2 {
    public static void main(String[] args) {

        Observable<String> toObservable1 = new TestCommand1("ddd").toObservable();

        System.out.println("");
    }
    @org.junit.Test
    public void test1() throws InterruptedException, ExecutionException {
        String result = new TestCommand1("aaa").execute();
        System.out.println(result);
        Future<String> future = new TestCommand1("bbb").queue();
        Thread.sleep(1000);
        System.out.println(future.get());
    }

    /**
     * observe()：事件注册前执行run()/construct()。
     * 以demo为例，
     * 第一步是事件注册前，先调用observe()自动触发执行run()/construct()（如果继承的是HystrixCommand，hystrix将创建新线程非堵塞执行run()；
     * 如果继承的是HystrixObservableCommand，将以调用程序线程堵塞执行construct()），
     *
     * 第二步是从observe()返回后调用程序调用subscribe()完成事件注册，如果run()/construct()执行成功则触发onNext()和onCompleted()，如果执行异常则触发onError()

     * @throws IOException
     */

    @Test
    public void test2_2() throws IOException {
        Observable<String> observable =  new TestObserverCommand("t").observe();
        System.out.println("------------------------------------------------------------");
        observable.subscribe(new Observer<String>() {
            public void onCompleted() {
                System.out.println("test2-----onCompleted");
            }

            public void onError(Throwable throwable) {
                System.out.println("test2-----onError:");
                throwable.printStackTrace();
            }

            public void onNext(String s) {
                    System.out.println("test2-----onNext:" + s);
            }
        });
        System.out.println("main end");
        System.in.read();
    }
}
