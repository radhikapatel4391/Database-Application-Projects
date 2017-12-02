package cs636.pizza.dao;
// Example JUnit4 test 
import org.junit.After;
import cs636.pizza.config.StandAloneDataSourceFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class MenuDAOTest11 {
	private DbDAO db;
	private MenuDAO menuDAO;
	private Connection connection;
	
	// This executes before *each* test, so every test starts out the same way
	@Before
	public void setUp() throws Exception {
		// we use HSQLDB as the db for testing
		// Note: need to start and load it first
		DataSource ds = null;
		ds = StandAloneDataSourceFactory.createDataSource("hsql"); 
		db = new DbDAO(ds);
		db.initializeDb();  // no orders, toppings, sizes
		menuDAO = new MenuDAO(db);
	}

	@After
	public void tearDown() throws Exception {
		// This executes even after an exception
		// so we need to rollback here in case of exception
		// (If the transaction was successful, it's already
		// committed, and this won't hurt.)
		db.rollbackAfterException(connection);
	}
	
	@Test
	public void testCreateTopping() throws SQLException
	{
		connection = db.startTransaction();
		menuDAO.createMenuTopping(connection, "anchovies");	
		int count = menuDAO.findMenuToppings(connection).size();
		assertTrue("first topping not created", count == 1);
		db.commitTransaction(connection);
	}
	
	@Test
	public void testCreateSize() throws SQLException
	{
		connection = db.startTransaction();
		menuDAO.createMenuSize(connection, "huge");	
		int count = menuDAO.findMenuSizes(connection).size();
		assertTrue("first topping not created", count == 1);
		db.commitTransaction(connection);
	}

}
