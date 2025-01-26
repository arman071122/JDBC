package week1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Procedures {
	public static void main(String[] args) throws SQLException {
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "arman", "arman@07");
		CallableStatement st = c.prepareCall("call displayAll()");
		ResultSet res = st.executeQuery();
		while (res.next())
			System.out.println(res.getInt(1) + " " + res.getString(2) + " " + res.getInt(3));
	}

}
