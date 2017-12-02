package cs636.pizza.presentation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import cs636.pizza.config.PizzaSystemConfig;
import cs636.pizza.service.AdminService;
import cs636.pizza.service.PizzaOrderData;
import cs636.pizza.service.ServiceException;
import cs636.pizza.service.StudentService;

/**
 * @author Saaid Baraty
 * 
 *         This class tests the whole system.
 */
public class SystemTest {

	private AdminService adminService;
	private StudentService studentService;
	private String inFile;

	public SystemTest(String dbUrl, String usr, String psswd, String inFile)
			throws Exception {
		this.inFile = inFile;
		PizzaSystemConfig.configureServices(dbUrl, usr, psswd);
		adminService = PizzaSystemConfig.getAdminService();
		studentService = PizzaSystemConfig.getStudentService();
	}

	public static void main(String[] args) {
		String inFile = null;
		String dbUrl = null;
		String usr = null;
		String pw = null;
		if (args.length == 0) {  // no args: run on HSQLDB with test.dat
			inFile = "test.dat";
			// leave dbUrl null, for HSQLDB
		} else if (args.length == 1) {
			inFile = args[0];
			// leave dbUrl null, for HSQLDB
		} else if (args.length == 4) {
			dbUrl = args[0];
			usr = args[1];
			pw = args[2];
			inFile = args[3];
		} else {
			System.out
					.println("usage:java <dbURL> <user> <passwd> <inputFile> ");
			return;
		}
		try {
			SystemTest test = new SystemTest(dbUrl, usr, pw, inFile);
			test.run();
			System.out.println("Run complete, exiting");
		} catch (Exception e) {
			System.out.println("Error in run of SystemTest: " );
			System.out.println(PizzaSystemConfig.exceptionReport(e));
		}
	}

	public void run() throws IOException, ServiceException {
		String command = null;
		Scanner in = new Scanner(new File(inFile));
		while ((command = getNextCommand(in)) != null) {
			System.out.println("\n\n*************" + command
					+ "***************\n");
			if (command.equalsIgnoreCase("ai")) { // admin init db
				adminService.initializeDb(); // create new tables, etc.
				adminService.addSize("small");
				adminService.addTopping("Pepperoni");
			} else if (command.equalsIgnoreCase("anr")) // admin next ready
				adminService.markNextOrderReady();
			else if (command.equalsIgnoreCase("aad")) // admin advance day
				adminService.advanceDay();
		    else if (command.equalsIgnoreCase("aip")) { // admin in-progress report
				List<PizzaOrderData> report = 
						adminService.getTodaysOrdersByStatus(PizzaOrderData.PREPARING);
				PresentationUtils.printReport(report, System.out);
				report = 
						adminService.getTodaysOrdersByStatus(PizzaOrderData.BAKED);
				PresentationUtils.printReport(report, System.out);
			} else if (command.startsWith("ss")) // student status
				handleOrderStatus(command);
			else if (command.startsWith("so")) // student order
				handleStudentOrder(command);
			else if (command.startsWith("sr")) // student order received
				handleOrderReceive(command);
			else
				System.out.println("Invalid Command: " + command);
			System.out.println("----OK");
		}
		in.close();
	}

	
	private void handleOrderStatus(String command) throws ServiceException {
		String[] tokens = getTokens(command);
		int roomNumber = Integer.parseInt(tokens[1]);
		List<PizzaOrderData> report = studentService.getOrderStatus(roomNumber);
		PresentationUtils.printOrderStatus(report, System.out);
	}
	
	private void handleOrderReceive(String command) throws ServiceException {
		String[] tokens = getTokens(command);
		int roomNumber = Integer.parseInt(tokens[1]);
		studentService.receiveOrders(roomNumber);
	}

	private void handleStudentOrder(String command) throws ServiceException {
		String[] tokens = getTokens(command);
		int roomNumber = Integer.parseInt(tokens[1]);
		Set<String> allToppings = studentService.getToppingNames();
		Set<String> allSizes = studentService.getSizeNames();
		// get a particular PizzaSize
		String chosenPizzaSize = allSizes.iterator().next();
		studentService.makeOrder(roomNumber, chosenPizzaSize, allToppings);
	}
	
	// Return line or null if at end of file
	public String getNextCommand(Scanner in) throws IOException {
		String line = null;
		try {
			line = in.nextLine();
		} catch (NoSuchElementException e) { } // leave line null
		return (line != null) ? line.trim() : line;
	}

	// use powerful but somewhat mysterious split method of String
	private String[] getTokens(String command) {
		return command.split("\\s+"); // white space
	}

}
