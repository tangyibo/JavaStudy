package cn.com.ruijie.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 *  文件操作工具
 * 
 */
public class FileUtil {
    /**
     * 读取指定文件中的内容
     *
     * @param filename 要读取文件的文件名
     * @return  文件内容
     */
    public static String readFile(String filename) {
        String content = null;
        try {
            File file = new File(filename);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");   // 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content += lineTxt;
                }
                read.close();
            }
        } catch (IOException e) {
            System.out.print("read file failed:" + e.toString());
        }

        return content;
    }
    
    
    /**
     * 向指定文件中写入内容
     *
     * @param filename 要写入文件的文件名
     * @param data 要写入文件中到内容
     */
       public static void writeFile(String filename,String data) {
        try {
            File f = new File(filename);
            try (Writer wt = new FileWriter(f)) {
                wt.write(data);
            }
        } catch (IOException e) {
            System.out.print("write file failed:"+e.toString());
        }
    }
       
    /**
     * 删除单个文件
     *
     * @param filepath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static int deleteFile(String filepath) {
        int flag = -1;
        File file = new File(filepath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = 0;
        }
        return flag;
    }

    /**
     * 只删除此路径的最末路径下所有文件和文件夹
     *
     * @param folderPath 文件路径 (
     */
    public static void deleteFolder(String folderPath) {
        try {
            deleteAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean deleteAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) { //如果不存在
            return flag;
        }
        
        if (!file.isDirectory()) {    //如果不是文件夹
            return flag;
        }

        String[] tempList = file.list();
        for (int i = 0; i < tempList.length; i++) {
            File temp = null;
            if (path.endsWith(File.separator)) {  //File.separator文件分隔符
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }

            if (temp.isFile()) {
                temp.delete();
            }
            
            if (temp.isDirectory()) {
                deleteAllFile(path + "/" + tempList[i]);//先删除文件夹里边的文件
                deleteFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }//end of  for ( int i = 0; i <tempList.length ; i++ ) {
        return flag;
    }

    /**
     * 获取程序所在到绝对路径目录
     *
     * @return 成功返回路径，失败返回null
     */
    public static String getCurrentPath() {
        java.net.URL url = FileUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        try {
            path = java.net.URLDecoder.decode(url.getPath(), "utf-8");
            if (path.endsWith(".jar")) {
                int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
                int lastIndex = path.lastIndexOf(File.separator) + 1;
                path = path.substring(firstIndex, lastIndex);
            }

            java.io.File file = new java.io.File(path);
            path = file.getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return path;
    }

    public static void main(String[] args) {
        System.out.println(FileUtil.getCurrentPath());
        deleteFolder("G:/demo/dira"); //删除dira文件夹下的文件，dira也会删除
        deleteAllFile("G:/demo/dira"); //只删除dira文件夹下的文件，dira不会删除
    }
}
