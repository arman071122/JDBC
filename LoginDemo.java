package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginDemo {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		String tempPass;
		System.out.println(" LOGIN PAGE");
		System.out.println("************\n");

		System.out.print("Username : ");
		String un = sc.next();
		System.out.print("Password : ");
		String pw = sc.next();

		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "arman", "arman@07");

		PreparedStatement checkUser = c.prepareStatement("SELECT * FROM users WHERE username=?");
		checkUser.setString(1, un);

		ResultSet userRes = checkUser.executeQuery();
		if (userRes.next()) {
			System.out.println("\nUser found");

			PreparedStatement login = c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
			login.setString(1, un);
			login.setString(2, pw);

			ResultSet loginRes = login.executeQuery();
			try {
				if (loginRes.next() && pw.equals(loginRes.getString("password"))) {
					System.out.println("Login success");
				} else
					System.out.println("\nCheck Password");
			} catch (SQLException e) {
				System.out.println("\nCheck Password");
			}
		} else
			System.out.println(un + " not found");
	}
}
