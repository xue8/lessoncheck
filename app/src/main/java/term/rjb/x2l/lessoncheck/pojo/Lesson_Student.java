package term.rjb.x2l.lessoncheck.pojo;

import java.lang.ref.PhantomReference;

import cn.bmob.v3.BmobObject;

public class Lesson_Student extends BmobObject {
    private String lessonNumber;
    private User student;

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
