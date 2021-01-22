package utils;


import net.sf.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Description 微信推送
 * @Author Rorschach
 * @Date 2021/1/19 14:50
 */
public class ServerJ {
    public  static boolean pushToWx(String text,String desp){
        text+=GetTime.getTime();
        String params="?text=今日校园"+text+"&desp="+desp+Math.random();
        String url= GetConfig.getInstance().getServerJ();
        String result = null;
        try {
            result = HttpUtils.sendGet(url+ URLEncoder.encode(params,"UTF-8"), null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JSONObject json= JSONObject.fromObject(result);
        System.out.println(json.toString());
        if("success".equals(json.getString("errmsg"))||json.getInt("errno")==0) return true;
        return false;
    }

}
