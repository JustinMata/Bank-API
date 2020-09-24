package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectionManager {

	public static Connection getConnection() {
		Connection conn = null;
		InputStream iStream = null;
		Properties props = new Properties();

		try {
			iStream = ConnectionManager.class.getResourceAsStream("/application.properties");
			props.load(iStream);
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(props.getProperty("dbconnectionstring"), props.getProperty("dbusername"),
					props.getProperty("dbpassword"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

		return conn;
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeStatement(Statement stmt) {
		try {
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet set) {
		try {
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
