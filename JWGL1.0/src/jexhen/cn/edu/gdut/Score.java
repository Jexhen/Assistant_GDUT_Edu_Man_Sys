package jexhen.cn.edu.gdut;

public class Score {
	private String courseName;
	private int score;
	private double credit;
	private double gradePoint;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
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
	@Override
	public String toString() {
		return "Score [courseName=" + courseName + ", score=" + score + ", credit=" + credit + ", GradePoint="
				+ gradePoint + "]";
	}
	
}
