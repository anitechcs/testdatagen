package com.esspl.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.h2.util.StringUtils;

import com.esspl.datagen.common.DatabaseSessionManager;
import com.esspl.datagen.common.GeneratorBean;
import com.esspl.datagen.generator.Generator;
import com.esspl.datagen.generator.impl.ExcelDataGenerator;
import com.esspl.datagen.ui.ExecutorView;
import com.esspl.datagen.ui.ExplorerView;
import com.esspl.datagen.ui.ResultView;
import com.esspl.datagen.ui.ToolBar;
import com.esspl.datagen.util.DataGenConstant;
import com.esspl.datagen.util.DataGenEventHandler;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

/**
 * Sample application for generating realistic test data.
 *
 * @author Tapas
 *
 */
@SuppressWarnings("serial")
public class DataGenApplication extends Application implements ValueChangeListener{

	private static final Logger log = Logger.getLogger(DataGenApplication.class);
    private ArrayList<GeneratorBean> rowList = new ArrayList<GeneratorBean>();
    private int tableLength = 1;//for internal use only

    public DatabaseSessionManager databaseSessionManager;
    public ResultView resultWindow;
    public Button rowsBttn;
    public Table listing;
    public TextField rowNum;
    public TextField resultNum;
    public HorizontalLayout generateTypeHl;
    public Panel sqlPanel;
    public Panel csvPanel;
    public Panel xmlPanel;
    public OptionGroup generateType;
    public CheckBox createQuery;
    public TextField tblName;
    public Select database;
    public TextField csvDelimiter;
    public TabSheet tabSheet;
    public Tab executorTab;
    public Tab explorerTab;
    public VerticalLayout generator;
    public ExecutorView executor;
    public ExplorerView explorer;
    public TextField rootNode;
    public TextField recordNode;
    public Label connectedString;
    public ToolBar toolbar;

    @Override
    public void init() {
    	log.debug("DataGenApplication - init() start");
    	databaseSessionManager = new DatabaseSessionManager();
        //We'll build the whole UI inside the method buildMainLayout
        buildMainLayout();
        setTheme("testdatagenerator");
        log.debug("DataGenApplication init() end");
    }

