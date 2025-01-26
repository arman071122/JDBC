package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertQParam {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
	Scanner sc = new Scanner(System.in);
	Class.forName("com.mysql.cj.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3306/jdbc";
	String username = "arman";
	String password = "arman@07";

	Connection c = DriverManager.getConnection(url, username, password);

	String q = "insert into students(id,name,age) values (?,?,?);";
	PreparedStatement ps = c.prepareStatement(q);
	System.out.println("Enter id, name and age");
	int id = sc.nextInt();
	String name = sc.next();
	int age = sc.nextInt();

	ps.setInt(1, id);
	ps.setString(2, name);
	ps.setInt(3, age);

	int res = ps.executeUpdate();
	if (res > 0)
	    System.out.println(res + " entries inserted");
	else
	    System.out.println("error");
    }

}
