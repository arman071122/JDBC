package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class deleteQ {
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String username = "arman";
		String password = "arman@07";
		Connection c = DriverManager.getConnection(url, username, password);

		String q = "delete from students where id in(6,7);";
		Statement st = c.createStatement();
		int res = st.executeUpdate(q);

		if (res > 0)
			System.out.println(res + " rows deleted");
		else
			System.out.println("error");
	}
}