    private void buildMainLayout(){
    	log.debug("DataGenApplication - buildMainLayout() start");
        VerticalLayout rootLayout = new VerticalLayout();
        final Window root = new Window("DATA Gen", rootLayout);
        root.setStyleName("tData");

        setMainWindow(root);
        
        rootLayout.setSizeFull();
        rootLayout.setMargin(false, true, false, true);

        // Top area, containing logo and header
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        root.addComponent(top);

        // Create the placeholders for all the components in the top area
        HorizontalLayout header = new HorizontalLayout();

        // Add the components and align them properly
        top.addComponent(header);
        top.setComponentAlignment(header, Alignment.TOP_LEFT);

        top.setStyleName("top");
        top.setHeight("75px"); // Same as the background image height

        // header controls
        Embedded logo = new Embedded();
        logo.setSource(DataGenConstant.LOGO);
        logo.setWidth("100%");
        header.addComponent(logo);
        header.setSpacing(false);
        
        //Show which connection profile is connected
        connectedString = new Label("Connected to - Oracle");
        connectedString.setStyleName("connectedString");
        connectedString.setWidth("500px");
        connectedString.setVisible(false);
        top.addComponent(connectedString);
        
        //Toolbar
        toolbar = new ToolBar(this);
        top.addComponent(toolbar);
        top.setComponentAlignment(toolbar, Alignment.TOP_RIGHT);
        
        listing = new Table();
        listing.setHeight("240px");
        listing.setWidth("100%");
        listing.setStyleName("dataTable");
        listing.setImmediate(true);

        // turn on column reordering and collapsing
        listing.setColumnReorderingAllowed(true);
        listing.setColumnCollapsingAllowed(true);
        listing.setSortDisabled(true);

        // Add the table headers
        listing.addContainerProperty("Sl No.", Integer.class, null);
        listing.addContainerProperty("Column Name", TextField.class, null);
        listing.addContainerProperty("Data Type", Select.class, null);
        listing.addContainerProperty("Format", Select.class, null);
        listing.addContainerProperty("Examples", Label.class, null);
        listing.addContainerProperty("Additional Data", HorizontalLayout.class, null);
        listing.setColumnAlignments(new String[]{Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_CENTER});
        
        //From the starting create 5 rows
        addRow(4);
        
        //Add different style for IE browser
        WebApplicationContext context = ((WebApplicationContext)getMainWindow().getApplication().getContext());
        WebBrowser browser = context.getBrowser();

        //Create a TabSheet
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        if(!browser.isIE()){
        	tabSheet.setStyleName("tabSheet");
        }

        //Generator Tab content start
        generator = new VerticalLayout();
        generator.setMargin(true, true, false, true);

        generateTypeHl = new HorizontalLayout();
        generateTypeHl.setMargin(false, false, false, true);
        generateTypeHl.setSpacing(true);
        List<String> generateTypeList = Arrays.asList(new String[]{"Sql", "Excel", "XML", "CSV"});
        generateType = new OptionGroup("Generation Type", generateTypeList);
        generateType.setNullSelectionAllowed(false); // user can not 'unselect'
        generateType.select("Sql"); // select this by default
        generateType.setImmediate(true); // send the change to the server at once
        generateType.addListener(this); // react when the user selects something
        generateTypeHl.addComponent(generateType);
        
        //SQL Options
        sqlPanel = new Panel("SQL Options");
        sqlPanel.setHeight("180px");
        if(browser.isIE()){
        	sqlPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanelIE");
        }else{
        	sqlPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanel");
        }
        VerticalLayout vl1 = new VerticalLayout();
        vl1.setMargin(false);
        Label lb1 = new Label("DataBase");
        database = new Select();
        database.addItem("Oracle");
        database.addItem("Sql Server");
        database.addItem("My Sql");
        database.addItem("Postgress Sql");
        database.select("Oracle");
        database.setWidth("160px");
        database.setNullSelectionAllowed(false);
        HorizontalLayout dbBar = new HorizontalLayout();
        dbBar.setMargin(false, false, false, false);
        dbBar.setSpacing(true);
        dbBar.addComponent(lb1);
        dbBar.addComponent(database);
        vl1.addComponent(dbBar);
        
        Label tblLabel = new Label("Table Name");
        tblName = new TextField();
        tblName.setWidth("145px");
        tblName.setStyleName("mandatory");
        HorizontalLayout tableBar = new HorizontalLayout();
        tableBar.setMargin(true, false, false, false);
        tableBar.setSpacing(true);
        tableBar.addComponent(tblLabel);
        tableBar.addComponent(tblName);
        vl1.addComponent(tableBar);
        
        createQuery = new CheckBox("Include CREATE TABLE query");
        createQuery.setValue(true);
        HorizontalLayout createBar = new HorizontalLayout();
        createBar.setMargin(true, false, false, false);
        createBar.addComponent(createQuery);
        vl1.addComponent(createBar);
        sqlPanel.addComponent(vl1);
     
        generateTypeHl.addComponent(sqlPanel);
        generator.addComponent(generateTypeHl);

        //CSV Option
        csvPanel = new Panel("CSV Options");
        csvPanel.setHeight("130px");
        if(browser.isIE()){
        	csvPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanelIE");
        }else{
        	csvPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanel");
        }
        Label delimiter = new Label("Delimiter Character(s)"); 
        VerticalLayout vl2 = new VerticalLayout();
        vl2.setMargin(false);
        csvDelimiter = new TextField();
        HorizontalLayout csvBar = new HorizontalLayout();
        csvBar.setMargin(true, false, false, false);
        csvBar.setSpacing(true);
        csvBar.addComponent(delimiter);
        csvBar.addComponent(csvDelimiter);
        vl2.addComponent(csvBar);
        csvPanel.addComponent(vl2);

        //XML Options
        xmlPanel = new Panel("XML Options");
        xmlPanel.setHeight("160px");
        if(browser.isIE()){
        	xmlPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanelIE");
        }else{
        	xmlPanel.setStyleName(Runo.PANEL_LIGHT +" sqlPanel");
        }
        
        VerticalLayout vl3 = new VerticalLayout();
        vl3.setMargin(false);
        Label lb4 = new Label("Root node name");
        rootNode = new TextField();
        rootNode.setWidth("125px");
        rootNode.setStyleName("mandatory");
        HorizontalLayout nodeBar = new HorizontalLayout();
        nodeBar.setMargin(true, false, false, false);
        nodeBar.setSpacing(true);
        nodeBar.addComponent(lb4);
        nodeBar.addComponent(rootNode);
        vl3.addComponent(nodeBar);
        
        Label lb5 = new Label("Record node name");
        recordNode = new TextField();
        recordNode.setWidth("112px");
        recordNode.setStyleName("mandatory");
        HorizontalLayout recordBar = new HorizontalLayout();
        recordBar.setMargin(true, false, false, false);
        recordBar.setSpacing(true);
        recordBar.addComponent(lb5);
        recordBar.addComponent(recordNode);
        vl3.addComponent(recordBar);
        xmlPanel.addComponent(vl3);

        HorizontalLayout noOfRowHl = new HorizontalLayout();
        noOfRowHl.setSpacing(true);
        noOfRowHl.setMargin(true, false, false, true);
        noOfRowHl.addComponent(new Label("Number of Results"));
        resultNum = new TextField();
        resultNum.setImmediate(true);
        resultNum.setNullSettingAllowed(false);
        resultNum.setStyleName("mandatory");
        resultNum.addValidator(new IntegerValidator("Number of Results must be an Integer"));
        resultNum.setWidth("5em");
        resultNum.setMaxLength(5);
        resultNum.setValue(50);
        noOfRowHl.addComponent(resultNum);
        generator.addComponent(noOfRowHl);

        HorizontalLayout addRowHl = new HorizontalLayout();
        addRowHl.setMargin(true, false, true, true);
        addRowHl.setSpacing(true);
        addRowHl.addComponent(new Label("Add"));
        rowNum = new TextField();
        rowNum.setImmediate(true);
        rowNum.setNullSettingAllowed(true);
        rowNum.addValidator(new IntegerValidator("Row number must be an Integer"));
        rowNum.setWidth("4em");
        rowNum.setMaxLength(2);
        rowNum.setValue(1);
        addRowHl.addComponent(rowNum);
        rowsBttn = new Button("Row(s)");
        rowsBttn.setIcon(DataGenConstant.ADD);
        rowsBttn.addListener(ClickEvent.class, this, "addRowButtonClick"); // react to clicks
        addRowHl.addComponent(rowsBttn);
        generator.addComponent(addRowHl);

        //Add the Grid
        generator.addComponent(listing);

        //Generate Button
        Button bttn = new Button("Generate");
        bttn.setDescription("Generate Gata");
        bttn.addListener(ClickEvent.class, this, "generateButtonClick"); // react to clicks
        bttn.setIcon(DataGenConstant.VIEW);
        bttn.setStyleName("generate");
        generator.addComponent(bttn);
        //Generator Tab content end
        
        //Executer Tab content start - new class created to separate execution logic
        executor = new ExecutorView(this);
        //Executer Tab content end
        
        //Explorer Tab content start - new class created to separate execution logic
        explorer = new ExplorerView(this, databaseSessionManager);
        //explorer.setMargin(true);
        //Explorer Tab content end
        
        //About Tab content start
        VerticalLayout about = new VerticalLayout();
        about.setMargin(true);
        Label aboutRichText = new Label(DataGenConstant.ABOUT_CONTENT);
        aboutRichText.setContentMode(Label.CONTENT_XHTML);
        about.addComponent(aboutRichText);
        //About Tab content end
        
        //Help Tab content start
        VerticalLayout help = new VerticalLayout();
        help.setMargin(true);
        Label helpText = new Label(DataGenConstant.HELP_CONTENT);
        helpText.setContentMode(Label.CONTENT_XHTML);
        help.addComponent(helpText);
        
        Embedded helpScreen = new Embedded();
        helpScreen.setSource(DataGenConstant.HELP_SCREEN);
        help.addComponent(helpScreen);
        
        Label helpStepsText = new Label(DataGenConstant.HELP_CONTENT_STEPS);
        helpStepsText.setContentMode(Label.CONTENT_XHTML);
        help.addComponent(helpStepsText);
        //Help Tab content end
        
        //Add the respective contents to the tab sheet
        tabSheet.addTab(generator, "Generator", DataGenConstant.HOME_ICON);
        executorTab = tabSheet.addTab(executor, "Executor", DataGenConstant.EXECUTOR_ICON);
        explorerTab = tabSheet.addTab(explorer, "Explorer", DataGenConstant.EXPLORER_ICON);
        tabSheet.addTab(about, "About", DataGenConstant.ABOUT_ICON);
        tabSheet.addTab(help, "Help", DataGenConstant.HELP_ICON);

        HorizontalLayout content = new HorizontalLayout();
        content.setSizeFull();
        content.addComponent(tabSheet);

        rootLayout.addComponent(content);
        rootLayout.setExpandRatio(content, 1);
        log.debug("DataGenApplication - buildMainLayout() end");
    }

