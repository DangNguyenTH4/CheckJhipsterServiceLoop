package HTTPREQUEST.HTTREQUEST;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoopCheckService implements Runnable {
	private GetService getService;
	Notification n = null;
	static Logger logger = LoggerFactory.getLogger(LoopCheckService.class);
	
	public LoopCheckService(GetService getService) {
		this.getService = getService;
		n = new Notification();
	}

	public void run() {
		boolean enoughService = Boolean.TRUE;
		while (true) {
			try {
				Thread.sleep(5000);
				enoughService = getService.checkEnoughService();
				logger.info("Check service.");

				if (!enoughService) {
					logger.info("Service die: " + new Date());
//					notify1();
					List<String> listServiceDead = getService.getServiceDie();
					String message = listServiceDead.size()+"--"+listServiceDead.toString();
					n.displayNotify(message);
					logger.info(message);
					Thread.sleep(30000);
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}

	}

	private void notify1() throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("C:\\Windows\\notepad.exe C:\\test.txt");
	}

}
