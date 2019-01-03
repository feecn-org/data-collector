package com.feecn.org.service.job;

/**
 * Created by pczhangyu on 2019/1/2.
 */
public class MetricsCollector extends Thread{

    @Override
    public void run() {
        System.out.println("collector metrics");
        super.run();
    }

    public void runMetricsCollector(){
        this.run();
    }
}
