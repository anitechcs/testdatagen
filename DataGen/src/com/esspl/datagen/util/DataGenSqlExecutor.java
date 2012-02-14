package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

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
			splitter = "GO";
		}else{
			splitter = ";";
		}
		
		try{
			//Load database driver
			Class.forName(DRIVER_NAME).newInstance();
			addLog("Driver loaded successfully");
		}
		catch(Exception e){
			addLog("## Driver loading failed ##");
			addLog("## Error : "+e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
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
		try {
			StringBuilder sbScript = new StringBuilder(script);
			//Using appropriate splitter, extract the well formed sql statements
			addLog("Connecting to Database...");
			String[] inst = sbScript.toString().split(splitter);
			Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
			Statement st = c.createStatement();
			addLog("Connection Successful.");

			for(int j = 0; j<inst.length; j++){
				if(!inst[j].trim().equals("")){
					try {
						addLog("Execution started->> "+inst[j]);
						st.executeUpdate(inst[j]);
						addLog("Execution completed sucessfully");
						excutedScriptNumber++;
					}catch(Exception e){
						addLog("## Error occoured at : "+inst[j]+". Make sure every sql statements must ends with ';' ");
						addLog("## Error : "+e.getMessage());
						for(int i = 0; i < e.getStackTrace().length; i++){
							addLog(e.getStackTrace()[i].toString());
						}
					}
				}
			}
			addLog("######################");
			addLog("## Execution Summary ##");
			addLog("######################");
			addLog("Total No. of Scripts- "+ (inst.length - 1));
			addLog("Successfully Executed Scripts- "+excutedScriptNumber);
		}
		catch(Exception e){
			addLog("## Oops! Error occoured during Script execution.");
			addLog(e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
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
			addLog("Connection Successful.");
		} catch (Exception e) {
			addLog("## Error in connecting to Database ##");
			addLog("## Error : "+e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
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

