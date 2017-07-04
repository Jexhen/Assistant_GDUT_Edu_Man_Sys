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
	
	//��¼
	public boolean login() {
		boolean isSuccess = false;
		try {
			HttpGet welGet = new HttpGet(HOST + HOME_URI);
			welGet.setHeader("Cookie", cookie);
			welGet.setHeader("Referer", HOST);
			HttpResponse welResponse = client.execute(welGet);
			HttpEntity httpEntity = welResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);
			if (content.contains("��¼ʱ��")) {
				System.out.println("��¼�ɹ���");
				isSuccess = true;
			} else {
				System.out.println("ѧ�Ż����������");
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
	
	//��ѯ�ɼ�
	public void queryResult(String yearSemesterCode) {
		HttpPost queryResultPost = new HttpPost(HOST + SCORE_URI);
		queryResultPost.addHeader("Cookie", cookie);
		queryResultPost.addHeader("Referer", HOST + SCORE_REFERER_URI + "?firstquery=" + 1);
		NameValuePair xnxq = new BasicNameValuePair("xnxqdm", yearSemesterCode);//ѧ��ѧ�ڴ���
		NameValuePair jhlx = new BasicNameValuePair("jhlxdm", "");//
		NameValuePair page = new BasicNameValuePair("page", "1");//�ڶ���ҳ
		NameValuePair rows = new BasicNameValuePair("rows", "50");//ÿҳ��ʾ������
		NameValuePair sort = new BasicNameValuePair("sort", "xnxqdm");//��������
		NameValuePair order = new BasicNameValuePair("order", "asc");//�������
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
			List<Score> scores = new ArrayList<Score>();//�洢ץȡ���Ŀγ̳ɼ�
			//ץȡ�γ�����
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z]]*");
			Matcher m = p.matcher(content);
			while (m.find()) {
				Score score = new Score();
				String courseName = m.group().split(":\"")[1];
				score.setCourseName(courseName);
				scores.add(score);
			}
			//ץȡ�γ̳ɼ�
			p = Pattern.compile("\"zcj\":\"[0-9]+");
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				String scoreStr = m.group().split(":\"")[1];
				int score = Integer.parseInt(scoreStr);
				scores.get(i).setScore(score);
				i++;
			}
			//ץȡ�γ�ѧ��
			p = Pattern.compile("\"xf\":\"[0-9]+[^\"&&(.)]?[0-9]?");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String creditStr = m.group().split(":\"")[1];
				double credit = Double.parseDouble(creditStr);
				scores.get(i).setCredit(credit);
				i++;
			}
			//ץȡ�γ̼���
			p = Pattern.compile("\"cjjd\":\"[0-9]+[^\"&&(.)]?[0-9]?");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String gpStr = m.group().split(":\"")[1];
				double gradePoint = Double.parseDouble(gpStr);
				scores.get(i).setGradePoint(gradePoint);
				i++;
			}
			//��ץȡ�����ݵĴ���
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
	
	//����ƽ������
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
	
	//��ѯ�α�
	public void queryCourseChart(String yearSemesterCode, int week) {
		HttpGet courseChartGet = new HttpGet(HOST + COURSE_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
		courseChartGet.addHeader("cookie", cookie);
		courseChartGet.addHeader("Referer", HOST + COURSE_REFERER_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
		try {
			CloseableHttpResponse courseChartResponse = client.execute(courseChartGet);
			HttpEntity entity = courseChartResponse.getEntity();
			String content = EntityUtils.toString(entity);
			List<Course> courses = new ArrayList<Course>();
			//ץȡ�γ�����
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z]]*");
			Matcher m = p.matcher(content);
			while (m.find()) {
				String courseName = m.group().split(":\"")[1];
				Course course = new Course();
				course.setCourseName(courseName);
				courses.add(course);
			}
			//ץȡ��ʦ����
			p = Pattern.compile("\"teaxms\":\"[^\\x00-\\xff[a-zA-Z]]*");
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				String teacherName = m.group().split(":\"")[1];
				courses.get(i).setTeacherName(teacherName);
				i++;
			}
			//ץȡ�ܴ�
			p = Pattern.compile("\"zc\":\"[0-9]*");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekNum = m.group().split(":\"")[1];
				courses.get(i).setWeekNum(weekNum);
				i++;
			}
			//ץȡ�ܼ�
			p = Pattern.compile("\"xq\":\"[0-9]*");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekDay = m.group().split(":\"")[1];
				courses.get(i).setWeekDay(weekDay);
				i++;
			}
			//ץȡ�ڴ�
			p = Pattern.compile("\"jcdm2\":\"[0-9]{2},[0-9]{2}");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String section = m.group().split(":\"")[1];
				courses.get(i).setSection(section);
				i++;
			}
			//ץȡ����
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
