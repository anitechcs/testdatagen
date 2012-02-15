package com.esspl.datagen.ui;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenSqlExecutor;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

/**
 * @author Tapas
 */
public class ExecutorLayout extends VerticalLayout implements ValueChangeListener{
	
	private static final Logger log = Logger.getLogger(ExecutorLayout.class);
	private DataGenApplication dataApp;
	private AbsoluteLayout abs;
	private Accordion accordian;
	private TextField driverName;
	private Select scriptDatabase;
	private TextField dbUrl;
	private TextField userName;
	private PasswordField password;
	public Panel logPanel;
	
	public ExecutorLayout(final DataGenApplication dataGenApplication){
		log.debug("ExecutorLayout - constructor start");
		dataApp = dataGenApplication;
       
        HorizontalLayout sriptHl = new HorizontalLayout();
        sriptHl.setSizeFull();
        
        //Script TextArea
        dataApp.sqlScript = new TextArea();
        dataApp.sqlScript.setSizeFull();
        dataApp.sqlScript.setWordwrap(false);
        dataApp.sqlScript.setStyleName("noResizeTextArea");
		sriptHl.addComponent(dataApp.sqlScript);
		
		//Setting fields
		scriptDatabase = new Select("DataBase");
		scriptDatabase.setWidth("330px");
		scriptDatabase.addItem("Oracle");
		scriptDatabase.addItem("Sql Server");
		scriptDatabase.addItem("My Sql");
		scriptDatabase.addItem("Postgress Sql");
		scriptDatabase.setImmediate(true);
		scriptDatabase.addListener(this);
        
		driverName = new TextField("Driver Class Name");
		driverName.setWidth("330px");
		
		dbUrl = new TextField("Database URL");
		dbUrl.setWidth("330px");
		
		userName = new TextField("User Name");
		userName.setWidth("155px");
		
		password = new PasswordField("Password");
		password.setWidth("155px");
		
		abs = new AbsoluteLayout();
		abs.setHeight("400px");
		abs.setWidth("448px");
		abs.setStyleName("showBorder");
		abs.addComponent(scriptDatabase, "top:40px; left:50px");
		abs.addComponent(driverName, "top:100px; left:50px");
		abs.addComponent(dbUrl, "top:160px; left:50px");
		abs.addComponent(userName, "top:220px; left:50px");
		abs.addComponent(password, "top:220px; left:225px");
		
		Button testConnection = new Button("Test Connection");
		testConnection.addListener(ClickEvent.class, this, "testConnectionButtonClick");
		testConnection.setIcon(DataGenConstant.ACCEPT_ICON);
		abs.addComponent(testConnection, "top:270px; left:140px");
		
		accordian = new Accordion();
		accordian.setHeight("480px");
		accordian.setWidth("450px");
		accordian.addTab(dataApp.sqlScript, "SQL Script", DataGenConstant.EXECUTOR_ICON);
		accordian.addTab(abs, "Settings", DataGenConstant.SETTING_ICON);

		sriptHl.addComponent(accordian);
		sriptHl.setComponentAlignment(accordian, Alignment.TOP_LEFT);
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(true);
		vl.setSizeFull();
		Button executeBttn = new Button("Execute");
		executeBttn.setDescription("Execute Script");
		executeBttn.addListener(ClickEvent.class, this, "executeButtonClick");
		executeBttn.setIcon(DataGenConstant.EXECUTOR_ICON);
		vl.addComponent(executeBttn);
		
		Button clearLogBttn = new Button("Clear Log");
		clearLogBttn.setDescription("Clear the logs");
		clearLogBttn.addListener(ClickEvent.class, this, "clearLogButtonClick");
		clearLogBttn.setIcon(DataGenConstant.CLEAR_ICON);
		vl.addComponent(clearLogBttn);
		
		Button clearScriptBttn = new Button("Clear Sql");
		clearScriptBttn.setDescription("Clear the Sql Scripts");
		clearScriptBttn.addListener(ClickEvent.class, this, "clearScriptButtonClick");
		clearScriptBttn.setIcon(DataGenConstant.CLEAR_ICON);
		vl.addComponent(clearScriptBttn);
		
		sriptHl.addComponent(vl);
		sriptHl.setComponentAlignment(vl, Alignment.MIDDLE_LEFT);
		
        //Show the log
        logPanel = new Panel("Execution Log");
        logPanel.setWidth("450px");
        logPanel.setHeight("450px");
        logPanel.setScrollable(true);
        sriptHl.addComponent(logPanel);
        sriptHl.setComponentAlignment(logPanel, Alignment.TOP_RIGHT);
        
        this.addComponent(sriptHl);
        log.debug("ExecutorLayout - constructor end");
	}
	
