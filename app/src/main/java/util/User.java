package util;

import java.io.Serializable;

public class User implements Serializable {
    public int userId;
    public String userName;
    public String stuId;
    public String college;
    public String gender;
    public int ruleBreak;
    public User(){
    }
    public User(int uId, String uName, String sId, String col,
                String gen, int rule){
        userId = uId;
        userName = uName;
        stuId = sId;
        college = col;
        gender = gen;
        ruleBreak = rule;
    }
}
