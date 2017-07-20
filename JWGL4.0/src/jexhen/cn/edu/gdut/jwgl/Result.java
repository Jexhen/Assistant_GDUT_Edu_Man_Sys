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

	protected Shell resultShell;//�ɼ���������
	private JWGL jwgl;//��ܼң�ȫ����ѯ����������ִ��
	private Table table;//��ʾ��ѯ����ı�
	private Text GPABar;//��ʾ��ѯ�õ��ļ���
	
	
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
		resultShell = new Shell(SWT.CLOSE|SWT.MIN);//���ܸ��Ľ���Ĵ�С
		resultShell.setToolTipText("2016-2017");//Ĭ�ϲ�ѯѧ��
		resultShell.setSize(450, 379);
		resultShell.setText("\u67E5\u8BE2\u6210\u7EE9");
		
		//ѡ����Ҫ��ѯ�ɼ���ѧ���������
		Combo schoolYear = new Combo(resultShell, SWT.NONE);
		schoolYear.setItems(new String[] {"2017-2018", "2016-2017", "2015-2016", "2014-2015", "2013-2014", "2012-2013", "2011-2012", "2010-2011"});
		schoolYear.setBounds(117, 10, 88, 25);
		schoolYear.select(1);
		
		//ѡ��ѡӴ��ѯ�ɼ���ѧ�ڵ�������
		Combo semester = new Combo(resultShell, SWT.NONE);
		semester.setItems(new String[] {"1", "2"});
		semester.setBounds(233, 10, 88, 25);
		semester.select(1);
		
		Button qureyBySemester = new Button(resultShell, SWT.NONE);
		qureyBySemester.addSelectionListener(new SelectionAdapter() {
			//��ѯ��ť�����
			@Override
			public void widgetSelected(SelectionEvent e) {
				//��ȡ��������ѧ���ѧ��
				String schoolYearStr = schoolYear.getText();
				String semesterStr = semester.getText();
				//��ѧ��ѧ��ת��Ϊѧ��ѧ�ڴ���
				String yearSemesterCode = getYearSemesterCode(schoolYearStr, semesterStr);
				showResult(jwgl.getScores(yearSemesterCode));//����ѧ��ѧ�ڴ����ѯ���������ʾ���������
			}
		});
		qureyBySemester.setBounds(10, 43, 80, 27);
		qureyBySemester.setText("\u6309\u5B66\u671F\u67E5\u8BE2");
		
		Button queryByYear = new Button(resultShell, SWT.NONE);
		queryByYear.addSelectionListener(new SelectionAdapter() {
			//��ѯ��ť�����
			@Override
			public void widgetSelected(SelectionEvent e) {
				int schoolYearNum = 0;
				String year = schoolYear.getText();//��ȡѧ������������Ϣ
				//������Ϣ���ѧ��
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
				showResult(jwgl.getScores(schoolYearNum));//����ѧ���ѯ�ɼ������ҳɼ���ʾ������
			}
		});
		
		queryByYear.setBounds(175, 43, 80, 27);
		queryByYear.setText("\u6309\u5B66\u5E74\u67E5\u8BE2");
		
		Button queryAll = new Button(resultShell, SWT.NONE);
		queryAll.setToolTipText("");
		queryAll.addSelectionListener(new SelectionAdapter() {
			//��ȫ����ѯ���İ�ť�����
			@Override
			public void widgetSelected(SelectionEvent e) {
				//��õ�ǰ���
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				int endYear  = Integer.parseInt(sdf.format(new Date()));
				//�Ե�ǰ�����ǰ��ѯ�ɼ������ҽ������ʾ������
				showResult(jwgl.getAllScores(endYear));
			}
		});
		queryAll.setBounds(344, 43, 80, 27);
		queryAll.setText("\u5168\u90E8\u67E5\u8BE2");
		
		//��ʾ��ѯ����ı��
		table = new Table(resultShell, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBounds(0, 105, 434, 225);
		
		//����ͷ
		TableColumn courseNameCol = new TableColumn(table, SWT.NONE);
		courseNameCol.setWidth(214);
		courseNameCol.setText("\u8BFE\u7A0B\u540D\u79F0");
		
		//����ͷ
		TableColumn scoreCol = new TableColumn(table, SWT.NONE);
		scoreCol.setWidth(216);
		scoreCol.setText("\u8BFE\u7A0B\u6210\u7EE9");
		
		//��ʾƽ�������text
		GPABar = new Text(resultShell, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		GPABar.setEditable(false);
		GPABar.setBounds(0, 76, 434, 23);

	}
	
	/**
	 * ������ѡ���ѧ���ѧ�ڣ�ת���ɶ�Ӧ��ѧ��ѧ�ڴ���
	 * @param year ѧ��
	 * @param semester ѧ��
	 * @return ����ת�����ѧ��ѧ�ڴ���
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
	 * ����ѯ���Ľ����ʾ������
	 * @param scores ��ѯ���ĳɼ������
	 */
	private void showResult(java.util.List<Score> scores) {
		//ÿ�ζ���Ҫ��ǰ��Ĳ�ѯ�������ػ�
		table.removeAll();
		table.redraw();
		//���ɼ������ʾ�������
		for (Score score : scores) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(new String[] {score.getCourseName(), "" + score.getScore()});
		}
		//��ƽ��������ʾ��text��
		GPABar.setText("ƽ������: " + jwgl.countGPA(scores));
	}
}
