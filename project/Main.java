import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame implements ActionListener {

    JButton registerBtn, loginBtn, exitBtn;
    JButton teacherEnterMarksBtn, teacherViewMarksBtn, teacherViewRecheckBtn, teacherViewPasswordsBtn, teacherTotalAccountsBtn, teacherDeleteStudentBtn, teacherBackBtn;
    JButton studentViewResultBtn, studentRequestRecheckBtn, studentUpdateYearSemBtn, studentBackBtn;
    JTextArea output;
    JPanel mainPanel, teacherPanel, studentPanel;
    CardLayout cardLayout;
    JPanel cardPanel;

    // Current user info
    String currentUid, currentName, currentRole, currentYear, currentSemester;

    // ================= ONLY EDIT HERE IF NEEDED =================
    String DB_URL = "jdbc:mysql://localhost:3306/examdb";
    String DB_USER = "root";
    String DB_PASS = "Tushar@2005";
    // ============================================================

    Connection getCon() throws Exception {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    String[] subjects = {
        "DSA","OS","DBMS","COA","CN","SE","TOC","DM","OOP","AI-ML"
    };

    Main() {
        setTitle("Exam Result Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        createMainPanel();
        createTeacherPanel();
        createStudentPanel();

        cardPanel.add(mainPanel, "MAIN");
        cardPanel.add(teacherPanel, "TEACHER");
        cardPanel.add(studentPanel, "STUDENT");

        output = new JTextArea();
        output.setFont(new Font("Monospaced", Font.PLAIN, 12));
        output.setEditable(false);
        output.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output"));

        // Layout
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 240, 240));

        registerBtn = new JButton("Register");
        loginBtn = new JButton("Login");
        exitBtn = new JButton("Exit");

        // Style buttons
        registerBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exitBtn.setFont(new Font("Arial", Font.BOLD, 14));

        registerBtn.setBackground(new Color(100, 150, 255));
        registerBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(new Color(100, 200, 100));
        loginBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(new Color(255, 100, 100));
        exitBtn.setForeground(Color.WHITE);

        // Add tooltips
        registerBtn.setToolTipText("Register a new user");
        loginBtn.setToolTipText("Login to your account");
        exitBtn.setToolTipText("Exit the application");

        // Add hover effects
        addHoverEffect(registerBtn, new Color(100, 150, 255), new Color(130, 180, 255));
        addHoverEffect(loginBtn, new Color(100, 200, 100), new Color(130, 230, 130));
        addHoverEffect(exitBtn, new Color(255, 100, 100), new Color(255, 130, 130));

        mainPanel.add(registerBtn);
        mainPanel.add(loginBtn);
        mainPanel.add(exitBtn);

        // Add listeners
        registerBtn.addActionListener(this);
        loginBtn.addActionListener(this);
        exitBtn.addActionListener(this);
    }

    private void createTeacherPanel() {
        teacherPanel = new JPanel();
        teacherPanel.setLayout(new GridLayout(6, 1, 10, 10));
        teacherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        teacherPanel.setBackground(new Color(240, 240, 240));

        teacherEnterMarksBtn = new JButton("Enter Marks");
        teacherViewMarksBtn = new JButton("View Marks");
        teacherViewRecheckBtn = new JButton("View Recheck Requests");
        teacherViewPasswordsBtn = new JButton("View Student Passwords");
        teacherTotalAccountsBtn = new JButton("Total Accounts");
        teacherDeleteStudentBtn = new JButton("Delete Student Account");
        teacherBackBtn = new JButton("Back");

        JButton[] teacherBtns = {teacherEnterMarksBtn, teacherViewMarksBtn, teacherViewRecheckBtn, teacherViewPasswordsBtn, teacherTotalAccountsBtn, teacherDeleteStudentBtn, teacherBackBtn};
        Color[] colors = {new Color(100, 150, 255), new Color(100, 200, 100), new Color(150, 150, 100), new Color(100, 180, 220), new Color(150, 100, 150), new Color(255, 150, 100), new Color(200, 200, 200)};
        String[] tooltips = {"Enter marks for a student", "View marks for a student", "View recheck requests", "View all student passwords", "View total accounts", "Delete a student account", "Go back to main menu"};

        for(int i = 0; i < teacherBtns.length; i++) {
            teacherBtns[i].setFont(new Font("Arial", Font.BOLD, 14));
            teacherBtns[i].setBackground(colors[i]);
            teacherBtns[i].setForeground(Color.WHITE);
            teacherBtns[i].setToolTipText(tooltips[i]);
            addHoverEffect(teacherBtns[i], colors[i], new Color(
                Math.min(colors[i].getRed() + 30, 255),
                Math.min(colors[i].getGreen() + 30, 255),
                Math.min(colors[i].getBlue() + 30, 255)
            ));
            teacherPanel.add(teacherBtns[i]);
            teacherBtns[i].addActionListener(this);
        }
    }

    private void createStudentPanel() {
        studentPanel = new JPanel();
        studentPanel.setLayout(new GridLayout(4, 1, 10, 10));
        studentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        studentPanel.setBackground(new Color(240, 240, 240));

        studentViewResultBtn = new JButton("View Result");
        studentRequestRecheckBtn = new JButton("Request Recheck");
        studentUpdateYearSemBtn = new JButton("Update Year/Semester");
        studentBackBtn = new JButton("Back");

        JButton[] studentBtns = {studentViewResultBtn, studentRequestRecheckBtn, studentUpdateYearSemBtn, studentBackBtn};
        Color[] colors = {new Color(100, 150, 255), new Color(100, 200, 100), new Color(150, 150, 100), new Color(200, 200, 200)};
        String[] tooltips = {"View your result", "Request recheck", "Update year/semester", "Go back to main menu"};

        for(int i = 0; i < studentBtns.length; i++) {
            studentBtns[i].setFont(new Font("Arial", Font.BOLD, 14));
            studentBtns[i].setBackground(colors[i]);
            studentBtns[i].setForeground(Color.WHITE);
            studentBtns[i].setToolTipText(tooltips[i]);
            addHoverEffect(studentBtns[i], colors[i], new Color(
                Math.min(colors[i].getRed() + 30, 255),
                Math.min(colors[i].getGreen() + 30, 255),
                Math.min(colors[i].getBlue() + 30, 255)
            ));
            studentPanel.add(studentBtns[i]);
            studentBtns[i].addActionListener(this);
        }
    }

    private void addHoverEffect(JButton button, Color originalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    // UID logic
    String getRole(String uid) {
        int last = Integer.parseInt(uid.substring(6));
        if(last >= 1 && last <= 100) return "TEACHER";
        else return "STUDENT";
    }

    String getGrade(int marks) {
        if(marks >= 90) return "A+";
        if(marks >= 80) return "A";
        if(marks >= 70) return "B+";
        if(marks >= 60) return "B";
        if(marks >= 50) return "C+";
        if(marks >= 40) return "C";
        return "F";
    }

    static class SubjectMark {
        String subject;
        int marks;

        SubjectMark(String subject, int marks) {
            this.subject = subject;
            this.marks = marks;
        }
    }

    String buildStudentResult(String name, String uid, String year, String semester, List<SubjectMark> marks) {
        StringBuilder data = new StringBuilder();
        data.append("========================================\n");
        data.append("        EXAM RESULT MANAGEMENT SYSTEM\n");
        data.append("========================================\n\n");
        data.append(String.format("Name      : %s\n", name));
        data.append(String.format("UID       : %s\n", uid));
        if(year != null) data.append(String.format("Year      : %s\n", year));
        if(semester != null) data.append(String.format("Semester  : %s\n", semester));
        data.append("\n");
        data.append("----------------------------------------\n");
        data.append(String.format("%-7s %-12s %-7s %s\n", "Sl No", "Subject", "Marks", "Grade"));
        data.append("----------------------------------------\n");

        int total = 0;
        int count = 0;
        boolean isPass = true;
        int serial = 0;

        // Create a map for quick lookup
        java.util.Map<String, Integer> markMap = new java.util.HashMap<>();
        for(SubjectMark sm : marks) {
            markMap.put(sm.subject, sm.marks);
        }

        for(String subject : subjects) {
            int mark = markMap.getOrDefault(subject, 0);
            serial++;
            data.append(String.format("%-7d %-12s %-7d %s\n", serial, subject, mark, getGrade(mark)));
            total += mark;
            count++;
            if(mark < 40) isPass = false;
        }

        data.append("\n----------------------------------------\n");
        data.append(String.format("Total Marks : %d / %d\n", total, count * 100));
        double avg = count == 0 ? 0 : total / (double) count;
        data.append(String.format("Average     : %.2f %%\n", avg));
        data.append("Result      : " + (isPass && avg >= 50 ? "PASS" : "FAIL") + "\n");
        data.append("----------------------------------------");
        return data.toString();
    }

    public void actionPerformed(ActionEvent e) {

        try {

            // ================= REGISTER =================
            if(e.getSource() == registerBtn) {

                String name = JOptionPane.showInputDialog("Enter Name:");
                if(name == null) return;

                String uid = JOptionPane.showInputDialog("Enter UID (9 digits):");
                if(uid == null) return;

                if(!uid.matches("\\d{9}")) {
                    output.setText("UID must be exactly 9 digits");
                    return;
                }

                JPasswordField pf = new JPasswordField();
                int ok = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
                if(ok != JOptionPane.OK_OPTION) return;
                String pass = new String(pf.getPassword());

                String role = getRole(uid);

                String year = null;
                String semester = null;
                if(role.equals("STUDENT")) {
                    year = JOptionPane.showInputDialog("Enter Year (1-4):");
                    if(year == null) return;
                    try {
                        int y = Integer.parseInt(year);
                        if(y < 1 || y > 4) {
                            output.setText("Year must be between 1 and 4");
                            return;
                        }
                    } catch(NumberFormatException ex) {
                        output.setText("Year must be a number between 1 and 4");
                        return;
                    }
                    semester = JOptionPane.showInputDialog("Enter Semester (1-8):");
                    if(semester == null) return;
                    try {
                        int s = Integer.parseInt(semester);
                        if(s < 1 || s > 8) {
                            output.setText("Semester must be between 1 and 8");
                            return;
                        }
                    } catch(NumberFormatException ex) {
                        output.setText("Semester must be a number between 1 and 8");
                        return;
                    }
                }

                Connection con = getCon();

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(uid,name,password,role,year,semester) VALUES(?,?,?,?,?,?)"
                );

                ps.setString(1, uid);
                ps.setString(2, name);
                ps.setString(3, pass);
                ps.setString(4, role);
                if(role.equals("STUDENT")) {
                    ps.setString(5, year);
                    ps.setString(6, semester);
                } else {
                    ps.setNull(5, java.sql.Types.VARCHAR);
                    ps.setNull(6, java.sql.Types.VARCHAR);
                }

                ps.executeUpdate();
                output.setText("Registered successfully as " + role);

                con.close();
            }

            // ================= LOGIN =================
            else if(e.getSource() == loginBtn) {

                String uid = JOptionPane.showInputDialog("Enter UID:");
                JPasswordField pf = new JPasswordField();
                int ok = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION);
                if(ok != JOptionPane.OK_OPTION) return;
                String pass = new String(pf.getPassword());

                Connection con = getCon();

                PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE uid=? AND password=?"
                );

                ps.setString(1, uid);
                ps.setString(2, pass);

                ResultSet rs = ps.executeQuery();

                if(!rs.next()) {
                    output.setText("Invalid UID or Password");
                    con.close();
                    return;
                }

                currentUid = uid;
                currentRole = rs.getString("role");
                currentName = rs.getString("name");
                currentYear = rs.getString("year");
                currentSemester = rs.getString("semester");

                if(currentRole.equals("TEACHER")) {
                    cardLayout.show(cardPanel, "TEACHER");
                } else {
                    cardLayout.show(cardPanel, "STUDENT");
                }

                output.setText("Logged in successfully as " + currentRole);

                con.close();
            }

            // ================= EXIT =================
            else if(e.getSource() == exitBtn) {
                System.exit(0);
            }

            // ================= TEACHER BUTTONS =================
            else if(e.getSource() == teacherEnterMarksBtn) {
                String stu = JOptionPane.showInputDialog("Enter Student UID:");
                if(stu == null || stu.trim().isEmpty()) return;

                Connection con = getCon();

                PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE uid=? AND role='STUDENT'"
                );
                check.setString(1, stu);
                ResultSet cr = check.executeQuery();

                if(!cr.next()) {
                    output.setText("Student not registered");
                    con.close();
                    return;
                }

                while(true) {
                    String subject = (String) JOptionPane.showInputDialog(
                            null,"Select Subject","Subjects",
                            JOptionPane.QUESTION_MESSAGE,null,
                            subjects,subjects[0]);
                    if(subject == null) break;

                    String marksInput = JOptionPane.showInputDialog("Enter Marks:");
                    if(marksInput == null || marksInput.trim().isEmpty()) break;

                    int marks;
                    try {
                        marks = Integer.parseInt(marksInput);
                    } catch(NumberFormatException ex) {
                        output.setText("Please enter a valid numeric mark");
                        continue;
                    }

                    if(marks < 0 || marks > 100) {
                        output.setText("Marks must be 0–100");
                        continue;
                    }

                    PreparedStatement checkMark = con.prepareStatement(
                        "SELECT * FROM marks WHERE uid=? AND subject=?"
                    );
                    checkMark.setString(1, stu);
                    checkMark.setString(2, subject);

                    ResultSet markRs = checkMark.executeQuery();

                    if(markRs.next()) {
                        PreparedStatement update = con.prepareStatement(
                            "UPDATE marks SET marks=? WHERE uid=? AND subject=?"
                        );
                        update.setInt(1, marks);
                        update.setString(2, stu);
                        update.setString(3, subject);
                        update.executeUpdate();
                        output.setText("Marks updated for UID " + stu + " subject " + subject);
                    } else {
                        PreparedStatement insert = con.prepareStatement(
                            "INSERT INTO marks(uid,subject,marks) VALUES(?,?,?)"
                        );
                        insert.setString(1, stu);
                        insert.setString(2, subject);
                        insert.setInt(3, marks);
                        insert.executeUpdate();
                        output.setText("Marks inserted successfully for UID " + stu + " subject " + subject);
                    }

                    int again = JOptionPane.showConfirmDialog(this,
                            "Enter marks for another subject for student " + stu + "?",
                            "Continue?", JOptionPane.YES_NO_OPTION);
                    if(again != JOptionPane.YES_OPTION) break;
                }

                con.close();
            }

            else if(e.getSource() == teacherViewMarksBtn) {
                String stu = JOptionPane.showInputDialog("Enter Student UID:");
                if(stu == null || stu.trim().isEmpty()) return;

                Connection con = getCon();

                PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE uid=? AND role='STUDENT'"
                );
                check.setString(1, stu);
                ResultSet cr = check.executeQuery();

                if(!cr.next()) {
                    output.setText("Student not found");
                    con.close();
                    return;
                }

                String studentName = cr.getString("name");
                String studentYear = cr.getString("year");
                String studentSemester = cr.getString("semester");

                PreparedStatement psMarks = con.prepareStatement(
                    "SELECT subject, marks FROM marks WHERE uid=? ORDER BY subject"
                );
                psMarks.setString(1, stu);
                ResultSet r = psMarks.executeQuery();

                List<SubjectMark> markList = new ArrayList<>();
                while(r.next()) {
                    markList.add(new SubjectMark(r.getString(1), r.getInt(2)));
                }

                output.setText(buildStudentResult(studentName, stu, studentYear, studentSemester, markList));

                con.close();
            }

            else if(e.getSource() == teacherViewRecheckBtn) {
                Connection con = getCon();
                ResultSet r = con.createStatement().executeQuery("SELECT * FROM recheck_requests");

                StringBuilder data = new StringBuilder();
                data.append("UID         Message\n");
                data.append("-----------------------------------------\n");

                boolean hasRequests = false;
                while(r.next()) {
                    hasRequests = true;
                    String uid = r.getString(1);
                    String message = r.getString(2);
                    data.append(String.format("%-11s %s\n", uid, message));
                }

                if(!hasRequests) {
                    data.append("No recheck requests found.\n");
                }

                output.setText(data.toString());
                con.close();
            }

            else if(e.getSource() == teacherViewPasswordsBtn) {
                Connection con = getCon();
                ResultSet r = con.createStatement().executeQuery("SELECT uid, name, password FROM users WHERE role='STUDENT' ORDER BY uid");

                StringBuilder data = new StringBuilder();
                data.append("UID         Name                 Password\n");
                data.append("-----------------------------------------------------\n");

                boolean found = false;
                while(r.next()) {
                    found = true;
                    String uid = r.getString("uid");
                    String name = r.getString("name");
                    String password = r.getString("password");
                    data.append(String.format("%-11s %-20s %s\n", uid, name, password));
                }

                if(!found) {
                    data.append("No student accounts found.\n");
                }

                output.setText(data.toString());
                con.close();
            }

            else if(e.getSource() == teacherTotalAccountsBtn) {
                Connection con = getCon();
                ResultSet countRs = con.createStatement().executeQuery("SELECT COUNT(*) FROM users");
                if(countRs.next()) {
                    output.setText("Total accounts created: " + countRs.getInt(1));
                } else {
                    output.setText("Total accounts created: 0");
                }
                con.close();
            }

            else if(e.getSource() == teacherDeleteStudentBtn) {
                String stu = JOptionPane.showInputDialog("Enter Student UID to delete:");
                if(stu == null || stu.trim().isEmpty()) return;

                Connection con = getCon();

                PreparedStatement check = con.prepareStatement(
                    "SELECT * FROM users WHERE uid=? AND role='STUDENT'"
                );
                check.setString(1, stu);
                ResultSet cr = check.executeQuery();

                if(!cr.next()) {
                    output.setText("Student not found or not a student");
                    con.close();
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student " + stu + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if(confirm != JOptionPane.YES_OPTION) {
                    con.close();
                    return;
                }

                PreparedStatement delMarks = con.prepareStatement("DELETE FROM marks WHERE uid=?");
                delMarks.setString(1, stu);
                delMarks.executeUpdate();

                PreparedStatement delRecheck = con.prepareStatement("DELETE FROM recheck_requests WHERE uid=?");
                delRecheck.setString(1, stu);
                delRecheck.executeUpdate();

                PreparedStatement delUser = con.prepareStatement("DELETE FROM users WHERE uid=? AND role='STUDENT'");
                delUser.setString(1, stu);
                delUser.executeUpdate();

                output.setText("Student account deleted successfully");
                con.close();
            }

            else if(e.getSource() == teacherBackBtn) {
                cardLayout.show(cardPanel, "MAIN");
                output.setText("Logged out successfully");
            }

            // ================= STUDENT BUTTONS =================
            else if(e.getSource() == studentViewResultBtn) {
                Connection con = getCon();
                PreparedStatement ps2 = con.prepareStatement(
                    "SELECT subject, marks FROM marks WHERE uid=? ORDER BY subject"
                );
                ps2.setString(1, currentUid);
                ResultSet r = ps2.executeQuery();

                List<SubjectMark> markList = new ArrayList<>();
                while(r.next()) {
                    markList.add(new SubjectMark(r.getString(1), r.getInt(2)));
                }

                if(markList.isEmpty()) {
                    output.setText("No marks available yet");
                } else {
                    output.setText(buildStudentResult(currentName, currentUid, currentYear, currentSemester, markList));
                }
                con.close();
            }

            else if(e.getSource() == studentRequestRecheckBtn) {
                String msg = JOptionPane.showInputDialog("Enter your request:");
                if(msg == null || msg.trim().isEmpty()) return;

                Connection con = getCon();
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO recheck_requests(uid,message) VALUES(?,?)"
                );
                ps2.setString(1, currentUid);
                ps2.setString(2, msg);
                ps2.executeUpdate();
                output.setText("Recheck request sent to teacher");
                con.close();
            }

            else if(e.getSource() == studentUpdateYearSemBtn) {
                String newYear = JOptionPane.showInputDialog("Enter new Year (1-4):");
                if(newYear == null) return;
                try {
                    int y = Integer.parseInt(newYear);
                    if(y < 1 || y > 4) {
                        output.setText("Year must be between 1 and 4");
                        return;
                    }
                } catch(NumberFormatException ex) {
                    output.setText("Year must be a number between 1 and 4");
                    return;
                }
                String newSemester = JOptionPane.showInputDialog("Enter new Semester (1-8):");
                if(newSemester == null) return;
                try {
                    int s = Integer.parseInt(newSemester);
                    if(s < 1 || s > 8) {
                        output.setText("Semester must be between 1 and 8");
                        return;
                    }
                } catch(NumberFormatException ex) {
                    output.setText("Semester must be a number between 1 and 8");
                    return;
                }

                Connection con = getCon();
                PreparedStatement ps2 = con.prepareStatement("UPDATE users SET year=?, semester=? WHERE uid=?");
                ps2.setString(1, newYear);
                ps2.setString(2, newSemester);
                ps2.setString(3, currentUid);
                ps2.executeUpdate();
                output.setText("Year and Semester updated successfully");
                currentYear = newYear;
                currentSemester = newSemester;
                con.close();
            }


            else if(e.getSource() == studentBackBtn) {
                cardLayout.show(cardPanel, "MAIN");
                output.setText("Logged out successfully");
            }

        } catch(Exception ex) {
            output.setText("ERROR: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}