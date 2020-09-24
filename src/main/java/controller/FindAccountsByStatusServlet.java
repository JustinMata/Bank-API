package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Account;
import model.Roles;
import model.Status;
import repository.AccountRepo;

/**
 * Servlet implementation class FindAccountsByStatus
 */
public class FindAccountsByStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AccountRepo accountRepo;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FindAccountsByStatusServlet() {
        super();
        accountRepo = new AccountRepo();
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
				
				if(id < 0 || id > 4) {
					final String msg = "Invalid status id";
					
					response.sendError(400, msg);
					return;
				}
				
				ArrayList<Account> acc = accountRepo.findAccountsByStatus(Status.valueOf(id));
				
				if(acc == null) {
					final String msg = "There are no accounts under this status";
					
					response.sendError(400, msg);
					return;
				} else {
					response.setStatus(200);
					response.getWriter().write(new ObjectMapper().writeValueAsString(acc));					
				}				
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
