package service;

import net.sf.json.JSONObject;
import utils.*;
import java.util.HashMap;
import java.util.Random;

/**
 * @Description
 * @Author Rorschach
 * @Date 2021/1/19 11:54
 */
public class Start {
    public static String  mainHandler(KeyValueClass kv) {//云函数入口方法
       main(null);
       return String.format("测试通过哦\t"+GetTime.getTime());
    }
    public static void main(String[] test) {
        //1.获取配置文件
        GetConfig config = GetConfig.getInstance();
        //获取服务
        Service service= Service.getInstance();
        //设置非提交请求头
        String Cookie = config.getString("Cookie");
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", Cookie);
        headers.put("Content-Type", config.getString("Content-Type"));
        headers.put("User-Agent", config.getString("User-Agent"));
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Encoding", "gzip");
        //2.测试Cookie是否可用
//        CookieLogin cookieLogin = new CookieLogin(headers);
//        if (!cookieLogin.login(headers)) {
//            System.out.println("登录失败,请检查cookie");
//            QmsgJ.pushToQQ("登录失败,请检查cookie");
//            return;
//        }
//        System.out.println("登录成功");
        //延迟提交
        Delay.getDelay();
        //3.登录成功，开始获取表单
//####################第一次请求获取当天最新任务###############################################
        //设置请求体
        JSONObject params=new JSONObject();
        params.put("pageNumber", "1");
        params.put("pageSize", "20");
//——————--------此处测试,如已签到或未发布,可注释此段,给collectorWid指定一个值，可测试程序运行情况—————————————
        //发送查询任务请求
        int result1= service.queryFormList(params.toString(), headers);
        if(result1==0) return;//如果不为0,则作为下一次请求体
//####################第二次请求获取任务详情###############################################
        String collectorWid=String.valueOf(result1);
//————————————————————————————————————此处测试——————————————————————————————————————————————
//        String collectorWid="35872";
        JSONObject result2=service.detialCollector("{\"collectorWid\":\""+collectorWid+"\"}",headers);//接受返回的json
        System.out.println(result2);
        //6.获取表单具体内容
        if(result2==null) return;//如果不为null,则可获取到schoolTaskWid
        String schoolTaskWid = result2.getString("schoolTaskWid");//第四次请求提交表单的请求体用到
        String formWid = result2.getString("formWid");
//####################第三次请求体查询表单文本内容###############################################
        params.put("formWid",formWid);
        params.put("collectorWid",collectorWid);//第三次请求体数据准备完成
        JSONObject result3 = service.getFormFields(params.toString(), headers);
//####################第四次请求提交表单###############################################
        String longitude=config.getString("lon")+String.format("%02d",new Random().nextInt(99));
        String latitude=config.getString("lat")+String.format("%02d",new Random().nextInt(99));
        //在此前基础上添加提交表单需要的请求头参数
        headers.put("CpdailyStandAlone", "0");
        headers.put("extension", "1");
        headers.put("Cpdaily-Extension", CpdailyExtension.generateCpdailyExtension(longitude,latitude));
        String body= HandleForm.getSubmitForm(formWid,collectorWid,schoolTaskWid,result3,longitude,latitude);//生成提交表单
        if(!VerifyForm.verify(body)){//校验表单
            QmsgJ.pushToQQ("表单校验失败,暂不提交");
            return;
        }
        String s = service.submitForm(config.getHost() + config.getSubmitForm(), body, headers);//提交后返回的信息
        System.out.println(s);
        QmsgJ.pushToQQ(s);
    }
}
