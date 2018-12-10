package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Teacher_Lesson extends BmobObject {
    private String number;
    private Lesson lesson;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
