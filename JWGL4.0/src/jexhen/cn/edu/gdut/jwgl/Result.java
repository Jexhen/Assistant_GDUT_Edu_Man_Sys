package jexhen.cn.edu.gdut.jwgl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Result {

	protected Shell resultShell;//成绩的主界面
	private JWGL jwgl;//大管家，全部查询操作均由它执行
	private Table table;//显示查询结果的标
	private Text GPABar;//显示查询得到的绩点
	
	
	public Result(JWGL jwgl) {
		this.jwgl = jwgl;
	}


	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		resultShell.open();
		resultShell.layout();
		while (!resultShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		resultShell = new Shell(SWT.CLOSE|SWT.MIN);//不能更改界面的大小
		resultShell.setToolTipText("2016-2017");//默认查询学期
		resultShell.setSize(450, 379);
		resultShell.setText("\u67E5\u8BE2\u6210\u7EE9");
		
		//选择需要查询成绩的学年的下拉栏
		Combo schoolYear = new Combo(resultShell, SWT.NONE);
		schoolYear.setItems(new String[] {"2017-2018", "2016-2017", "2015-2016", "2014-2015", "2013-2014", "2012-2013", "2011-2012", "2010-2011"});
		schoolYear.setBounds(117, 10, 88, 25);
		schoolYear.select(1);
		
		//选择选哟查询成绩的学期的下拉栏
		Combo semester = new Combo(resultShell, SWT.NONE);
		semester.setItems(new String[] {"1", "2"});
		semester.setBounds(233, 10, 88, 25);
		semester.select(1);
		
		Button qureyBySemester = new Button(resultShell, SWT.NONE);
		qureyBySemester.addSelectionListener(new SelectionAdapter() {
			//查询按钮被点击
			@Override
			public void widgetSelected(SelectionEvent e) {
				//获取下拉栏的学年和学期
				String schoolYearStr = schoolYear.getText();
				String semesterStr = semester.getText();
				//将学年学期转化为学年学期代码
				String yearSemesterCode = getYearSemesterCode(schoolYearStr, semesterStr);
				showResult(jwgl.getScores(yearSemesterCode));//根据学年学期代码查询结果，并显示结果到界面
			}
		});
		qureyBySemester.setBounds(10, 43, 80, 27);
		qureyBySemester.setText("\u6309\u5B66\u671F\u67E5\u8BE2");
		
		Button queryByYear = new Button(resultShell, SWT.NONE);
		queryByYear.addSelectionListener(new SelectionAdapter() {
			//查询按钮被点击
			@Override
			public void widgetSelected(SelectionEvent e) {
				int schoolYearNum = 0;
				String year = schoolYear.getText();//获取学年下拉栏的信息
				//根据信息获得学年
				if (year.equals("2017-2018")) {
					schoolYearNum = 2017;
				} else if (year.equals("2016-2017")) {
					schoolYearNum = 2016;
				} else if (year.equals("2015-2016")) {
					schoolYearNum = 2015;
				} else if (year.equals("2014-2015")) {
					schoolYearNum = 2014;
				} else if (year.equals("2013-2014")) {
					schoolYearNum = 2013;
				} else if (year.equals("2012-2013")) {
					schoolYearNum = 2012;
				} else if (year.equals("2011-2012")) {
					schoolYearNum = 2011;
				} else if (year.equals("2010-2011")) {
					schoolYearNum = 2011;
				}
				showResult(jwgl.getScores(schoolYearNum));//根据学年查询成绩，并且成绩显示到界面
			}
		});
		
		queryByYear.setBounds(175, 43, 80, 27);
		queryByYear.setText("\u6309\u5B66\u5E74\u67E5\u8BE2");
		
		Button queryAll = new Button(resultShell, SWT.NONE);
		queryAll.setToolTipText("");
		queryAll.addSelectionListener(new SelectionAdapter() {
			//“全部查询”的按钮被点击
			@Override
			public void widgetSelected(SelectionEvent e) {
				//获得当前年份
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				int endYear  = Integer.parseInt(sdf.format(new Date()));
				//以当前年份向前查询成绩，并且将结果显示到界面
				showResult(jwgl.getAllScores(endYear));
			}
		});
		queryAll.setBounds(344, 43, 80, 27);
		queryAll.setText("\u5168\u90E8\u67E5\u8BE2");
		
		//显示查询结果的表格
		table = new Table(resultShell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 105, 434, 225);
		
		//表格表头
		TableColumn courseNameCol = new TableColumn(table, SWT.NONE);
		courseNameCol.setWidth(214);
		courseNameCol.setText("\u8BFE\u7A0B\u540D\u79F0");
		
		//表格表头
		TableColumn scoreCol = new TableColumn(table, SWT.NONE);
		scoreCol.setWidth(216);
		scoreCol.setText("\u8BFE\u7A0B\u6210\u7EE9");
		
		//显示平均绩点的text
		GPABar = new Text(resultShell, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		GPABar.setEditable(false);
		GPABar.setBounds(0, 76, 434, 23);

	}
	
	/**
	 * 根据所选择的学年和学期，转化成对应的学年学期代码
	 * @param year 学年
	 * @param semester 学期
	 * @return 返回转化后的学年学期代码
	 */
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
	
	/**
	 * 将查询到的结果显示到界面
	 * @param scores 查询到的成绩结果集
	 */
	private void showResult(java.util.List<Score> scores) {
		//每次都需要将前面的查询结果清空重画
		table.removeAll();
		table.redraw();
		//将成绩结果显示到表格中
		for (Score score : scores) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {score.getCourseName(), "" + score.getScore()});
		}
		//将平均绩点显示到text中
		GPABar.setText("平均绩点: " + jwgl.countGPA(scores));
	}
}
