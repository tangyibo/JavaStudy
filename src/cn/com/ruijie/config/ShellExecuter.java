package cn.com.ruijie.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 *
 * Description:执行shell命令
 * 
 * @author tang
 */
public class ShellExecuter {
 
    static class StreamGobbler extends Thread {

        private InputStream is;
        private String type;
        private OutputStream os;

        StreamGobbler(InputStream is, String type) {
            this(is, type, null);
        }

        StreamGobbler(InputStream is, String type, OutputStream redirect) {
            this.is = is;
            this.type = type;
            this.os = redirect;
        }

        private synchronized void work() {
            try {
                PrintWriter pw = null;
                if (os != null) {
                    pw = new PrintWriter(os);
                }
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (pw != null) {
                        pw.println(line);
                    }
                    System.out.println(type + ">" + line);
                }
                if (pw != null) {
                    pw.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            work();
        }
    }

    /********************************************************/
    
    /**
     *
     * Description:执行shell命令,不需要返回执行结果
     *
     * @param bashCmd, String shell命令
     * @return boolean,成功返回true，否则返回false
     * @throws java.io.IOException
     */
    public static boolean execute0(String bashCmd) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = null;

        try {
            process = rt.exec(bashCmd);
            int status = process.waitFor();
            if (status != 0) {
                return false;
            }
        } catch (InterruptedException e) {
            throw new IOException(e.toString());
        } finally {
            if (null != process) {
                process.destroy();
            }
        }

        return true;
    }

    /**
     *
     * Description:执行shell命令(短时任务),返回执行结果
     *
     * @param bashCmd, String shell命令
     * @return String,成功返回执行结果，否则返回null
     * @throws java.io.IOException
     */
    public static String execute1(String bashCmd) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = null;

        try {
            process = rt.exec(bashCmd);
            int status = process.waitFor();
            if (status != 0) {
                System.out.println("Failed to call shell's command ");
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (InterruptedException e) {
            throw new IOException(e.toString());
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    }
    
    
     /**
     *
     * Description:执行批量shell命令(短时任务),无需执行结果
     *
     * @param bashCmd, String shell命令
     * @return String,成功返回执行结果，否则返回null
     * @throws java.io.IOException
     */
    public static boolean execute2(String[] bashCmd) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = null;

        try {
            process = rt.exec(bashCmd[0]);
            if (bashCmd.length > 1) {
                // 命令1和命令2....命令N要放在一起执行
                OutputStream os = process.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os);
                for (int i = 0; i <= bashCmd.length; i++) {
                    writer.write(bashCmd[i]);
                }
                writer.flush();
                writer.close();
                os.close();
            }
            
            int status = process.waitFor();
            if (status != 0) {
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                throw new IOException(sb.toString());
            }

            return true;
        } catch (InterruptedException e) {
            throw new IOException(e.toString());
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    }
    
    
     /**
     *
     * Description:执行shell命令(长时任务),返回执行结果
     *
     * @param bashCmd, String shell命令     
     * @return  String,成功返回执行结果，否则返回null
     * @throws java.io.IOException
     */
    public static boolean execute3(String bashCmd) throws IOException {
        Runtime rt = Runtime.getRuntime();
        Process process = null;

        try {
            StreamGobbler errorGobbler = new StreamGobbler(
                    process.getErrorStream(), "ERROR");
            errorGobbler.start();

            StreamGobbler outGobbler = new StreamGobbler(
                    process.getInputStream(), "STDOUT");
            outGobbler.start();

            process = rt.exec(bashCmd);
            int status = process.waitFor();
            if (status != 0) {
                String str = new BufferedReader(new InputStreamReader(
                        process.getErrorStream())).readLine();
                
                System.out.println("exec command failed[ '" + bashCmd + "'],error: " + str);
                outGobbler.join();
                errorGobbler.join();
                
                return false;
            }
            
            return true;
        } catch (InterruptedException e) {
            throw new IOException(e.toString());
        } finally {
            if (null != process) {
                process.destroy();
            }
        }
    } 
    
    
    /**
     * Description:使用与测试
     * @param args
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.println(ShellExecuter.execute3("  cmd /c \"C: && dir\"  "));
    }
}

