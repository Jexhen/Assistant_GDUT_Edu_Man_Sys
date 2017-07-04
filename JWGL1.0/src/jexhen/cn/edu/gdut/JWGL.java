package jexhen.cn.edu.gdut;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class JWGL {
	private final static String HOST = "http://222.200.98.147";
	private final static String LOGIN_URI = "/login!doLogin.action";
	private final static String HOME_URI = "/login!welcome.action";
	private final static String SCORE_REFERER_URI = "/xskccjxx!xskccjList.action";
	private final static String SCORE_URI = "/xskccjxx!getDataList.action";
	private final static String COURSE_REFERER_URI = "/xsgrkbcx!xskbList.action";
	private final static String COURSE_URI = "/xsgrkbcx!getKbRq.action";
	private String usrname;
	private String password;
	private String cookie;
	private CloseableHttpClient client;
	
	public JWGL(String usrname, String password) {
		this.usrname = usrname;
		this.password = password;
		this.cookie = getCookie();
	}
	
	private String getCookie() {
		client = HttpClients.createDefault();
		HttpPost loginPost = new HttpPost(HOST + LOGIN_URI);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		NameValuePair account = new BasicNameValuePair("account", usrname);
		NameValuePair pwd = new BasicNameValuePair("pwd", password);
		NameValuePair verifycode = new BasicNameValuePair("verifycode", "");
		nameValuePairs.add(account);
		nameValuePairs.add(pwd);
		nameValuePairs.add(verifycode);
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			loginPost.setEntity(entity);
			HttpResponse loginResponse = client.execute(loginPost);
			String cookieLine = loginResponse.getFirstHeader("Set-Cookie").getValue();
			cookie = cookieLine.split(";")[0];
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cookie;
	}
	
	//登录
	public boolean login() {
		boolean isSuccess = false;
		try {
			HttpGet welGet = new HttpGet(HOST + HOME_URI);
			welGet.setHeader("Cookie", cookie);
			welGet.setHeader("Referer", HOST);
			HttpResponse welResponse = client.execute(welGet);
			HttpEntity httpEntity = welResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);
			if (content.contains("登录时间")) {
				System.out.println("登录成功！");
				isSuccess = true;
			} else {
				System.out.println("学号或者密码错误！");
				isSuccess = false;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	//查询成绩
	public void queryResult(String yearSemesterCode) {
		HttpPost queryResultPost = new HttpPost(HOST + SCORE_URI);
		queryResultPost.addHeader("Cookie", cookie);
		queryResultPost.addHeader("Referer", HOST + SCORE_REFERER_URI + "?firstquery=" + 1);
		NameValuePair xnxq = new BasicNameValuePair("xnxqdm", yearSemesterCode);//学年学期代码
		NameValuePair jhlx = new BasicNameValuePair("jhlxdm", "");//
		NameValuePair page = new BasicNameValuePair("page", "1");//第多少页
		NameValuePair rows = new BasicNameValuePair("rows", "50");//每页显示多少行
		NameValuePair sort = new BasicNameValuePair("sort", "xnxqdm");//排序依据
		NameValuePair order = new BasicNameValuePair("order", "asc");//排序次序
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(xnxq);
		nameValuePairs.add(jhlx);
		nameValuePairs.add(page);
		nameValuePairs.add(rows);
		nameValuePairs.add(sort);
		nameValuePairs.add(order);
		UrlEncodedFormEntity entity;
		try {
			entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			queryResultPost.setEntity(entity);
			HttpResponse queryResultResponse = client.execute(queryResultPost);
			HttpEntity httpEntity = queryResultResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);
			List<Score> scores = new ArrayList<Score>();//存储抓取到的课程成绩
			//抓取课程名称
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z]]*");
			Matcher m = p.matcher(content);
			while (m.find()) {
				Score score = new Score();
				String courseName = m.group().split(":\"")[1];
				score.setCourseName(courseName);
				scores.add(score);
			}
			//抓取课程成绩
			p = Pattern.compile("\"zcj\":\"[0-9]+");
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				String scoreStr = m.group().split(":\"")[1];
				int score = Integer.parseInt(scoreStr);
				scores.get(i).setScore(score);
				i++;
			}
			//抓取课程学分
			p = Pattern.compile("\"xf\":\"[0-9]+[^\"&&(.)]?[0-9]?");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String creditStr = m.group().split(":\"")[1];
				double credit = Double.parseDouble(creditStr);
				scores.get(i).setCredit(credit);
				i++;
			}
			//抓取课程绩点
			p = Pattern.compile("\"cjjd\":\"[0-9]+[^\"&&(.)]?[0-9]?");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String gpStr = m.group().split(":\"")[1];
				double gradePoint = Double.parseDouble(gpStr);
				scores.get(i).setGradePoint(gradePoint);
				i++;
			}
			//对抓取的数据的处理
			for (Score score : scores) {
				System.out.println(score);
			}
			double GPA = countGPA(scores);
			System.out.println(GPA);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//计算平均绩点
	private double countGPA(List<Score> courses) {
		double allCreditScoreSum = 0.0;
		double allCreditSum = 0.0;
		
		for (int i = 0; i < courses.size(); i++) {
			Score course = courses.get(i);
			allCreditScoreSum +=  course.getCredit() * course.getGradePoint();
			allCreditSum += course.getCredit();
		}
		return allCreditScoreSum / allCreditSum;
	}
	
	//查询课表
	public void queryCourseChart(String yearSemesterCode, int week) {
		HttpGet courseChartGet = new HttpGet(HOST + COURSE_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
		courseChartGet.addHeader("cookie", cookie);
		courseChartGet.addHeader("Referer", HOST + COURSE_REFERER_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
		try {
			CloseableHttpResponse courseChartResponse = client.execute(courseChartGet);
			HttpEntity entity = courseChartResponse.getEntity();
			String content = EntityUtils.toString(entity);
			List<Course> courses = new ArrayList<Course>();
			//抓取课程名称
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z]]*");
			Matcher m = p.matcher(content);
			while (m.find()) {
				String courseName = m.group().split(":\"")[1];
				Course course = new Course();
				course.setCourseName(courseName);
				courses.add(course);
			}
			//抓取教师名字
			p = Pattern.compile("\"teaxms\":\"[^\\x00-\\xff[a-zA-Z]]*");
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				String teacherName = m.group().split(":\"")[1];
				courses.get(i).setTeacherName(teacherName);
				i++;
			}
			//抓取周次
			p = Pattern.compile("\"zc\":\"[0-9]*");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekNum = m.group().split(":\"")[1];
				courses.get(i).setWeekNum(weekNum);
				i++;
			}
			//抓取周几
			p = Pattern.compile("\"xq\":\"[0-9]*");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekDay = m.group().split(":\"")[1];
				courses.get(i).setWeekDay(weekDay);
				i++;
			}
			//抓取节次
			p = Pattern.compile("\"jcdm2\":\"[0-9]{2},[0-9]{2}");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String section = m.group().split(":\"")[1];
				courses.get(i).setSection(section);
				i++;
			}
			//抓取教室
			p = Pattern.compile("\"jxcdmc\":\"[^\\x00-\\xff[a-zA-z]]*(-)?[0-9]*");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String classroom = m.group().split(":\"")[1];
				courses.get(i).setClassroom(classroom);
				i++;
			}
			for (Course course : courses) {
				System.out.println(course);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JWGL jwgl = new JWGL("3114004047", "jy123456");
		if (jwgl.login()) {
			jwgl.queryResult("201602");
			jwgl.queryCourseChart("201701", 16);
		}
	}
	
	
}
