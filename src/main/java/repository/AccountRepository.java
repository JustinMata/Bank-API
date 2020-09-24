package repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import model.Account;
import model.Status;
import model.Type;
import model.User;

public interface AccountRepository {

	void insertAccount(Account acc, User user);
	
	void linkAccount(Account acc, User user);

	ArrayList<Account> findAllAccounts();
	
	ArrayList<Account> findAccountsUnderBalance(BigDecimal balance);

	ArrayList<Account> findAccountsOverBalance(BigDecimal balance);

	ArrayList<Account> findAccountsByStatus(Status status);

	ArrayList<Account> findAccountsByType(Type type);

	Account findAccountbyId(int id);

	void updateAccount(Account acc);

	void deleteAccount(Account acc);
	
	Boolean isAccountLinked(User user, Account acc);
}
