package week1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertPro {
	Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws SQLException {
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "arman", "arman@07");
		CallableStatement st = c.prepareCall("call insert(?,?,?)");

	}

}
