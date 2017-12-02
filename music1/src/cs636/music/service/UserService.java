package cs636.music.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import cs636.music.dao.DbDAO;
import cs636.music.dao.DownloadDAO;
import cs636.music.dao.InvoiceDAO;
import cs636.music.dao.LineItemDAO;
import cs636.music.dao.ProductDAO;
import cs636.music.dao.UserDAO;
import cs636.music.domain.Cart;
import cs636.music.domain.CartItem;
import cs636.music.domain.Download;
import cs636.music.domain.Invoice;
import cs636.music.domain.LineItem;
import cs636.music.domain.Product;
import cs636.music.domain.Track;
import cs636.music.domain.User;
import cs636.music.service.data.CartItemData;
import cs636.music.service.data.UserData;

public class UserService {

	private UserDAO userdb;
	private DbDAO db;
	private ProductDAO productdb;
	private DownloadDAO downloaddb;
	private LineItemDAO lineItemdb;
	private InvoiceDAO invoicedb;

	public UserService(DbDAO dbDao, ProductDAO productDao, UserDAO userDao, DownloadDAO downloadDao,
			LineItemDAO lineItemDao, InvoiceDAO invoiceDao) {
		db = dbDao;
		userdb = userDao;
		productdb = productDao;
		downloaddb = downloadDao;
		lineItemdb = lineItemDao;
		invoicedb = invoiceDao;

	}

	public void insertUser(String fname, String lname, String email) throws ServiceException {
		User user = new User(fname, lname, email);

		try {
			userdb.insertUser(user);

		} catch (SQLException e) {
			if (e.getMessage().contains("unique")) {
				throw new ServiceException("Duplicates value, data already exists...!!!!", e);
			} else {
				throw new ServiceException("User was not added successfullyccccccc", e);
			}
		}

	}

	public UserData getUserByemail(String email) throws IOException, ServiceException {
		UserData userData = null;

		try {
			User user = userdb.findUserByEmailId(email);

			if (user != null) {
				userData = new UserData(user);
			}

		} catch (SQLException e) {
			throw new ServiceException("Error in user serch by email address", e);
		}
		return userData;
	}

	public Set<Product> findAllProducts() throws IOException, ServiceException {
		Set<Product> products;
		try {
			products = productdb.findAllProducts();

		} catch (SQLException e) {
			throw new ServiceException("Error in Find all Products", e);
		}

		return products;
	}

	public Product findProductsById(Long pid) throws IOException, ServiceException {
		Product product;
		try {
			product = productdb.findProductByPID(pid);

		} catch (SQLException e) {
			throw new ServiceException("Error in find product by product id", e);
		}

		return product;
	}

	public Product findProductsByPCode(String pcode) throws IOException, ServiceException {
		Product product;
		try {
			product = productdb.findProductByPCode(pcode);

		} catch (SQLException e) {
			throw new ServiceException("Error in find product by product id....", e);
		}

		return product;
	}

	public Cart newCart() throws IOException, ServiceException {
		Cart cart = new Cart();
		return cart;
	}

	public Set<CartItemData> showCart(Cart cart) throws IOException, ServiceException {
		Set<CartItemData> cartData = new HashSet<CartItemData>();
		Set<CartItem> cartItems = cart.getItems();
		for (CartItem i : cartItems) {
			Product product = findProductsById(i.getProductId());
			cartData.add(new CartItemData(i.getProductId(), i.getQuantity(), product.getCode(),
					product.getDescription(), product.getPrice()));
		}
		return cartData;
	}

	public void Chekout(UserData userdata, Cart cart) throws IOException, ServiceException {

		Invoice invoice = new Invoice();
		BigDecimal totalAmount = BigDecimal.ZERO;
		Set<LineItem> items = new HashSet<LineItem>();
		Set<CartItem> cartItems = cart.getItems();
		for (CartItem i : cartItems) {
			Product product = findProductsById(i.getProductId());
			LineItem lineItem = new LineItem(product, invoice, i.getQuantity());
			totalAmount = totalAmount.add(product.getPrice());
			items.add(lineItem);
		}

		invoice.setProcessed(true);
		invoice.setTotalAmount(totalAmount);
		invoice.setLineItems(items);

		try {
			User user = userdb.findUserByID(userdata.getId());
			invoice.setUser(user);
			invoicedb.insertInvoice(invoice);

		} catch (SQLException e) {
			throw new ServiceException("Error in inserting invoice at checkout", e);
		}

	}

	public void addProducttoCart(Cart cart, Product product, int quntity) throws IOException, ServiceException {

		// CartItemData cartItem = new CartItemData
		// (product.getId(),quntity,product.getCode(),product.getDescription(),product.getPrice());
		CartItem item = new CartItem(product.getId(), quntity);
		cart.addItem(item);

	}
	public void addDownloadByUserAndTrack(UserData userdata, Track track) throws IOException, ServiceException{
		
		
		
		//download.setDownloadDate(current);
		
		try{
			User user = userdb.findUserByID(userdata.getId());
			Download download = new Download();
			download.setTrack(track);
			download.setUser(user);
			downloaddb.insertDownload(download);
			
		}catch(Exception e){
			throw new ServiceException("Error in inserting at Download", e);
		}
		
	}

}
