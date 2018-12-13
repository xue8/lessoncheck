package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Lesson_Sign extends BmobObject {


    private String lessonNumber;
    private String signNumber;
    private int lastMinute;


    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public void setLastMinute(int lastMinute) {
        this.lastMinute = lastMinute;
    }

    public int getLastMinute() {
        return lastMinute;
    }


    public String getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(String signNumber) {
        this.signNumber = signNumber;
    }
}
