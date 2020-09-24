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
import repository.AccountRepo;
import repository.UserRepo;

/**
 * Servlet implementation class ViewAccountsServlet
 */
public class ViewAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AccountRepo accountRepo;
	UserRepo userRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewAccountsServlet() {
		super();
		accountRepo = new AccountRepo();
		userRepo = new UserRepo();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		if (session != null) {
			Roles role = Roles.valueOf((Integer) session.getAttribute("role"));

			if (role == Roles.EMPLOYEE || role == Roles.ADMIN) {
				final String JSON = new ObjectMapper().writeValueAsString(accountRepo.findAllAccounts());

				response.setStatus(200);
				response.getWriter().write(JSON);
			} else if (role == Roles.STANDARD || role == Roles.PREMIUM){
				User user = userRepo.findUserByUsername((String) session.getAttribute("username"));
				
				final String JSON = new ObjectMapper().writeValueAsString(user.getAccounts());

				response.setStatus(200);
				response.getWriter().write(JSON);
			}

		} else {
			final String msg = "You must login to view all account information";

			response.sendError(401, msg);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
