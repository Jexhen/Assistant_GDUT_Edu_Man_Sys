package jexhen.cn.edu.gdut.jwgl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class Login {

	protected Shell clientShell;//������
	private Label usrNameLabel;//��ʾ��ѧ�š��ı�ǩ
	private Text usrName;//ѧ�ŵ��ı���
	private Label passwordLabel;//��ʾ�����롱�ı�ǩ
	private Text password;//������ı���
	private Label authorLabel;//��������Ϣ��ǩ
	private JWGL jwgl;//ִ�����в�����JWGL��
	private Label loginTips;//��ʾ��¼��Ϣ�ı�ǩ
	private Label label;
	private Text verifyCode;
	private Canvas verifyCodeCanvas;

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
		clientShell = new Shell(SWT.CLOSE|SWT.MIN);//���ڲ��ɸı��С
		clientShell.setSize(450, 306);
		clientShell.setText("\u5E7F\u4E1C\u5DE5\u4E1A\u5927\u5B66\u6559\u52A1\u7BA1\u7406\u52A9\u624B");
		
		usrNameLabel = new Label(clientShell, SWT.NONE);
		usrNameLabel.setAlignment(SWT.CENTER);
		usrNameLabel.setBounds(109, 70, 61, 17);
		usrNameLabel.setText("\u5B66\u53F7");
		
		usrName = new Text(clientShell, SWT.BORDER);
		usrName.setBounds(176, 67, 127, 23);
		
		passwordLabel = new Label(clientShell, SWT.NONE);
		passwordLabel.setAlignment(SWT.CENTER);
		passwordLabel.setText("\u5BC6\u7801");
		passwordLabel.setBounds(109, 109, 61, 17);
		
		password = new Text(clientShell, SWT.BORDER | SWT.PASSWORD);
		password.addKeyListener(new KeyAdapter() {
			//��⵽���̵��Enterִ�е�¼����
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == 13) {
					doLogin();
				}
			}
		});
		password.setBounds(176, 109, 127, 23);
		
		Button loginBtn = new Button(clientShell, SWT.NONE);
		//��¼��ť�����ִ�е�¼����
		loginBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doLogin();
			}
		});
		loginBtn.setBounds(176, 203, 80, 27);
		loginBtn.setText("\u767B\u5F55");
		
		authorLabel = new Label(clientShell, SWT.NONE);
		authorLabel.setBounds(176, 256, 76, 17);
		authorLabel.setText("\u5ED6\u5FD7\u884C\u00A92017");
		
		loginTips = new Label(clientShell, SWT.NONE);
		loginTips.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		loginTips.setAlignment(SWT.CENTER);
		loginTips.setBounds(10, 180, 434, 17);
		
		label = new Label(clientShell, SWT.NONE);
		label.setText("\u9A8C\u8BC1\u7801");
		label.setAlignment(SWT.CENTER);
		label.setBounds(109, 154, 61, 17);
		
		verifyCode = new Text(clientShell, SWT.BORDER);
		verifyCode.setBounds(176, 151, 127, 23);
		
		verifyCodeCanvas = new Canvas(clientShell, SWT.NONE);
		verifyCodeCanvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				refresh();
			}
		});
		verifyCodeCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Image image = new Image(Display.getDefault(), "verifycode.jpeg");
				if (image != null) {
					e.gc.drawImage(image, 0, 0);
				}
			}
		});
		verifyCodeCanvas.setBounds(309, 114, 140, 60);
	}
	
	/**
	 * ִ�е�¼�ķ���
	 */
	private void doLogin() {
		//��ȡѧ�ź�����
		String usrNameStr = usrName.getText();
		String passwordStr = password.getText();
		String verifyCodeStr = verifyCode.getText(); 
		//ѧ�Ż�������Ϊ��
		if (usrNameStr.equals("")) {
			loginTips.setText("ѧ�Ų���Ϊ�գ�");
			return;
		}
		if (passwordStr.equals("")) {
			loginTips.setText("���벻��Ϊ�գ�");
			return;
		}
		//ѧ�ź����벻Ϊ�գ�����jwglִ�����в���
		jwgl = new JWGL(usrNameStr, passwordStr, verifyCodeStr);
		
		//��¼�ɹ��������˵�������������Ϊ���ɼ�
		int statusCode = jwgl.login();
		if (statusCode == 1) {
			loginTips.setText("��¼�ɹ���");
			clientShell.setVisible(false);
			new Menu(jwgl).open();
			//�û��˳�����������loginShellΪ�ɼ�
			loginTips.setText("");//��յ�¼��ʾ
			refresh();
			clientShell.setVisible(true);
		} else if (statusCode == 0){
			loginTips.setText("�û��������������");
			refresh();
		} else {
			loginTips.setText("�������Ӵ������������������ӣ�");
		}
	}
	
	private void refresh() {
		JWGL.getVerifyCode();
		verifyCodeCanvas.redraw();
	}
}
