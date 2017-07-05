package jexhen.cn.edu.gdut;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;


public class Login {

	protected Shell clientShell;//主窗口
	private Label usrNameLabel;//显示“学号”的标签
	private Text usrName;//学号的文本框
	private Label passwordLabel;//显示“密码”的标签
	private Text password;//密码的文本框
	private Label authorLabel;//开发者信息标签
	private JWGL jwgl;//执行所有操作的JWGL类
	private Label loginTips;//提示登录信息的标签

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		clientShell.open();
		clientShell.layout();
		while (!clientShell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		clientShell = new Shell(SWT.CLOSE|SWT.MIN);//窗口不可改变大小
		clientShell.setSize(450, 290);
		clientShell.setText("\u5E7F\u4E1C\u5DE5\u4E1A\u5927\u5B66\u6559\u52A1\u7BA1\u7406\u52A9\u624B");
		
		usrNameLabel = new Label(clientShell, SWT.NONE);
		usrNameLabel.setAlignment(SWT.CENTER);
		usrNameLabel.setBounds(109, 67, 61, 17);
		usrNameLabel.setText("\u5B66\u53F7");
		
		usrName = new Text(clientShell, SWT.BORDER);
		usrName.setBounds(176, 67, 127, 23);
		
		passwordLabel = new Label(clientShell, SWT.NONE);
		passwordLabel.setAlignment(SWT.CENTER);
		passwordLabel.setText("\u5BC6\u7801");
		passwordLabel.setBounds(109, 109, 61, 17);
		
		password = new Text(clientShell, SWT.BORDER | SWT.PASSWORD);
		password.addKeyListener(new KeyAdapter() {
			//检测到键盘点击Enter执行登录操作
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					doLogin();
				}
			}
		});
		password.setBounds(176, 109, 127, 23);
		
		Button loginBtn = new Button(clientShell, SWT.NONE);
		//登录按钮被点击执行登录操作
		loginBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doLogin();
			}
		});
		loginBtn.setBounds(176, 171, 80, 27);
		loginBtn.setText("\u767B\u5F55");
		
		authorLabel = new Label(clientShell, SWT.NONE);
		authorLabel.setBounds(180, 234, 76, 17);
		authorLabel.setText("\u5ED6\u5FD7\u884C\u00A92017");
		
		loginTips = new Label(clientShell, SWT.NONE);
		loginTips.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		loginTips.setAlignment(SWT.CENTER);
		loginTips.setBounds(0, 148, 434, 17);
	}
	
	/**
	 * 执行登录的方法
	 */
	private void doLogin() {
		//获取学号和密码
		String usrNameStr = usrName.getText();
		String passwordStr = password.getText();
		//学号或者密码为空
		if (usrNameStr.equals("")) {
			loginTips.setText("学号不能为空！");
			return;
		}
		if (passwordStr.equals("")) {
			loginTips.setText("密码不能为空！");
			return;
		}
		//学号和密码不为空，创建jwgl执行所有操作
		jwgl = new JWGL(usrNameStr, passwordStr);
		//登录成功，开启菜单并且设置自身为不可见
		int statusCode = jwgl.login();
		if (statusCode == 1) {
			loginTips.setText("登录成功！");
			clientShell.setVisible(false);
			new Menu(jwgl).open();
			//用户退出后重新设置loginShell为可见
			loginTips.setText("");//清空登录提示
			clientShell.setVisible(true);
		} else if (statusCode == 0){
			loginTips.setText("用户名或者密码错误！");
		} else {
			loginTips.setText("网络连接错误！请检查您的网络连接！");
		}
	}

}
