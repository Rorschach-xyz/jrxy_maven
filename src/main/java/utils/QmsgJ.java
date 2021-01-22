package utils;

import net.sf.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * @Description QQ推送
 * @Author Rorschach
 * @Date 2021/1/20 21:12
 */
public class QmsgJ {
    public  static boolean pushToQQ(String msg){
        msg+=GetTime.getTime();
        String url = null;
        try {
            url = GetConfig.getInstance().getString("api_QmsgJ") +"?msg="+ URLEncoder.encode(msg,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = HttpUtils.sendGet(url, null);
        JSONObject json=JSONObject.fromObject(result);
//        System.out.println(json.toString());
        if(json.getBoolean("success")) return true;
        return false;
    }


}
