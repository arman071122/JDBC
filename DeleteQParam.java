package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteQParam {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String username = "arman";
		String password = "arman@07";

		Connection c = DriverManager.getConnection(url, username, password);

		String q = "delete from students where id=?";
		PreparedStatement ps = c.prepareStatement(q);
		System.out.println("Enter id u want to delete");
		int id = sc.nextInt();

		ps.setInt(1, id);

		int res = ps.executeUpdate();
		if (res > 0)
			System.out.println(res + " entries deleted");
		else
			System.out.println("error");
	}

}
