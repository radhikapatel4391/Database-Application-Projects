import java.io.IOException;
import cs636.music.config.MusicSystemConfig;
import cs636.music.service.ServiceException;
import cs636.music.service.UserService;

public class Register {

	private UserService userService;

	public Register(String dbUrl, String usr, String psswd) throws Exception {
		MusicSystemConfig.configureServices(dbUrl, usr, psswd);
		userService = MusicSystemConfig.getUserService();

	}

	public static void main(String[] args) {
		try {
			Register register;
			if (args.length == 0)
				register = new Register(null, null, null);
			else
				register = new Register(args[0], args[1], args[2]);
			
			register.insert();//inser data..
			
			System.out.println("Exiting Register app ---");
			
		} catch (Exception e) {			
			e.printStackTrace();
			System.out.println("Error in run, shorter report: " + MusicSystemConfig.exceptionReport(e));
		}
	}
	public void insert() throws IOException, ServiceException {

		userService.insertUser("Radhika", "Patel", "r.patel002@umb.edu");

	}
}

//
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
//
//// import cs636.music.config.MusicSystemConfig;
//
//
// public class Register {
//
// private static Connection connection;
// private static PreparedStatement preparedStatement;
//
//
// public static void main(String[] args) {
//
// String dbUrl = null;
// String usr = null;
// String pw = null;
//
// if (args.length == 3) {
// dbUrl = args[0]; //Required info for db connection
// usr = args[1];
// pw = args[2];
// } else {
// dbUrl = "jdbc:hsqldb:MyDB";//jdbc:hsqldb:MyDB
// usr = "sa";
// pw = "";
// System.out.println("usage:java <dbURL> <user> <passwd> ");
// return;
// }
// try {
// connection = DriverManager.getConnection(dbUrl, usr, pw);
// System.out.println("connected.");
// run(connection);// insert data
//
// } catch (SQLException e) {
// System.out.println("Problem with JDBC Connection\n");
// e.printStackTrace();
//
// System.exit(4);
// } finally {
// // Close the connection, if it was obtained, no matter what happens
// // above or within called methods
// if (connection != null) {
// try {
// connection.close(); // this also closes the Statement and
// // ResultSet, if any
// } catch (SQLException e) {
// System.out.println("Problem with closing JDBC Connection\n");
// //printSQLException(e);
// System.exit(5);
// }
// }
// }
// }
//
// static public void run(Connection connection) {
//
// try {
// String query = "insert into site_user
// (user_id,firstname,lastname,email_address,company_name,address1,address2,"
// +
// "city,state,zip,country,creditcard_type,creditcard_number,creditcard_expirationdate)
// "
// + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
// preparedStatement = connection.prepareStatement(query);
// preparedStatement.setInt(1, 9);
// preparedStatement.setString(2, "Radhika");
// preparedStatement.setString(3, "Patel");
// preparedStatement.setString(4, "r.ppatel002@gmail.com");
// preparedStatement.setString(5, "CRD");
// preparedStatement.setString(6, "Apt 4");
// preparedStatement.setString(7, "6 Arizona terrace");
// preparedStatement.setString(8, "Arlington");
// preparedStatement.setString(9, "MA");
// preparedStatement.setString(10, "02474");
// preparedStatement.setString(11, "USA");
// preparedStatement.setString(12, "VISA");
// preparedStatement.setString(13, "0123456789");
// preparedStatement.setString(14, "7/25");
//
// preparedStatement.execute();
// } catch (SQLException e) {
// e.printStackTrace();
//
// }
// }
// }
