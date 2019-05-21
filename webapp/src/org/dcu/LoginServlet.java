package org.dcu;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

import oracle.jdbc.driver.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 private String JDBCUrl = "jdbc:oracle:thin:@ee417.c7clh2c6565n.eu-west-1.rds.amazonaws.com:1521:EE417";
 private String username = "ee_user";
 private String password = "ee_pass";
 private User doesUserExist = null;
 private String Uname = null;
 private ArrayList<String> Authors = new ArrayList<String>(); 
 private ArrayList<String> Title = new ArrayList<String>();
 private ArrayList<String> Available = new ArrayList<String>(); 
 private ArrayList<String> userid = new ArrayList<String>(); 
 private ArrayList<Integer> bookid = new ArrayList<Integer>(); 
 private ArrayList<String> users = new ArrayList<String>(); 
 private ArrayList<String> date = new ArrayList<String>(); 
 private String table, table2, table3;
 Connection con = null;
 Statement stmt = null;
 ResultSet rs = null;
 
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
     res.setContentType("text/html");
     PrintWriter out = res.getWriter();
     String firstname ="0", lastname ="0", type="-1";
     Uname = req.getParameter("uname");
     String psw = req.getParameter("psw");
     HttpSession session = req.getSession(true);
     if(req.getParameter("signout") != null) {
			System.out.println(req.getParameter("signout"));
			session.invalidate();
			session = req.getSession();
			Authors= null;
			date = null;
			Title = null;
			table=null;
			table2=null;
			table3 = null;
			users =null;
			userid = null;
			Available=null;
		}
    
    
     
 	if(Uname != null  && psw != null) {
     try {
		 Class.forName("oracle.jdbc.driver.OracleDriver");
		 con = DriverManager.getConnection(JDBCUrl, username, password);
		 stmt = con.createStatement();
	     String command ="SELECT * FROM RDLIB5_ACCS WHERE USERNAME = '"+ Uname+"'";
		 rs = stmt.executeQuery(command);
		 while(rs.next()) {
			 type = rs.getString(1);
			 firstname = rs.getString(2);
			 lastname = rs.getString(3);
			 System.out.println(" "+ type + " " + firstname + " " + lastname );
		 }
	 }catch(Exception e){
		 e.printStackTrace();
	 }finally {
		 try {
			 if (rs != null) rs.close();
			 if (stmt != null) stmt.close();
			 if (con != null) con.close();
			 
		 }catch (SQLException e) { e.printStackTrace();}
	 }
     	doesUserExist = new User(firstname, lastname, Uname, psw, type);
		session.setAttribute("theUser", doesUserExist);
		System.out.println("Just created User");
 	
 	}
 	else {
		doesUserExist = (User) session.getAttribute("theUser");
 	}
 	if(validateUser()) {
 		
 		if(doesUserExist.gettype().equals("User")) {
 		if(table == null) {
 			Userbooks();
 			maketable();
 		}
 		out.println("<!DOCTYPE html>\n" + 
 				"<html>\n"+
			 	"<head>\n"+
					"<title> Radwan's DCU Library-login</title>\n"+
					"<Link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n"+
				"</head>\n"+
				"<div class=\"header\">\n"+
					  "<a href=\"home.html\"><img src=\"images/logo2.jpg\"></a>\n"+
				"</div>\n"+
				"<div class=\"topnav\">\n"+
					  "<a href=\"home.html\">Home</a>\n"+
					  "<a href=\"/webapp/login.html\">Login </a>"+
					  "<a href=\"/webapp/Search.html\">Search</a>"+
					  "<a href=\"/webapp/report.html\">Report</a>"+
				"</div>\n"+
 				"<body>\n" + 
	 				"<div class=\"row\">\n"+
					  "<div id =\"tabs-1\" class=\"leftcolumn\">\n"+
						"<div class=\"card\">\n"+
						  "<h2>Welcome " + doesUserExist.getFirstname() + " "+ doesUserExist.getSurname() + ". You are now logged in!</h2>\n"+
						  "<br>\n"+
						  "<table id=\"table\">\n" +
						  "<tr>\n" +
						    "<th>AUTHOR</th>\n"+
						    "<th>TITLE</th>\n" +
						    "<th>DUE DATE</th>\n" +
						  "</tr>\n" +
						    table +
						"</table>\n" +
						  "</div>\n"+
					
					  "</div>\n" +
					"</div>\n" +
				 "</body>\n"+
				"</html>");
     }
 		else if(doesUserExist.gettype().equals("staff")){
 			if(table2 == null) {
 	 			books();
 	 			admintable();
 	 		}
 			out.println("<!DOCTYPE html>\n" + 
 	 				"<html>\n"+
 				 	"<head>\n"+
 						"<title> Radwan's DCU Library-login</title>\n"+
 						"<Link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n"+
 					"</head>\n"+
 					"<div class=\"header\">\n"+
 						  "<a href=\"home.html\"><img src=\"images/logo2.jpg\"></a>\n"+
 					"</div>\n"+
 					"<div class=\"topnav\">\n"+
 						  "<a href=\"home.html\">Home</a>\n"+
 						  "<a href=\"/webapp/login.html\">Login </a>"+
 						  "<a href=\"/webapp/Search.html\">Search</a>"+
 						  "<a href=\"/webapp/report.html\">Report</a>"+
 					"</div>\n"+
 	 				"<body>\n" + 
 		 				"<div class=\"row\">\n"+
 						  "<div id =\"tabs-1\" class=\"leftcolumn\">\n"+
 							"<div class=\"card\">\n"+
 							  "<h2>Welcome " + doesUserExist.getFirstname() + " "+ doesUserExist.getSurname() + ". You are now logged in!</h2>\n"+
 							  "<br>\n"+
 							  "<table id=\"table\">\n" +
 							  "<tr>\n" +
 							 "<th> </th>\n"+
 							  "<th>AUTHOR</th>\n"+
 							  "<th>TITLE</th>\n" +
 							  "<th>AVALIABLE</th>\n" +
 							  "<th>USERID</th>\n" +
 							  "<th>DUE DATE</th>\n" +
 							  "</tr>\n" +
 							    table2 + 
 							"</table>\n" +
 							"<br>\n"+
 							  "</div>\n"+
 							
 						  "</div>\n" +
 						"</div>\n" +
 					 "</body>\n"+
 					"</html>");
     }
 		out.close();
 	}else {
 		res.sendRedirect("login.html");
 	}
 }
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
    public boolean validateUser() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(JDBCUrl, username, password);
			stmt = con.createStatement();
			String command = "SELECT USERNAME, PASSWORD FROM RDLIB5_ACCS";
			rs = stmt.executeQuery(command);
			System.out.println(doesUserExist.getUsername() + " " + doesUserExist.getPassword());
			boolean flag = false;
			
			while(rs.next()) {
				users.add(rs.getString(1));
				if(doesUserExist.getUsername().equals(rs.getString(1))) {
					if(doesUserExist.getPassword().equals(rs.getString(2))) {
						
						flag =true;
					}
				}
			}
			
			System.out.println(users.toString());
			return flag;

		} catch (NullPointerException e) {
			return false;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		} catch (SQLException c) {
			c.printStackTrace();
		} finally {
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
    public void maketable() {
    	
    	for(int i =0; i<Authors.size(); i++) {		
    		table += "<tr>\n"+ "<td>"+Authors.get(i) +"</td>\n" +
    				"<td>"+Title.get(i) +"</td>\n" +
    				"<td>"+date.get(i) +"</td>\n" +"</tr>\n";
    		
    	
    	}
    	
    	Authors.clear();
    	Title.clear();
    	Available.clear();
    	userid.clear();
    	users.clear();
    	date.clear();
    	
    	System.out.println(table);   
    }
    public void admintable() {
    	for(int j=0; j< users.size();j++) {
    		if(!users.get(j).equals("hats")){
			table3 +="<option>"+users.get(j) +"</option>\n";}
		}
    	
    	for(int i =0; i<Authors.size(); i++) {
    		String author,title,urid,date2,id;
    		author =Authors.get(i);
    		title =Title.get(i);
    		urid =userid.get(i);
    		date2 =date.get(i);
    		id = bookid.get(i).toString();
    		System.out.println("------------------------"+id+"---------------");
    		String option = "yes";
    		if(Available.get(i).equals("yes")) {
    			option ="no";
    		}
    		
    		table2 += "<tr>\n"+ 
    				"<form action= \"databaseU\" method= \"POST\">"+
    				"<td><input type=\"hidden\" name \"bookid\" value= \"" + id + "\"></td>\n"+
    				"<td><input readonly name=\"Author\" value =\""+author +"\"></td>\n" +
    				"<td><input readonly name=\"Title\" value =\""+title +"\"></td>\n"+
    				"<td><select name=\"avilable\">" +
    					"<option>"+Available.get(i)+"</option>\n"+
    					"<option>"+option +"</option>\n</select></td>\n"+
    					"<td><select name=\"userid\">" +
    					"<option>"+urid+"</option>\n"+
    					table3+
    					"</select></td>\n"+
    				"<td><input name=\"date2\" value =\""+date2 +"\">"+
    					"<input type=\"submit\" value= \"Update\">"+
    				"</td>\n" +
    				"</form></tr>\n";
    	}
    	
    	Authors.clear();
    	Title.clear();
    	Available.clear();
    	userid.clear();
    	users.clear();
    	date.clear();
    	
    	System.out.println(table2); 
    }
    public void books() {
        try {
   		 Class.forName("oracle.jdbc.driver.OracleDriver");
   		 con = DriverManager.getConnection(JDBCUrl, username, password);
   		 stmt = con.createStatement();
   	     String command ="SELECT * FROM RDLIB5_BOOKS ";
   		 rs = stmt.executeQuery(command);
   		 while(rs.next()) {
   			 	bookid.add(rs.getInt(1));
   				Authors.add(rs.getString(2));
   				Title.add(rs.getString(3));
   				Available.add(rs.getString(4));
   				userid.add(rs.getString(5));
   				date.add(rs.getString(6));
   			 
   			
   		 
   		 }
   		 System.out.println(bookid.toString());
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }finally {
   		 try {
   			 if (rs != null) rs.close();
   			 if (stmt != null) stmt.close();
   			 if (con != null) con.close();
   			 
   		 }catch (SQLException e) { e.printStackTrace();}
   	 }
    }
    public void Userbooks() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(JDBCUrl, username, password);
			stmt = con.createStatement();
			String command = "SELECT * FROM RDLIB5_BOOKS WHERE USERID = '"+Uname+"'";
			rs = stmt.executeQuery(command);
			
			while(rs.next()) {
				Authors.add(rs.getString(2));
				Title.add(rs.getString(3));
				date.add(rs.getString(6));
			}

		} catch (NullPointerException e) {
			
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		} catch (SQLException c) {
			c.printStackTrace();
		} finally {
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}