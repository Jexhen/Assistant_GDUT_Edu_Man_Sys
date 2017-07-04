package jexhen.cn.edu.gdut;

public class Course {
	private String courseName;
	private String teacherName;
	private String weekNum;
	private String weekDay;
	private String section;
	private String classroom;
	
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
