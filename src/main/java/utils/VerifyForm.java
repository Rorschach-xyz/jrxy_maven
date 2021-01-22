package utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.ResourceBundle;

/**
 * @Description 传入要提交的表单，开始根据配置校验，如果有变动则不执行提交，通知用户
 * @Author Rorschach
 * @Date 2021/1/20 13:10
 */
public class VerifyForm {
    public static boolean verify(String submit) {
        //form 是问题数组，问题中有答案数组
        JSONArray form = JSONObject.fromObject(submit).getJSONArray("form");
        ResourceBundle bundle = ResourceBundle.getBundle("form");
        int num = Integer.parseInt(bundle.getString("num"));
        if (form.size() != num) {
            QmsgJ.pushToQQ(form.toString());//表单问题数目有变动就把新表单推送给用户
            QmsgJ.pushToQQ("表单问题数目有变动，暂不执行");
            return false;
        }

        for (Object temp : form) {//遍历每个问题 json对象
            JSONObject question = JSONObject.fromObject(temp);
            JSONArray fieldItems = question.getJSONArray("fieldItems");//答案数组
            String title = question.getString("title");//题目
            if (fieldItems.size() == 0) {//如果fieldItems为空，则检测value值
                String value = question.getString("value");
//                System.out.println(title + "\t" + value);
//                System.out.println(title + "\t" + bundle.getString(title));
                if (bundle.getString(title) == null || !bundle.getString(title).equals(value)) {
                    QmsgJ.pushToQQ(form.toString());
                    return false;
                }
            } else {//fieldItems不为空，获取其中对象，检测content
                JSONObject answer = fieldItems.getJSONObject(0);//单选答案
                String content = answer.getString("content");
//                System.out.println(title + "\t" + content);
//                System.out.println(title + "\t" + bundle.getString(title));
                if (bundle.getString(title) != null && bundle.getString(title).equals(content) && answer.getString("isSelected").equals("1")) continue;
                //如果这里执行则表示失败，推送新表单
                QmsgJ.pushToQQ(form.toString());
                return false;
            }
        }
        QmsgJ.pushToQQ("表单校验成功");
        return true;
    }

}
