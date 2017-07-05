package jexhen.cn.edu.gdut;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;

public class Menu {

	protected Shell Menushell;
	private JWGL jwgl;//�洢�����ȷcookie��jwgl

	public Menu(JWGL jwgl) {
		this.jwgl = jwgl;
	}
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		Menushell.open();
		Menushell.layout();
		while (!Menushell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		Menushell = new Shell(SWT.CLOSE|SWT.MIN);
		Menushell.setSize(450, 300);
		Menushell.setText("\u83DC\u5355");
		
		Button queryResultBtn = new Button(Menushell, SWT.NONE);
		queryResultBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new Result(jwgl).open();//��������ѯ�ɼ��İ�ť��������ѯ�ɼ���Shell
			}
		});
		
		queryResultBtn.setBounds(179, 78, 80, 27);
		queryResultBtn.setText("\u67E5\u8BE2\u6210\u7EE9");
		
		Button queryCourseChartBtn = new Button(Menushell, SWT.NONE);
		queryCourseChartBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new CourseChart(jwgl).open();//�����⵽�����ѯ�α�İ�ť��������ѯ�α��Shell
			}
		});
		queryCourseChartBtn.setText("\u67E5\u8BE2\u8BFE\u8868");
		queryCourseChartBtn.setBounds(179, 125, 80, 27);
		
		Button logOutBtn = new Button(Menushell, SWT.NONE);
		logOutBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Menushell.close();
			}
		});
		logOutBtn.setBounds(179, 172, 80, 27);
		logOutBtn.setText("\u9000\u51FA\u767B\u5F55");
		
		Label welLabel = new Label(Menushell, SWT.HORIZONTAL | SWT.CENTER);
		welLabel.setAlignment(SWT.CENTER);
		welLabel.setBounds(0, 40, 446, 17);
		welLabel.setText("��ӭ���� " + jwgl.getStuName() + "ͬѧ");
	}
}
