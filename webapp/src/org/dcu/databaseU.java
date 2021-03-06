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

@WebServlet("/databaseU")
public class databaseU extends HttpServlet {
 private static final long serialVersionUID = 1L;
 private String JDBCUrl = "jdbc:oracle:thin:@ee417.c7clh2c6565n.eu-west-1.rds.amazonaws.com:1521:EE417";
 private String username = "ee_user";
 private String password = "ee_pass";
 private ArrayList<String> Authors = new ArrayList<String>(); 
 private ArrayList<String> Title = new ArrayList<String>();
 private ArrayList<String> Available = new ArrayList<String>(); 
 private ArrayList<String> userid = new ArrayList<String>(); 
 private ArrayList<Integer> bookid = new ArrayList<Integer>(); 
 private ArrayList<String> users = new ArrayList<String>(); 
 private ArrayList<String> date = new ArrayList<String>(); 
 private String table;
 Connection con = null;
 Statement stmt = null;
 ResultSet rs = null;
 
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
     res.setContentType("text/html");
     PrintWriter out = res.getWriter();
     String id = req.getParameter("bookid");
     String Author = req.getParameter("Author");
     String tit = req.getParameter("Title");
     String available = req.getParameter("avilable");
     String usrid = req.getParameter("userid");
     String due = req.getParameter("date2");
     
     System.out.println("----------" +id +":"+ Author+":"+tit+":" +available +":"+usrid+":"+due);
     try {
   		 Class.forName("oracle.jdbc.driver.OracleDriver");
   		 con = DriverManager.getConnection(JDBCUrl, username, password);
   		 stmt = con.createStatement();
   	     //String command ="UPDATE RDLIB5_BOOKS SET AVAILABLE='"+available+"',USERID='"+usrid+"',DUE='"+due+"'\nWHERE TITLE='"+tit+"'";
   		 String command = "UPDATE RDLIB5_BOOKS SET AVAILABLE= '"+available+"',USERID='"+usrid+"',DUE='"+due+"'\r\n" + 
   		 		"WHERE TITLE='"+tit+"'";
   		 System.out.println(command);
   	     rs = stmt.executeQuery(command);
   		
   	 }catch(Exception e){
   		 e.printStackTrace();
   	 }finally {
   		 try {
   			 if (rs != null) rs.close();
   			 if (stmt != null) stmt.close();
   			 if (con != null) con.close();
   			 
   		 }catch (SQLException e) { e.printStackTrace();}
   	 }
     
     books();
  	maketable();

  		out.println("<!DOCTYPE html>\n" + 
  				"<html>\n"+
 			 	"<head>\n"+
 					"<title> Radwan's DCU Library-Search</title>\n"+
 					"<Link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">\n"+
 				"</head>\n"+
 				"<div class=\"header\">\n"+
 					  "<a href=\"/webapp/home.html\"><img src=\"images/logo2.jpg\"></a>\n"+
 				"</div>\n"+
 				"<div class=\"topnav\">\n"+
 					  "<a href=\"/webapp/home.html\">Home</a>\n"+
 					  "<a href=\"/webapp/login.html\">Login </a>"+
 					  "<a href=\"/webapp/Search.html\">Search</a>"+
 					 "<a href=\"/webapp/report.html\">Report</a>"+
 				"</div>\n"+
  				"<body>\n" + 
 	 				"<div class=\"row\">\n"+
 					  "<div id =\"tabs-1\" class=\"leftcolumn\">\n"+
 						"<div class=\"card\">\n"+
 						  "<h2>Welcome to library search!</h2>\n"+
 						  "<br>\n"+
 						  "<table id=\"table\">\n" +
 						  "<tr>\n" +
 						    "<th>AUTHOR</th>\n"+
 						    "<th>TITLE</th>\n" +
 						    "<th>AVALIABLE</th>\n" +
 						    "<th>USERID</th>\n" +
 						    "<th>DUE DATE</th>\n" +
 						  "</tr>\n" +
 						    table +
 						"</table>\n" +
 						  "</div>\n"+
 						  
 					  "</div>\n" +
 					"</div>\n" +
 				 "</body>\n"+
 				"</html>");
  	
      out.close();
      table =null;
     
 
 }
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
    public void maketable() {
    	for(int i =0; i<Authors.size(); i++) {
    		table += "<tr>\n"+ "<td>"+Authors.get(i) +"</td>\n" +
    				"<td>"+Title.get(i) +"</td>\n" +
    				"<td>"+Available.get(i) +"</td>\n" +
    				"<td>"+userid.get(i) +"</td>\n" +
    				"<td>"+date.get(i) +"</td>\n" +"</tr>\n";
    	}
    	Authors.clear();
    	Title.clear();
    	Available.clear();
    	userid.clear();
    	date.clear();
    	System.out.println(table);
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
    
   
}
