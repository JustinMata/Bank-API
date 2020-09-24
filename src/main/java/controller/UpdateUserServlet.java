package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Role;
import model.Roles;
import model.User;
import repository.UserRepo;

/**
 * Servlet implementation class UpdateUserServlet
 */
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserServlet() {
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

		if (session != null) {

			final String username = request.getParameter("username");
			final String password = request.getParameter("password");
			final String firstName = request.getParameter("firstname");
			final String lastName = request.getParameter("lastname");
			final String email = request.getParameter("email");
			final String role = request.getParameter("role");

			Roles callerRole = Roles.valueOf((Integer) session.getAttribute("role"));

			if (callerRole == Roles.STANDARD || callerRole == Roles.PREMIUM || callerRole == Roles.EMPLOYEE) {
				if (role != null) {
					final String msg = "Only admins are allowed to change roles";

					response.sendError(401, msg);
					return;
				}

				User user = userRepo.findUserByUsername((String) session.getAttribute("username"));

				if (username != null) {
					if (userRepo.findUserByUsername(username.toLowerCase()) == null) {
						user.setUsername(username.toLowerCase());
						session.setAttribute("username", username.toLowerCase());
					} else {
						final String msg = "Username already exists";

						response.sendError(400, msg);
						return;
					}
				}

				if (password != null)
					user.setPassword(password);

				if (firstName != null) {					
					user.setFirstName(firstName.toLowerCase());					
					session.setAttribute("firstname", firstName.toLowerCase());
				}

				if (lastName != null) {			
					user.setLastName(lastName.toLowerCase());					
					session.setAttribute("lastname", lastName.toLowerCase());
				}

				if (email != null) {
					if (userRepo.findUserByEmail(email.toLowerCase()) == null) {
						user.setEmail(email.toLowerCase());
					} else {
						final String msg = "Email already exists";

						response.sendError(400, msg);
						return;
					}
				}

				userRepo.updateUser(user);

				final String msg = "Account successfully updated!";

				response.setStatus(200);
				response.getWriter().write(msg);

			} else if (callerRole == Roles.ADMIN) {
				final String originalUsername = request.getParameter("originalusername");
				
				if(originalUsername == null) {
					final String msg = "Please provide the original username to update";
					
					response.sendError(400, msg);
					return;
				}
				
				final Boolean isAdminAccount = originalUsername.equals((String) session.getAttribute("username"));
				
				User user = userRepo.findUserByUsername(originalUsername.toLowerCase());
				
				if(user == null) {
					final String msg = "Username does not exist";
					
					response.sendError(400, msg);
					return;
				}

				if (username != null) {
					if (userRepo.findUserByUsername(username.toLowerCase()) == null) {
						user.setUsername(username.toLowerCase());
						if(isAdminAccount)
							session.setAttribute("username", username.toLowerCase());
					} else {
						final String msg = "Username already exists";

						response.sendError(400, msg);
						return;
					}
				}

				if (password != null)
					user.setPassword(password);

				if (firstName != null) {
					user.setFirstName(firstName.toLowerCase());
					if(isAdminAccount)
						session.setAttribute("firstname", firstName.toLowerCase());				
				}

				if (lastName != null) {
					user.setLastName(lastName.toLowerCase());
					if(isAdminAccount)
						session.setAttribute("lastname", lastName.toLowerCase());	
				}

				if (email != null) {
					if (userRepo.findUserByEmail(email.toLowerCase()) == null) {
						user.setEmail(email.toLowerCase());
					} else {
						final String msg = "Email already exists";

						response.sendError(400, msg);
						return;
					}
				}

				if (role != null) {
					try {
						user.setRole(new Role(Roles.valueOf(Integer.parseInt(role))));
						if(isAdminAccount)
							session.setAttribute("role", Integer.parseInt(role));
					} catch (NumberFormatException e) {
						final String msg = "Incorrect role id";
						
						response.sendError(400, msg);
						return;
					}

				}
				
				userRepo.updateUser(user);

				final String msg = "Account successfully updated!";

				response.setStatus(200);
				response.getWriter().write(msg);
			}
		} else {
			final String msg = "Please login before updating user information";

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
