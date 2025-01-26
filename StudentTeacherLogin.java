package week1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class StudentTeacherLogin {
	static Scanner sc = new Scanner(System.in);

	static void signUp(Connection c) throws SQLException {
		int Tclass = 0;
		System.out.println("\n=====================");
		System.out.println("****** SIGN UP ******");
		System.out.println("=====================");

		try {
			PreparedStatement signUp = c
					.prepareStatement("INSERT INTO teacher(Tname, Class_In_Charge, password) VALUES (?, ?, ?)");

			System.out.println("\nEnter ");
			System.out.print("NAME            : ");
			String Tname = sc.next();
			System.out.print("CLASS IN CHARGE : ");
			Tclass = sc.nextInt();
			System.out.print("PASSWORD        : ");
			String Tpass = sc.next();

			System.out.println("\nConfirm the details:");
			System.out.println("\nNAME            : " + Tname);
			System.out.println("CLASS IN CHARGE : " + Tclass);
			System.out.println("PASSWORD        : " + Tpass);
			System.out.print("\nProceed with registration? (y/n): ");
			char confirm = sc.next().charAt(0);

			if (confirm == 'y') {
				signUp.setString(1, Tname);
				signUp.setInt(2, Tclass);
				signUp.setString(3, Tpass);
				int SignUpRes = signUp.executeUpdate();

				if (SignUpRes > 0)
					System.out.println("\nTeacher Account Created Successfully");
			} else {
				System.out.println("\nRegistration canceled.");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println("\nClass In_charge for class " + Tclass + " already exists");
		}
	}

	static void login(Connection c) throws SQLException {
		boolean loggedIn = false;

		while (!loggedIn) {
			System.out.println("\n===================");
			System.out.println("****** LOGIN ******");
			System.out.println("===================");

			System.out.println();
			System.out.print("NAME     : ");
			String name = sc.next();
			System.out.print("PASSWORD : ");
			String pass = sc.next();

			PreparedStatement Slogin = c.prepareStatement("select * from student where sname=? and password=?");
			Slogin.setString(1, name);
			Slogin.setString(2, pass);

			PreparedStatement Tlogin = c.prepareStatement("select * from teacher where tname=? and password=?");
			Tlogin.setString(1, name);
			Tlogin.setString(2, pass);

			ResultSet Sres = Slogin.executeQuery();
			ResultSet Tres = Tlogin.executeQuery();

			if (Sres.next()) {
				loggedIn = true;
				boolean exit = false;
				int SidLogin = Sres.getInt("Sid");
				int ch;

				while (!exit) {
					do {
						System.out.println("\n======================================");
						System.out.println("       Welcome student, " + name);
						System.out.println("======================================");
						System.out.println("\n1. PERSONAL INFO");
						System.out.println("2. VIEW MARK");
						System.out.println("3. CHANGE PASSWORD");
						System.out.println("4. LOGOUT");

						System.out.print("\nEnter choice : ");
						ch = sc.nextInt();

						switch (ch) {
						case 1: {
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
							break;
						}
						case 2: {
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
							break;
						}
						case 3: {
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
								} else
									System.out.println("\nError: Unable to change password");
							} else {
								System.out.println("\nPassword update canceled.");
							}
							break;
						}

						case 4:
							System.out.println("\nLogging out of " + name + "....");
							loggedIn = false;
							exit = true;
							break;
						default:
							System.out.println("Invalid input");
						}
					} while (ch != 4);
				}
			} else if (Tres.next()) {
				loggedIn = true;
				boolean exit = false;
				int ch;

				while (!exit) {
					do {
						System.out.println("\n======================================");
						System.out.println("       Welcome teacher, " + name);
						System.out.println("======================================");
						System.out.println("\n1. ADD STUDENT");
						System.out.println("2. ADD MARKS");
						System.out.println("3. VIEW CLASS");
						System.out.println("4. VIEW STUDENT");
						System.out.println("5. DELETE STUDENT");
						System.out.println("6. MODIFY STUDENT");
						System.out.println("7. PERSONAL INFO");
						System.out.println("8. EDIT PERSONAL INFO");
						System.out.println("9. LOGOUT");

						PreparedStatement s = c
								.prepareStatement("select Class_In_Charge,Tid from teacher where tname=?");
						s.setString(1, name);
						ResultSet getClass = s.executeQuery();

						getClass.next();
						int classIn = getClass.getInt(1);
						int Tid = getClass.getInt(2);

						System.out.print("\nEnter choice : ");
						ch = sc.nextInt();
						switch (ch) {
						case 1: {
							char yn;
							do {
								System.out.println("\n==========================");
								System.out.println("******* ADD STUDENT ******");
								System.out.println("==========================");

								System.out.println("\nEnter student's");
								System.out.print("NAME     : ");
								String Sname = sc.next();
								System.out.print("PASSWORD : ");
								String password = sc.next();
								System.out.println();

								System.out.println("Confirm the details:");
								System.out.println("\nNAME     : " + Sname);
								System.out.println("CLASS    : " + classIn);
								System.out.println("PASSWORD : " + password);
								System.out.print("\nProceed with addition? (y/n): ");
								char confirm = sc.next().charAt(0);

								if (confirm == 'y') {
									PreparedStatement add = c.prepareStatement(
											"INSERT INTO student(Sname, class, password) VALUES (?, ?, ?)");
									add.setString(1, Sname);
									add.setInt(2, classIn);
									add.setString(3, password);
									int addRes = add.executeUpdate();

									PreparedStatement getId = c
											.prepareStatement("SELECT Sid FROM student WHERE Sname=?");
									getId.setString(1, Sname);
									ResultSet getIdRes = getId.executeQuery();
									getIdRes.next();
									int id = getIdRes.getInt(1);

									if (addRes > 0)
										System.out.println("\nSuccessfully added student, " + Sname + " to class "
												+ classIn + " and his/her id is " + id);
								} else {
									System.out.println("\nStudent addition canceled.");
								}
								System.out.print("\nDo you want to add another student? (y/n): ");
								yn = sc.next().charAt(0);
							} while (yn == 'y');
							break;
						}

						case 2: {
							char yn;
							do {
								boolean validStudent = false;

								while (!validStudent) {
									System.out.println("\n========================");
									System.out.println("******* ADD MARKS ******");
									System.out.println("========================");

									System.out.print("\nEnter ID of student for entering marks: ");
									int id = sc.nextInt();
									PreparedStatement s2 = c
											.prepareStatement("SELECT Sname, class FROM student WHERE Sid=?");
									s2.setInt(1, id);

									ResultSet getStuName = s2.executeQuery();
									if (getStuName.next()) {
										String StuName = getStuName.getString(1);
										int Sclass = getStuName.getInt(2);

										if (Sclass == classIn) {
											PreparedStatement isMark = c
													.prepareStatement("SELECT * FROM marklist WHERE Sid=?");
											isMark.setInt(1, id);
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
													PreparedStatement addMark = c.prepareStatement(
															"INSERT INTO marklist VALUES (?, ?, ?, ?, ?, ?)");
													addMark.setInt(1, id);
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
												System.out.println("\nMarks for student " + id + " " + StuName
														+ " already found. For updation go to option 6.");
											}

											validStudent = true;
										} else {
											System.out.println("\nError: Student " + id + " " + StuName
													+ " not from your class. Try again.");
										}
									} else {
										System.out.println("\nError: No student found with ID " + id + ". Try again.");
									}
								}

								System.out.print("\nDo you want to add marks for another student? (y/n): ");
								yn = sc.next().charAt(0);
							} while (yn == 'y');
							break;
						}

						case 3: {
							System.out.println("\n======================");
							System.out.println("******* CLASS " + classIn + " ******");
							System.out.println("======================");

							boolean hasRows = false;
							PreparedStatement viewCls = c.prepareStatement(
									"SELECT s.Sid, s.Sname, m.Maths, m.Sci, m.Eng, m.SS, m.Mal FROM student s JOIN markList m ON s.Sid = m.Sid WHERE s.class = (SELECT Class_In_Charge FROM teacher WHERE Class_In_Charge = ?)");
							viewCls.setInt(1, classIn);

							ResultSet viClsRes = viewCls.executeQuery();
							if (viClsRes.next()) {
								hasRows = true;
								System.out.printf("\n%-5s %-10s %-9s %-9s %-9s %-9s %-9s\n", "Sid", "Sname", "Maths",
										"Science", "English", "SS", "Malayalam");
								System.out.println(
										"----------------------------------------------------------------------------");
							}

							viClsRes = viewCls.executeQuery();
							while (viClsRes.next()) {
								System.out.printf("%-5d %-10s %-9d %-9d %-9d %-9d %-9d\n", viClsRes.getInt("Sid"),
										viClsRes.getString("Sname"), viClsRes.getInt("Maths"), viClsRes.getInt("Sci"),
										viClsRes.getInt("Eng"), viClsRes.getInt("SS"), viClsRes.getInt("Mal"));
							}

							if (!hasRows)
								System.out.println("\nNo student found in class " + classIn);
							break;
						}
						case 4: {
							char yn;

							do {
								System.out.println("\n=================================");
								System.out.println("******* VIEW STUDENT BY ID ******");
								System.out.println("=================================");

								boolean hasRows = false;
								System.out.print("\nEnter the id of student : ");
								int sId = sc.nextInt();
								PreparedStatement vSid = c.prepareStatement(
										"select s.sid,s.sname,s.class,m.maths,m.sci,m.eng,m.ss,m.mal from student s join marklist m ON s.sid=m.sid where s.sid=?");
								vSid.setInt(1, sId);
								ResultSet vSidRes = vSid.executeQuery();

								if (vSidRes.next()) {
									hasRows = true;
									System.out.printf("\n%-5s %-10s %-9s %-9s %-9s %-9s %-9s %-9s\n", "Sid", "Sname",
											"Class", "Maths", "Science", "English", "SS", "Malayalam");
									System.out.println(
											"---------------------------------------------------------------------------------");
								}

								vSidRes = vSid.executeQuery();
								while (vSidRes.next()) {
									System.out.printf("%-5d %-10s %-9d %-9d %-9d %-9d %-9d %-9d\n",
											vSidRes.getInt("Sid"), vSidRes.getString("Sname"), vSidRes.getInt("Class"),
											vSidRes.getInt("Maths"), vSidRes.getInt("Sci"), vSidRes.getInt("Eng"),
											vSidRes.getInt("SS"), vSidRes.getInt("Mal"));
								}

								if (!hasRows) {
									System.out.println("\nStudent id " + sId + " not found");
								}

								System.out.print("\nDo you want to view another student? (y/n): ");
								yn = sc.next().charAt(0);

							} while (yn == 'y');
							break;
						}

						case 5: {
							char yn;

							do {
								System.out.println("\n=================================");
								System.out.println("*******   REMOVE STUDENT   ******");
								System.out.println("=================================");

								System.out.print("\nEnter student ID: ");
								int Sid = sc.nextInt();

								PreparedStatement getStu = c
										.prepareStatement("SELECT Sname, Class FROM student WHERE Sid = ?");
								getStu.setInt(1, Sid);
								ResultSet stuRes = getStu.executeQuery();

								if (stuRes.next()) {
									String delName = stuRes.getString("Sname");
									int delClass = stuRes.getInt("Class");

									System.out.println("\nDeleting student: " + delName + ", Class: " + delClass);
									System.out.print("\nAre you sure you want to delete this student? (y/n): ");
									char confirm = sc.next().charAt(0);

									if (confirm == 'y') {
										PreparedStatement delStu = c
												.prepareStatement("DELETE FROM student WHERE Sid = ?");
										PreparedStatement delMark = c
												.prepareStatement("DELETE FROM marklist WHERE Sid = ?");
										delStu.setInt(1, Sid);
										delMark.setInt(1, Sid);

										int delSRes = delStu.executeUpdate();
										int delMRes = delMark.executeUpdate();

										System.out.println("\nDeleted " + delSRes + " row(s) from student and "
												+ delMRes + " row(s) from marklist.");
									} else {
										System.out.println("\nDeletion canceled.");
									}
								} else {
									System.out.println("\nNo student found with Sid = " + Sid);
								}

								System.out.print("\nDo you want to delete another student? (y/n): ");
								yn = sc.next().charAt(0);
							} while (yn == 'y');
							break;
						}

						case 6: {
							char yn;

							do {
								boolean validStudent = false;

								while (!validStudent) {
									System.out.println("\n=================================");
									System.out.println("*******   MODIFY STUDENT   ******");
									System.out.println("=================================");

									System.out.print("\nEnter student id: ");
									int Sid = sc.nextInt();

									PreparedStatement getStu = c
											.prepareStatement("SELECT Sname, Class FROM student WHERE Sid = ?");
									getStu.setInt(1, Sid);
									ResultSet stuRes = getStu.executeQuery();

									if (stuRes.next()) {
										String modName = stuRes.getString("Sname");
										int modClass = stuRes.getInt("Class");

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
												PreparedStatement mod1 = c.prepareStatement(
														"UPDATE student SET Sname=?, Class=? WHERE Sid=?");
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

											PreparedStatement isMark = c
													.prepareStatement("SELECT * FROM marklist WHERE Sid=?");
											isMark.setInt(1, Sid);
											ResultSet isMarkRes = isMark.executeQuery();

											if (isMarkRes.next()) {
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

												System.out.println("\nConfirm the new marks:");
												System.out.println("\nMATHS     : " + mat);
												System.out.println("SCIENCE   : " + sci);
												System.out.println("ENGLISH   : " + eng);
												System.out.println("SS        : " + ss);
												System.out.println("MALAYALAM : " + mal);
												System.out.print("\nProceed with update? (y/n): ");
												char confirm = sc.next().charAt(0);

												if (confirm == 'y') {
													PreparedStatement modMarks = c.prepareStatement(
															"UPDATE marklist SET Maths=?, Sci=?, Eng=?, SS=?, Mal=? WHERE Sid=?");
													modMarks.setInt(1, mat);
													modMarks.setInt(2, sci);
													modMarks.setInt(3, eng);
													modMarks.setInt(4, ss);
													modMarks.setInt(5, mal);
													modMarks.setInt(6, Sid);

													int modMarksRes = modMarks.executeUpdate();
													if (modMarksRes > 0)
														System.out
																.println("\nSuccessfully updated marks for " + modName);
												} else {
													System.out.println("\nUpdate canceled.");
												}

											} else {
												System.out.println("\nNo marks found for student " + Sid
														+ ". Please add marks first.");
											}
										}
										validStudent = true;
									} else {
										System.out.println("\nError: No student found with Sid = " + Sid);
									}
								}

								System.out.print("\nDo you want to modify another student? (y/n): ");
								yn = sc.next().charAt(0);

							} while (yn == 'y');
							break;
						}

						case 7: {
							System.out.println("\n============================");
							System.out.println("******* PERSONAL INFO ******");
							System.out.println("============================");

							PreparedStatement view = c
									.prepareStatement("select Tid,Tname,Class_In_Charge from teacher where Tid=?");
							view.setInt(1, Tid);

							ResultSet viewRes = view.executeQuery();
							while (viewRes.next()) {
								System.out.println("\nID                 : " + viewRes.getInt(1));
								System.out.println("NAME               : " + viewRes.getString(2));
								System.out.println("CLASS IN CHARGE OF : " + viewRes.getString(3));
							}
							break;
						}

						case 8: {
							System.out.println("\n=================================");
							System.out.println("******* EDIT PERSONAL INFO ******");
							System.out.println("=================================");

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
									PreparedStatement upTc = c.prepareStatement(
											"UPDATE teacher SET Tname=?, Class_In_Charge=?, password=? WHERE Tid=?");
									upTc.setString(1, newname);
									upTc.setInt(2, newclass);
									upTc.setString(3, newpass);
									upTc.setInt(4, Tid);

									int upTcRes = upTc.executeUpdate();
									if (upTcRes > 0) {
										System.out.println("\nSuccessfully updated details of " + newname);
										System.out.println(
												"\nPersonal details have been modified. You will be logged out for security reasons.");
										System.out.println("Please log in again with your new credentials.");
										exit = true;
									}
								} catch (SQLIntegrityConstraintViolationException e) {
									System.out.println(
											"\nError: Class " + newclass + " is already assigned to another teacher.");
								}
							} else {
								System.out.println("\nUpdate operation canceled.");
							}
							break;
						}

						case 9: {
							System.out.println("\nLogging out of " + name + "....");
							exit = true;
							break;
						}
						default: {
							System.out.println("\nInvalid Input");
						}
						}
					} while (ch != 9);
				}
			} else {
				System.out.println("\nInvalid credentials. Please try again.");
			}
		}
	}

	public static void main(String[] args) throws SQLException {

		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "arman", "arman@07");

		int ch;
		do {
			System.out.println("\n======================================");
			System.out.println("****** SCHOOL MANAGEMENT SYSTEM ******");
			System.out.println("======================================");
			System.out.println("\n1. CREATE ACCOUNT (ONLY TEACHER)");
			System.out.println("2. LOGIN");
			System.out.println("3. EXIT");
			System.out.print("\nEnter choice : ");
			ch = sc.nextInt();

			switch (ch) {
			case 1: {
				signUp(c);
				break;
			}
			case 2: {
				login(c);
				break;
			}
			case 3: {
				System.out.println("\nExiting Application....");
				break;
			}
			default: {
				System.out.println("\nInvalid Input");
			}
			}

		} while (ch != 3);
	}
}