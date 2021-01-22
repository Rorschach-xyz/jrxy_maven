package utils;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Description 处理http请求
 * @Author Rorschach
 * @Date 2021/1/18 18:44
 */
public class HttpUtils {
    /**
     *
     * @param url 目标地址
     * @param params 请求参数
     * @param headers 请求头
     * @return result 返回的字符串
     */
    public static String sendPost(String url, String params, Map<String, String> headers) {
        BufferedWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 必须设置false，否则会自动重定向到目标地址
            conn.setInstanceFollowRedirects(false);
            if (headers != null) {
                Set<Entry<String, String>> set = headers.entrySet();
                for (Entry<String, String> header : set) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            if(params!=null){
                JSONObject json=JSONObject.fromObject(params);
                out.write(json.toString());
            }

            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;//返回的json字符串
            while ((line = in.readLine()) != null) {
                result.append(line) ;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！");
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    public static String sendGet(String url, Map<String, String> headers) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 必须设置false，否则会自动重定向到目标地址
            conn.setInstanceFollowRedirects(false);
            if (headers != null) {
                Set<Entry<String, String>> set = headers.entrySet();
                for (Entry<String, String> header : set) {
                    conn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！");
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
