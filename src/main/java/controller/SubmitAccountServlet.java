package controller;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Account;
import model.AccountStatus;
import model.AccountType;
import model.Roles;
import model.Status;
import model.Type;
import model.User;
import repository.AccountRepo;
import repository.UserRepo;

/**
 * Servlet implementation class SubmitAccountServlet
 */
public class SubmitAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;
	private AccountRepo accountRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SubmitAccountServlet() {
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
			final String balance = request.getParameter("balance");
			final String accountType = request.getParameter("accounttype");

			try {
				Double.parseDouble(request.getParameter("balance"));
				Integer.parseInt(accountType);
			} catch (NumberFormatException e) {
				final String msg = "Incorrect balance format or incorrect account type format";

				response.sendError(400, msg);
				return;
			}
			
			if(Double.parseDouble(balance) < 0) {
				final String msg = "You may not open an account with a negative balance";
				
				response.sendError(400, msg);
				return;
			}
			
			if(Integer.parseInt(accountType) < 0 || Integer.parseInt(accountType) > 2) {
				final String msg = "Invalid account type";
				
				response.sendError(400, msg);
				return;
			}

			final Roles role = Roles.valueOf((Integer) session.getAttribute("role"));

			if (role == Roles.STANDARD || role == Roles.PREMIUM) {
				
				User user = userRepo.findUserByUsername((String) session.getAttribute("username"));
				Account acc = new Account(0, BigDecimal.valueOf(Double.parseDouble(balance)),
						new AccountStatus(Status.valueOf(1)),
						new AccountType(Type.valueOf(Integer.parseInt(accountType))));

				accountRepo.insertAccount(acc, user);

				final String msg = "Account successfully created! An employee will review your account shortly.";

				response.setStatus(201);
				response.getWriter().write(msg);

			} else if (role == Roles.EMPLOYEE || role == Roles.ADMIN) {
				
				final String username = request.getParameter("username");
				final String accountStatus = request.getParameter("accountstatus");
				
				if(Integer.parseInt(accountStatus) < 0 || Integer.parseInt(accountStatus) > 4) {
					final String msg = "Invalid account status";
					
					response.sendError(400, msg);
					return;
				}
				
				User user = userRepo.findUserByUsername(username);
				
				if(user == null) {
					final String msg = "User does not exist";
					
					response.sendError(400, msg);
					return;
				}
				
				Account acc = new Account(0, BigDecimal.valueOf(Double.parseDouble(balance)),
						new AccountStatus(Status.valueOf(Integer.parseInt(accountStatus))),
						new AccountType(Type.valueOf(Integer.parseInt(accountType))));

				accountRepo.insertAccount(acc, user);

				final String msg = "Account successfully created!";

				response.setStatus(201);
				response.getWriter().write(msg);
			}
		} else {
			final String msg = "You must be logged in to open an account.";

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
