package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Tchrstd_login {

	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost:3306/demo";
		String username = "arman";
		String password = "arman@07";
		Connection c = DriverManager.getConnection(url, username, password);
		System.out.println("\n1.Create Account(Only For Teachers)\n2.Sign-in\n3.Exit");
		Scanner Sc = new Scanner(System.in);
		int sl = Sc.nextInt();
		Statement st = c.createStatement();
		switch (sl) {

		case 1:
			PreparedStatement pr = c.prepareStatement("insert into Teachers values(?,?,?) ");

			System.out.println("Enter your Name:\n");
			String name = Sc.next();
			System.out.println("Enter which class you are in charge of :\n");
			int clss = Sc.nextInt();
			System.out.println("Enter your Password :\n");
			String psswrd = Sc.next();
			pr.setString(1, name);
			pr.setInt(2, clss);
			pr.setString(3, psswrd);
			pr.executeUpdate();
			System.out.println("Account successfully created\n " + "\n\nPlease login using your name and password");
			break;
		case 2:
			System.out.println("\n1.Teacher\n2.Student");
			int person = Sc.nextInt();
			if (person == 1) {

				System.out.println("\n1.Personal info\n" + "2.View Marklist\n" + "3.Change password\n" + "4.logout\n");
				int select = Sc.nextInt();
				switch (select) {
				case 1:
					String s = "select Sid,Sname,Sclass,Spassword,T.Tname from students S join Teachers T where T.Tclass_teacher_of = S.Sclass";
					ResultSet rs = st.executeQuery(s);
					while (rs.next()) {
						System.out.println("\nStudent id : " + rs.getInt(1) + "\nStudent name :" + rs.getString(2)
								+ "\nClass : \n" + rs.getInt(3) + "\nCurrent password : " + rs.getString(4)
								+ "\nClass Teacher : " + rs.getString(5));

					}
					break;
				case 2:
					String mrk = "select * from Marklist ";
					ResultSet rst = st.executeQuery(mrk);
					while (rst.next()) {
						System.out.println("\nStudent id : " + rst.getInt(1) + "\nStudent name :" + rst.getString(2)
								+ "\n    Your Marks are :" + "\n_________" + "\n\nSocial Studies : " + rst.getInt(3)
								+ "%" + "\nScience         : " + rst.getInt(4) + "%" + "\nMaths           : "
								+ rst.getInt(5) + "%" + "\nEnglish         : " + rst.getInt(6) + "%");

					}
					break;

				case 3:
					String upd = "update students set Spassword = ? where Sid = ? ";
					PreparedStatement pst = c.prepareStatement(upd);
					System.out.println("Enter the Current password : \n");

					System.out.println("Enter the new password : \n");
					String ps = Sc.next();
					pst.setString(1, ps);
					int executeUpdate = pst.executeUpdate();
					System.out.println(executeUpdate + " Password Successfully Changed ");
					// Exit and login again
					break;

				case 4:
					System.out.println("Successfully Logged out");
					break;

				default:
					System.out.println("Invalid Input");
				}
			} else if (person == 2) {
				System.out.println(
						"\n1. Add Student\n2. Add Marks\n3. View Class\n4. View Student\n5. Delete Student\n6. Update Student\n7. Personal Info\n8. Edit Personal Info\n 9.Logout");
				int slt = Sc.nextInt();
				switch (slt) {

				case 1:
					System.out.println("Enter which class to be added to :");
					int add = Sc.nextInt();
					PreparedStatement gg = c.prepareStatement("insert into students values(?,?,?)");
					System.out.println("Enter name of Student : ");
					String namenew = Sc.next();
					gg.setString(1, namenew);
					gg.setInt(2, add);
					System.out.println("Enter student  password : ");
					String passnew = Sc.next();
					gg.setString(3, passnew);
					int ii = gg.executeUpdate();
					System.out.println(namenew + " Successfully added to class " + add);
					break;
				case 2:
					System.out.println("Enter whose marks are to be added :");
					String nm = Sc.next();
					PreparedStatement gs = c.prepareStatement(
							"update Marklist set Social_Studies_by_Mrs_Sumathi=?,Science_by_Mrs_Sheela=?,Maths_by_Mr_Biju=?,English_by_Mr_Rateesh=?"
									+ "where Sname = ?");
					System.out.println("Marks in Social Studies : ");
					int SS = Sc.nextInt();
					gs.setInt(1, SS);
					System.out.println("Marks in Science : ");
					int S = Sc.nextInt();
					gs.setInt(2, S);
					System.out.println("Marks in Maths : ");
					int mths = Sc.nextInt();
					gs.setInt(3, mths);
					System.out.println("Marks in English : ");
					int eng = Sc.nextInt();
					gs.setInt(4, eng);
					gs.setString(5, nm);
					int ig = gs.executeUpdate();
					System.out.println(" Marklist of " + nm + "successfully updated ");
					break;
				case 3:
					System.out.println("Enter class to view :");
					int viewclass = Sc.nextInt();
					PreparedStatement stt = c.prepareStatement(
							"select Sid,Sname,Sclass,T.Tname from students S join Teachers T where S.Sclass = T.Tclass_teacher_of and Sclass=?;");
					stt.setInt(1, viewclass);
					ResultSet RS = stt.executeQuery();
					while (RS.next()) {
						System.out.println(
								RS.getInt(1) + "   " + RS.getString(2) + "   " + RS.getInt(3) + "  " + RS.getString(4));
					}
					break;

				case 4:
					System.out.println("Enter the same of student : ");
					String nms = Sc.next();
					PreparedStatement pst = c.prepareStatement(" select * from students where Sname=?");
					pst.setString(1, nms);
					ResultSet rslt = pst.executeQuery();
					while (rslt.next()) {
						System.out.println("\nStudent id : " + rslt.getInt(1) + " \nStudent name : " + rslt.getString(2)
								+ "\nClass : " + rslt.getInt(3) + "\nCurrent password : " + rslt.getString(4));
					}
					break;
				case 5:
					System.out.println("Enter the name of student : ");
					String dltstu = Sc.next();
					PreparedStatement dlt = c.prepareStatement(" delete from students where Sname=?");
					dlt.setString(1, dltstu);
					int dlts = dlt.executeUpdate();
					System.out.println(dltstu + " Successfully removed ");
					break;

				case 6:
					System.out.println("Enter the id of student : ");
					int upstu = Sc.nextInt();
					PreparedStatement upd = c.prepareStatement(" update students set Sname=? where Sid=? ");
					System.out.println(" Enter new name : ");
					String updst = Sc.next();
					upd.setString(1, updst);
					upd.setInt(2, upstu);
					upd.executeUpdate();
					System.out.println("Student name changed : " + updst);
					break;

				case 7:
					System.out.println("Enter your id : ");
					int td = Sc.nextInt();
					PreparedStatement ps = c.prepareStatement(" select * from Teachers where Tid=?");
					ps.setInt(1, td);
					ResultSet persnl = ps.executeQuery();
					while (persnl.next()) {
						System.out.println(persnl.getInt(1) + "   " + persnl.getString(2) + "   " + persnl.getInt(3)
								+ "  " + persnl.getString(4));
					}
					break;
				case 8:

					break;
				case 9:
					System.out.println(" Successfully logged out ");
					break;
				default:
					System.out.println(" Invalid Input ");
					break;
				}
			}

		case 3:
			System.out.println(" ***********Process successfully Terminated******* ");
			System.exit(0);
		}

	}
}