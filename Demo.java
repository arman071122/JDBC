package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Demo {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String username = "arman";
		String password = "arman@07";

		Connection c = DriverManager.getConnection(url, username, password);
		Statement st = c.createStatement();

//	String q = "INSERT INTO students (id, name, age) VALUES (8,'dolu',45);";
//	boolean res = st.execute(q);
//	
//	if (res == true)
//	    System.out.println("fail");
//	else
//	    System.out.println("success");

		String q = "select * from students";
		ResultSet res = st.executeQuery(q);
		while (res.next()) {
			System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getInt(3));
		}
	}
}
