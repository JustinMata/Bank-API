package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import repository.UserRepo;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserRepo userRepo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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

			User user = userRepo.findUserByUsername(username);

			if (user != null) {
				if (user.getPassword().equals(password)) {
					session = request.getSession();
					session.setAttribute("username", username);
					session.setAttribute("firstname", user.getFirstName());
					session.setAttribute("lastname", user.getLastName());
					session.setAttribute("role", user.getRole().getRoleId());

					final String msg = "Welcome back " + user.getFirstName() + " " + user.getLastName() + ".";

					response.setStatus(200);
					response.getWriter().write(msg);
				} else {
					final String msg = "Username or password was incorrect";

					response.sendError(400, msg);
				}
			} else {
				final String msg = "Username or password was incorrect";

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
