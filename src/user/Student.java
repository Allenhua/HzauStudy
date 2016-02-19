package user;

/**
 * Created by Hua on 2016/1/24.
 */
public class Student {

    private String stuSno;//学号
    private String stuName;
    private String stuPwd;

    public Student() {
    }

    public Student(String stuSno, String stuName, String stuPwd) {
        this.stuSno = stuSno;
        this.stuName = stuName;
        this.stuPwd = stuPwd;
    }

    public String getStuSno() {
        return stuSno;
    }

    public void setStuSno(String stuSno) {
        this.stuSno = stuSno;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuPwd() {
        return stuPwd;
    }

    public void setStuPwd(String stuPwd) {
        this.stuPwd = stuPwd;
    }
}
