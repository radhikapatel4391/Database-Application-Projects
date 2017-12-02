package cs636.music.presentation;

import java.io.IOException;


import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.User;
import cs636.music.service.ServiceException;
import cs636.music.service.UserService;


public class Register {
	
	private UserService userService;

	public Register(String dbUrl, String usr, String psswd) throws Exception {
		MusicSystemConfig.configureServices(dbUrl, usr, psswd);
		userService = MusicSystemConfig.getUserService();
		
	}

	public static void main(String[] args) {
		try {
			Register register;
			if (args.length == 0)
				register = new Register(null, null, null);
			else
				register = new Register(args[0], args[1], args[2]);
			System.out.println("starting Register");
			register.insert();
			System.out.println("Exiting Register app ---");
		} catch (Exception e) {
			System.out.println("Error in run: StackTrace for it: ");
			e.printStackTrace();
			System.out.println("Error in run, shorter report: "
					+ MusicSystemConfig.exceptionReport(e));
		}
	}

	public void insert() throws IOException, ServiceException  {		
			
		userService.insertUser("Radhika","Patel","r.patel002@umb.edu");	
		
		
	}
}