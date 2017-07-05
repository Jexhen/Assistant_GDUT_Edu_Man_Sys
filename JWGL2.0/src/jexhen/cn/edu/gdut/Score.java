package jexhen.cn.edu.gdut;

public class Score {
	private String courseName;//课程名称
	private String score;//课程成绩
	private double credit;//课程学分
	private double gradePoint;//课程绩点
	private String courseType;//课程类型
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public double getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(double gradePoint) {
		this.gradePoint = gradePoint;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	@Override
	public String toString() {
		return "Score [courseName=" + courseName + ", score=" + score + ", credit=" + credit + ", gradePoint="
				+ gradePoint + ", courseType=" + courseType + "]";
	}
	
}