	/**
	 * This method is responsible for executing the sql script.
	 * 
	 * @param event
	 */
	public void executeButtonClick(ClickEvent event) {
		log.debug("ExecutorLayout - executeButtonClick() called");
		if(isBlankFieldsPresent()){
			accordian.setSelectedTab(abs);
			return;
		}else{
			if(dataApp.sqlScript.getValue() == null || dataApp.sqlScript.getValue().toString().equals("")){
				accordian.setSelectedTab(dataApp.sqlScript);
				dataApp.getMainWindow().showNotification("Provide the SQL Scripts to execute!", Notification.TYPE_ERROR_MESSAGE);
				return;
			}else{
				DataGenSqlExecutor executor = new DataGenSqlExecutor(scriptDatabase.getValue().toString(), driverName.getValue().toString(), dbUrl.getValue().toString(), userName.getValue().toString(), password.getValue().toString(), logPanel);
				executor.execute(dataApp.sqlScript.getValue().toString());
			}
		}
    }
	
	/**
	 * Test whether given Database credentials are valid or not.
	 * 
	 * @param event
	 */
	public void testConnectionButtonClick(ClickEvent event) {
		log.debug("ExecutorLayout - testConnectionButtonClick() called");
		if(!isBlankFieldsPresent()){
			DataGenSqlExecutor executor = new DataGenSqlExecutor(scriptDatabase.getValue().toString(), driverName.getValue().toString(), dbUrl.getValue().toString(), userName.getValue().toString(), password.getValue().toString(), logPanel);
			executor.testConnection();
		}
    }
	
	/**
	 * Clears the previous logs.
	 * 
	 * @param event
	 */
	public void clearLogButtonClick(ClickEvent event) {
		log.debug("ExecutorLayout - clearLogButtonClick() called");
		logPanel.removeAllComponents();
    }
	
	/**
	 * Clears the previous scripts.
	 * 
	 * @param event
	 */
	public void clearScriptButtonClick(ClickEvent event) {
		log.debug("ExecutorLayout - clearScriptButtonClick() called");
		dataApp.sqlScript.setValue("");
    }
	
	/**
	 * Test method is responsible for populating Driver class as per the selected database.
	 * 
	 * @param event
	 */
	public void valueChange(ValueChangeEvent event) {
    	log.debug("ExecutorLayout - valueChange() called");
    	//If blank value selected from dropdown
    	if(event.getProperty().getValue() == null){
    		driverName.setValue("");
    		return;
    	}
    			
    	if(event.getProperty().getValue().equals("Oracle")){
    		driverName.setValue("oracle.jdbc.driver.OracleDriver");
        }else if(event.getProperty().getValue().equals("Sql Server")){
        	driverName.setValue("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }else if(event.getProperty().getValue().equals("My Sql")){
        	driverName.setValue("com.mysql.jdbc.Driver");
        }else if(event.getProperty().getValue().equals("Postgress Sql")){
        	driverName.setValue("org.postgresql.Driver");
        }
	}
	
	/**
	 * Test method is responsible for validating DB credential fields.
	 * 
	 * @param event
	 */
	public boolean isBlankFieldsPresent() {
		log.debug("ExecutorLayout - isBlankFieldsPresent() called");
		if(scriptDatabase.getValue() == null || scriptDatabase.getValue().toString().equals("")){
			dataApp.getMainWindow().showNotification("DataBase cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
			return true;
		}
		
		if(driverName.getValue() == null || driverName.getValue().toString().equals("")){
			dataApp.getMainWindow().showNotification("Driver Class Name cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
			return true;
		}
		
		if(dbUrl.getValue() == null || dbUrl.getValue().toString().equals("")){
			dataApp.getMainWindow().showNotification("Database URL cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
			return true;
		}
		
		if(userName.getValue() == null || userName.getValue().toString().equals("")){
			dataApp.getMainWindow().showNotification("User Name cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
			return true;
		}
		
		if(password.getValue() == null || password.getValue().toString().equals("")){
			dataApp.getMainWindow().showNotification("Password cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
			return true;
		}
		return false;
	}
}
