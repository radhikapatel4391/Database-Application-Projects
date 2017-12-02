package cs636.music.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static cs636.music.dao.DBConstants.*;

/**
 * Database connection and initialization.
 * Implemented singleton on this class.
 * 
 */
public class DbDAO {
	   
	private Connection connection;
		
	/**
	 *  Use to connect to databases through JDBC drivers
	 *  @param dbUrl connection string
	 *  @param usr  user name
	 *  @param passwd password
	 *  @throws  SQLException
	 */
	public DbDAO(String dbUrl, String usr, String passwd) throws SQLException {
			String driver = null;
			if (dbUrl == null)
				dbUrl = HSQLDB_URL;  // default to HSQLDB
			if (dbUrl.contains("oracle")) {				
				driver = ORACLE_DRIVER;
			} else if (dbUrl.contains("mysql")) {
				driver = MYSQL_DRIVER;
			} else if (dbUrl.contains("hsqldb")) {
				driver = HSQLDB_DRIVER;
				usr = "sa";
				passwd = "";
			} else throw new SQLException("Unknown DB URL pattern in DbDAO constructor");
			System.out.println("Connecting using driver "+ driver+ ", DB URL " + dbUrl);
			try { 
				Class.forName(driver);
			} catch (Exception e) {
				throw new SQLException("Problem with loading driver: " + e);
			}			
			connection = DriverManager.getConnection(dbUrl, usr, passwd);			
	   }
	/**
	 *  Return the built connection
	 *  @return  Connection object established by DbDAO
	 */
	public Connection getConnection() {		
		return connection;
	}
	
	/**
	 *  Terminate the built connection
	 *  @throws  SQLException
	 */
	public void close() throws SQLException {
		connection.close();  // this object opened it, so it gets to close it
	}
	
	/**
	*  bring DB back to original state
	*  @throws  SQLException
	**/
	public void initializeDb() throws SQLException {
		clearTable(DOWNLOAD_TABLE);
		clearTable(LINEITEM_TABLE);
		clearTable(INVOICE_TABLE);
		clearTable(USER_TABLE);
		clearTable(SYS_TABLE);
		initSysTable();		
	}

	/**
	*  Delete all records from the given table
	*  @param tableName table name from which to delete records
	*  @throws  SQLException
	**/
	private void clearTable(String tableName) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.execute("delete from " + tableName);
		} finally {
			stmt.close();
		}
	}
	
	/**
	*  Set all the index number used in other tables back to 1
	*  @throws  SQLException
	**/
	private void initSysTable() throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			stmt.execute("insert into " + SYS_TABLE + " values (1,1,1,1)");
		} finally {
			stmt.close();
		}
	}

}
