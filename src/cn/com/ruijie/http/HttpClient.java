package cn.com.ruijie.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author Administrator
 */
public class HttpClient {

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    public static String http_get(String url) {
        int connect_timeout = 2000;
        int read_timeout = 5000;

        return http_get(url, connect_timeout, read_timeout);
    }

    public static String http_get(String url, int connect_timeout) {
        int read_timeout = 5000;

        return http_get(url, connect_timeout, read_timeout);
    }

    public static String https_get(String url) {
        int connect_timeout = 2000;
        int read_timeout = 5000;

        return https_get(url, connect_timeout, read_timeout);
    }

    public static String https_get(String url, int connect_timeout) {
        int read_timeout = 5000;

        return https_get(url, connect_timeout, read_timeout);
    }
    
    public static String http_get(String url, int connect_timeout, int read_timeout) {
        OutputStream out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL console = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) console.openConnection();

            conn.setConnectTimeout(connect_timeout);
            conn.setReadTimeout(read_timeout);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            conn.disconnect();
        } catch (IOException e) {
            System.out.println(String.format("http_get() Exception :" + e.getMessage()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(String.format("http_get() in close Exception") + e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println(String.format("http_get() outclose Exception") + e);
                }
            }
        }

        return result;
    }

    public static String https_get(String url, int connect_timeout, int read_timeout) {
        BufferedReader in = null;
        String result = "";

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,
                    new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom()
            );
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setConnectTimeout(connect_timeout);
            conn.setReadTimeout(read_timeout);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            conn.disconnect();

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            System.out.println(String.format("https_get() Exception :" + e.getMessage()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(String.format("https_get() in close Exception") + e);
                }
            }
        }

        return result;
    }

    public static String http_post(String url, String data) {
        int connect_timeout = 2000;
        int read_timeout = 5000;

        return http_post(url, data, connect_timeout, read_timeout);
    }

    public static String http_post(String url, String data, int connect_timeout) {
        int read_timeout = 5000;

        return http_post(url, data, connect_timeout, read_timeout);
    }

    public static String https_post(String url, String data) {
        int connect_timeout = 2000;
        int read_timeout = 5000;

        return https_post(url, data, connect_timeout, read_timeout);
    }

    public static String https_post(String url, String data, int connect_timeout) {
        int read_timeout = 5000;

        return https_post(url, data, connect_timeout, read_timeout);
    }

    public static String http_post(String url, String data, int connect_timeout, int read_timeout) {
        BufferedReader in = null;
        String result = "";

        try {
            URL console = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) console.openConnection();

            conn.setConnectTimeout(connect_timeout);
            conn.setReadTimeout(read_timeout);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(data.getBytes("utf-8"));
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            conn.connect();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

            conn.disconnect();
        } catch (IOException e) {
            System.out.println(String.format("http_post() Exception :" + e.getMessage()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(String.format("http_post() in close Exception") + e);
                }
            }
        }

        return result;
    }

    public static String https_post(String url, String data, int connect_timeout, int read_timeout) {
        BufferedReader in = null;
        String result = "";

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,
                    new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom()
            );
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setConnectTimeout(connect_timeout);
            conn.setReadTimeout(read_timeout);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            if (data != null && data.length() != 0) {
                conn.getOutputStream().write(data.getBytes("utf-8"));
                conn.getOutputStream().flush();
                conn.getOutputStream().close();
            }
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            conn.disconnect();
        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            System.out.println(String.format("https_post() Exception :" + e.getMessage()));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(String.format("https_post() in close Exception") + e);
                }
            }
        }

        return result;
    }
}
