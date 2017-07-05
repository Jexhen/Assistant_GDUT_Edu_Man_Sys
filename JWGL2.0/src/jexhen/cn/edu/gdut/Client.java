package jexhen.cn.edu.gdut;

public class Client {
	private Login login = new Login();

	private void doLogin() {
		login.open();
	}
	
	public static void main(String[] args) {
		new Client().doLogin();
	}
}
