package utils;

import domain.Form;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

/**
 * @Description 构造提交的表单
 * @Author Rorschach
 * @Date 2021/1/20 12:49
 * @Return 返回可直接提交的表单
 */
public class HandleForm {
    public static String getSubmitForm(String formWid,String collectorWid,String schoolTaskWid,JSONObject form,String latitude,String longtitude){
        String handle = handle(form);//处理后的表单数据
        Form subForm = new Form(formWid,collectorWid,schoolTaskWid,longtitude,latitude,handle);
        return subForm.toString();
    }
    public static String handle(JSONObject form){
        //1.获取rows数组
        JSONArray rows = form.getJSONObject("datas").getJSONArray("rows");
        ArrayList<String> qList=new ArrayList<>();
        //2.遍历每个问题   fieldType=1表示填值  fieldType=2表示单选，单选有答案数组，这里不处理fieldItems为空的，因为值是填好的
        for(Object question:rows){
            //fieldItems答案数组
            JSONArray fieldItems =(JSONObject.fromObject(question)).getJSONArray("fieldItems");//得到选项
            JSONObject qtemp=JSONObject.fromObject(question);
            for(Object fieldItem:fieldItems){
                //System.out.println(fieldItem.toString());
                JSONObject answer=JSONObject.fromObject(fieldItem);
                if(answer.has("isSelected")&&answer.getString("isSelected").equals("1"))
                qtemp.put("fieldItems","["+answer.toString()+"]");
            }
            qList.add(qtemp.toString());
        }
        //System.out.println(qList.toString());
        return qList.toString();
    }
}
