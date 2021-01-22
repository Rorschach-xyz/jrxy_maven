package service;

import net.sf.json.JSONObject;
import utils.GetConfig;
import utils.HttpUtils;
import utils.QmsgJ;

import java.util.HashMap;

/**
 * @Description 测试cookie是否有效，子线程维持cookie；弃用
 * @Author Rorschach
 * @Date 2021/1/18 17:19
 */
public class CookieLogin {
    private String cookie = null;
    private GetConfig config = GetConfig.getInstance();

    public CookieLogin(HashMap<String, String> headers) {
        if (headers.get("Cookie") != null) {
            this.cookie = headers.get("Cookie");
        }
    }

    //开始用cookie登录
    public boolean login(HashMap<String, String> headers) {
        //1.初始化post请求内容
        if (cookie==null) {
            System.out.println("未填入cookie");
            QmsgJ.pushToQQ("未填入cookie");
            return false;
        }
        if (testCookie(headers)) {
            new Thread(()->{//子线程只维持一天,因为每天定时触发
                int i=1;
                while(testCookie(headers)&&i<=144){
                    QmsgJ.pushToQQ("当前cookie有效");
                    try {
                        System.out.println("开始维持cookie存活");
                        Thread.sleep(1000 * 10*60);//10s,云函数内部时钟1s大概是20或40s、60,6分钟
                    } catch (InterruptedException e) {
                        QmsgJ.pushToQQ("子线程睡眠异常");
                    }
                    i++;
                }
                QmsgJ.pushToQQ("子线程关闭，cookie以维持" + i + "次");
            }).start();
            return true;
        }
        return false;
    }

    public boolean testCookie(HashMap<String, String> headers) {
        String api_login = HttpUtils.sendPost(config.getHost() + config.getString("api_login"), null, headers);
        JSONObject json = JSONObject.fromObject(api_login);
        if (json.getJSONObject("datas").getBoolean("hasLogin")) {
            return true;
        }
        QmsgJ.pushToQQ("cookie已失效");
        return false;
    }
}