    /*
     * This method is called when user click on Generate button.
     * 
     */
    public void generateButtonClick (Button.ClickEvent event) {
    	log.debug("DataGenApplication - generateButtonClick() start");
    	if(resultNum.getValue() == null || StringUtils.isNullOrEmpty(resultNum.getValue().toString())){
    		getMainWindow().showNotification("Number of result field cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
    		log.info("DataGenApplication - No. Of result field is blank");
    		return;
    	}else if(!StringUtils.isNumber(resultNum.getValue().toString())){
    		getMainWindow().showNotification("Number of results must be an Integer!", Notification.TYPE_ERROR_MESSAGE);
    		log.info("DataGenApplication - No. Of results is Text");
    		return;
    	}else if(Integer.parseInt(resultNum.getValue().toString()) > 10000){
    		getMainWindow().showNotification("Number of results cannot be more than 10000!", Notification.TYPE_ERROR_MESSAGE);
    		log.info("DataGenApplication - No. Of results is greater than 10000");
    		return;
    	}
    	String dataOption = generateType.getValue().toString();
        if(dataOption.equalsIgnoreCase("sql")){
        	if(tblName.getValue() == null || tblName.getValue().toString().equals("")){
        		getMainWindow().showNotification("Table Name cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
        		log.info("DataGenApplication - Table name field is blank");
        		return;
        	}
        }else if(dataOption.equalsIgnoreCase("xml")){
        	if(rootNode.getValue() == null || rootNode.getValue().toString().equals("") || recordNode.getValue() == null || recordNode.getValue().toString().equals("")){
        		getMainWindow().showNotification("Root Node and Record Node cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
        		log.info("DataGenApplication - Root Node and Record Node field is blank");
        		return;
        	}
        }
    	
    	rowList.clear();
        for (Iterator i = listing.getItemIds().iterator(); i.hasNext();) {
        	log.debug("DataGenApplication - Populating GeneratorBean for row- "+i);
	        int iid = (Integer)i.next();
	        Item item = listing.getItem(iid);
	
	        //get the data from grid and populate the GeneratorBean.
	        TextField tf = (TextField)(item.getItemProperty("Column Name").getValue());
	        Select sel = (Select)(item.getItemProperty("Data Type").getValue());
	
	        if(tf.getValue() != null && !tf.getValue().toString().equals("")){
	        	if(sel.getValue() == null){
	        		getMainWindow().showNotification("Select the Data Type for row - " +iid, 3);
	        		return;
	        	}
	        	
	        	GeneratorBean bean = new GeneratorBean();
	        	bean.setId(iid);
	        	bean.setColumnName(tf.getValue().toString());
	        	bean.setDataType(sel.getValue().toString());
	        	
	        	rowList.add(bean);
	        }
	        
        }
        if(rowList.size() == 0){
        	getMainWindow().showNotification("Provide at leat one column to generate data.", 2);
        	return;
        }
        
        //If EXCEL option is selected then no need to open Dialog box. Directly build the Excel sheet by using JExcel Api
        if(dataOption.equalsIgnoreCase("excel")){
        	log.debug("DataGenApplication - generateButtonClick() - Generating Excel Sheet");
        	Generator genrator = new ExcelDataGenerator();
        	genrator.generate(this, rowList);
        	return;
        }
        
    	// Create the result window...
        resultWindow = new ResultView(this, rowList);
        resultWindow.setModal(true);
        resultWindow.setResizable(false);
        resultWindow.setCaption("Generated Data");
        getMainWindow().addWindow(resultWindow);
        log.debug("DataGenApplication - generateButtonClick() end");
    }

    /*
     * This method is called when user click on AddRows button.
     * 
     */
    public void addRowButtonClick(ClickEvent event) {
    	log.debug("DataGenApplication - addRowButtonClick() start");
    	if(rowNum.getValue() == null || StringUtils.isNullOrEmpty(rowNum.getValue().toString())){
    		getMainWindow().showNotification("Number of rows field cannot be blank!", Notification.TYPE_ERROR_MESSAGE);
    		return;
    	}else if(!StringUtils.isNumber(rowNum.getValue().toString())){
    		getMainWindow().showNotification("Number of rows must be an Integer!", Notification.TYPE_ERROR_MESSAGE);
    		log.info("DataGenApplication - No. Of rows is Text");
    		return;
    	}
    	addRow(Integer.parseInt(rowNum.getValue().toString()));
    	log.debug("DataGenApplication - addRowButtonClick() end");
    }

    /*
     * This method will be called when some value changed in Data Type, 
     * Format Select and Generation Type group box.
     */
    public void valueChange(ValueChangeEvent event) {
    	log.debug("DataGenApplication - valueChange() start");
    	DataGenEventHandler handler = new DataGenEventHandler();
    	if(event.getProperty().getValue() == null){
    		handler.onChangeSelect(event);
        	return;
    	}
        if(event.getProperty().getValue().equals("CSV")){
        	log.info("DataGenApplication : valueChange() - CSV type selected");
        	generateTypeHl.removeComponent(xmlPanel);
        	generateTypeHl.removeComponent(sqlPanel);
        	generateTypeHl.addComponent(csvPanel);
        	executorTab.setEnabled(false);
        	explorerTab.setEnabled(false);
        }else if(event.getProperty().getValue().equals("XML")){
        	log.info("DataGenApplication : valueChange() - XML type selected");
        	generateTypeHl.removeComponent(csvPanel);
        	generateTypeHl.removeComponent(sqlPanel);
        	generateTypeHl.addComponent(xmlPanel);
        	executorTab.setEnabled(false);
        	explorerTab.setEnabled(false);
        }else if(event.getProperty().getValue().equals("Sql")){
        	log.info("DataGenApplication : valueChange() - SQL type selected");
        	generateTypeHl.removeComponent(xmlPanel);
        	generateTypeHl.removeComponent(csvPanel);
        	generateTypeHl.addComponent(sqlPanel);
        	executorTab.setEnabled(true);
        	explorerTab.setEnabled(true);
        }else if(event.getProperty().getValue().equals("Excel")){
        	log.info("DataGenApplication : valueChange() - Excel type selected");
        	generateTypeHl.removeComponent(xmlPanel);
        	generateTypeHl.removeComponent(sqlPanel);
        	generateTypeHl.removeComponent(csvPanel);
        	executorTab.setEnabled(false);
        	explorerTab.setEnabled(false);
        }else{
        	handler.onChangeSelect(event);
        }
        log.debug("DataGenApplication - valueChange() end");
    }
    
    /*
     * This method is responsible for adding a row in Generator grid.
     * 
     */
    public void addRow(int noOfRow) {
    	log.debug("DataGenApplication - addRow() start");
    	int maxSize = tableLength+noOfRow;
    	for (int i = tableLength; i <= maxSize; i++) {
    		log.info("DataGenApplication - addRow(): Adding row- "+i);
            Select dataType = new Select();
            dataType.addItem("Name");
            dataType.addItem("Title");
            dataType.addItem("Phone/Fax");
            dataType.addItem("Email");
            dataType.addItem("Date");
            dataType.addItem("Street Address");
            dataType.addItem("City");
            dataType.addItem("Postal/Zip");
            dataType.addItem("State/Provience/County");
            dataType.addItem("Country");
            dataType.addItem("Random Text");
            dataType.addItem("Fixed Text");
            dataType.addItem("Incremental Number");
            dataType.addItem("Number Range");
            dataType.addItem("Alphanumeric");
            dataType.addItem("Marital Status");
            dataType.addItem("Department Name");
            dataType.addItem("Company Name");
            dataType.addItem("Boolean Flag");
            dataType.addItem("Passport Number");
            dataType.setSizeFull();
            dataType.setImmediate(true);
            dataType.setData(i);
            dataType.setCaption("DataType");
            dataType.addListener(this);

            Select format = new Select();
            format.setData(i);
            format.setCaption("Format");
            format.setImmediate(true);
            format.setSizeFull();
            format.setEnabled(false);
            format.addListener(this);

            Label example = new Label("NA");
            example.setSizeFull();

            TextField field = new TextField("");
            
            HorizontalLayout addBar = new HorizontalLayout();
            addBar.setSizeFull();
            addBar.addComponent(new Label("NA"));
            
            listing.addItem(new Object[] {i, field, dataType, format, example, addBar}, i);
            tableLength = i;
    	}
    	log.debug("DataGenApplication - addRow() end");
    }
}

