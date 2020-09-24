package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Account;
import model.Roles;
import model.User;
import repository.AccountRepo;
import repository.UserRepo;

/**
 * Servlet implementation class LinkAccountsServlet
 */
public class LinkAccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;
	private AccountRepo accountRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LinkAccountsServlet() {
		super();
		userRepo = new UserRepo();
		accountRepo = new AccountRepo();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session != null) {
			final String userId = request.getParameter("userid");
			final String accountId = request.getParameter("accountid");

			try {
				Integer.parseInt(userId);
				Integer.parseInt(accountId);
			} catch (NumberFormatException e) {
				final String msg = "Incorrect user id format or incorrect account id format";

				response.sendError(400, msg);
				return;
			}

			final Roles role = Roles.valueOf((Integer) session.getAttribute("role"));

			if (role == Roles.EMPLOYEE || role == Roles.ADMIN) {

				User user = userRepo.findUserById(Integer.parseInt(userId));
				
				if(user == null) {
					final String msg = "User does not exist";
					
					response.sendError(400, msg);
					return;
				}
		
				Account acc = accountRepo.findAccountbyId(Integer.parseInt(accountId));
				
				if(acc == null) {
					final String msg = "Account does not exist";
					
					response.sendError(400, msg);
					return;
				}

				accountRepo.linkAccount(acc, user);

				final String msg = "Account and user successfully linked!";

				response.setStatus(201);
				response.getWriter().write(msg);
			} else {
				final String msg = "You do not have permission to link an account with a user";
				
				response.sendError(401, msg);
			}
		} else {
			final String msg = "You must be logged in to link an account with a user.";

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
