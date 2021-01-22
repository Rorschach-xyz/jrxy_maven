package utils;

import java.util.ResourceBundle;

/**
 * @Description 获取配置
 * @Author Rorschach
 * @Date 2021/1/18 18:06
 */
public class GetConfig {
        private static GetConfig instance = null;
        private static ResourceBundle config=null;

        private GetConfig(){}

        public static GetConfig getInstance(){
            if(instance==null){
//                System.out.println("首次初始化");
                instance=new GetConfig();
                config = ResourceBundle.getBundle("config");
                return instance;
            }
//            System.out.println("已经初始化");
            return instance;
        }

    //获取系统配置
    public String getHost() {
        return config.getString("api_host");
    }
    public String getQueryList() {
        return config.getString("api_queryList");
    }
    public String getDetailCollector() {
        return config.getString("api_detailCollector");
    }
    public String getFormFields() {
        return config.getString("api_formFields");
    }
    public String getSubmitForm() {
        return config.getString("api_submitForm");
    }
    public String getServerJ() {
        return config.getString("api_ServerJ");
    }

    //获取用户配置
    public String getString(String key){
        return config.getString(key);
    }

}
