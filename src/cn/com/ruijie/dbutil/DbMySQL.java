package cn.com.ruijie.dbutil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class DbMySQL{
  
    public static JSONArray resultToJsonArray(ResultSet rs) {
        if (rs == null) {
            return null;
        }
        
        JSONArray ret = new JSONArray();
        try {
            while (rs.next()) {
                ResultSetMetaData metaData = rs.getMetaData();
                JSONObject result = new JSONObject();
                for (int j = 1; j <= metaData.getColumnCount(); ++j) {
                    String key = metaData.getColumnName(j).toLowerCase();
                    String value = rs.getString(key);
                    if (value == null) {
                        value = "";
                    }
                    result.put(key, value.trim());
                }
                ret.put(result);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        
        return ret;
    }
    
    public static void writeFile(String filename,String data) {
        try {
            File f = new File(filename);
            Writer wt = new FileWriter(f);
            wt.write(data);
            wt.close();
        } catch (IOException e) {
            System.out.print("write file failed:"+e.toString());
        }
    }
            

    public static void main(String[] args)
    {
        String type="mysql";
        String dbname="whistle_sync";
        String host="172.16.56.119";
        String port="3306";
        String username="tangyibo";
        String password="tangyibo";
        String mode="";
        Connection conn=DbConnector.open(type, dbname, host, port, username, password, mode);
        if(null!=conn)
        {
            String tablename="ofuser";
            String sql = String.format("select * from %s", tablename);
            
            PreparedStatement pstmt=null;
            ResultSet rs=null;
            try {
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                JSONArray ret = resultToJsonArray(rs);
                if (ret != null) {
                    //System.out.println(ret.toString());
                    writeFile("e:/content.txt",ret.toString());
                }

            } catch (SQLException e) {
                //Log.error("select userdata Exception" + e);
            } finally {
                DbConnector.close(conn,pstmt,rs);
            }
        }
    }
    
    
}
