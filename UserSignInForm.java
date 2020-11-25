package ca.java.library.user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ca.java.library.user.connection.DbConstants;

import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class UserSignInForm {

	private JFrame frame;
	private JTextField userEmailField;
	private JPasswordField userPasswordField;
	
	//private static final String ALL_USERS_QUERY = "SELECT * FROM USERS";
	private static final String ALL_BOOKS_QUERY = "SELECT * FROM BOOKS";
	
	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		getConnection();
		System.out.println("get conn works");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserSignInForm window = new UserSignInForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserSignInForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setForeground(Color.DARK_GRAY);
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 283, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		userEmailField = new JTextField();
		userEmailField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = userEmailField.getText();
				System.out.println(email);
			}
		});
		
		userEmailField.setBounds(49, 85, 179, 45);
		frame.getContentPane().add(userEmailField);
		userEmailField.setColumns(10);
		
		userPasswordField = new JPasswordField();
		/*
		userPasswordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userPassword = String.valueOf(userPasswordField.getPassword());
				System.out.println(userPassword);
			}
		});
		*/
		userPasswordField.setBounds(49, 152, 179, 45);
		frame.getContentPane().add(userPasswordField);
		
		JButton LogInButton = new JButton("Log in");
		LogInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = userEmailField.getText();
				String userPassword = String.valueOf(userPasswordField.getPassword());
				Connection con = null;
				Statement stmt = null;
				ResultSet rs = null;
				
				if(email.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter email");
				} else if (userPassword.equals("")) {
					JOptionPane.showMessageDialog(null, "Enter password");
				} else {
						try {
							con = getConnection();
							stmt = con.createStatement();
							stmt.executeQuery("USE LIBRARY_MGMT");
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String st = ("SELECT * FROM USERS2 WHERE EMAIL='"+email+"' AND PASSWORD='"+userPassword+"'");
						try {
							rs = stmt.executeQuery(st);
							if (rs.next() == false) {
								System.out.println("No user");
								JOptionPane.showMessageDialog(null, "Wrong username or password, try again");
							} else {
								//frame.getContentPane().removeAll();
								frame.dispose();
								//frame.getContentPane().setVisible(false);
								printBooks(con, ALL_BOOKS_QUERY, frame);
							} 
						} catch (SQLException e1) {
							
							// TODO Auto-generated catch block
							e1.printStackTrace();
							
						
						}
				} 
			}	
			});
		
		LogInButton.setBounds(80, 237, 117, 51);
		frame.getContentPane().add(LogInButton);
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.
				getConnection(DbConstants.CONN_STRING, DbConstants.USER, DbConstants.PASSWORD);
		return conn;
	}
	
	public static ResultSet getData(Connection conn, String query) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public static void printBooks(Connection conn, String query, JFrame frame) throws SQLException {
		System.out.println("You are connected");
		Statement stmt = conn.createStatement();
		ResultSet rsBooks = stmt.executeQuery(query);
		DefaultListModel<String> lm = new DefaultListModel<>();
		while (rsBooks.next()) {
			lm.addElement(rsBooks.getInt("book_id") + " " + rsBooks.getString("title") + " "
					      + rsBooks.getString("author") + " " + (rsBooks.getBoolean("issued") ? "yes" : "no"));
		}
		JList<String> list = new JList<>(lm);
		list.setBounds(100, 100, 100, 100);
		frame.getContentPane().add(list);
		frame.getContentPane().setVisible(true);
	}
}
