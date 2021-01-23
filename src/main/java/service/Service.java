package service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import utils.GetConfig;
import utils.HttpUtils;
import utils.QmsgJ;

import java.util.HashMap;

/**
 * @Description 发送和接收请求
 * @Author Rorschach
 * @Date 2021/1/19 12:29
 */
public class Service {
    public static Service instance=null;
    public static GetConfig config=null;
    private Service(){
    }
    public static Service getInstance(){
        if(instance==null){
            instance=new Service();
            config=GetConfig.getInstance();
        }
        return instance;
    }

    /**
     * 查询任务
     * @param params 请求体 pageSize pageNumber
     * @param headers 请求头
     * @return 返回0表示异常，否则返回wid(collectorWid,第二次请求获取表单详情需要的body参数)
     */
    public  int queryFormList(String params,HashMap<String,String> headers){
        String result= HttpUtils.sendPost(config.getHost()+config.getQueryList(), params, headers);
        if(result.startsWith("<")){
            System.out.println("返回的是网页，请检查cookie是否有效");
            return 0;
        }
        if(result.startsWith("{")&&result.endsWith("}")){
            JSONObject json = JSONObject.fromObject(result);
            //判断所需数据是否存在,判断rows数组是否存在，里面是否有数据
            if(!json.has("datas")&&json.getJSONObject("datas").has("rows")){
                System.out.println("未检测到rows数据");
                return 0;
            }
            //判断是否已经签到
            JSONArray rows=json.getJSONObject("datas").getJSONArray("rows");
            if(rows.size()==0|| rows.getJSONObject(0).getInt("isHandled")==1){
//                System.out.println(config.getString("senderUserName"));
                System.out.println("已经提交或者任务未发布");
                return 0;
            }
            JSONObject task = rows.getJSONObject(0);
            return task.getInt("wid");
        }
        return 0;
    }

    /**
     * 获取任务详情 判断是否是自己的辅导员发布的任务
     * @param params collectorWid
     * @param headers
     * @return
     */
    public JSONObject detialCollector(String params, HashMap<String,String> headers){
//        System.out.println(params);
        //发送post请求
        String result= HttpUtils.sendPost(config.getHost()+config.getDetailCollector(),params,headers);
        JSONObject json = JSONObject.fromObject(result);
        if(result.startsWith("<")){
            System.out.println("返回的是网页，请检查cookie是否有效");
            return null;
        }
        if(result.startsWith("{")&&result.endsWith("}")) {
            if (!json.has("datas") && json.getJSONObject("datas").has("collector")) {
                System.out.println("没有collector数据");
                return null;//没有数据
            }
            JSONObject collector = json.getJSONObject("datas").getJSONObject("collector");
            if (!collector.getString("senderUserName").equals(config.getString("senderUserName"))) {
                System.out.println(collector.getString("senderUserName"));
                System.out.println(config.getString("senderUserName"));
                QmsgJ.pushToQQ("辅导员名字错误");
                return null;
            }
            String wid = collector.getString("wid");
            String formWid = collector.getString("formWid");
            JSONObject temp = new JSONObject();
            temp.put("formWid", formWid);
            temp.put("collectorWid", wid);
            temp.put("schoolTaskWid", collector.getString("schoolTaskWid"));
            return temp;
        }
        return null;
    }

    /**
     * 获取表单内容
     * @param params
     * @param headers
     * @return 返回表单具体内容
     */
    public JSONObject getFormFields(String params,HashMap<String,String> headers){
        String result= HttpUtils.sendPost(config.getHost()+config.getFormFields(),params,headers);
        System.err.println(result);
        return  JSONObject.fromObject(result);
    }

    /**
     * 提交表单
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public String submitForm(String url,String params,HashMap<String,String> headers){
        return  HttpUtils.sendPost(config.getHost() + config.getSubmitForm(),params, headers);
    }
}
