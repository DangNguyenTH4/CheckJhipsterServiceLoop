package HTTPREQUEST.HTTREQUEST;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
	static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) throws ClientProtocolException, IOException, InterruptedException
    {
    	System.out.println(new Date());
    	GetService a = new GetService();
    	a.getToken();
    	LoopCheckService lcs = new LoopCheckService(a);
    	Thread t = new Thread(lcs);
    	t.start();
    	logger.info("Out main");
    }
    
   
    
}
