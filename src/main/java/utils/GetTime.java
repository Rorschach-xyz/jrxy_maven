package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author Rorschach
 * @Date 2021/1/21 21:32
 */
public class GetTime {
    public static String getTime(){
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
