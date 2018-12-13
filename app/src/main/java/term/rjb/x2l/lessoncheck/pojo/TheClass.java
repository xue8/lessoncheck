package term.rjb.x2l.lessoncheck.pojo;

public class TheClass {
    protected String teacherName;
    protected String className;
    protected String classNumber;
    protected Integer studentsNum;
    protected String objID;

    public TheClass(String teacherName,String className,String classNumber,Integer studentsNum,String ObjID)
    {
        this.teacherName=teacherName;
        this.className=className;
        this.classNumber=classNumber;
        this.studentsNum=studentsNum;
        this.objID=ObjID;
    }
    public String getClassNumber()
    {
        return classNumber;
    }
    public String getClassObjID()
    {
        return objID;
    }
}
