package term.rjb.x2l.lessoncheck.pojo;

import java.lang.ref.PhantomReference;

import cn.bmob.v3.BmobObject;

public class Lesson_Student extends BmobObject {
    private String lessonObjectId;
    private Student student;

    public String getLessonObjectId() {
        return lessonObjectId;
    }

    public void setLessonObjectId(String lessonObjectId) {
        this.lessonObjectId = lessonObjectId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
