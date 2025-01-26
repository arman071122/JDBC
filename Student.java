package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Student {

	public void PersonalInfo(Connection c, String name) throws SQLException {
		System.out.println("\n============================");
		System.out.println("******* PERSONAL INFO ******");
		System.out.println("============================");

		PreparedStatement view = c.prepareStatement(
				"select s.Sid,s.Sname,s.class,t.Tname from student s join teacher t on s.Class=t.Class_In_Charge where Sname=?;");
		view.setString(1, name);

		ResultSet viewRes = view.executeQuery();
		while (viewRes.next()) {
			System.out.println("\nID              : " + viewRes.getInt(1));
			System.out.println("NAME            : " + viewRes.getString(2));
			System.out.println("CLASS           : " + viewRes.getInt(3));
			System.out.println("CLASS IN CHARGE : " + viewRes.getString(4));
		}
	}

	public void ViewMark(Connection c, int SidLogin) throws SQLException {
		System.out.println("\n=========================");
		System.out.println("******* VIEW MARKS ******");
		System.out.println("=========================");

		PreparedStatement viewMark = c.prepareStatement(
				"SELECT Maths, Sci, Eng, SS, Mal FROM markList where Sid IN(Select Sid from student where sid=?)");
		viewMark.setInt(1, SidLogin);
		boolean hasRows = false;

		ResultSet viewMarkRes = viewMark.executeQuery();
		while (viewMarkRes.next()) {
			hasRows = true;
			System.out.println("\nMATHS     : " + viewMarkRes.getInt(1));
			System.out.println("SCIENCE   : " + viewMarkRes.getInt(2));
			System.out.println("ENGLISH   : " + viewMarkRes.getInt(3));
			System.out.println("SS        : " + viewMarkRes.getInt(4));
			System.out.println("MALAYALAM : " + viewMarkRes.getInt(5));
		}
		if (!hasRows)
			System.out.println("\nMarks not published");
	}

	public boolean ChangePass(Connection c, Scanner sc, int SidLogin, String name, boolean logout) throws SQLException {
		System.out.println("\n==============================");
		System.out.println("******* CHANGE PASSWORD ******");
		System.out.println("==============================");

		System.out.print("\nNEW PASSWORD : ");
		String passN = sc.next();

		System.out.println("\nConfirm the new password: " + passN);
		System.out.print("\nProceed with update? (y/n): ");
		char confirm = sc.next().charAt(0);

		if (confirm == 'y') {
			PreparedStatement chP = c.prepareStatement("UPDATE student SET password=? WHERE Sid=?");
			chP.setString(1, passN);
			chP.setInt(2, SidLogin);

			int chPRes = chP.executeUpdate();
			if (chPRes > 0) {
				System.out.println("\nChanged password of " + name);
				System.out
						.println("\nPersonal details have been modified. You will be logged out for security reasons.");
				System.out.println("Please log in again with your new credentials.");
				logout = true;
			} else
				System.out.println("\nError: Unable to change password");
		} else {
			System.out.println("\nPassword update canceled.");
		}
		return logout;
	}
}
