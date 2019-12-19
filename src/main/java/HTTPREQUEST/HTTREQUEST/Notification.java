package HTTPREQUEST.HTTREQUEST;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notification {
	public void displayNotify(String message) {
		SystemTray tray = SystemTray.getSystemTray();
		
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		
		TrayIcon trayIcon = new TrayIcon(image,"Traydemo");
		
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip(message);
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
		trayIcon.displayMessage("Service die", message, MessageType.WARNING);
//		System.out.println("Out notify");
	}
}
