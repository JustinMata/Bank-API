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
import repository.AccountRepo;

/**
 * Servlet implementation class UpdateAccountServlet
 */
public class UpdateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AccountRepo accountRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateAccountServlet() {
		super();
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

			Roles role = Roles.valueOf((Integer) session.getAttribute("role"));

			if (role == Roles.ADMIN) {
				final String accountId = request.getParameter("accountid");
				final String balance = request.getParameter("balance");
				final String accountStatus = request.getParameter("accountstatus");
				final String accountType = request.getParameter("accounttype");

				try {
					Integer.parseInt(accountId);
				} catch (NumberFormatException e) {
					final String msg = "Incorrect account id";

					response.sendError(400, msg);
					return;
				}

				Account acc = accountRepo.findAccountbyId(Integer.parseInt(accountId));

				if (acc == null) {
					final String msg = "Account does not exist";

					response.sendError(400, msg);
					return;
				}

				if (balance != null) {
					try {
						Double.parseDouble(balance);
					} catch (NumberFormatException e) {
						final String msg = "Incorrect balance format";
						
						response.sendError(400, msg);
						return;
					}
					if(Double.parseDouble(balance) < 0) {
						final String msg = "Balance cannot be less than 0";
						
						response.sendError(400, msg);
						return;
					} else {
						acc.setBalance(BigDecimal.valueOf(Double.parseDouble(balance)));
					}
				}
				
				if (accountStatus != null) {
					try {
						Integer.parseInt(accountStatus);
					} catch (NumberFormatException e) {
						final String msg = "Incorrect account status format";
						
						response.sendError(400, msg);
						return;
					}
					if(Integer.parseInt(accountStatus) < 0 || Integer.parseInt(accountStatus) > 4) {
						final String msg = "Invalid status id";
						
						response.sendError(400, msg);
						return;
					} else {
						acc.setStatus(new AccountStatus(Status.valueOf(Integer.parseInt(accountStatus))));
					}
				}
				
				if (accountType != null) {
					try {
						Integer.parseInt(accountType);
					} catch (NumberFormatException e) {
						final String msg = "Incorrect account type format";
						
						response.sendError(400, msg);
						return;
					}
					if(Integer.parseInt(accountType) < 0 || Integer.parseInt(accountType) > 2) {
						final String msg = "Invalid type id";
						
						response.sendError(400, msg);
						return;
					} else {
						acc.setType(new AccountType(Type.valueOf(Integer.parseInt(accountType))));
					}
				}


				accountRepo.updateAccount(acc);

				final String msg = "Account successfully updated!";

				response.setStatus(200);
				response.getWriter().write(msg);

			} else {
				final String msg = "Only admins are allowed to change account information";

				response.sendError(401, msg);
			}
		} else

		{
			final String msg = "Please login before updating account information";

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
