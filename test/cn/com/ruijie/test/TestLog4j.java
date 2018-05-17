/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.ruijie.test;

import org.apache.log4j.PropertyConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Administrator
 */
public class TestLog4j {
    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties ");
        Logger Log = LoggerFactory.getLogger(TestLog4j.class);
        Log.trace(String.format("dourl Exception:%s","test"));
        Log.debug(String.format("dourl Exception:%d",222));
        Log.info(String.format("dourl Exception:%d",222));
        Log.warn(String.format("dourl Exception:%s","test"));
        Log.error(String.format("dourl Exception:%s","test"));
    }
}
