package domain;

import net.sf.json.JSONObject;
import utils.GetConfig;

/**
 * @Description
 * @Author Rorschach
 * @Date 2021/1/20 17:23
 */
public class Form {
    private String formWid;
    private String address;
    private String collectWid;
    private String schoolTaskWid;
    private String form;
    private boolean uaIsCpadaily = true;
    private String latitude;
    private String longitude;

    public Form(String formWid,String collectWid,String schoolTaskWid,String longitude,String latitude, String form) {
        GetConfig config= GetConfig.getInstance();
        this.formWid = formWid;
        this.address = config.getString("address");
        this.collectWid = collectWid;
        this.schoolTaskWid = schoolTaskWid;
        this.form = form;
        this.longitude = longitude;
        this.latitude = latitude;

    }



    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        data.put("formWid", formWid);
        data.put("address", address);
        data.put("collectWid", collectWid);
        data.put("schoolTaskWid", schoolTaskWid);
        data.put("form", form);
        data.put("uaIsCpadaily", uaIsCpadaily);
        data.put("latitude",latitude);
        data.put("longitude", longitude);
        return data.toString();
    }
}
