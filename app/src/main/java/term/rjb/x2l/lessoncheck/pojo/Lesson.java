package term.rjb.x2l.lessoncheck.pojo;

import cn.bmob.v3.BmobObject;

public class Lesson extends BmobObject {
    private int id;
    private int studentNum;
    private String lessonNumber;
    private String lessonName;
    private String teacherName;
    private User teacher;

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(int studentNum) {
        this.studentNum = studentNum;
    }

    public String getlessonNumber() {
        return lessonNumber;
    }

    public void setlessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getlessonName() {
        return lessonName;
    }

    public void setlessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
}
