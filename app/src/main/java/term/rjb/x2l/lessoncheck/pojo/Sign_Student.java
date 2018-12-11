package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Sign_Student extends BmobObject {
    private String signNumber;
    private Student student;

    public String getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(String signNumber) {
        this.signNumber = signNumber;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
