package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Roles;
import model.User;
import repository.UserRepo;

/**
 * Servlet implementation class FindAccountsByUserServlet
 */
public class FindAccountsByUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindAccountsByUserServlet() {
        super();
        userRepo = new UserRepo();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session != null) {
			Roles role = Roles.valueOf((Integer) session.getAttribute("role"));
			
			if(role == Roles.STANDARD || role == Roles.PREMIUM) {			
				final String msg = "You do not have permission to view other peoples accounts";
				
				response.sendError(401, msg);
				return;
			} else if (role == Roles.EMPLOYEE || role == Roles.ADMIN) {
				String uri = request.getRequestURI();
				
				String []uriPieces = uri.split("/");
				
				Integer id = null;
				
				try {
					id = Integer.parseInt(uriPieces[3]);
				} catch (NumberFormatException e) {
					final String msg = "Incorrect path id";
					
					response.sendError(400, msg);
					return;
				}
				
				User user = userRepo.findUserById(id);
				
				if(user == null) {
					final String msg = "User does not exist";
					
					response.sendError(400, msg);
					return;
				}
				
				response.setStatus(200);
				response.getWriter().write(new ObjectMapper().writeValueAsString(user.getAccounts()));							
			}
		} else {
			final String msg = "Please login before viewing user accounts";
			
			response.sendError(401, msg);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
