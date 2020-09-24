package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Account;
import model.Role;
import model.Roles;
import model.User;
import repository.UserRepo;

/**
 * Handles registering a user
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		userRepo = new UserRepo();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			final String username = request.getParameter("username").toLowerCase();
			final String password = request.getParameter("password");
			final String firstName = request.getParameter("firstname").toLowerCase();
			final String lastName = request.getParameter("lastname").toLowerCase();
			final String email = request.getParameter("email").toLowerCase();
			try {
				Integer.parseInt(request.getParameter("role"));
			} catch (NumberFormatException e) {
				final String msg = "Incorrect role id";
				
				response.sendError(400, msg);
				return;
			}
			
			if(Integer.parseInt(request.getParameter("role")) < 0 || Integer.parseInt(request.getParameter("role")) > 4) {
				final String msg = "Invalid role id";
				
				response.sendError(400, msg);
				return;
			}
			
			final Role role = new Role(Roles.valueOf(Integer.parseInt(request.getParameter("role"))));

			if (userRepo.findUserByUsername(username) == null && userRepo.findUserByEmail(email) == null) {
				User user = new User(0, username, password, firstName, lastName, email, role, new ArrayList<Account>());

				userRepo.insertUser(user);

				session = request.getSession();
				session.setAttribute("username", username);
				session.setAttribute("firstname", firstName);
				session.setAttribute("lastname", lastName);
				session.setAttribute("role", role.getRoleId());

				final String msg = "Account successfully created! Welcome to BankApp " + firstName + " " + lastName
						+ ".";

				response.setStatus(201);
				response.getWriter().write(msg);
			} else {
				final String msg = "Username or email already exists. Please try again.";
				response.sendError(400, msg);
			}
		} else {
			final String msg = "You are already logged in " + session.getAttribute("firstname") + " "
					+ session.getAttribute("lastname") + ".";

			response.setStatus(200);
			response.getWriter().write(msg);
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
