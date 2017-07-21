package jexhen.cn.edu.gdut.jwgl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CourseChart {

	protected Shell courseChart;
	private Table courseTable;
	private JWGL jwgl;

	public CourseChart(JWGL jwgl) {
		this.jwgl = jwgl;
	}

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		courseChart.open();
		courseChart.layout();
		while (!courseChart.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		courseChart = new Shell(SWT.CLOSE|SWT.MIN);
		courseChart.setSize(737, 440);
		courseChart.setText("\u67E5\u8BE2\u8BFE\u7A0B");
		
		courseTable = new Table(courseChart, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		courseTable.setHeaderVisible(true);
		courseTable.setLinesVisible(true);
		courseTable.setBounds(10, 46, 711, 345);
		
		TableColumn tableColumn = new TableColumn(courseTable, SWT.CENTER);
		tableColumn.setWidth(76);
		tableColumn.setText("\u8282\u6B21");
		
		TableColumn tableColumn_1 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_1.setWidth(90);
		tableColumn_1.setText("\u5468\u4E00");
		
		TableColumn tableColumn_2 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_2.setWidth(90);
		tableColumn_2.setText("\u5468\u4E8C");
		
		TableColumn tableColumn_3 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_3.setWidth(90);
		tableColumn_3.setText("\u5468\u4E09");
		
		TableColumn tableColumn_4 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_4.setWidth(90);
		tableColumn_4.setText("\u5468\u56DB");
		
		TableColumn tableColumn_5 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_5.setWidth(90);
		tableColumn_5.setText("\u5468\u4E94");
		
		TableColumn tableColumn_6 = new TableColumn(courseTable, SWT.CENTER);
		tableColumn_6.setWidth(90);
		tableColumn_6.setText("\u5468\u516D");
		
		TableColumn tblclmnNewColumn = new TableColumn(courseTable, SWT.CENTER);
		tblclmnNewColumn.setWidth(90);
		tblclmnNewColumn.setText("\u5468\u65E5");
		
		Combo schoolYear = new Combo(courseChart, SWT.NONE);
		schoolYear.setItems(new String[] {"2017-2018", "2016-2017", "2015-2016", "2014-2015", "2013-2014", "2012-2013", "2011-2012", "2010-2011"});
		schoolYear.setBounds(178, 10, 88, 25);
		schoolYear.select(0);
		
		Label yearLabel = new Label(courseChart, SWT.NONE);
		yearLabel.setAlignment(SWT.CENTER);
		yearLabel.setBounds(111, 13, 61, 17);
		yearLabel.setText("\u5B66\u5E74");
		
		Label semesterLabel = new Label(courseChart, SWT.NONE);
		semesterLabel.setText("\u5B66\u671F");
		semesterLabel.setAlignment(SWT.CENTER);
		semesterLabel.setBounds(286, 13, 61, 17);
		
		Combo semester = new Combo(courseChart, SWT.NONE);
		semester.setItems(new String[] {"1", "2"});
		semester.setBounds(353, 10, 88, 25);
		semester.select(0);
		
		Combo week = new Combo(courseChart, SWT.NONE);
		week.setItems(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"});
		week.setBounds(536, 10, 88, 25);
		week.select(0);
		
		Label weekLable = new Label(courseChart, SWT.NONE);
		weekLable.setText("\u5468\u6B21");
		weekLable.setAlignment(SWT.CENTER);
		weekLable.setBounds(469, 13, 61, 17);
		
		Button queryBtn = new Button(courseChart, SWT.NONE);
		queryBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String year = schoolYear.getText();
				String sem = semester.getText();
				String yearSemesterCode = getYearSemesterCode(year, sem);
				int weekNum = Integer.parseInt(week.getText());
				List<Course> courses = jwgl.queryCourseChart(yearSemesterCode, weekNum);
				showCourseChart(courses);
			}
		});
		queryBtn.setBounds(630, 8, 80, 27);
		queryBtn.setText("\u67E5\u8BE2");

	}
	
	private String getYearSemesterCode(String year, String semester) {
		String yearSemesterCode = null;
		if (year.equals("2017-2018") && semester.equals("2")) {
			yearSemesterCode = "201702";
		} else if (year.equals("2017-2018") && semester.equals("1")) {
			yearSemesterCode = "201701";
		} else if (year.equals("2016-2017") && semester.equals("2")) {
			yearSemesterCode = "201602";
		} else if (year.equals("2016-2017") && semester.equals("1")) {
			yearSemesterCode = "201601";
		} else if (year.equals("2015-2016") && semester.equals("2")) {
			yearSemesterCode = "201502";
		} else if (year.equals("2015-2016") && semester.equals("1")) {
			yearSemesterCode = "201501";
		} else if (year.equals("2014-2015") && semester.equals("2")) {
			yearSemesterCode = "201402";
		} else if (year.equals("2014-2015") && semester.equals("1")) {
			yearSemesterCode = "201401";
		} else if (year.equals("2013-2014") && semester.equals("2")) {
			yearSemesterCode = "201302";
		} else if (year.equals("2013-2014") && semester.equals("1")) {
			yearSemesterCode = "201301";
		} else if (year.equals("2012-2013") && semester.equals("2")) {
			yearSemesterCode = "201202";
		} else if (year.equals("2012-2013") && semester.equals("1")) {
			yearSemesterCode = "201201";
		} else if (year.equals("2011-2012") && semester.equals("2")) {
			yearSemesterCode = "201102";
		} else if (year.equals("2011-2012") && semester.equals("1")) {
			yearSemesterCode = "201101";
		} else if (year.equals("2010-2011") && semester.equals("2")) {
			yearSemesterCode = "201002";
		} else if (year.equals("2010-2011") && semester.equals("1")) {
			yearSemesterCode = "201001";
		} 
		return yearSemesterCode;
	}
	
	private void showCourseChart(List<Course> courses) {
		String[][] courseStr = new String[14][8];
		for (Course course : courses) {
			int weekday = Integer.parseInt(course.getWeekDay());
			String[] sections = course.getSection().split(",");
			for (String section : sections) {
				int sectionNum = Integer.parseInt(section);
				//上午的课是奇数节课开始
				if (sectionNum < 6) {
					if (sectionNum % 2 == 1) {
						courseStr[sectionNum][weekday] = course.getCourseName();
					} else {
						courseStr[sectionNum][weekday] = course.getClassroom();
					}
				//下午的课是偶数节课开始
				} else {
					if (sectionNum % 2 == 0) {
						courseStr[sectionNum][weekday] = course.getCourseName();
					} else {
						courseStr[sectionNum][weekday] = course.getClassroom();
					}
				}
				
			}
		}
		courseTable.removeAll();
		courseTable.redraw();
		for (int i = 0; i < courseStr.length; i++) {
			if (i != 0)
				courseStr[i][0] = "" + i;
			TableItem tableItem = new TableItem(courseTable, SWT.NONE);
			tableItem.setText(courseStr[i]);
		}
	}
}
