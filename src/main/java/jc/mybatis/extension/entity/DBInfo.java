package jc.mybatis.extension.entity;

public class DBInfo {

	private String url;
	
	private String user; 
	
	private String password; 
	
	private String driverClassName;

	public DBInfo() {
		super();
	}

	public DBInfo(String url, String user, String password, String driverClassName) {
		super();
		this.url = url;
		this.user = user;
		this.password = password;
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	
}
