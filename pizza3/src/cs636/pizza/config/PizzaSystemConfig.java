package cs636.pizza.config;

//System configuration for JPA using a single DB. See persistence.xml
//for its JDBC properties.
//Here we set up singleton service objects
//and DAOs for the app.

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.naming.InitialContext;
import javax.naming.NoInitialContextException;
import javax.sql.DataSource;

import cs636.pizza.dao.DbDAO;
import cs636.pizza.dao.MenuDAO;
import cs636.pizza.dao.AdminDAO;
import cs636.pizza.dao.PizzaOrderDAO;
import cs636.pizza.service.AdminService;
import cs636.pizza.service.StudentService;

public class PizzaSystemConfig {
	// for ease of testing, handle only a few rooms--
	public static final int NUM_OF_ROOMS = 10;

	// the service objects in use, representing all lower layers to the 
	// presentation layer
	private static AdminService adminService = null;
	private static StudentService studentService= null;
	// the DataSource that holds the Connection pool, providing already-connected
	// Connections for each request to use. It is created by tomcat in the
	// web case, or by special code in file StandAloneDataSourceFactory off-web.
	private static DataSource pizzaDs = null;

	// configure service and DAO objects
	public static void configureServices(String dbName) throws Exception {
		try {
			pizzaDs = obtainDataSource(dbName);

			System.out.println("calling dbDAO constructor");
			DbDAO dbDAO = new DbDAO(pizzaDs);
			// configure rest of service and DAO singleton objects--
			// These objects get what they need at creation-time
			// This is "constructor injection"
			AdminDAO adminDAO = new AdminDAO();
			MenuDAO menuDAO = new MenuDAO(dbDAO);
			PizzaOrderDAO pizzaOrderDAO = new PizzaOrderDAO(dbDAO, menuDAO);
			adminService = new AdminService(dbDAO, adminDAO, pizzaOrderDAO, menuDAO);
			studentService = new StudentService(dbDAO, pizzaOrderDAO, menuDAO, adminDAO);
		} catch (Exception e) {
			System.out.println("Problem with contacting DB");
			// rethrow to notify caller (caller should print exception details)
			throw new Exception("Exception in configureServices", e);
		}
	}

	// Compose an exception report
	// and return the string for callers to use
	public static String exceptionReport(Exception e) {
		String message = e.toString(); // exception name + message
		if (e.getCause() != null) {
			message += "\n  cause: " + e.getCause();
			if (e.getCause().getCause() != null) {
				message += "\n    cause's cause: " + e.getCause().getCause();
			}
		}
		message += "\n Stack Trace: " + exceptionStackTraceString(e);
		return message;
	}

	private static String exceptionStackTraceString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	// Try to access tomcat's JNDI object repository for the needed DataSource
	// object
	// A DataSource object provides a JDBC Connection pool for the webapp.
	// If the JNDI lookup fails, i.e., we're running outside tomcat, create a
	// DataSource from known properties and cs636 environment variables (ORACLE_USER, etc).
	private static DataSource obtainDataSource(String dbName) throws Exception {
		if (dbName == null) {
			System.out.println("PizzaSystemConfig: " + dbName + " is null (defaulting to hsqldb)");
			dbName = "hsql";
		}

		String jndiName = "jdbc/" + dbName; // as seen in tomcat's context.xml
		DataSource dataSource = null;
		try {
			InitialContext ic = new InitialContext(); // finds tomcat's JNDI repository in its JVM
			dataSource = (DataSource) ic.lookup("java:comp/env/" + jndiName);
		} catch (NoInitialContextException e) {
			// not in tomcat JVM: fall through to handle as client-server execution
		} catch (Exception e) {
			System.out.println("Failed to lookup JndiName " + jndiName + ", error = " + e);
			return null;
		}
		if (dataSource == null) {
			System.out.println("JNDI lookup for " + dbName + " failed, will create standalone DataSource");
			// use Apache "commons-dbcp2" support to build a DataSource from scratch
			// using cs636 environment variables like ORACLE_USER
			dataSource = StandAloneDataSourceFactory.createDataSource(dbName);
		}
		return dataSource;
	}

	// call this to free up system resources
	// allocated by configureJPA().
	public static void shutdownServices() {
      // Note that a DataSource has no close or shutdown method.
	}

	// Let the apps get the business logic layer services
	public static AdminService getAdminService() {
		return adminService;
	}

	public static StudentService getStudentService() {
		return studentService;
	}
}
