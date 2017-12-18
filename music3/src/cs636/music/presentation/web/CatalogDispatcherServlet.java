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
import cs636.music.service.CatalogService;

public class CatalogDispatcherServlet extends HttpServlet {
	
	private static final long serialVersionUID = 3L;
	private static CatalogService catalogService;
	
	private static final String HOME_URL = "/welcome.html";
	private static final String HOME_VIEW = "/welcome.jsp";
	private static final String CATALOG_URL = "/catalog.html";
	private static final String CATALOG_VIEW = "/WEB-INF/jsp/catalog.jsp";
	private static final String PRODUCT_URL = "/product.html";
	// private static final String PRODUCT_VIEW = "/WEB-INF/jsp/product.jsp";
	private static final String SOUND_URL = "/sound.html";
	// private static final String SOUND_VIEW = "/WEB-INF/jsp/sound.jsp";
	private static final String CART_URL = "/cart.html";
	private static final String UPDATE_CART_URL = "/updateCart.html";
	private static final String REMOVE_CART_ITEM_URL = "/removeCart.html";
	//private static final String CART_VIEW = "/WEB-INF/jsp/cart.jsp";

	private CatalogController catalogController;

	@Override
	public void init() throws ServletException {
		System.out.println("Starting catalog dispatcher servlet initialization");

		// in top-level directory, read file for db to use
		String dbName = "hsql";
		ServletContext context = getServletContext();
		try {
			String path = context.getRealPath("/catalogDbName");
			System.out.println("dbName file path = " + path);
			Scanner in = new Scanner(new File(path));
			dbName = in.next(); // file has one token
			System.out.println("Using catalog DB " + dbName);
			in.close();
		} catch (Exception e) {
			System.out.println("Can't read dbName file, defaulting to hsql");
		}

		try {
			MusicSystemConfig.configureCatalogService(dbName);
		} catch (Exception e) {
			// log the error in tomcat's log
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException(e); // fatal error
		}
		catalogService = MusicSystemConfig.getCatalogService();
		if (catalogService == null)
			throw new ServletException("Catalog DispatcherServlet: bad initialization");

		// create all the controllers and their forward URLs
		catalogController = new CatalogController(catalogService);

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get webapp-specific part of the URL, the part after
		// the context path that identifies the webapp
		String requestURL = request.getServletPath();
		System.out.println("CatalogDispatcherServlet: requestURL = " + requestURL);

		// Dispatch to the appropriate Controller, which will determine
		// the next URL to use as well as do its own actions.
		// The URL returned by handleRequest will be a context-relative URL,
		// like /WEB-INF/jsp/foo.jsp (a view)
		// or /something.html (for a controller).
		// Note that although resources below /WEB-INF are inaccessible
		// to direct HTTP requests, they are accessible to forwards
		String forwardURL = null;
		if (requestURL.equals(CATALOG_URL))
			forwardURL = CATALOG_VIEW; // no controller needed
			else if (requestURL.equals(HOME_URL))
				forwardURL = HOME_VIEW;
			else if (requestURL.equals(CART_URL))
				forwardURL = catalogController.handleshowCartRequest(request, response);
			else if(requestURL.equals("/addItemTOCart.html"))
				forwardURL = catalogController.handleAddCartItemRequest(request, response);
			else if (requestURL.equals(UPDATE_CART_URL))
				forwardURL = catalogController.handleUpdateCartRequest(request, response);
			else if (requestURL.equals(REMOVE_CART_ITEM_URL))
				forwardURL = catalogController.handleRemoveCartItemRequest(request, response);
			else if (requestURL.equals(PRODUCT_URL))
				forwardURL = catalogController.handleProductRequest(request, response);
			else if (requestURL.equals(SOUND_URL))
				forwardURL = catalogController.handleSoundPageRequest(request, response);
			else if (requestURL.equals("/track.html"))
				forwardURL = catalogController.handleTrackDownloadRequest(request, response);	

		else
			throw new ServletException("Catalog DispatcherServlet: Unknown servlet path: " + requestURL);
		// Here with good forwardURL to forward to
		System.out.println("catalog DispatcherServlet: forwarding to " + forwardURL);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(forwardURL);
		dispatcher.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
