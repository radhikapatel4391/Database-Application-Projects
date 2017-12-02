package cs636.pizza.dao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cs636.pizza.config.StandAloneDataSourceFactory;
import cs636.pizza.domain.PizzaOrder;
import cs636.pizza.domain.PizzaSize;
import cs636.pizza.domain.PizzaTopping;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

public class PizzaOrderDAOTest1 {
	private DbDAO db;
	private MenuDAO menuDAO;
	private PizzaOrderDAO pizzaOrderDAO;
	private Connection connection;
	
	// This executes before *each* test, so every test starts out the same way
	@Before
	public void setUp() throws Exception {
		// we use HSQLDB as the db for testing
		// Note: need to start and load it first
		DataSource ds = null;
		ds = StandAloneDataSourceFactory.createDataSource("hsql"); 
		db = new DbDAO(ds);
		Connection connection = db.startTransaction();
		db.initializeDb();  // no orders, toppings, sizes
		menuDAO = new MenuDAO(db);
		pizzaOrderDAO = new PizzaOrderDAO(db, menuDAO);
		menuDAO.createMenuSize(connection, "small");  // make a legit size
		menuDAO.createMenuTopping(connection, "pepperoni"); 
		db.commitTransaction(connection);
	}

	@After
	public void tearDown() throws Exception {
		// This executes even after an exception
		// so we need to rollback here in case of exception
		// (If the transaction was successful, it's already
		// committed, and this won't hurt.)
		db.rollbackAfterException(connection);	
	}
	
	// in JUnit4, an exception will cause a failure, unless "expected"
	// expected exceptions are listed in @Test, as in the last case,
	// so we don't have to code try-catch in simple test 

	@Test
	public void testMakeOrder() throws SQLException
	{	
		connection = db.startTransaction();
		PizzaSize size = new PizzaSize("small");
		Set<PizzaTopping> tops = new TreeSet<PizzaTopping>();
		tops.add(new PizzaTopping("pepperoni"));		
		PizzaOrder order = new PizzaOrder(1, 5, size, tops, 1, 1);
		pizzaOrderDAO.insertOrder(connection, order);
		db.commitTransaction(connection);
	}

	@Test
	public void testOrderNumber() throws SQLException
	{
		connection = db.startTransaction();
		int ordNo = db.findNextId(connection, "next_order_id");
		db.commitTransaction(connection);
		assertTrue("first ordNo 0 or negative", ordNo > 0);		
	}
	
	// no orders yet, so findFirstOrder should fail
	// Note that even if insertOrder has already executed, the setup
	// for this test will have reinitialized the DB to be empty again
	@Test
	public void noFirstOrderYet() throws SQLException
	{
		connection = db.startTransaction();
		PizzaOrder po = pizzaOrderDAO.findFirstOrder(connection, PizzaOrder.PREPARING);
		db.commitTransaction(connection);
		assertTrue("first order exists but shouldn't", po == null);
	}
	
	// case of expected Exception: 	
	@Test(expected=SQLException.class)
	public void badMakeOrder() throws SQLException
	{	
		connection = db.startTransaction();
		PizzaSize size = new PizzaSize("tiny");  // bad size (not on menu)
		Set<PizzaTopping> tops = new TreeSet<PizzaTopping>();
		tops.add(new PizzaTopping("pepperoni"));
		
		PizzaOrder order = new PizzaOrder(5, size, tops, 1, 1);
		pizzaOrderDAO.insertOrder(connection, order);
		db.commitTransaction(connection);
	}
}
