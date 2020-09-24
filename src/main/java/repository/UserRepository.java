package repository;

import java.util.ArrayList;
import model.Roles;
import model.User;

public interface UserRepository {

	void insertUser(User user);

	ArrayList<User> findAllUsers();

	ArrayList<User> findUsersByRole(Roles role);

	ArrayList<User> findUsersByName(String firstName, String lastName);

	User findUserById(int id);

	User findUserByUsername(String username);

	User findUserByEmail(String email);

	void updateUser(User user);

	void deleteUser(User user);
}
