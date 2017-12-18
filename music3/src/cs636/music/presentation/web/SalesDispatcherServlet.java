package cs636.music.presentation.web;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.service.SalesService;

public class SalesDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 3L;
	private static SalesService salesService;

//	private static final String INVOICE_URL = "/invoice.sa";
//	private static final String INVOICE_VIEW = "/WEB-INF/jsp/invoice.jsp";
//	private static final String REGISTER_URL = "/register.sa";
//	private static final String REGISTER_VIEW = "/WEB-INF/jsp/register.jsp";
	private static final String COMPLETE_ORDER_URL = "/completeOrder.sa";
//	private static final String COMPLETE_ORDER_VIEW = "/WEB-INF/jsp/complete.jsp";
	private static final String LOGIN_URL = "/login.sa";
	private static final String LOGIN_VIEW = "/WEB-INF/jsp/login.jsp";

	private static final String REGISTER_RECEIVE_FORM_URL = "/registerFormReceive.sa";
//	private static final String LOGIN_RESPONCE_URL = "/loginResponce.sa";
//	private static final String LOGOUT_URL = "/logout.sa";
	private static final String CHECKOUT_URL = "/checkOut.sa";
	

	private SaleController saleController;

	@Override
	public void init() throws ServletException {
		System.out.println("Starting sale dispatcher servlet initialization");

		// in top-level directory, read file for db to use
		String dbName = "hsql";
		ServletContext context = getServletContext();
		try {
			String path = context.getRealPath("/salesDbName");
			System.out.println("dbName file path = " + path);
			Scanner in = new Scanner(new File(path));
			dbName = in.next(); // file has one token
			System.out.println("Using sales DB " + dbName);
			in.close();
		} catch (Exception e) {
			System.out.println("Can't read dbName file, defaulting to hsql");
		}

		try {
			MusicSystemConfig.configureSalesService(dbName);
		} catch (Exception e) {
			// log the error in tomcat's log
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException(e); // fatal error
		}
		salesService = MusicSystemConfig.getSalesService();
		if (salesService == null)
			throw new ServletException("sale DispatcherServlet: bad initialization");

		// create all the controllers and their forward URLs
		saleController = new SaleController(salesService);

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		String requestURL = request.getServletPath();
		System.out.println("SaleDispatcherServlet: requestURL = " + requestURL);		
		String forwardURL = null;
		
		if (requestURL.equals(LOGIN_URL))
			forwardURL = LOGIN_VIEW; // no controller needed		
			else if (requestURL.equals(CHECKOUT_URL)) 
				forwardURL = saleController.handleCheckOutRequest(request, response);			
			else if (requestURL.equals(REGISTER_RECEIVE_FORM_URL))
				forwardURL = saleController.handleRegisterRequest(request, response);		
			else if (requestURL.equals(COMPLETE_ORDER_URL))
				forwardURL = saleController.handleCompleteOrderRequest(request, response);
		else
			throw new ServletException("Catalog DispatcherServlet: Unknown servlet path: " + requestURL);
		
		System.out.println("catalog DispatcherServlet: forwarding to " + forwardURL);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
		dispatcher.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
