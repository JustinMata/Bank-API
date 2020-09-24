package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.User;
import model.Account;
import model.AccountStatus;
import model.AccountType;
import model.Role;
import model.Roles;
import model.Status;
import model.Type;
import util.ConnectionManager;

public class UserRepo implements UserRepository {

	// Insert user into table
	public void insertUser(User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "insert into bank_app.users values(default, ?, ?, ?, ?, ?, ?)";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL);

			stmt.setString(1, user.getUsername().toLowerCase());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName().toLowerCase());
			stmt.setString(4, user.getLastName().toLowerCase());
			stmt.setString(5, user.getEmail().toLowerCase());
			stmt.setInt(6, user.getRole().getRoleId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Finds all users
	public ArrayList<User> findAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		ResultSet numberOfAccountsSet = null;
		final String SQL1 = "select * from bank_app.users";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			set = stmt.executeQuery();

			// Go through every user
			while (set.next()) {
				// Generate a user model to be added to users ArrayList later
				User user = generateUser(set);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				numberOfAccountsSet = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, numberOfAccountsSet, user);

				// Add user
				users.add(user);
				ConnectionManager.closeResultSet(numberOfAccountsSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return users;
	}

	// Finds all users with role
	public ArrayList<User> findUsersByRole(Roles role) {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		ResultSet numberOfAccountsSet = null;
		final String SQL1 = "select * from bank_app.users where aRole = ?";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			stmt.setInt(1, role.getValue());
			set = stmt.executeQuery();

			// Go through every matched user
			while (set.next()) {
				// Generate a user model to be added to users ArrayList later
				User user = generateUser(set);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				numberOfAccountsSet = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, numberOfAccountsSet, user);

				// Add user
				users.add(user);
				ConnectionManager.closeResultSet(numberOfAccountsSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return users;
	}

	// Finds all users with name
	public ArrayList<User> findUsersByName(String firstName, String lastName) {
		ArrayList<User> users = new ArrayList<User>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		ResultSet numberOfAccountsSet = null;
		final String SQL1 = "select * from bank_app.users where firstName = ? and lastName = ?";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			stmt.setString(1, firstName);
			stmt.setString(2, lastName);
			set = stmt.executeQuery();

			// Go through every matched user
			while (set.next()) {
				// Generate a user model to be added to users ArrayList later
				User user = generateUser(set);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				numberOfAccountsSet = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, numberOfAccountsSet, user);

				// Add user
				users.add(user);
				ConnectionManager.closeResultSet(numberOfAccountsSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return users;
	}

	// Finds a user by their Id
	public User findUserById(int id) {
		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL1 = "select * from bank_app.users where id = ?";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			stmt.setInt(1, id);
			set = stmt.executeQuery();

			// If user is found, generate a user model to be returned
			if (set.next()) {
				user = generateUser(set);

				// Clearing for reuse
				ConnectionManager.closeResultSet(set);
				ConnectionManager.closeStatement(stmt);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				set = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, set, user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return user;
	}

	// Finds a user by their username
	public User findUserByUsername(String username) {
		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL1 = "select * from bank_app.users where username = ?";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			stmt.setString(1, username);
			set = stmt.executeQuery();

			// If user is found, generate a user model to be returned
			if (set.next()) {
				user = generateUser(set);

				// Clearing for reuse
				ConnectionManager.closeResultSet(set);
				ConnectionManager.closeStatement(stmt);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				set = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, set, user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return user;
	}

	// Finds a user by their email
	public User findUserByEmail(String email) {
		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL1 = "select * from bank_app.users where email = ?";
		final String SQL2 = "select account_id from bank_app.users_accounts where user_id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL1);
			stmt.setString(1, email);
			set = stmt.executeQuery();

			// If user is found, generate a user model to be returned
			if (set.next()) {
				user = generateUser(set);

				// Clearing for reuse
				ConnectionManager.closeResultSet(set);
				ConnectionManager.closeStatement(stmt);

				// Searching bridge table users_accounts for any accounts linked to this user
				stmt = conn.prepareStatement(SQL2);
				stmt.setInt(1, user.getUserId());
				set = stmt.executeQuery();

				// Append any found accounts to the user's Accounts ArrayList
				appendAccounts(conn, set, user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return user;
	}

	// Updates the user in table
	public void updateUser(User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "update bank_app.users set username = ?, pw = ?, firstName = ?, lastName = ?, email = ?, aRole = ? where id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL);

			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getRole().getRoleId());
			stmt.setInt(7, user.getUserId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Deletes the user and any links it had to accounts, this is done through the
	// bridge table which cascades on delete
	public void deleteUser(User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "delete from bank_app.users where id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, user.getUserId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Generates a user using the given ResultSet
	public User generateUser(ResultSet set) {
		User user = null;
		try {
			user = new User(set.getInt(1), set.getString(2).toLowerCase(), set.getString(3),
					set.getString(4).toLowerCase(), set.getString(5).toLowerCase(), set.getString(6).toLowerCase(),
					new Role(Roles.valueOf(set.getInt(7))), new ArrayList<Account>());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	// Populates the User's Accounts ArrayList
	public void appendAccounts(Connection conn, ResultSet set, User user) {
		if (conn == null)
			return;

		PreparedStatement stmt = null;
		ResultSet accountSet = null;
		final String SQL = "select * from bank_app.accounts where id = ?";

		try {
			// Repeat while there are still accounts to be added
			while (set.next()) {
				stmt = conn.prepareStatement(SQL);
				stmt.setInt(1, set.getInt(1));
				accountSet = stmt.executeQuery();

				// If the account exists, append it to the User's accounts
				if (accountSet.next()) {
					Account acc = new Account(accountSet.getInt(1), accountSet.getBigDecimal(2),
							new AccountStatus(Status.valueOf(accountSet.getInt(3))),
							new AccountType(Type.valueOf(accountSet.getInt(4))));
					user.getAccounts().add(acc);
				}

				ConnectionManager.closeResultSet(accountSet);
				ConnectionManager.closeStatement(stmt);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}