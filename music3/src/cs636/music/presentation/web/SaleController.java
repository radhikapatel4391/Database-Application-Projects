package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.service.SalesService;
import cs636.music.service.data.InvoiceData;
import cs636.music.service.data.UserData;
import cs636.music.domain.Cart;



//Provide methods for SalesDispatcher servlet
public class SaleController {
	
	private SalesService salesService;

	public SaleController(SalesService salesService) {
		System.out.println("setting salesService in saleController (isnull = " + (salesService == null));
		this.salesService = salesService;		
	}	
	
	public String handleRegisterRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        String firstName = request.getParameter("firstName");        
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email"); 
        String address = request.getParameter("address");
        
       try{
        	salesService.registerUser(firstName, lastName, email);        	
        	 UserData user = salesService.getUserInfoByEmail(email);
        	salesService.addUserAddress(user.getId(), address);
        	request.setAttribute("user", user);
        	request.getSession().setAttribute("user",user);
        }catch (Exception e){
        	
        	return "/WEB-INF/jsp/register.jsp";
        	
        }
       
        String url = (String) request.getSession().getAttribute("parent");
       
        if(url!=null)
        	return url;
        else
        	return "/WEB-INF/jsp/catalog.jsp";        
    }
	
	public String handleCheckOutRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException{

        HttpSession session = request.getSession();        
        UserData user = (UserData) session.getAttribute("user");
       // if(user!=null && !(checkUser(user)))
        if(user == null )
        {	
        	session.setAttribute("parent", "/checkOut.sa");
        	return "/WEB-INF/jsp/register.jsp";
        	}
        if(!checkUser(user))
        {
        	session.setAttribute("parent", "/checkOut.sa");
        	return "/WEB-INF/jsp/register.jsp";
        }
        Cart cart = (Cart) session.getAttribute("cart");
        try{
        	if(cart != null)
        	{
        		
        		InvoiceData invoice = salesService.checkout(cart, user.getId());
        		
        		user = salesService.getUserInfo(user.getId()) ;        		
        		session.setAttribute("invoice", invoice);        		
        		//session.setAttribute("userAddress", user.);
        		request.setAttribute("invoice", invoice);        		
        		request.setAttribute("user", session.getAttribute("user"));
        		
        	}
        	 } catch (Exception e) {
        	 throw new ServletException("check out  error: ", e);
        	 }
        return "/WEB-INF/jsp/invoice.jsp";
    }
	public String handleCompleteOrderRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException{
		
		request.setAttribute("user", request.getSession().getAttribute("user"));
		System.out.println(request.getSession().getAttribute("user"));
		return "/WEB-INF/jsp/complete.jsp";
	}

	private Boolean checkUser(UserData user) throws IOException, ServletException {
  
       
       try{
       	if (salesService.getUserInfoByEmail(user.getEmailAddress())!= null)
           	return true;
           
       }catch (Exception e){
       	
       	throw new ServletException("user not exist: ", e);
       }
       return false;        
   }
//	private Boolean loginUser(HttpServletRequest request,
//  HttpServletResponse response) throws IOException, ServletException {
//
//String username = request.getParameter("username");
//  String password = request.getParameter("password");
//
//try{
//	if (salesService.)
//  	return true;
//  
//}catch (Exception e){
//	
//	throw new ServletException("user not exist: ", e);
//}
//return false;        
//}
}
