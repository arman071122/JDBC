package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MenuQ {
	static Scanner sc = new Scanner(System.in);

	static void add(Connection c) throws SQLException {
		PreparedStatement add = c.prepareStatement("Insert into students(id,name,age) values(?,?,?)");

		System.out.println("\n Enter details");
		System.out.print("ID : ");
		int id = sc.nextInt();

		System.out.print("NAME : ");
		String name = sc.next();

		System.out.print("AGE : ");
		int age = sc.nextInt();

		add.setInt(1, id);
		add.setString(2, name);
		add.setInt(3, age);

		int addRes = add.executeUpdate();
		if (addRes > 0)
			System.out.println(addRes + " entries inserted");
		else
			System.out.println("error");
	}

	static void view(Connection c) throws SQLException {
		Statement view = c.createStatement();
		ResultSet viewRes = view.executeQuery("select * from students");

		System.out.println();
		boolean hasRows = false;

		while (viewRes.next()) {
			hasRows = true;
			System.out.println(viewRes.getInt(1) + " " + viewRes.getString(2) + " " + viewRes.getInt(3));
		}
		if (!hasRows)
			System.out.println("Empty set");
	}

	static void update(Connection c) throws SQLException {
		PreparedStatement update = c.prepareStatement("update students set name=?,age=? where id=?");
		System.out.print("\n Enter the id which u want to edit : ");
		int id = sc.nextInt();

		System.out.println("\n Enter new name and age");
		String name = sc.next();
		int age = sc.nextInt();

		update.setString(1, name);
		update.setInt(2, age);
		update.setInt(3, id);

		int upRes = update.executeUpdate();
		if (upRes > 0)
			System.out.println(upRes + " entries edited");
		else
			System.out.println("error" + id + " not found");
	}

	static void delete(Connection c) throws SQLException {
		System.out.println("\n1. DELETE ALL ");
		System.out.println("2. DELETE ONE BY ONE");
		System.out.print("\nEnter choice : ");
		int ch = sc.nextInt();

		switch (ch) {
		case 1: {
			Statement delete = c.createStatement();

			int delRes = delete.executeUpdate("delete from students");

			if (delRes > 0)
				System.out.println("\nAll " + delRes + " entries deleted");
			else
				System.out.println("\nNo entries found");
			break;
		}
		case 2: {
			PreparedStatement delete = c.prepareStatement("delete from students where id=?");
			System.out.print("\nEnter id of student u want to delete : ");
			int id = sc.nextInt();

			delete.setInt(1, id);
			int delRes = delete.executeUpdate();

			if (delRes > 0)
				System.out.println("\n" + delRes + " entries deleted");
			else
				System.out.println("\n" + id + " not found error");
			break;
		}
		}
	}

	public static void main(String[] args) throws SQLException {
		System.out.println("Student Management System");
		System.out.println("**************************\n");

		System.out.println("1. ADD STUDENT");
		System.out.println("2. VIEW ALL STUDENT");
		System.out.println("3. UPDATE STUDENT");
		System.out.println("4. DELETE STUDENT");
		System.out.println("5. EXIT\n");

		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc", "arman", "arman@07");

		int ch;
		do {
			System.out.print("\nEnter choice : ");
			ch = sc.nextInt();
			switch (ch) {
			case 1: {
				char yn;
				do {
					add(c);
					System.out.println("\nContinue adding? (y/n)");
					yn = sc.next().charAt(0);
				} while (yn == 'y');
				break;
			}
			case 2:
				view(c);
				break;

			case 3: {
				char yn;
				do {
					update(c);
					System.out.println("\nContinue updating? (y/n)");
					yn = sc.next().charAt(0);
				} while (yn == 'y');
				break;
			}
			case 4: {
				char yn;
				do {
					delete(c);
					System.out.println("\nContinue deleting? (y/n)");
					yn = sc.next().charAt(0);
				} while (yn == 'y');
				break;
			}
			default: {
				if (ch == 5)
					System.out.println("Bye Bye");
				else
					System.out.println("Invalid input\n");
			}
			}
		} while (ch != 5);
	}
}
