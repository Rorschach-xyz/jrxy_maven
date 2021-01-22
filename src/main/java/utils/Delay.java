package utils;

import java.util.Random;

/**
 * @Description
 * @Author Rorschach
 * @Date 2021/1/21 21:30
 */
public class Delay {
    public static void getDelay(){
        GetConfig config=GetConfig.getInstance();
        if(config.getString("delay").equals("false")) return;
        //随机延迟提交时间 分钟
        int[] delay={12*60,3*60,30*60,21*60,25*60,5*60};
        //主程序延迟提交
        try {
            int temp=delay[new Random().nextInt(6)];
            QmsgJ.pushToQQ("预计"+String.valueOf(temp/60)+"分钟后提交");
            Thread.sleep(1000*temp);
        } catch (InterruptedException e) {
            QmsgJ.pushToQQ("程序延迟提交错误");
            e.printStackTrace();
        }
    }
}
