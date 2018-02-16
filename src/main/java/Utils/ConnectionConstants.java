package Utils;

public final class ConnectionConstants {
	private ConnectionConstants(){}
	public static final boolean Is_Secure_Endpoint=false;//get from property file
	public static final boolean Is_Proxy_Auth=false;//get from property file
	public static final String Proxy_Address="xx.xx.xx.xx";//get from property file
	public static final int Proxy_Port=Integer.parseInt("xxxx");//get from property file
	public static final String Proxy_UserName="xxxxxxxx";//get from property file
	public static final String Proxy_Password="xxxxxxxx";//get from property file

	public static final boolean Is_Log_Enalbled=false;//get from property file
	public static final String SSL_CERTIFICATE_KEY="path/to/certificate.jks";//get from property file

}
