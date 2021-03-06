package mhfc.net.common.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateSystem {

	public static final String urlString = "https://raw.githubusercontent.com/Heltrato/MHFC/master/versioncheck";
	public static String versionString;
	public static String newVersionStr;
	public static int newUpdate = 2;
	public static int noUpdate = 1;
	public static int offline = 0;

	public static void init(String version) {
		versionString = version;
	}

	public static int isUpdateAvailable() {
		try {

			BufferedReader versionFile = new BufferedReader(
					new InputStreamReader(new URL(urlString).openStream()));
			String curVersion = versionFile.readLine();
			newVersionStr = curVersion;
			versionFile.close();

			if (!curVersion.equals(versionString)) {
				System.out.println("New Update");
				return newUpdate;
			} else {
				System.out.println("No Update");
				return noUpdate;
			}
		} catch (Exception e) {
			return offline;
		}
	}
}
