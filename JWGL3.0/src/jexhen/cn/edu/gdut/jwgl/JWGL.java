package jexhen.cn.edu.gdut.jwgl;

import java.io.File;
import java.io.FileOutputStream;
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
	private final static String HOST = "http://222.200.98.147";//学校教务系统地址
	private final static String VERIFY_CODE_URI = "/yzm";
//	private final static String NEED_VERIFY_URI = "/login!isNeedVerify.action";
	private final static String LOGIN_URI = "/login!doLogin.action";//处理登录请求的URI
	private final static String HOME_URI = "/login!welcome.action";//主页的URI
	private final static String SCORE_REFERER_URI = "/xskccjxx!xskccjList.action";//需要发送到查询成绩的Referer
	private final static String SCORE_URI = "/xskccjxx!getDataList.action";//处理查询成绩请求的URI
	private final static String COURSE_REFERER_URI = "/xsgrkbcx!xskbList.action";//需要发送到查询课表的Referer
	private final static String COURSE_URI = "/xsgrkbcx!getKbRq.action";//处理查询课表请求的URI
	private String usrname;//学号
	private String password;//密码
	private String verifyCode;//验证码
//	private String cookie;//每次请求发送的cookie
	private static CloseableHttpClient client = HttpClients.createDefault();//HttpClient
	private String stuName;
	
	
	
	public JWGL(String usrname, String password, String verifyCode) {
		this.usrname = usrname;
		this.password = password;
		this.verifyCode = verifyCode;
//		this.cookie = getCookieOfVerify();//根据用户名和密码获取cookie
	}
	
	/**
	 * 用于获取每次都需发送的cookie
	 * @return cookie字符串
	 */
	/*private String getCookie() {
		HttpPost loginPost = null;
		try {
			loginPost = new HttpPost(HOST + LOGIN_URI);
			//提交的post表单的参数
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			NameValuePair account = new BasicNameValuePair("account", usrname);
			NameValuePair pwd = new BasicNameValuePair("pwd", password);
			NameValuePair verifycode = new BasicNameValuePair("verifycode", verifyCode);
			nameValuePairs.add(account);
			nameValuePairs.add(pwd);
			nameValuePairs.add(verifycode);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			loginPost.setEntity(entity);
			HttpResponse loginResponse = client.execute(loginPost);//执行提交
			//如果得到的cookie是空的，用户可能没联网，直接返回null
			if (loginResponse.getFirstHeader("Set-Cookie")==null) {
				return null;
			}
			String cookieLine = loginResponse.getFirstHeader("Set-Cookie").getValue();//返回的cookie行
			cookie = cookieLine.split(";")[0];//单提取cookie
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (loginPost != null) {
				loginPost.abort();
			}
		}
		return cookie;
	}
	
	private String getCookieOfVerify() {
		HttpGet cookieGet = null;
		String cookie = null;
		try {
			cookieGet = new HttpGet(HOST + NEED_VERIFY_URI);
			cookieGet.addHeader("Referer", HOST);
			CloseableHttpResponse cookieResponse = client.execute(cookieGet);
			cookie = cookieResponse.getFirstHeader("Set-Cookie").getValue();
			System.out.println(cookie);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cookie;
	}*/
	
	public static void getVerifyCode() {
		HttpGet verifyCodeGet = null;
		FileOutputStream out = null;
		try {
			verifyCodeGet = new HttpGet(HOST + VERIFY_CODE_URI);
			CloseableHttpResponse verifyCodeResponse = client.execute(verifyCodeGet);
			HttpEntity entity = verifyCodeResponse.getEntity();
			String filename = "verifycode.jpeg";
			out = new FileOutputStream(new File(filename));
			entity.writeTo(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (verifyCodeGet != null) {
				verifyCodeGet.abort();
			}
		}
	}
	
	/**
	 * 根据获得的cookie尝试进入主页
	 * @return 返回状态码1：登录成功；0：账号或者密码错误；-1：网络未连接
	 */
	public int login() {
		int statusCode = 0;
//		if (cookie == null) {
//			return -1;//cookie为空返回状态码-1，表示网络未连接
//		}
		HttpPost loginPost = null;
		HttpGet welGet = null;
		try {
			loginPost = new HttpPost(HOST + LOGIN_URI);
			//提交的post表单的参数
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			NameValuePair account = new BasicNameValuePair("account", usrname);
			NameValuePair pwd = new BasicNameValuePair("pwd", password);
			NameValuePair verifycode = new BasicNameValuePair("verifycode", verifyCode);
			nameValuePairs.add(account);
			nameValuePairs.add(pwd);
			nameValuePairs.add(verifycode);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			loginPost.setEntity(entity);
			/*HttpResponse loginResponse =*/ client.execute(loginPost);
			//构造Get
			welGet = new HttpGet(HOST + HOME_URI);
			//为get设置必要的Header
//			welGet.setHeader("Cookie", cookie);
			welGet.setHeader("Referer", HOST);
			//接收响应response
			HttpResponse welResponse = client.execute(welGet);
			HttpEntity httpEntity = welResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);//获取html内容
			//分析得知如果登录成功得到的html内容包含登录时间，所以判断有无登录成功秩序判断内容是否包含“登录时间”的子串
			if (content.contains("登录时间")) {
				statusCode = 1;
				Matcher m =  Pattern.compile("\"top\">[\\s]*[^\\x00-\\xff[a-zA-Z]]*").matcher(content);
				if (m.find()) {
					stuName = m.group().split("\"top\">[\\s]*")[1];
				}
			} else {
				statusCode = 0;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (loginPost != null) {
				loginPost.abort();
			}
			if (welGet != null) {
				welGet.abort();
			}
		}
		return statusCode;
	}
	
	/**
	 * 根据学年学期代码获得html，并丛中抓取有用信息，将有用信息封装到每一个Score对象中
	 * @param yearSemesterCode 学年学期代码
	 * @return 返回一个由Score对象构成的List
	 */
	private List<Score> queryResult(String yearSemesterCode) {
		List<Score> scores = new ArrayList<Score>();//存储抓取到的课程成绩
		HttpPost queryResultPost = null;
		try {
			//构造post请求
			queryResultPost = new HttpPost(HOST + SCORE_URI);
			//添加一些必要的Header信息
//			queryResultPost.addHeader("Cookie", cookie);
			queryResultPost.addHeader("Referer", HOST + SCORE_REFERER_URI + "?firstquery=" + 1);
			//需要提交的表单参数
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
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			queryResultPost.setEntity(entity);//将表单参数封装至Post请求
			//尝试获得Response
			HttpResponse queryResultResponse = client.execute(queryResultPost);
			HttpEntity httpEntity = queryResultResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);//读取获得的html内容
			//抓取课程名称
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z][0-9]]+(\\([^\\x00-\\xff[a-zA-Z][0-9]]+\\))?");
			Matcher m = p.matcher(content);
			while (m.find()) {
				Score score = new Score();
				String courseName = m.group().split(":\"")[1];
				score.setCourseName(courseName);
				scores.add(score);
			}
			//抓取课程成绩
			p = Pattern.compile("\"zcj\":\"[^\\x00-\\xff[a-zA-Z][0-9]]+");
			m = p.matcher(content);
			int i = 0;
			while (m.find()) {
				String score = m.group().split(":\"")[1];
				scores.get(i).setScore(score);
				i++;
			}
			//提取有用信息将其封装至Score对象中
			//抓取课程学分
			p = Pattern.compile("\"xf\":\"[0-9]+[^(\")&&(.)]?[0-9]?");
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
			//抓取课程类型
			p = Pattern.compile("\"kcdlmc\":\"[^\\x00-\\xff[a-zA-Z]]+");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String courseType = m.group().split(":\"")[1];
				scores.get(i).setCourseType(courseType);
				i++;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (queryResultPost != null) {
				queryResultPost.abort();
			}
		}
		return scores;
	}
	
	/**
	 * 计算平均绩点
	 * @param scores 需要计算绩点的所有成绩（Score对象）List
	 * @return double型的平均绩点值
	 */
	public double countGPA(List<Score> scores) {
		double allCreditScoreSum = 0.0;
		double allCreditSum = 0.0;
		
		for (int i = 0; i < scores.size(); i++) {
			Score course = scores.get(i);
			String courseType = course.getCourseType();
			if (courseType.contains("双专业") || courseType.contains("双学位") || courseType.contains("辅修"))
				continue;
			else {
				allCreditScoreSum +=  course.getCredit() * course.getGradePoint();
				allCreditSum += course.getCredit();
			}
		}
		return allCreditScoreSum / allCreditSum;
	}
	
	/**
	 * 根据学年学期代码和查询的周数查询课表
	 * @param yearSemesterCode 学年学期代码
	 * @param week 需查询的周数
	 * @return 一个由多个课程（Course对象）组成的List
	 */
	public List<Course> queryCourseChart(String yearSemesterCode, int week) {
		List<Course> courses = new ArrayList<Course>();//存储课程的List
		HttpGet courseChartGet = null;
		try {
			//生成get
			courseChartGet = new HttpGet(HOST + COURSE_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
			//为get添加一些必要的Header参数
//			courseChartGet.addHeader("cookie", cookie);
			courseChartGet.addHeader("Referer", HOST + COURSE_REFERER_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
			//接收服务器返回的Response
			CloseableHttpResponse courseChartResponse = client.execute(courseChartGet);
			HttpEntity entity = courseChartResponse.getEntity();
			String content = EntityUtils.toString(entity);//尝试读取内容
			//提取Html中一些有用信息
			//抓取课程名称
			Pattern p = Pattern.compile("\"kcmc\":\"[^\\x00-\\xff[a-zA-Z][0-9]]+(\\([^\\x00-\\xff[a-zA-Z][0-9]]+\\))?");
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
				String teacherName = "";
				//有些老师信息可能不存在，不存在的话抓取结果为"teaxms:"，所以不存在时split生成的数组的元素只有一个
				//数组元素只有一个时直接保存空串
				if (m.group().split(":\"").length >= 2) 
					teacherName = m.group().split(":\"")[1];
				courses.get(i).setTeacherName(teacherName);
				i++;
			}
			//抓取周次
			p = Pattern.compile("\"zc\":\"[0-9]+");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekNum = m.group().split(":\"")[1];
				courses.get(i).setWeekNum(weekNum);
				i++;
			}
			//抓取周几
			p = Pattern.compile("\"xq\":\"[0-9]+");
			m = p.matcher(content);
			i = 0;
			while (m.find()) {
				String weekDay = m.group().split(":\"")[1];
				courses.get(i).setWeekDay(weekDay);
				i++;
			}
			//抓取节次
			p = Pattern.compile("\"jcdm2\":\"[0-9,]+");
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
				String classroom = "";
				//同教师名字一样，课室信息有可能不存在，不存在的话抓取结果为"jxcdmc:"，所以不存在时split生成的数组的元素只有一个
				//数组元素只有一个时直接保存空串
				if (m.group().split(":\"").length >= 2)
					classroom =	m.group().split(":\"")[1];
				courses.get(i).setClassroom(classroom);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (courseChartGet != null) {
				courseChartGet.abort();
			}
		}
		return courses;
	}
	/**
	 * 供外界调用的获取成绩的方法,本方法用于获取一个学年的成绩
	 * @param schoolYear 需要查询成绩的所在学年
	 * @return 返回查询结果集
	 */
	public List<Score> getScores(int schoolYear) {
		List<Score> scores = new ArrayList<Score>();//存储所有查询结果的List
		List<Score> temp = queryResult("" + schoolYear + "01");//查询该学年第一学期的成绩
		scores.addAll(temp);//第一学期的所有结果添加至返回结果的List中
		temp.clear();//重复利用临时保存学期结果的List,第二次调用时应将清空上一次的结果
		temp = queryResult("" + schoolYear + "02");//查询该学年第二学期的结果
		scores.addAll(temp);//将第二学期的查询结果全部放进最终结果集
		return scores;
	}
	
	/**
	 * 重载供外界调用的获取成绩的方法,本方法用于获取一个学期的成绩
	 * @param yearSemesterCode 需要查询的学年学期代码
	 * @return
	 */
	public List<Score> getScores(String yearSemesterCode) {
		List<Score> scores = queryResult(yearSemesterCode);
		return scores;
	}
	
	/**
	 * 根据当前年份从后往前查询所有成绩
	 * @param endYear
	 * @return
	 */
	public List<Score> getAllScores(int endYear) {
		List<Score> scores = new ArrayList<Score>();
		List<Score> temp = null;
		int flag = 0;//用于第一次没找到的继续向前试探标志，但最多试探3次
		//如果找到的是空的继续向前试探，但最多只能试探3次
		for (int i = endYear; !((temp=getScores(i)).isEmpty()) || flag < 3; i--) {
			if (!temp.isEmpty())
				scores.addAll(temp);
			flag++;
		}
		return scores;
	}
	
	/**
	 * 获取登录成功的姓名
	 * @return String姓名
	 */
	public String getStuName() {
		return stuName;
	}
	
	/*public static void main(String[] args) {
		
	}*/

	
}
