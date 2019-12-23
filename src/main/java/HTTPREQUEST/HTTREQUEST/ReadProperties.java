package HTTPREQUEST.HTTREQUEST;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	private static Properties props;

	private ReadProperties() {

		System.out.println("Init props");
		try (InputStream fis = App.class.getResourceAsStream("vars.properties")) {
			props = new Properties();
			props.load(fis);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Properties getInstance() {
		if (props == null) {
			System.out.println("Init props");
			try (InputStream fis = App.class.getResourceAsStream("vars.properties")) {
				props = new Properties();
				props.load(fis);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}

	public static String getProperty(String propertyName) {
		String result = getInstance().getProperty(propertyName);
		return result;
	}

	public static String setProperty(String propertyName, String value) {
		getInstance().setProperty(propertyName, value);
		return getProperty(propertyName);
	}
}
