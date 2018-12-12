package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobUser;

public class Student extends BmobUser {
    private int id;
    private String studentNumber;
    private String name;
    private String sex;
    private int secretId;
    private String secretAnswer;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getSex() {
        return sex;
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
