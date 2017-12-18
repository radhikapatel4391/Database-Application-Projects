package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.service.CatalogServiceAPI;
import cs636.music.service.SalesServiceAPI;
import cs636.music.service.ServiceException;
import cs636.music.service.data.DownloadData;
import cs636.music.service.data.InvoiceData;

public class AdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private SalesServiceAPI salesService;
	private CatalogServiceAPI catalogService; // for Downloads

	private static final String ADMIN_JSP_DIR = "/WEB-INF/admin/";

	// Initialization of servlet: runs before any request is
	// handled in the web app. 
	@Override
	public void init() throws ServletException {
		System.out.println("Starting admin servlet initialization");
		// the user-pages dispatcher servlet(s) should have initialized system first
		// by load-on-startup settings in web.xml
		// but if not, call MusicSystemConfig here to set up the system (temporarily)
		salesService = MusicSystemConfig.getSalesService();
		catalogService = MusicSystemConfig.getCatalogService();
		if (salesService == null) {
			System.out.println("!!!!!!!!!!!!!!!AdminServlet initialization problem!!!!!!!!!");
			System.out.println("AdminServlet needs to call MusicSystemConfig if dispatcher servlet is not implemented");
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String url = null;
		String requestURI = request.getRequestURI();
		System.out.println("doGet requestURI = " + requestURI);
		String uname = (String) request.getSession().getAttribute("adminUser");
		if (requestURI.contains("listVariables.html")) {
			url = ADMIN_JSP_DIR + "listVariables.jsp"; // allow this without login
		} else if (requestURI.contains("logout.html")) { // and this
			System.out.println("logging out...");
			request.getSession().invalidate(); // drop session
			url = ADMIN_JSP_DIR + "logout.jsp";
		} else if (uname == null) {
			requestURI = "adminWelcome.html"; // if not logged in, force login
			url = handleAdminLogin(request, response); // you can change these names if you want
		} else if (requestURI.contains("adminWelcome.html")) { // but this shows the expected functionality
			url = handleAdminLogin(request, response);
		} else if (requestURI.contains("processInvoices.html")) {
			url = handleProcessInvoices(request, response);
		} else if (requestURI.contains("initDB.html")) {
			url = initializeDB(request, response);
		} else if (requestURI.contains("displayReports.html")) {
			url = handleDisplayReports(request, response);
		} else if (requestURI.contains("viewInvoice.html")) {
			url = handleUserInvoice(request, response);
		} else if (requestURI.contains("processInvoice.html")) {
			url = handleProcessInvoice(request, response);
		} else {
			System.out.println("Unknown request URI: " + requestURI);
			throw new ServletException("Unknown request URI: " + requestURI);
		}

		// Need to check what needs to be done if the url is null
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);

	}

	/*
	 * Returns the url of the page that needs to be forwarded to
	 */
	private String handleAdminLogin(HttpServletRequest request, HttpServletResponse response) {
		String url = "/welcome.jsp"; // URL of admin menu if successful, else back to admin login page
		return url;
	}

	private String handleProcessInvoices(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		return url;
	}

	private String initializeDB(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = ADMIN_JSP_DIR + "initializeDB.jsp"; // or whatever you call it
		return url;
	}

	private String handleDisplayReports(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		return url;
	}

	private String handleUserInvoice(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		return url;
	}

	private String handleProcessInvoice(HttpServletRequest request, HttpServletResponse response) {
		String url = null;
		return url;
	}

}
