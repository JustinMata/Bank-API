package repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Account;
import model.AccountStatus;
import model.AccountType;
import model.Status;
import model.Type;
import model.User;
import util.ConnectionManager;

public class AccountRepo implements AccountRepository {

	// Inserts an account into the table and links it to the user
	public void insertAccount(Account acc, User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet generatedKeys = null;
		final String SQL = "insert into bank_app.accounts values(default, ?, ?, ?)";

		try {
			conn = ConnectionManager.getConnection();
			// Calling return generated keys to get the id of the newly created account
			stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			stmt.setBigDecimal(1, acc.getBalance());
			stmt.setInt(2, acc.getStatus().getStatusId());
			stmt.setInt(3, acc.getType().getTypeId());
			stmt.executeUpdate();

			generatedKeys = stmt.getGeneratedKeys();
			// Grab the newly generated id and create a link between this account and the
			// user
			if (generatedKeys.next()) {
				acc.setAccountId(generatedKeys.getInt(1));
				linkAccount(acc, user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Links an account to a user
	// Takes a connection so that it knows whether it was called by insertAccount or
	// not
	public void linkAccount(Account acc, User user) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "insert into bank_app.users_accounts values(?, ?)";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, user.getUserId());
			stmt.setInt(2, acc.getAccountId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Finds all accounts
	public ArrayList<Account> findAllAccounts() {
		ArrayList<Account> accs = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			set = stmt.executeQuery();

			// For every account, generate an account model and append it to the ArrayList
			while (set.next())
				accs.add(generateAccount(set));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return accs;
	}

	// Finds all accounts under a certain balance
	public ArrayList<Account> findAccountsUnderBalance(BigDecimal balance) {
		ArrayList<Account> accs = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts where balance <= ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setBigDecimal(1, balance);
			set = stmt.executeQuery();

			// For every account, generate an account model and append it to the ArrayList
			while (set.next())
				accs.add(generateAccount(set));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return accs;
	}

	// Finds all accounts over a certain balance
	public ArrayList<Account> findAccountsOverBalance(BigDecimal balance) {
		ArrayList<Account> accs = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts where balance >= ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setBigDecimal(1, balance);
			set = stmt.executeQuery();

			// For every account, generate an account model and append it to the ArrayList
			while (set.next())
				accs.add(generateAccount(set));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return accs;
	}

	// Finds all accounts with a specific status
	public ArrayList<Account> findAccountsByStatus(Status status) {
		ArrayList<Account> accs = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts where accountStatus = ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, status.getValue());
			set = stmt.executeQuery();

			// For every account, generate an account model and append it to the ArrayList
			while (set.next())
				accs.add(generateAccount(set));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return accs;
	}

	// Finds all accounts with a specific type
	public ArrayList<Account> findAccountsByType(Type type) {
		ArrayList<Account> accs = new ArrayList<Account>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts where accountType = ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, type.getValue());
			set = stmt.executeQuery();

			// For every account, generate an account model and append it to the ArrayList
			while (set.next())
				accs.add(generateAccount(set));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return accs;
	}

	// Finds an account by its id
	public Account findAccountbyId(int id) {
		Account acc = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		final String SQL = "select * from bank_app.accounts where id = ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, id);
			set = stmt.executeQuery();

			// If account is found, generate an account model to be returned
			if (set.next())
				acc = generateAccount(set);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return acc;
	}

	// Updates the account
	public void updateAccount(Account acc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "update bank_app.accounts set balance = ?, accountStatus = ?, accountType = ? where id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL);

			stmt.setBigDecimal(1, acc.getBalance());
			stmt.setInt(2, acc.getStatus().getStatusId());
			stmt.setInt(3, acc.getType().getTypeId());
			stmt.setInt(4, acc.getAccountId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}

	// Deletes the account and any links it had to users, this is done through the
	// bridge table which cascades on delete
	public void deleteAccount(Account acc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final String SQL = "delete from bank_app.accounts where id = ?";

		try {
			conn = ConnectionManager.getConnection();
			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, acc.getAccountId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeStatement(stmt);
		}
	}
	
	// Determines if an account is linked to a User
	public Boolean isAccountLinked(User user, Account acc) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet set = null;
		Boolean isLinked = false;
		final String SQL = "select * from bank_app.users_accounts where user_id = ? and account_id = ?";

		try {
			conn = ConnectionManager.getConnection();

			stmt = conn.prepareStatement(SQL);
			stmt.setInt(1, user.getUserId());
			stmt.setInt(2, acc.getAccountId());
			set = stmt.executeQuery();

			// If account is found, generate an account model to be returned
			if (set.next())
				isLinked = true;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.closeConnection(conn);
			ConnectionManager.closeResultSet(set);
			ConnectionManager.closeStatement(stmt);
		}
		return isLinked;
	}

	// Generates an account using the given ResultSet
	public Account generateAccount(ResultSet set) {
		Account acc = null;
		try {
			acc = new Account(set.getInt(1), set.getBigDecimal(2), new AccountStatus(Status.valueOf(set.getInt(3))),
					new AccountType(Type.valueOf(set.getInt(4))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acc;
	}
}
