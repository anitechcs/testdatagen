package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * @author Tapas
 *
 */
public class DataGenSqlExecutor {
	
	private static final Logger log = Logger.getLogger(DataGenSqlExecutor.class);
	private String DRIVER_NAME = "";
	private String URL = "";
	private String USER = "";
	private String PASSWORD = "";
	
	private String splitter = "";
	private StringBuilder sbLogMsg = new StringBuilder();
	private Panel logPanel;
	
	/**
	 * This Constructor take Database credentials and initialize the respective driver.
	 * 
	 * @param argDataBase
	 * @param argClassName
	 * @param argDbUrl
	 * @param argUserName
	 * @param argPassword
	 * @param argLogPanel
	 */
	public DataGenSqlExecutor(String argDataBase, String argClassName, String argDbUrl, String argUserName, String argPassword, Panel argLogPanel){
		log.debug("DataGenSqlExecutor - constructor start");
		DRIVER_NAME = argClassName;
		URL = argDbUrl;
		USER = argUserName;
		PASSWORD = argPassword;
		logPanel = argLogPanel;
		// Scroll to the end + hack
        logPanel.setScrollTop(1000000); // Keep it at the end
        sbLogMsg.setLength(0);
		
		//Initialize the splitter according to the database
		if(argDataBase.equalsIgnoreCase("sql server")){
			splitter = "GO\n *";
		}else{
			splitter = ";\n *";
		}
		
		try{
			//Load database driver
			Class.forName(DRIVER_NAME).newInstance();
			addLog("<font color='green'>Driver loaded successfully</font>");
		}
		catch(Exception e){
			addLog("<font color='red'>## Driver loading failed ##");
			addLog("## Error : "+e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
			addLog("</font>");
			e.printStackTrace();
		}
		log.debug("DataGenSqlExecutor - constructor end");
	}

	/**
	 * This method is responsible for executing sql statements.
	 *
	 */
	public void execute(String script) {
		log.debug("DataGenSqlExecutor - execute() method start");
		int excutedScriptNumber = 0;
		int totalScriptCount = 0;
		Connection con = null;
		Statement stmt = null;
		
		try {
			addLog("Connecting to Database...");
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.createStatement();
			addLog("<font color='green'>Connection Successful.</font>");
			Scanner scanner =  new Scanner(script).useDelimiter(splitter);  
	        while (scanner.hasNext()) {  
	            String statement = scanner.next(); 
	            if(!statement.trim().equals("")){
	            	try {
	            		totalScriptCount++;
						addLog("Execution started->> "+statement);
						stmt.executeUpdate(statement);
						addLog("<font color='green'>Execution completed sucessfully</font>");
						excutedScriptNumber++;
					}catch(Exception e){
						addLog("<font color='red'>## Error occoured at : "+statement+". Make sure every sql statements must ends with '"+splitter+"'");
						addLog("## Error : "+e.getMessage());
						for(int i = 0; i < e.getStackTrace().length; i++){
							addLog(e.getStackTrace()[i].toString());
						}
						addLog("</font>");
						e.printStackTrace();
					}
	            }
	        }  
			addLog("<font color='green'>######################");
			addLog("## Execution Summary ##");
			addLog("######################</font>");
			addLog("Total No. of Scripts- "+ totalScriptCount);
			addLog("Successfully Executed Scripts- "+excutedScriptNumber);
		}
		catch(Exception e){
			addLog("<font color='red'>## Oops! Error occoured during Script execution.");
			addLog(e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
			addLog("</font>");
			e.printStackTrace();
		}finally{
			try {
				con.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.debug("DataGenSqlExecutor - execute() method end");

	}
	
	/**
	 * This is the method to test whether given DB credentials are valid or not.
	 *
	 */
	public boolean testConnection() {
		log.debug("DataGenSqlExecutor - testConnection() method called");
		addLog("Connecting to Database...");
		try {
			DriverManager.getConnection(URL, USER, PASSWORD);
			addLog("<font color='green'>Connection Successful.</font>");
		} catch (Exception e) {
			addLog("<font color='red'>## Error in connecting to Database ##");
			addLog("## Error : "+e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
			addLog("</font>");
			return false;
		}
		return true;
	}
	
	/**
	 * Add a message to log.
	 *
	 */
	public void addLog(String message){
		sbLogMsg.append(message).append("<br>");
		logPanel.removeAllComponents();
		Label log = new Label(sbLogMsg.toString());
		log.setContentMode(Label.CONTENT_XHTML);
		logPanel.addComponent(log);
	}
}

