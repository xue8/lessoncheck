package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobUser;

public class Teacher extends BmobUser {
    private int id;
    private String teacherNumber;
    private String name;
    private String sex;
    private int secretId;
    private String secretAnswer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherNumber() {
        return teacherNumber;
    }

    public void setTeacherNumber(String teacherNumber) {
        this.teacherNumber = teacherNumber;
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

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }
}
