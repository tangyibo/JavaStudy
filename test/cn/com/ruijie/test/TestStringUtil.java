package cn.com.ruijie.test;

import org.apache.commons.lang3.SystemUtils;


public class TestStringUtil {

    public static void main(String[] args) {
        System.out.println(SystemUtils.getHostName());
        System.out.println(SystemUtils.getUserDir());
        System.out.println(SystemUtils.getJavaHome());
        System.out.println(SystemUtils.getJavaIoTmpDir());
    }
    
    
    
}
