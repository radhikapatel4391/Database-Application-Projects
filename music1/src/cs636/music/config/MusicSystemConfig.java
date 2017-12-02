package cs636.music.config;

import cs636.music.dao.AdminDAO;
import cs636.music.dao.DbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.LineItemDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.dao.UserDAO;
import cs636.music.service.AdminService;
import cs636.music.service.UserService;


public class MusicSystemConfig {

	public static final String SOUND_BASE_URL = "http://www.cs.umb.edu/cs636/music1-setup/sound/";
	
	private static DownloadDAO downloadDao;
	private static InvoiceDAO invoiceDao;
	private static LineItemDAO lineItemDao;
	private static ProductDAO productDao;
	private static UserDAO userDao;
	private static AdminDAO adminDao;
	private static DbDAO dbDAO;
	
	private static AdminService adminService;
	private static UserService userService;
	
	
	// set up service API, data access objects
	public static void configureServices(String dbUrl, String usr, String pw) throws Exception {
		// configure service layer and DAO objects--
		// The service objects get what they need at creation-time
		// This is known as "constructor injection"

		try {
			if (dbUrl == null)
				System.out.println("configureServices: dbUrl is null (defaulting to HSQLDB)");
			else
				System.out.println("configureServices: dbUrl = " + dbUrl + ", usr =" + usr + "pw = " + pw);
			System.out.println(
					"TEMPORARY (remove this println): Stub implementation of configureServices, does not use DB fully yet");

			dbDAO = new DbDAO(dbUrl, usr, pw);
			adminDao = new AdminDAO(dbDAO);
			userDao = new UserDAO(dbDAO);
			productDao = new ProductDAO(dbDAO);
			downloadDao = new DownloadDAO(dbDAO, userDao, productDao);
			lineItemDao = new LineItemDAO(dbDAO);
			invoiceDao = new InvoiceDAO(dbDAO, lineItemDao, userDao, productDao);

			adminService = new AdminService(dbDAO, adminDao, downloadDao, lineItemDao, invoiceDao);
			userService = new UserService(dbDAO, productDao, userDao, downloadDao, lineItemDao, invoiceDao);
			
		} catch (Exception e) {
			System.out.println(exceptionReport(e));
			e.printStackTrace(); // causes lots of output
			System.out.println("Problem with contacting DB: " + e);
			if (dbUrl == null || dbUrl.contains("hsqldb"))
				System.out.println("HSQLDB not available: may need server startup");
			throw (e); // rethrow to notify caller (caller should print
						// exception details)
		}
	}

	// Compose an exception report
	// and return the string for callers to use
	public static String exceptionReport(Exception e) {
		String message = e.toString(); // exception name + message
		if (e.getCause() != null) {
			message += "\n  cause: " + e.getCause().toString();
			if (e.getCause().getCause() != null)
				message += "\n    cause's cause: " + e.getCause().getCause().toString();
		}
		return message;
	}

	// When the app exits, the shutdown happens automatically
	// For other cases, call this to free up the JDBC Connection
	public static void shutdownServices() throws Exception {
		dbDAO.close(); // close JDBC connection
	}

	// Let the apps get the business logic layer services
	public static AdminService getAdminService() {
		return adminService;
	}

	//
	public static UserService getUserService() {
		return userService;
	}
}
