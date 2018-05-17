package cn.com.ruijie.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件为zip
 */
public class FileZip {

    /**
     * 将文件夹内到所有文件压缩为ZIP包
     *
     * @param inputFileName 要压缩的文件夹(整个完整路径)
     * @param zipFileName 压缩后的文件(整个完整路径)
     * @throws java.lang.Exception
     */
    public static void zip(String inputFileName, String zipFileName) throws Exception {
        zip(zipFileName, new File(inputFileName));
    }

    /**
     * 将ZIP包文件解压到指定目录下（目录不存在则会创建）
     *
     * @param zipFile 要解压到ZIP文件路径
     * @param destDirectory 解压存放到文件夹路径
     * @throws java.lang.Exception
     */
    public void unzip(String zipFile, String destDirectory)  throws Exception {
        unzip(new File(zipFile),destDirectory);
    }
    
    private static void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.flush();
        out.close();
    }
    
    private static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] f1 = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < f1.length; i++) {
                zip(out, f1[i], base + f1[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            try (FileInputStream in = new FileInputStream(f)) {
                int b;
                while (-1 != (b = in.read())) {
                    out.write(b);
                }
            }
        }
    }

    private void unzip(File zipFile, String descDir) {
        File pathFile = new File(descDir);
        
        // 如果指定目录文件不存在 ，那么创建它
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
               
        try {
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {

                // 获取每个压缩文件夹中的文件
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipName = entry.getName();

                // 输入流, 将压缩包中的文件打到输入流中  
                InputStream in = zip.getInputStream(entry);

                // 输出文件路径
                String outPath = (descDir + zipName).replaceAll("\\*", "/");
                System.out.println(outPath);

                // 新建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf("/")));
                if (!file.exists()) {
                    file.mkdirs();
                }

                // 输出流
                OutputStream os = new FileOutputStream(outPath);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    os.write(buf, 0, len);
                }

                in.close();
                os.close();
            }

        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //测试
    public static void main(String[] args) {
        try {
            zip("E:\\study", "E:\\study-test.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
