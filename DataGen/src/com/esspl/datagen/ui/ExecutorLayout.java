package com.esspl.datagen.ui;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.util.DataGenConstant;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

/**
 * @author Tapas
 */
public class ExecutorLayout extends VerticalLayout{
	
	private DataGenApplication dataApp;
	
	public ExecutorLayout(final DataGenApplication dataGenApplication){
		dataApp = dataGenApplication;
        Label executerRichText = new Label("<h1>DATA Gen Sql Script Runner</h1>");
        executerRichText.setContentMode(Label.CONTENT_XHTML);
        this.addComponent(executerRichText);
        
        HorizontalLayout sriptHl = new HorizontalLayout();
        sriptHl.setSizeFull();
        
        //Script TextArea
        dataApp.sqlScript = new TextArea("Script");
        dataApp.sqlScript.setHeight("350px");
        dataApp.sqlScript.setWidth("450px");
        dataApp.sqlScript.setWordwrap(false);
        dataApp.sqlScript.setStyleName("noResizeTextArea");
		sriptHl.addComponent(dataApp.sqlScript);
		
		Button executeBttn = new Button("Execute");
		executeBttn.setDescription("Execute Script");
		executeBttn.addListener(ClickEvent.class, this, "executeButtonClick"); // react to clicks
		executeBttn.setIcon(DataGenConstant.EXECUTOR_ICON);
		sriptHl.addComponent(executeBttn);
		
		sriptHl.addComponent(executeBttn);
	    
        //Show the log
        final Panel logPanel = new Panel("Log");
        logPanel.setWidth("350px");
        logPanel.setHeight("450px");
        logPanel.setScrollable(true);
        sriptHl.addComponent(logPanel);
        
        this.addComponent(sriptHl);
	}
	
	public void executeButtonClick(ClickEvent event) {
		dataApp.getMainWindow().showNotification("Execute Script Button clicked");
    }
}
