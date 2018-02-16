package com.learn.SecurePersistentConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Hello world!
 *
 */
public class App 
{
	public static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

	public static void main( String[] args )
	{
		System.out.println( "Hello World!" );
		AsyncHttpClinetHandler handler =new AsyncHttpClinetHandler();
	}
}
