package controller;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class ViewServlet
 */
public class ViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserRepo userRepo;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewServlet() {
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
				final String JSON = new ObjectMapper().writeValueAsString(userRepo.findUserByUsername((String) session.getAttribute("username")));
				
				response.setStatus(200);
				response.getWriter().write(JSON);
				
			} else if (role == Roles.EMPLOYEE || role == Roles.ADMIN) {
				ArrayList<User> users = userRepo.findAllUsers();
				
				final String JSON = new ObjectMapper().writeValueAsString(users);
				
				response.setStatus(200);
				response.getWriter().write(JSON);
			}
		} else {
			final String msg = "Please login before viewing user information";
			
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
