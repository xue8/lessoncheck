package term.rjb.x2l.lessoncheck.pojo;

import java.lang.ref.PhantomReference;

import cn.bmob.v3.BmobObject;

public class Lesson_Student extends BmobObject {
    private String lessonObjectId;
    private User student;
    private String lessonNumber;

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public String getLessonObjectId() {
        return lessonObjectId;
    }

    public void setLessonObjectId(String lessonObjectId) {
        this.lessonObjectId = lessonObjectId;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
