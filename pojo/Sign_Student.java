package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Sign_Student extends BmobObject {
    private String signNumber;
    private User student;
    int isSign;

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public int getIsSign() {
        return isSign;
    }

    public String getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(String signNumber) {
        this.signNumber = signNumber;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
