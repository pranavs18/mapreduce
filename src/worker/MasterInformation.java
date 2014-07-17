package worker;

public class MasterInformation {
	
	private static String masterHost;

	public static String getMasterHost() {
		return masterHost;
	}

	public static void setMasterHost(String masterHost) {
		MasterInformation.masterHost = masterHost;
	}

}
