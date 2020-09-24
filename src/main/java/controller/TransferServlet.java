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
 * Servlet implementation class TransferServlet
 */
public class TransferServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;
	private AccountRepo accountRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TransferServlet() {
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
					Integer.parseInt(request.getParameter("sourceaccountid"));
					Integer.parseInt(request.getParameter("targetaccountid"));
				} catch (NumberFormatException e) {
					final String msg = "There was an incorrect account id";

					response.sendError(400, msg);
					return;
				}

				Account sourceAcc = accountRepo
						.findAccountbyId(Integer.parseInt(request.getParameter("sourceaccountid")));
				
				if(sourceAcc == null) {
					final String msg = "Source account does not exist";
					
					response.sendError(400, msg);
					return;
				}
				
				Account targetAcc = accountRepo
						.findAccountbyId(Integer.parseInt(request.getParameter("targetaccountid")));
				
				if(targetAcc == null) {
					final String msg = "Target account does not exist";
					
					response.sendError(400, msg);
					return;
				}

				if (accountRepo.isAccountLinked(user, sourceAcc)) {
					BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));

					if (sourceAcc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
						DecimalFormat df = new DecimalFormat("#,###.00");
						final String msg = "Insufficient funds. You may not transer more than the current balance of $"
								+ df.format(sourceAcc.getBalance()) + " from account " + sourceAcc.getAccountId() + ".";

						response.sendError(400, msg);
						return;
					}

					sourceAcc.setBalance(sourceAcc.getBalance().subtract(amount));

					accountRepo.updateAccount(sourceAcc);

					targetAcc.setBalance(targetAcc.getBalance().add(amount));

					accountRepo.updateAccount(targetAcc);

					DecimalFormat df = new DecimalFormat("#,###.00");
					final String msg = "You have transferred $" + df.format(amount) + " from account "
							+ sourceAcc.getAccountId() + " to account " + targetAcc.getAccountId()
							+ ".\n New balance is $" + df.format(sourceAcc.getBalance()) + " for account "
							+ sourceAcc.getAccountId() + ".";

					response.setStatus(200);
					response.getWriter().write(msg);
				} else {
					final String msg = "This account does not belong to you";

					response.sendError(401, msg);
					return;
				}
			} else if (role == Roles.ADMIN) {
				try {
					Integer.parseInt(request.getParameter("sourceaccountId"));
					Integer.parseInt(request.getParameter("targetaccountId"));
				} catch (NumberFormatException e) {
					final String msg = "There was an incorrect account id";

					response.sendError(400, msg);
					return;
				}

				Account sourceAcc = accountRepo
						.findAccountbyId(Integer.parseInt(request.getParameter("sourceaccountid")));
				
				if(sourceAcc == null) {
					final String msg = "Source account does not exist";
					
					response.sendError(400, msg);
					return;
				}
				
				Account targetAcc = accountRepo
						.findAccountbyId(Integer.parseInt(request.getParameter("targetaccountid")));
				
				if(targetAcc == null) {
					final String msg = "Target account does not exist";
					
					response.sendError(400, msg);
					return;
				}

				BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));

				if (sourceAcc.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
					DecimalFormat df = new DecimalFormat("#,###.00");
					final String msg = "Insufficient funds. You may not transer more than the current balance of $"
							+ df.format(sourceAcc.getBalance()) + " from account " + sourceAcc.getAccountId() + ".";

					response.sendError(400, msg);
					return;
				}

				sourceAcc.setBalance(sourceAcc.getBalance().subtract(amount));

				accountRepo.updateAccount(sourceAcc);

				targetAcc.setBalance(targetAcc.getBalance().add(amount));

				accountRepo.updateAccount(targetAcc);

				DecimalFormat df = new DecimalFormat("#,###.00");
				final String msg = "You have transferred $" + df.format(amount) + " from account "
						+ sourceAcc.getAccountId() + " to account " + targetAcc.getAccountId() + ".\n New balance is $"
						+ df.format(sourceAcc.getBalance()) + " for account " + sourceAcc.getAccountId() + ".";

				response.setStatus(200);
				response.getWriter().write(msg);
			} else {
				final String msg = "This account does not belong to this user";

				response.sendError(401, msg);
				return;
			}
		} else {
			final String msg = "Please log in to make a transfer";

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
