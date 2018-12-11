package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Lesson_Sign extends BmobObject {
    private String lessonObjectId;
    private String signNumber;

    public String getLessonObjectId() {
        return lessonObjectId;
    }

    public void setLessonObjectId(String lessonObjectId) {
        this.lessonObjectId = lessonObjectId;
    }

    public String getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(String signNumber) {
        this.signNumber = signNumber;
    }
}
