package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class Teacher {
	public int AddStu(Connection c, int classIn, Scanner sc, String Sname) throws SQLException {

		System.out.print("PASSWORD : ");
		String password = sc.next();
		System.out.println();

		System.out.println("Confirm the details:");
		System.out.println("\nNAME     : " + Sname);
		System.out.println("CLASS    : " + classIn);
		System.out.println("PASSWORD : " + password);
		System.out.print("\nProceed with addition? (y/n): ");
		char confirm = sc.next().charAt(0);

		int id = 0;
		if (confirm == 'y') {
			PreparedStatement add = c.prepareStatement("INSERT INTO student(Sname, class, password) VALUES (?, ?, ?)");
			add.setString(1, Sname);
			add.setInt(2, classIn);
			add.setString(3, password);
			int addRes = add.executeUpdate();

			PreparedStatement getId = c.prepareStatement("SELECT Sid FROM student WHERE Sname=?");
			getId.setString(1, Sname);
			ResultSet getIdRes = getId.executeQuery();
			getIdRes.next();
			id = getIdRes.getInt(1);

			if (addRes > 0)
				System.out.println(
						"\nSuccessfully added student, " + Sname + " to class " + classIn + " and his/her id is " + id);

		} else {
			System.out.println("\nStudent addition canceled.");
		}
		return id;
	}

	public void AddMark(Connection c, int classIn, Scanner sc, int Sid) throws SQLException {

		PreparedStatement s2 = c.prepareStatement("SELECT Sname, class FROM student WHERE Sid=?");
		s2.setInt(1, Sid);

		ResultSet getStuName = s2.executeQuery();
		if (getStuName.next()) {
			String StuName = getStuName.getString(1);
			int Sclass = getStuName.getInt(2);

			if (Sclass == classIn) {
				PreparedStatement isMark = c.prepareStatement("SELECT * FROM marklist WHERE Sid=?");
				isMark.setInt(1, Sid);
				ResultSet isMarkRes = isMark.executeQuery();

				if (!isMarkRes.next()) {
					System.out.println("\nYou are adding marks to student, " + StuName);
					System.out.print("\nMATHS     : ");
					int mat = sc.nextInt();
					System.out.print("SCIENCE   : ");
					int sci = sc.nextInt();
					System.out.print("ENGLISH   : ");
					int eng = sc.nextInt();
					System.out.print("SS        : ");
					int ss = sc.nextInt();
					System.out.print("MALAYALAM : ");
					int mal = sc.nextInt();

					System.out.println("\nConfirm the marks entered:");
					System.out.println("\nMATHS     : " + mat);
					System.out.println("SCIENCE   : " + sci);
					System.out.println("ENGLISH   : " + eng);
					System.out.println("SS        : " + ss);
					System.out.println("MALAYALAM : " + mal);
					System.out.print("\nProceed with adding marks? (y/n): ");
					char confirm = sc.next().charAt(0);

					if (confirm == 'y') {
						PreparedStatement addMark = c
								.prepareStatement("INSERT INTO marklist VALUES (?, ?, ?, ?, ?, ?)");
						addMark.setInt(1, Sid);
						addMark.setInt(2, mat);
						addMark.setInt(3, sci);
						addMark.setInt(4, eng);
						addMark.setInt(5, ss);
						addMark.setInt(6, mal);

						int addMarkRes = addMark.executeUpdate();
						if (addMarkRes > 0)
							System.out.println("\nSuccessfully added marks to " + StuName);
						else
							System.out.println("\nError");
					} else {
						System.out.println("\nMarks addition canceled.");
					}
				} else {
					System.out.println("\nMarks for student " + Sid + " " + StuName
							+ " already found. For updation go to option 6.");
				}

			} else {
				System.out.println("\nError: Student " + Sid + " " + StuName + " not from your class. Try again.");
			}
		} else {
			System.out.println("\nError: No student found with ID " + Sid + ". Try again.");
		}
	}

	public void ViewClass(Connection c, int classIn) throws SQLException {
		boolean hasRows = false;
		PreparedStatement viewCls = c.prepareStatement(
				"SELECT s.Sid, s.Sname, m.Maths, m.Sci, m.Eng, m.SS, m.Mal FROM student s JOIN markList m ON s.Sid = m.Sid WHERE s.class = (SELECT Class_In_Charge FROM teacher WHERE Class_In_Charge = ?)");
		viewCls.setInt(1, classIn);

		ResultSet viClsRes = viewCls.executeQuery();
		if (viClsRes.next()) {
			hasRows = true;
			System.out.printf("\n%-5s %-10s %-9s %-9s %-9s %-9s %-9s\n", "Sid", "Sname", "Maths", "Science", "English",
					"SS", "Malayalam");
			System.out.println("----------------------------------------------------------------------------");
		}

		viClsRes = viewCls.executeQuery();
		while (viClsRes.next()) {
			System.out.printf("%-5d %-10s %-9d %-9d %-9d %-9d %-9d\n", viClsRes.getInt("Sid"),
					viClsRes.getString("Sname"), viClsRes.getInt("Maths"), viClsRes.getInt("Sci"),
					viClsRes.getInt("Eng"), viClsRes.getInt("SS"), viClsRes.getInt("Mal"));
		}

		if (!hasRows)
			System.out.println("\nNo student found in class " + classIn);
	}

	public void ViewStu(Connection c, int classIn, Scanner sc) throws SQLException {

		boolean hasRows = false;
		System.out.print("\nEnter the id of student : ");
		int sId = sc.nextInt();
		PreparedStatement vSid = c.prepareStatement(
				"select s.sid,s.sname,s.class,m.maths,m.sci,m.eng,m.ss,m.mal from student s join marklist m ON s.sid=m.sid where s.sid=?");
		vSid.setInt(1, sId);
		ResultSet vSidRes = vSid.executeQuery();

		if (vSidRes.next()) {
			hasRows = true;
			System.out.printf("\n%-5s %-10s %-9s %-9s %-9s %-9s %-9s %-9s\n", "Sid", "Sname", "Class", "Maths",
					"Science", "English", "SS", "Malayalam");
			System.out.println("---------------------------------------------------------------------------------");
		}

		vSidRes = vSid.executeQuery();
		while (vSidRes.next()) {
			System.out.printf("%-5d %-10s %-9d %-9d %-9d %-9d %-9d %-9d\n", vSidRes.getInt("Sid"),
					vSidRes.getString("Sname"), vSidRes.getInt("Class"), vSidRes.getInt("Maths"), vSidRes.getInt("Sci"),
					vSidRes.getInt("Eng"), vSidRes.getInt("SS"), vSidRes.getInt("Mal"));
		}

		if (!hasRows) {
			System.out.println("\nStudent id " + sId + " not found");
		}

	}

	public void RemoveStu(Connection c, int classIn, Scanner sc) throws SQLException {

		System.out.print("\nEnter student ID: ");
		int Sid = sc.nextInt();

		PreparedStatement getStu = c.prepareStatement("SELECT Sname, Class FROM student WHERE Sid = ?");
		getStu.setInt(1, Sid);
		ResultSet stuRes = getStu.executeQuery();

		if (stuRes.next()) {
			String delName = stuRes.getString("Sname");
			int delClass = stuRes.getInt("Class");

			if (delClass == classIn) {
				System.out.println("\nDeleting student: " + delName + ", Class: " + delClass);
				System.out.print("\nAre you sure you want to delete this student? (y/n): ");
				char confirm = sc.next().charAt(0);

				if (confirm == 'y') {
					PreparedStatement delStu = c.prepareStatement("DELETE FROM student WHERE Sid = ?");
					PreparedStatement delMark = c.prepareStatement("DELETE FROM marklist WHERE Sid = ?");
					delStu.setInt(1, Sid);
					delMark.setInt(1, Sid);

					int delSRes = delStu.executeUpdate();
					int delMRes = delMark.executeUpdate();

					System.out.println(
							"\nDeleted " + delSRes + " row(s) from student and " + delMRes + " row(s) from marklist.");
				} else {
					System.out.println("\nDeletion canceled.");
				}
			} else {
				System.out.println(
						"\nError: Student " + Sid + " is not from your class. You cannot delete this student.");
			}
		} else {
			System.out.println("\nNo student found with Sid = " + Sid);
		}
	}

	public void ModifyStu(Connection c, int classIn, Scanner sc) throws SQLException {

		System.out.print("\nEnter student id: ");
		int Sid = sc.nextInt();

		PreparedStatement getStu = c.prepareStatement("SELECT Sname, Class FROM student WHERE Sid = ?");
		getStu.setInt(1, Sid);
		ResultSet stuRes = getStu.executeQuery();

		if (stuRes.next()) {
			String modName = stuRes.getString("Sname");
			int modClass = stuRes.getInt("Class");

			if (modClass == classIn) {
				System.out.println("\nModifying student: " + modName + ", Class: " + modClass);
				System.out.println("\n1. PERSONAL DETAILS");
				System.out.println("2. MARKS");
				System.out.print("\nChoose Modification: ");
				int cu = sc.nextInt();

				if (cu == 1) {
					System.out.println("\n=================================");
					System.out.println("*******   MODIFY DETAILS   ******");
					System.out.println("=================================");

					System.out.print("\nEnter NEW NAME  : ");
					String n = sc.next();
					System.out.print("Enter NEW CLASS : ");
					int cl = sc.nextInt();

					System.out.println("\nConfirm the new details:");
					System.out.println("\nNew NAME  : " + n);
					System.out.println("New CLASS : " + cl);
					System.out.print("\nProceed with update? (y/n): ");
					char confirm = sc.next().charAt(0);

					if (confirm == 'y') {
						PreparedStatement mod1 = c.prepareStatement("UPDATE student SET Sname=?, Class=? WHERE Sid=?");
						mod1.setString(1, n);
						mod1.setInt(2, cl);
						mod1.setInt(3, Sid);

						int mod1Res = mod1.executeUpdate();
						if (mod1Res > 0)
							System.out.println("\nSuccessfully updated details of " + modName);
					} else {
						System.out.println("\nUpdate canceled.");
					}

				} else if (cu == 2) {
					System.out.println("\n=================================");
					System.out.println("*******   MODIFY MARKS   ******");
					System.out.println("=================================");

					PreparedStatement isMark = c.prepareStatement("SELECT * FROM marklist WHERE Sid=?");
					isMark.setInt(1, Sid);
					ResultSet isMarkRes = isMark.executeQuery();

					if (isMarkRes.next()) {
						System.out.println("\n Current marks of student " + Sid + "." + modName + " are");
						System.out.printf("\n%-9s %-9s %-9s %-9s %-9s\n", "Maths", "Science", "English", "SS",
								"Malayalam");
						System.out.println("---------------------------------------------------------");

						isMarkRes = isMark.executeQuery();
						while (isMarkRes.next()) {
							System.out.printf("%-9d %-9d %-9d %-9d %-9d\n", isMarkRes.getInt("Maths"),
									isMarkRes.getInt("Sci"), isMarkRes.getInt("Eng"), isMarkRes.getInt("SS"),
									isMarkRes.getInt("Mal"));
						}

						String sub[] = { "Maths", "Sci", "Eng", "SS", "Mal" };
						int marks[] = new int[sub.length];
						boolean hasChanges = false;

						StringBuilder query = new StringBuilder("update marklist set ");

						System.out.println("\nEDIT MARKS\n");
						System.out.println("1. All at once");
						System.out.println("2. Individually");
						System.out.print("\nEnter choice : ");
						int ch = sc.nextInt();
						System.out.println();

						if (ch == 1) {
							for (int i = 0; i < sub.length; i++) {
								System.out.print(sub[i] + " : ");
								marks[i] = sc.nextInt();

								if (i > 0)
									query.append(",");
								query.append(sub[i]).append("=?");
							}
							hasChanges = true;
						} else if (ch == 2) {
							System.out.println("\nEnter -1 to denote no change for the subject\n");
							for (int i = 0; i < sub.length; i++) {
								System.out.print(sub[i] + " : ");
								marks[i] = sc.nextInt();

								if (marks[i] != -1) {
									if (hasChanges)
										query.append(",");

									query.append(sub[i]).append("=?");
									hasChanges = true;
								}
							}
						}

						if (hasChanges) {
							query.append(" where Sid=?");
							System.out.println(query);
							PreparedStatement modMark = c.prepareStatement(query.toString());

							int pi = 1; // parameter index
							for (int i = 0; i < sub.length; i++) {
								if (marks[i] != -1) {
									modMark.setInt(pi, marks[i]);
									pi++;
								}
							}
							modMark.setInt(pi, Sid);

							System.out.print("\nProceed with update? (y/n): ");
							char confirm = sc.next().charAt(0);
							if (confirm == 'y') {
								int modMarksRes = modMark.executeUpdate();
								if (modMarksRes > 0)
									System.out.println("\nSuccessfully updated marks for " + Sid);
							} else {
								System.out.println("\nUpdate canceled.");
							}
						} else {
							System.out.println("\nNo changes made.");
						}

					} else {
						System.out.println("\nNo marks found for student " + Sid + ". Please add marks first.");
					}
				}
			} else {
				System.out.println(
						"\nError: Student " + Sid + " is not from your class. You cannot modify this student.");
			}
		} else {
			System.out.println("\nError: No student found with Sid = " + Sid);
		}

	}

	public void PersonalInfo(Connection c, int Tid, Scanner sc, int classIn) throws SQLException {

		PreparedStatement view = c.prepareStatement("select Tid,Tname,Class_In_Charge from teacher where Tid=?");
		view.setInt(1, Tid);

		ResultSet viewRes = view.executeQuery();
		while (viewRes.next()) {
			System.out.println("\nID                 : " + viewRes.getInt(1));
			System.out.println("NAME               : " + viewRes.getString(2));
			System.out.println("CLASS IN CHARGE OF : " + viewRes.getString(3));
		}
	}

	public boolean Edit(Connection c, int classIn, Scanner sc, boolean logout, int Tid) throws SQLException {

		System.out.print("\nEnter NEW NAME             : ");
		String newname = sc.next();
		System.out.print("Enter New CLASS_IN_CHARGE  : ");
		int newclass = sc.nextInt();
		System.out.print("Enter New PASSWORD         : ");
		String newpass = sc.next();

		System.out.println("\nConfirm the details:");
		System.out.println("\nNAME             : " + newname);
		System.out.println("CLASS_IN_CHARGE  : " + newclass);
		System.out.println("PASSWORD         : " + newpass);
		System.out.print("\nConfirm changes? (y/n): ");
		char confirm = sc.next().charAt(0);

		if (confirm == 'y') {
			try {
				PreparedStatement upTc = c
						.prepareStatement("UPDATE teacher SET Tname=?, Class_In_Charge=?, password=? WHERE Tid=?");
				upTc.setString(1, newname);
				upTc.setInt(2, newclass);
				upTc.setString(3, newpass);

				int upTcRes = upTc.executeUpdate();
				if (upTcRes > 0) {
					System.out.println("\nSuccessfully updated details of " + newname);
					System.out.println(
							"\nPersonal details have been modified. You will be logged out for security reasons.");
					System.out.println("Please log in again with your new credentials.");
					logout = true;
				}
			} catch (SQLIntegrityConstraintViolationException e) {
				System.out.println("\nError: Class " + newclass + " is already assigned to another teacher.");
			}
		} else {
			System.out.println("\nUpdate operation canceled.");
		}
		return logout;
	}
}
