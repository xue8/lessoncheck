package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class User extends BmobUser {
    private int id;
    private String Number;
    private String name;
    private String sex;
    private int secretId;
    private int isTeacher;
    private String secretAnswer;
    private BmobGeoPoint address;

    public void setAddress(BmobGeoPoint address) {
        this.address = address;
    }

    public BmobGeoPoint getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSecretId() {
        return secretId;
    }

    public void setSecretId(int secretId) {
        this.secretId = secretId;
    }

    public int getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(int isTeacher) {
        this.isTeacher = isTeacher;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }
}
