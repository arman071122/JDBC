package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateQParam {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String username = "arman";
		String password = "arman@07";

		Connection c = DriverManager.getConnection(url, username, password);

		String q = "update students set name=? , age=? where id=?";
		PreparedStatement ps = c.prepareStatement(q);
		System.out.println("Enter the id u want to edit");
		int id = sc.nextInt();
		System.out.println("Enter new name and age");
		String name = sc.next();
		int age = sc.nextInt();

		ps.setString(1, name);
		ps.setInt(2, age);
		ps.setInt(3, id);

		int res = ps.executeUpdate();
		if (res > 0)
			System.out.println(res + " entries edited");
		else
			System.out.println("error");
	}
}
