package jexhen.cn.edu.gdut;

public class Course {
	private String courseName;//课程名称
	private String teacherName;//教师名字
	private String weekNum;//上课的周数
	private String weekDay;//周几
	private String section;//节次
	private String classroom;//教室
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getWeekNum() {
		return weekNum;
	}
	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	@Override
	public String toString() {
		return "Course [courseName=" + courseName + ", teacherName=" + teacherName + ", weekNum=" + weekNum
				+ ", weekDay=" + weekDay + ", section=" + section + ", classroom=" + classroom + "]";
	}
	
}
