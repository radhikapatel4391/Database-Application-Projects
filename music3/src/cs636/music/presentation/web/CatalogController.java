package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.domain.Cart;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.service.CatalogService;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.UserData;

public class CatalogController {

	private CatalogService catalogService;

	public CatalogController(CatalogService catalogService) {

		System.out.println("setting catalogService in CatalogController (isnull = " + (catalogService == null));
		this.catalogService = catalogService;
	}

	// methods for handling request related to catalog database and database
	// services.
	// request for Product page handle here.
	public String handleProductRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		try {
			Product product = catalogService.getProductByCode(request.getParameter("pid"));
			request.setAttribute("product", product);
			session.setAttribute("product", product);
		} catch (Exception e) {
			throw new ServletException("Product getting  error: ", e);
		}
		return "/WEB-INF/jsp/product.jsp";
	}

	// show cart page
	public String handleshowCartRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		try {
			if (cart == null || catalogService.getCartInfo(cart).isEmpty()) {
				request.setAttribute("cartItem", null);
			} else {
				Set<CartItemData> cartItems = catalogService.getCartInfo(cart);
				request.setAttribute("cartItem", cartItems);
			}
		} catch (Exception e) {
			throw new ServletException("show cart error: ", e);
		}
		return "/WEB-INF/jsp/cart.jsp";
	}

	public String handleAddCartItemRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");

		long productId = Long.parseLong(request.getParameter("productId"));

		try {
			if (cart == null)
				cart = catalogService.createCart();
			int quantity = 1;
			catalogService.addItemtoCart(productId, cart, quantity);
			Set<CartItemData> cartItems = catalogService.getCartInfo(cart);
			request.setAttribute("cartItem", cartItems);
		} catch (Exception e) {
			throw new ServletException("add item to cart error: ", e);
		}
		session.setAttribute("cart", cart);
		return "/WEB-INF/jsp/cart.jsp";
	}

	public String handleUpdateCartRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		long productId = Long.parseLong(request.getParameter("productId"));
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");

		try {

			if (quantity < 0)
				quantity = 1;
		} catch (NumberFormatException ex) {
			quantity = 1;
		}
		try {
			catalogService.changeCart(productId, cart, quantity);
			Set<CartItemData> cartItems = catalogService.getCartInfo(cart);
			request.setAttribute("cartItem", cartItems);
		} catch (Exception e) {
			throw new ServletException("update cart error: ", e);
		}
		// session.setAttribute("cart", cart);
		return "/WEB-INF/jsp/cart.jsp";
	}

	public String handleRemoveCartItemRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		long productId = Long.parseLong(request.getParameter("productId"));
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		try {
			catalogService.removeCartItem(productId, cart);
			Set<CartItemData> cartItems = catalogService.getCartInfo(cart);
			request.setAttribute("cartItem", cartItems);

		} catch (Exception e) {
			throw new ServletException("update cart error: ", e);
		}
		return "/WEB-INF/jsp/cart.jsp";
	}

	public String handleSoundPageRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		 UserData user = (UserData) session.getAttribute("user");
		String productCode = request.getParameter("productcode");

		 if (user == null) {
		 session.setAttribute("parent",
		 "/sound.html?productcode="+productCode);
		 //session.setAttribute("productCode", productCode);
		 return "/WEB-INF/jsp/register.jsp";
		 } else {
		try {
			Product product = (Product) catalogService.getProductByCode(productCode);
			session.setAttribute("product", product);
			request.setAttribute("product", product);
		} catch (Exception e) {
			throw new ServletException("update cart error: ", e);
		}

		return "/WEB-INF/jsp/sound.jsp";
		 }
	}

	public String handleTrackDownloadRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		UserData user = (UserData) session.getAttribute("user");
		int trackID = Integer.parseInt(request.getParameter("trackId"));
		String url = request.getParameter("linkURL");

		// if the User object doesn't exist, check transfer call to register
//		if (user == null) {
//			session.setAttribute("parent", url);
//			return "/WEB-INF/jsp/register.jsp";
//		} else {

			Product product = (Product) session.getAttribute("product");
			System.out.println("I am your product");

			try {
				Track track = product.findTrackbyID(trackID);

				catalogService.addDownload(user.getEmailAddress(), track);
			} catch (Exception e) {
				throw new ServletException("download added: ", e);
			}

			return url;
//		}

	}

}
