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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JWGL {
	private final static String HOST = "http://222.200.98.147";//ѧУ����ϵͳ��ַ
	private final static String VERIFY_CODE_URI = "/yzm";//��ȡ��֤���URI
	private final static String LOGIN_URI = "/login!doLogin.action";//�����¼�����URI
	private final static String HOME_URI = "/login!welcome.action";//��ҳ��URI
	private final static String SCORE_REFERER_URI = "/xskccjxx!xskccjList.action";//��Ҫ���͵���ѯ�ɼ���Referer
	private final static String SCORE_URI = "/xskccjxx!getDataList.action";//�����ѯ�ɼ������URI
	private final static String COURSE_REFERER_URI = "/xsgrkbcx!xskbList.action";//��Ҫ���͵���ѯ�α��Referer
	private final static String COURSE_URI = "/xsgrkbcx!getKbRq.action";//�����ѯ�α������URI
	private String usrname;//ѧ��
	private String password;//����
	private String verifyCode;//��֤��
	private static CloseableHttpClient client = HttpClients.createDefault();//HttpClient
	private String stuName;
	
	
	public JWGL(String usrname, String password, String verifyCode) {
		this.usrname = usrname;
		this.password = password;
		this.verifyCode = verifyCode;
	}
	
	/**
	 * ��ȡ��֤��
	 */
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
	 * ���ݻ�õ�cookie���Խ�����ҳ
	 * @return ����״̬��1����¼�ɹ���0���˺Ż����������-1������δ����
	 */
	public int login() {
		int statusCode = 0;
		HttpPost loginPost = null;
		HttpGet welGet = null;
		try {
			loginPost = new HttpPost(HOST + LOGIN_URI);
			//�ύ��post���Ĳ���
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			NameValuePair account = new BasicNameValuePair("account", usrname);
			NameValuePair pwd = new BasicNameValuePair("pwd", password);
			NameValuePair verifycode = new BasicNameValuePair("verifycode", verifyCode);
			nameValuePairs.add(account);
			nameValuePairs.add(pwd);
			nameValuePairs.add(verifycode);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			loginPost.setEntity(entity);
			client.execute(loginPost);
			//����Get
			welGet = new HttpGet(HOST + HOME_URI);
			//Ϊget���ñ�Ҫ��Header
			welGet.setHeader("Referer", HOST);
			//������Ӧresponse
			HttpResponse welResponse = client.execute(welGet);
			HttpEntity httpEntity = welResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);//��ȡhtml����
			//������֪�����¼�ɹ��õ���html���ݰ�����¼ʱ�䣬�����ж����޵�¼�ɹ������ж������Ƿ��������¼ʱ�䡱���Ӵ�
			if (content.contains("��¼ʱ��")) {
				statusCode = 1;
				Matcher m =  Pattern.compile("\"top\">[\\s]*[^\\x00-\\xff[a-zA-Z]]*").matcher(content);
				if (m.find()) {
					stuName = m.group().replaceAll("\"top\">\\s+", "");
					System.out.println(stuName);
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
	 * ����ѧ��ѧ�ڴ�����html��������ץȡ������Ϣ����������Ϣ��װ��ÿһ��Score������
	 * @param yearSemesterCode ѧ��ѧ�ڴ���
	 * @return ����һ����Score���󹹳ɵ�List
	 */
	private List<Score> queryResult(String yearSemesterCode) {
		List<Score> scores = new ArrayList<Score>();//�洢ץȡ���Ŀγ̳ɼ�
		HttpPost queryResultPost = null;
		try {
			//����post����
			queryResultPost = new HttpPost(HOST + SCORE_URI);
			//���һЩ��Ҫ��Header��Ϣ
			queryResultPost.addHeader("Referer", HOST + SCORE_REFERER_URI + "?firstquery=" + 1);
			//��Ҫ�ύ�ı�����
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
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			queryResultPost.setEntity(entity);//����������װ��Post����
			//���Ի��Response
			HttpResponse queryResultResponse = client.execute(queryResultPost);
			HttpEntity httpEntity = queryResultResponse.getEntity();
			String content = EntityUtils.toString(httpEntity);//��ȡ��õ�html����
			//��Gson��ȡjson�����е���Чֵ
			JsonParser parser = new JsonParser();
			JsonElement jsonEl = parser.parse(content);
			JsonObject jsonObj = jsonEl.getAsJsonObject();
			JsonArray scoreArr = jsonObj.get("rows").getAsJsonArray();
			for (int i = 0; i < scoreArr.size(); i++) {
				JsonElement jsonElement = scoreArr.get(i);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				Score scoreBean = new Score();
				//��ȡ�γ�����
				String courseName = jsonObject.get("kcmc").toString();
				courseName = deleteQuote(courseName);
				scoreBean.setCourseName(courseName);
				//��ȡ�γ̳ɼ�
				String score = jsonObject.get("zcj").toString();
				score = deleteQuote(score);
				scoreBean.setScore(score);
				//��ȡ�γ�ѧ��
				String credit = jsonObject.get("xf").toString();
				scoreBean.setCredit(Double.parseDouble(deleteQuote(credit)));
				//��ȡ�γ̼���
				String gradePoint = jsonObject.get("cjjd").toString();
				scoreBean.setGradePoint(Double.parseDouble(deleteQuote(gradePoint)));
				//��ȡ�γ�����
				String courseType = jsonObject.get("kcdlmc").toString();
				courseType = deleteQuote(courseType);
				scoreBean.setCourseType(courseType);
				scores.add(scoreBean);//����ȡ�Ŀγ̳ɼ���ӵ����ϵ���
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
	 * ����ƽ������
	 * @param scores ��Ҫ���㼨������гɼ���Score����List
	 * @return double�͵�ƽ������ֵ
	 */
	public double countGPA(List<Score> scores) {
		double allCreditScoreSum = 0.0;
		double allCreditSum = 0.0;
		
		for (int i = 0; i < scores.size(); i++) {
			Score course = scores.get(i);
			String courseType = course.getCourseType();
			if (courseType.contains("˫רҵ") || courseType.contains("˫ѧλ") || courseType.contains("����"))
				continue;
			else {
				allCreditScoreSum +=  course.getCredit() * course.getGradePoint();
				allCreditSum += course.getCredit();
			}
		}
		return allCreditScoreSum / allCreditSum;
	}
	
	/**
	 * ����ѧ��ѧ�ڴ���Ͳ�ѯ��������ѯ�α�
	 * @param yearSemesterCode ѧ��ѧ�ڴ���
	 * @param week ���ѯ������
	 * @return һ���ɶ���γ̣�Course������ɵ�List
	 */
	public List<Course> queryCourseChart(String yearSemesterCode, int week) {
		List<Course> courses = new ArrayList<Course>();//�洢�γ̵�List
		HttpGet courseChartGet = null;
		try {
			//����get
			courseChartGet = new HttpGet(HOST + COURSE_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
			//Ϊget���һЩ��Ҫ��Header����
			courseChartGet.addHeader("Referer", HOST + COURSE_REFERER_URI + "?xnxqdm="+ yearSemesterCode +"&zc=" + week);
			//���շ��������ص�Response
			CloseableHttpResponse courseChartResponse = client.execute(courseChartGet);
			HttpEntity entity = courseChartResponse.getEntity();
			String content = EntityUtils.toString(entity);//���Զ�ȡ����
			//��Gson��ȡjson�����е���Чֵ
			JsonParser parser = new JsonParser();
			JsonElement jsonEl = parser.parse(content);
			JsonArray jsonArray = jsonEl.getAsJsonArray();
			JsonElement courseArrEle = jsonArray.get(0);
			JsonArray courseArr = courseArrEle.getAsJsonArray();
			for (int i = 0; i < courseArr.size(); i++) {
				JsonElement jsonElement = courseArr.get(i);
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				Course course = new Course();
				//��ȡ�γ�����
				String courseName = jsonObject.get("kcmc").toString();
				courseName = deleteQuote(courseName);
				course.setCourseName(courseName);
				//��ȡ��ʦ����
				String teacherName = jsonObject.get("teaxms").toString();
				teacherName = deleteQuote(teacherName);
				course.setTeacherName(teacherName);
				//��ȡ�ܴ�
				String weekNum = jsonObject.get("zc").toString();
				weekNum = deleteQuote(weekNum);
				course.setWeekNum(weekNum);
				//��ȡ�ܼ�
				String weekDay = jsonObject.get("xq").toString();
				weekDay = deleteQuote(weekDay);
				course.setWeekDay(weekDay);
				//��ȡ�ڴ�
				String section = jsonObject.get("jcdm2").toString();
				section = deleteQuote(section);
				course.setSection(section);
				//��ȡ����
				String classroom = jsonObject.get("jxcdmc").toString();
				classroom = deleteQuote(classroom);
				course.setClassroom(classroom );
				courses.add(course);
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
	 * �������õĻ�ȡ�ɼ��ķ���,���������ڻ�ȡһ��ѧ��ĳɼ�
	 * @param schoolYear ��Ҫ��ѯ�ɼ�������ѧ��
	 * @return ���ز�ѯ�����
	 */
	public List<Score> getScores(int schoolYear) {
		List<Score> scores = new ArrayList<Score>();//�洢���в�ѯ�����List
		List<Score> temp = queryResult("" + schoolYear + "01");//��ѯ��ѧ���һѧ�ڵĳɼ�
		scores.addAll(temp);//��һѧ�ڵ����н����������ؽ����List��
		temp.clear();//�ظ�������ʱ����ѧ�ڽ����List,�ڶ��ε���ʱӦ�������һ�εĽ��
		temp = queryResult("" + schoolYear + "02");//��ѯ��ѧ��ڶ�ѧ�ڵĽ��
		scores.addAll(temp);//���ڶ�ѧ�ڵĲ�ѯ���ȫ���Ž����ս����
		return scores;
	}
	
	/**
	 * ���ع������õĻ�ȡ�ɼ��ķ���,���������ڻ�ȡһ��ѧ�ڵĳɼ�
	 * @param yearSemesterCode ��Ҫ��ѯ��ѧ��ѧ�ڴ���
	 * @return
	 */
	public List<Score> getScores(String yearSemesterCode) {
		List<Score> scores = queryResult(yearSemesterCode);
		return scores;
	}
	
	/**
	 * ���ݵ�ǰ��ݴӺ���ǰ��ѯ���гɼ�
	 * @param endYear
	 * @return
	 */
	public List<Score> getAllScores(int endYear) {
		List<Score> scores = new ArrayList<Score>();
		List<Score> temp = null;
		int flag = 0;//���ڵ�һ��û�ҵ��ļ�����ǰ��̽��־���������̽3��
		//����ҵ����ǿյļ�����ǰ��̽�������ֻ����̽3��
		for (int i = endYear; !((temp=getScores(i)).isEmpty()) || flag < 3; i--) {
			if (!temp.isEmpty())
				scores.addAll(temp);
			flag++;
		}
		return scores;
	}
	
	/**
	 * ��ȡ��¼�ɹ�������
	 * @return String����
	 */
	public String getStuName() {
		return stuName;
	}
	
	private String deleteQuote(String s) {
		return s.replaceAll("\"", "");
	}
	
	/*public static void main(String[] args) {
		
	}*/

	
}
