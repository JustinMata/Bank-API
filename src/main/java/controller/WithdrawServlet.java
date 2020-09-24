package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

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
 * Servlet implementation class WithdrawServlet
 */
public class WithdrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;
	private AccountRepo accountRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WithdrawServlet() {
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
			final Roles role = Roles.valueOf((Integer) (session.getAttribute("role")));

			if (role == Roles.STANDARD || role == Roles.PREMIUM || role == Roles.EMPLOYEE) {
				User user = userRepo.findUserByUsername((String) session.getAttribute("username"));

				try {
					Integer.parseInt(request.getParameter("accountid"));
				} catch (NumberFormatException e) {
					final String msg = "Incorrect account id";

					response.sendError(400, msg);
					return;
				}

				Account acc = accountRepo.findAccountbyId(Integer.parseInt(request.getParameter("accountid")));

				if (accountRepo.isAccountLinked(user, acc)) {
					BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
					
					if(acc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
						DecimalFormat df = new DecimalFormat("#,###.00");
						final String msg = "Insufficient funds. You may not withdraw more than the current balance of $" + df.format(acc.getBalance()) + ".";
						
						response.sendError(400, msg);
						return;
					}

					acc.setBalance(acc.getBalance().subtract(amount));

					accountRepo.updateAccount(acc);
					
					DecimalFormat df = new DecimalFormat("#,###.00");
					final String msg = "You have withdrawn $" + df.format(amount) + " from account " + acc.getAccountId()
							+ ".\n New balance is $" + df.format(acc.getBalance()) + ".";

					response.setStatus(200);
					response.getWriter().write(msg);
				} else {
					final String msg = "This account does not belong to you";

					response.sendError(401, msg);
					return;
				}
			} else if (role == Roles.ADMIN) {
				User user = userRepo.findUserByUsername(request.getParameter("userid"));

				if (user == null) {
					final String msg = "This user does not exist";

					response.sendError(400, msg);
					return;
				}

				try {
					Integer.parseInt(request.getParameter("accountId"));
				} catch (NumberFormatException e) {
					final String msg = "Incorrect account id";

					response.sendError(400, msg);
					return;
				}

				Account acc = accountRepo.findAccountbyId(Integer.parseInt(request.getParameter("accountid")));

				if (accountRepo.isAccountLinked(user, acc)) {
					BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
					
					if(acc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
						DecimalFormat df = new DecimalFormat("#,###.00");
						final String msg = "Insufficient funds. You may not withdraw more than the current balance of $" + df.format(acc.getBalance()) + ".";
						
						response.sendError(400, msg);
						return;
					}
					acc.setBalance(acc.getBalance().subtract(amount));

					accountRepo.updateAccount(acc);
					
					DecimalFormat df = new DecimalFormat("#,###.00");
					final String msg = "You have withdrawn $" + df.format(amount) + " from account " + acc.getAccountId()
							+ ".\n New balance is $" + df.format(acc.getBalance()) + ".";

					response.setStatus(200);
					response.getWriter().write(msg);
				} else {
					final String msg = "This account does not belong to this user";

					response.sendError(401, msg);
					return;
				}
			}
		} else {
			final String msg = "Please log in to make a withdrawal";

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
