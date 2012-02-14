package com.esspl.datagen.util;

import com.vaadin.terminal.ThemeResource;


/**
 * @author Tapas
 *
 * This class defines all the constants used in whole application.
 * 
 */
public class DataGenConstant {

	//Formats
	public static final String[] NAME_FORMATS = {"First_Name Last_Name", "First_Name", "Sur_Name Last_Name", "Sur_Name First_Name", "Sur_Name First_Name Last_Name"};
	
	public static final String[] DATE_FORMATS = {"MM/dd/yyyy", "dd/MM/yyyy", "yyyy-MM-dd", "MMM dd, yyyy", "dd MMM, yy", "yyyy-MM-dd HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "MM.dd.yyyy", "dd.MM.yyyy"};
	
	public static final String[] PHONE_FORMATS = {"India", "USA/Canada", "UK"};
	
	public static final String[] ZIP_FORMATS = {"India", "Canada", "USA"};
	
	public static final String[] STATE_FORMATS = {"Full", "Sort"};
	
	//Icons for the tabsheet
	public static final ThemeResource HOME_ICON = new ThemeResource("images/home.png");
	public static final ThemeResource HELP_ICON = new ThemeResource("images/help.png");
	public static final ThemeResource ABOUT_ICON = new ThemeResource("images/AboutUs.png");
	public static final ThemeResource EXECUTOR_ICON = new ThemeResource("images/Sql-runner.png");
	
	//Logo
	public static final ThemeResource LOGO = new ThemeResource("images/logo.jpg");
	public static final ThemeResource RESET = new ThemeResource("images/reload.gif");
	public static final ThemeResource ADD = new ThemeResource("images/add.png");
	public static final ThemeResource VIEW = new ThemeResource("images/view.gif");
	public static final ThemeResource CLOSE_ICON = new ThemeResource("images/red-cross.gif");
	public static final ThemeResource CLEAR_ICON = new ThemeResource("images/cross.png");
	public static final ThemeResource EXPORT_ICON = new ThemeResource("images/export.png");
	public static final ThemeResource COPY_ICON = new ThemeResource("images/copy.jpg");
	public static final ThemeResource SETTING_ICON = new ThemeResource("images/setting.png");
	public static final ThemeResource ACCEPT_ICON = new ThemeResource("images/accept.png");
	public static final ThemeResource HELP_SCREEN = new ThemeResource("images/DG.png");
	
	//Help Content
	public static final String HELP_CONTENT = "<h1>How to Create Test Data using DATA Gen™</h1>"
	+ "<p>In the 1st section [Screenshot below] user has to select the Sql option. This would select the Generation Type. According to this selection the Options are decided for the different outputs.</p>"
	+ "<p>The 2nd section is SQL Option here the user can select the data base type for which the data is to be generated. This can be done by selecting the dropdown having the label Data Base. Then user can set the table name for which the data is to be generated. There is a check box [Include CREATE TABLE] by selecting this option the Create statements will be added to the Generated data. So that when user runs the script the table gets created first and then the data is inserted into it.</p>"
	+ "<p>The 3rd section has two fields “Number of Results” & “Add”.  ‘Number of Results’ field decides how many rows of data are to be generated. ‘Add’ field has a text box and a +Row(s) button, here user can increase the number of columns that is need for data generation. User will be able to add a maximum of 99 rows and a minimum of 1 row. There can be more than 500+ rows added to one Data Generation.</p>"
	+ "<p>The 4th Section is where the details of the columns are to be set. It has multiple sections where user can set the Column Name, Data Type, Data Type, Format and Additional Data. There is another column as Examples which shows the user what type of data is to set, this would be a dummy data. The default is 5 rows.</p>"
	+ "<p>Then there is a Generate button on click there will be a Popup which will show the generated data and have controls to copy the data, save the generated data and load in Executor.</p>";
	
	public static final String HELP_CONTENT_STEPS = "<h1>1. Generation Type: SQL</h1>"
	+ "<p>STEP 1: In the first section user has to select the Sql option.</p>"
	+ "<p>STEP 2: User selects the Database to ORALCE and types a Table name as EMP.  The field include CREATE TABLE query is also selected.</p>"
	+ "<p>STEP 3: Number of rows is set as 10. In the Add section user Enters 2 and clicks on the +Row(s) button, this adds 2 more rows for data generation.</p>"
	+ "<p>STEP 4: Set the data in the column detail section.</p>"
	+ "<p>STEP 5: Click on the Generate button.</p>"
	+ "<p>STEP 6: Click on the Excecute button on Result window.</p>"
	+ "<p>STEP 7: Give the Database credentials in Setting section and execute sql statements in Executor tab.</p>"
	+ "<p>It’s done. You have now successfully created test data which is ready for use.</p>"
	+ "<h1>2. Generation Type: Excel</h1>"
	+ "<p>STEP 1: In the first section user has to select the Excel option.</p>"
	+ "<p>STEP 2: Number of rows is set as 10. In the Add section user Enters 2 and clicks on the +Row(s) button, this adds 2 more rows for data generation.</p>"
	+ "<p>STEP 3: Set the data in the column detail section.</p>"
	+ "<p>STEP 4: Click on the Generate button.</p>"
	+ "<p>STEP 5: Then Save/Open popup will open. Extension of the file would be *.xls.</p>"
	+ "<p>It’s done. You have now successfully created test data which is ready for use.</p>"
	+ "<h1>3. Generation Type: XML</h1>"
	+ "<p>STEP 1: In the first section user has to select the XML option.</p>"
	+ "<p>STEP 2: User selects the Root Node Name to PARENT and Record Node Name to CHILD.</p>"
	+ "<p>STEP 3: Number of rows is set as 10. In the Add section user Enters 2 and clicks on the +Row(s) button, this adds 2 more rows for data generation.</p>"
	+ "<p>STEP 4: Set the data in the column detail section.</p>"
	+ "<p>STEP 5: Click on the Generate button.</p>"
	+ "<p>STEP 6: Click on the Export to File button on the Result window. Extension of the file would be *.xml.</p>"
	+ "<p>It’s done. You have now successfully created test data which is ready for use.</p>"
	+ "<h1>4. Generation Type: CSV</h1>"
	+ "<p>STEP 1: In the first section user has to select the CSV option.</p>"
	+ "<p>STEP 2: User sets the delimiter that is to be used in separation of the data in the Delimiter Character(s) field.</p>"
	+ "<p>STEP 3: Number of rows is set as 10. In the Add section user Enters 2 and clicks on the +Row(s) button, this adds 2 more rows for data generation.</p>"
	+ "<p>STEP 4: Set the data in the column detail section.</p>"
	+ "<p>STEP 5: Click on the Generate button.</p>"
	+ "<p>STEP 6: Click on the Export to File button on the Result window. Extension of the file would be *.csv.</p>"
	+ "<p>It’s done. You have now successfully created test data which is ready for use.</p>";
	
	//About Content
	public static final String ABOUT_CONTENT = "<h1>What is DataGen?</h1>"
	+ "<p>There has always been this gap in generation of custom formatted test data or sample data. That’s the reason we have come up with this idea of Data Generator. A Free in house tool which is written in <b>JAVA [Rich user interface Frame work - VAADIN]</b> that can fill the gap in generation of data in a variety of formats which can be used in making dummy data bases & generation of Test Data. We provide a online demo for the creation of the data which can be found on the Help tab.</p>"
	+ "<h1>What Wiki has to say?</h1>"
	+ "<p><i>\"Test Data Generation, an important part of software testing, is the process of creating a set of data for testing the adequacy of new or revised software applications. It may be the actual data that has been taken from previous operations or artificial data created for this purpose. Test Data Generation is seen to be a complex problem and though a lot of solutions have come forth most of them are limited to toy programs. The use of dynamic memory allocation in most of the code written in industry is the most severe problem that the Test Data Generators face as the usage of the software then becomes highly unpredictable, due to this it becomes harder to anticipate the paths that the program could take making it nearly impossible for the Test Data Generators to generate exhaustive Test Data. However, in the past decade significant progress has been made in tackling this problem better by the use of genetic algorithms and other analysis algorithms. Moreover, Software Testing is an important part of the Software Development Life Cycle and is basically labour intensive. It also accounts for nearly third of the cost of the system development. In this view the problem of generating quality test data quickly, efficiently and accurately is seen to be important.\"</i></p>"
	+ "<h1>Why Do I need DataGen?</h1>"
	+ "<p>Manually creating test data is a very time consuming task and prone to human error. You can save a lot of time by using DataGen to generate realistic test data. DataGen can populate your entire database with a few clicks of the mouse.</p>"
	+ "<p>It can often take longer to create data to test a program than to write the code. It is extremely time consuming to make sure that it has the correct format and all the foreign keys are resolved. DataGen can populate every table with default data values and make sure that all foreign keys have a matching parent identifier.</p>"
	+ "<h1>Features :</h1>"
	+ "<p><ul><li>Browser-friendly (JS-enabled), its browser independent.</li></p>"
	+ "<p><li>Realistic data is provided for use.</li></p>"
	+ "<p><li>Many data types available: names, phone numbers, email addresses, cities, states, provinces, counties, dates, street addresses, number ranges, alphanumeric strings and more.</li></p>"
	+ "<p><li>Customized formats for different data types like Date, Name, Phone, etc.</li></p>"
	+ "<p><li>Option to generate data in XML, Excel, HTML, CSV or SQL.</li></p>"
	+ "<p><li>Country specific data (state / province / county) for Canada, US and UK.</li></p>"
	+ "<p><li>User can generate up to 10000 rows of data at a single click.</li></p>"
	+ "<p><li>Export your datas to file for later use.</li></p>"
	+ "<p><li>Instant Script Runner provided to excute the SQL scripts without using Toad.</li></ul></p>"
	+ "<h1>Team Members :</h1>"
	+ "<p><ul><li>Venugopal Prabhoo : venugopal@esspl.com</li>"
	+ "<li>Santosh D. Kiran &nbsp;&nbsp;&nbsp;: santosh@esspl.com</li>"
	+ "<li>Tapas Kumar Jena	&nbsp;: tapasj@esspl.com</li>"
	+ "<li>Anshuman Nayak &nbsp;&nbsp;&nbsp;: anshuman@esspl.com</li>"
	+ "<li>Basudha Das	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;: basudha@esspl.com</li></ul></p>"
	+ "<p><b>We would welcome feedbacks and suggestion from you.</b></p>";
}
