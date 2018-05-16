package cn.com.ruijie.config;

import java.io.IOException;
import java.util.Properties;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 配置文件操作类
 * @Author tang Create Date: 2018年5月10日13:28:17
 */
public class ConfigProperties {
    private Properties pps;
    private String config_file;
    
     /**
     *
     * Description:构造函数
     *
     * @param configfile, String 配置文件的路径     
     */
    public ConfigProperties(String configfile) {
        pps = new Properties();
        config_file=configfile;
        
        try {
            FileReader reader = new FileReader(config_file);
            pps.load(reader);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

     /**
     *
     * Description:获取String类型的值
     *
     * @param keyname, String 键名称
     * @return String,键对应的值     
     */    
    public synchronized String getPropertyString(String keyname) {
        return pps.getProperty(keyname,null);
    }

      /**
     *
     * Description:写入String类型的值
     *
     * @param keyname, String 键名称     
     * @param value     , String 值
     */   
    public synchronized void setPropertyString(String keyname, String value) {
        pps.setProperty(keyname, value);
        
        try {
            FileWriter writer = new FileWriter(config_file);
            pps.store(writer,"");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

     /**
     *
     * Description:获取int类型的值
     *
     * @param keyname, String 键名称
     * @return int,键对应的值     
     */      
    public synchronized int getPropertyInteger(String keyname) {
        String value = pps.getProperty(keyname,null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return 0;
    }

     /**
     *
     * Description:写入int类型的值
     *
     * @param keyname, String 键名称     
     * @param value     , int 值
     */  
    public void setPropertyInteger(String keyname, int value) {
        setPropertyString(keyname, String.valueOf(value));
    }
    
     /**
     * Description:使用与测试
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        ConfigProperties conf=new ConfigProperties("./config.ini");
        String key="connect_timeout";
        int val=conf.getPropertyInteger(key);
        System.out.println(val);
        conf.setPropertyInteger(key, 2000);
    }
}
