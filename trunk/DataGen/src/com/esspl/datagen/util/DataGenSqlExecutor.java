package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.esspl.datagen.ui.ExecutorView;
import com.esspl.datagen.ui.ResultSetTable;

/**
 * @author Tapas
 *
 */
public class DataGenSqlExecutor {
	
	private static final Logger log = Logger.getLogger(DataGenSqlExecutor.class);
	private String splitter = ";";
	private StringBuilder sbLogMsg = new StringBuilder();
	private ExecutorView executorView;

	/**
	 * This method is responsible for executing sql statements.
	 *
	 */
	public String executeScript(ExecutorView execView, String script, String argDataBase) {
		log.debug("DataGenSqlExecutor - executeScript() method start");
		executorView = execView;
		String statMsg = "";
		int excutedScriptNumber = 0;
		int totalScriptCount = 0;
		int totalUpdatedCount = 0;
		int maxRow = (Integer)executorView.maxRowsBox.getValue();
		boolean hasResultSet = true;
		Connection con = executorView.connection;
		PreparedStatement stmt = null;
		
		//Initialize the splitter according to the database
		if(argDataBase.equalsIgnoreCase("sql server")){
			splitter = "GO";
		}
		
		try {
			Scanner scanner =  new Scanner(script).useDelimiter(splitter);  
	        while (scanner.hasNext()) {  
	            String statement = scanner.next(); 
	            if(!statement.trim().equals("")){
	            	try {
	            		totalScriptCount++;
						addLog("<br>Execution started->> "+statement);
						stmt = con.prepareStatement(statement);
						stmt.setMaxRows(maxRow);
						hasResultSet = stmt.execute();
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
					if (!hasResultSet) {
						int cnt = stmt.getUpdateCount();
		            	totalUpdatedCount += cnt;
		            }
	            }
	            if (hasResultSet && stmt.getResultSet() != null) {
	            	ResultSetTable table = new ResultSetTable(stmt.getResultSet());
	                executorView.resultSheet.removeTab(executorView.resultTab);
	                executorView.resultTab = executorView.resultSheet.addTab(table, "Results");
	                executorView.resultSheet.setSelectedTab(table);
	                executorView.logText.setValue("");
	                statMsg = "rows fetched: " + table.getItemIds().size();
	            } else {
	                int cnt = stmt.getUpdateCount();
	                executorView.resultSheet.setSelectedTab(executorView.logText);
	                statMsg = "rows updated: " + totalUpdatedCount;
	            }
	            JdbcUtils.close(stmt);
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
		}
		log.debug("DataGenSqlExecutor - execute() method end");
		
		return statMsg;
	}
	
	/**
	 * Add a message to log.
	 *
	 */
	public void addLog(String message){
		sbLogMsg.append(message).append("<br>");
		executorView.logText.setValue(sbLogMsg.toString());
	}
}

