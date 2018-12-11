package term.rjb.x2l.lessoncheck.pojo;

public class TheClass {
    protected String teacherName;
    protected String className;
    protected String classNumber;
    protected Integer studentsNum;

    public TheClass(String teacherName,String className,String classNumber,Integer studentsNum)
    {
        this.teacherName=teacherName;
        this.className=className;
        this.classNumber=classNumber;
        this.studentsNum=studentsNum;
    }
    public String getClassNumber()
    {
        return classNumber;
    }
}
