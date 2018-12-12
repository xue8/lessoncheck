package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Lesson_Sign extends BmobObject {
    private String lessonObjectId;
    private String signNumber;
    private int lastMinute;

    public void setLastMinute(int lastMinute) {
        this.lastMinute = lastMinute;
    }

    public int getLastMinute() {
        return lastMinute;
    }

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
