package cn.com.ruijie.test;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class MyTimerHandler extends TimerTask {

    @Override
    public void run() {
        System.out.println("-------定时任务正在执行--------");
    }
}

public class TestTimer {
    
    // 第一种方法：设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time)
    // void schedule(TimerTask task, long delay)
    public static void timer1() {
        Timer timer = new Timer();
        TimerTask handler= new MyTimerHandler();
        timer.schedule(handler,2000);// 设定指定的时间time,此处为2000毫秒
    }

    // 第二种方法：设定指定任务task在指定延迟delay后进行固定延迟peroid的执行
    // schedule(TimerTask task, long delay, long period)
    public static void timer2() {
        Timer timer = new Timer();
        timer.schedule(new MyTimerHandler(), 1000, 3000);
    }

    // 第三种方法：设定指定任务task在指定延迟delay后进行固定频率peroid的执行。
    // scheduleAtFixedRate(TimerTask task, long delay, long period)
    public static void timer3() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerHandler(), 1000, 2000);
    }

    // 第四种方法：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
    // Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
    public static void timer4() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时
        calendar.set(Calendar.MINUTE, 0);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒

        Date time = calendar.getTime();     // 得出执行任务的时间,此处为今天的12：00：00

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerHandler(), time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
    }

    public static void main(String[] args) {
        TestTimer.timer3();
    }

}
