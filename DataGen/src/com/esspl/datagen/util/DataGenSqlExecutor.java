/**
 * Copyright (C) 2012 Enterprise System Solutions (P) Ltd. All rights reserved.
 *
 * This file is part of DATA Gen. http://testdatagen.sourceforge.net/
 *
 * DATA Gen is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DATA Gen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.esspl.datagen.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.esspl.datagen.ui.ExecutorView;
import com.esspl.datagen.ui.ResultSetTable;
import com.vaadin.ui.Window.Notification;

/**
 * DBScript Executor Utility for DataGen
 * 
 * @author Tapas
 *
 */
public class DataGenSqlExecutor extends Thread {
	
	private static final Logger log = Logger.getLogger(DataGenSqlExecutor.class);
	
	private String splitter = ";";
	private StringBuilder sbLogMsg = new StringBuilder();
	private ExecutorView executorView;
	private String sqlScript;
	private String selectedDatabase;
	private String statMsg = "";
	
	public DataGenSqlExecutor(ExecutorView execView, String script, String argDataBase) {
		executorView = execView;
		sqlScript = script;
		selectedDatabase = argDataBase;
	}
	
	/**
	 * This method is responsible for executing sql statements.
	 *
	 */
	@Override
	public void run() {
		log.debug("DataGenSqlExecutor - run() method start");
		int excutedScriptNumber = 0;
		int totalScriptCount = 0;
		int totalUpdatedCount = 0;
		int maxRow = (Integer)executorView.maxRowsBox.getValue();
		boolean hasResultSet = true;
		Connection con = executorView.connection;
		PreparedStatement stmt = null;
		
		//Initialize the splitter according to the selected database
		if(selectedDatabase.equalsIgnoreCase("sql server")){
			splitter = "GO";
		}
		
		executorView.refresher.setRefreshInterval(DataGenConstant.SLEEP_TIME_IN_MILLIS);
		executorView.loadingImg.setVisible(true);
		long start = System.currentTimeMillis();
		try {
			addLog("Connecting to the DataBase...");
			addLog("<font color='green'>Successfully Connected.</font>");
			addLog("Preparing to execute script...");
			Scanner scanner =  new Scanner(sqlScript).useDelimiter(splitter);  
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
						addLog("<font color='red'>## Error occoured at : "+statement+". <br>## NOTE : Make sure every sql statements must ends with '"+splitter+"'");
						addLog("## Error Details : "+e.getMessage());
						for(int i = 0; i < e.getStackTrace().length; i++){
							addLog(e.getStackTrace()[i].toString());
						}
						addLog("</font>");
						executorView.resultSheet.setSelectedTab(executorView.logText);
						//If error occurred for one script don't stop. keep executing
						continue;
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
	                executorView.resultSheet.setSelectedTab(executorView.logText);
	                statMsg = "rows updated: " + totalUpdatedCount;
	            }
	            JdbcUtils.close(stmt);
	        }  
		}
		catch(Exception e){
			addLog("<font color='red'>## Oops! The following Error occoured during ccript execution.");
			addLog(e.getMessage());
			for(int i = 0; i < e.getStackTrace().length; i++){
				addLog(e.getStackTrace()[i].toString());
			}
			addLog("</font>");
			e.printStackTrace();
		}
		finally {
			//Close the connection
			JdbcUtils.close(executorView.connection);

	        //Hide Loading Icon
	        executorView.loadingImg.setVisible(false);
	        
	        //Log the statistics
	        addLog("<br /><font color='green'>######################");
			addLog("## Execution Summary ##");
			addLog("######################</font>");
			addLog("Total No. of Scripts- "+ totalScriptCount);
			addLog("Successfully Executed Scripts- "+excutedScriptNumber);
			
	        //Show the Execution time
	        long end = System.currentTimeMillis();
	        executorView.getApplication().getMainWindow().showNotification("Query Stats<br/>", "exec time: " + (end - start) / 1000.0 + " ms<br/>" + statMsg, Notification.TYPE_TRAY_NOTIFICATION);
		
	        //Disable the UI refresher
	        executorView.refresher.setRefreshInterval(0);
	        
		}
		log.debug("DataGenSqlExecutor - run() method end");
		
	}
	
	/**
	 * Add a message to log and set the scrolltop of the panel.
	 *
	 */
	public void addLog(String message){
		sbLogMsg.append(message).append("<br>");
		executorView.logText.setValue(sbLogMsg.toString());
		
		//Hack to automatically scroll to the bottom of the log panel
		executorView.logPanel.setScrollTop(Short.MAX_VALUE);
		executorView.logPanel.requestRepaint();
	}

}

