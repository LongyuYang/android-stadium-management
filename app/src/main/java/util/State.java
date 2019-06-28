package util;

import java.util.ArrayList;
import java.util.List;

public class State {
    static private State state = new State();                //全局唯一State实例，由Service维护
    private User user = null;                                //当前的登录用户
    private List<Stadium> stadiums;                          //系统初起时加载所有场馆的信息
    private Stadium nowStadium;                              //正在被操作的场馆
    public List<Appoint> effectList = new ArrayList<>();      //当前正在生效的预约链表
    public int[] DEF_COLORS;                                 //正在被操作场馆时间条的颜色数组
    public float[] DEF_WEIGHTS;                              //正在被操作场馆的时间条颜色比例
    public boolean isServiceInit = false;
    static public State getState(){
        return state;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User u){
        user = u;
    }
    public List<Stadium> getStadiums(){
        return stadiums;
    }
    public void setStadiums(List<Stadium> s){
        stadiums = s;
    }
    public Stadium getNowStadium(){
        return nowStadium;
    }
    public void setNowStadium(Stadium now){
        nowStadium = now;
    }

}
