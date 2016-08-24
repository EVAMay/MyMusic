package zhuoxin.edu.mymusic.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import zhuoxin.edu.mymusic.bean.LrcInfo;

/**
 * Created by lenovo on 2016/7/6.
 */
public class LrcUtil {
//       private static final String LRC_DIR="/data/data/zhuoxin.edu.mymusic/";//模拟器测试用路径;
    private static final String LRC_DIR="/storage/emulated/0/MusicLrc/";//手机测试用路径;


    public static ArrayList<LrcInfo> getLrc(String path){
        ArrayList<LrcInfo> list=new ArrayList<>();
        list.add(new LrcInfo(0,path));
        File file=new File(LRC_DIR+path+".lrc");
        try {

//            BufferedReader br=new BufferedReader(new FileReader(file));
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            String line="";
            while ((line=br.readLine())!=null){
                line=line.replace("[","");
                String []temp=line.split("]");
                if (temp[0].matches("\\d{2}[:]\\d{2}[\\.]\\d{2}")){
                    Log.d("debug","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
                    int time=getTimeFromString(temp[0]);
                    list.add(new LrcInfo(time,temp[1].trim()));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static int getTimeFromString(String str) {
        String []tempTime=str.split(":");
        int min=Integer.parseInt(tempTime[0]);
        float second=Float.parseFloat(tempTime[1]);
        return (int) (min*60000+second*1000);
    }

}
