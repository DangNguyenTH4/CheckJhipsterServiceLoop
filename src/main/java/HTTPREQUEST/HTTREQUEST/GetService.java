package HTTPREQUEST.HTTREQUEST;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetService {
	private String token = "";
	private static String host,protocal,port,domain;
	static Logger logger = LoggerFactory.getLogger(GetService.class);
	
	static {
		try(InputStream fis =  App.class.getResourceAsStream("vars.properties")){
    		Properties prop = new Properties();
        	prop.load(fis);
        	host=prop.getProperty("host");
        	protocal=prop.getProperty("protocal");
        	port = prop.getProperty("port");
        	domain=protocal+"://"+host+":"+port;
        	logger.info(domain);
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
		
	}
	public String getToken() throws ParseException, IOException {
		String api = domain+"/api/authenticate";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost request = new HttpPost(api);
			request.addHeader("Accept", "application/json, text/plain, */*");
			request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0");
//	        	 request.addHeader("Authorization",token);
			request.addHeader("Content-Type", "application/json");

			StringBuilder json = new StringBuilder();
			json.append("{");
			json.append("\"username\":\"admin\",");
			json.append("\"password\":\"admin\",");
			json.append("\"rememberMe\":\"false\"");
			json.append("}");

			request.setEntity(new StringEntity(json.toString()));

			CloseableHttpResponse response = httpClient.execute(request);
			try {

				HttpEntity entity = response.getEntity();
				if (entity != null) {
					JSONObject js = new JSONObject(EntityUtils.toString(entity));
					token = (String) js.get("id_token");
					logger.info("Get token success: " + token);
				}

			} finally {
				response.close();
			}
		}catch (Exception e) {
			logger.info("Connection Error! Wait... to connect again!");
			logger.error(e.getMessage());
		}
		finally {
			httpClient.close();
		}

		return token;
	}

	// Get Service was connect
	public JSONArray getService() throws IOException {
		String api = domain+"/api/eureka/applications";
		JSONArray js1 = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(api);
			request.addHeader("Accept", "application/json, text/plain, */*");
			request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0");
			request.addHeader("Authorization", "Bearer " + token);
			CloseableHttpResponse response = httpClient.execute(request);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					JSONObject js = new JSONObject(EntityUtils.toString(entity));
  					js1 = ((JSONArray) js.get("applications"));
				}

			}catch (Exception e) {
				e.printStackTrace();
				js1 = new JSONArray();
			}
			
			finally {
				response.close();
			}
		}catch (HttpHostConnectException e) {
//			e.printStackTrace();
			logger.info("Get token again");
			getToken();
			js1 = new JSONArray();
			logger.error(e.getMessage());
		}
		
		finally {
			httpClient.close();
		}
		return js1;
	}

	public boolean checkEnoughService() throws IOException {
		JSONArray js = getService();
		return js.length() == 9;
	}

	String[] CONST = {"WAMCLOUDGATEWAY","WAMCLOUDMASTERDATASERVICE",
						"WAMCLOUDMONITORINGSERVICE","WAMCLOUDNOTIFICATIONSERVICE",
						"WAMCLOUDQUEUESERVICE","WAMCLOUDRULESERVICE",
						"WAMCLOUDSAMPLESERVICE","WAMCLOUDTENANTSERVICE","WAMCLOUDUSERSERVICE"};
	private List<String> initArray(){
		List<String> arr = new ArrayList<String>();
		for(int i = 0;i<CONST.length;i++) {
			arr.add(CONST[i]);
		}
		return arr;
	}
	public List<String> getServiceDie() throws IOException{
			List<String> listFixedService = initArray();
	    	JSONArray js = getService();
	    	List<String> services = new ArrayList<String>();
	    	for(int i = 0 ;i<js.length();i++) {
	    		String temp = (String) ((JSONObject)js.get(i)).get("name");
	    		services.add(temp);
	    	}
	    	listFixedService.removeAll(services);
	    	return listFixedService;
	    }
}
