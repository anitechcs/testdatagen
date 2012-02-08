package com.esspl.datagen.util;

import com.vaadin.terminal.ThemeResource;


/**
 * @author tapasj
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
	public static final ThemeResource RELOAD = new ThemeResource("images/reload.gif");
	public static final ThemeResource ADD = new ThemeResource("images/add.png");
	public static final ThemeResource VIEW = new ThemeResource("images/view.gif");
	public static final ThemeResource CLOSE_ICON = new ThemeResource("images/cross.png");
	public static final ThemeResource EXPORT_ICON = new ThemeResource("images/export.png");
	public static final ThemeResource COPY_ICON = new ThemeResource("images/copy.jpg");
	
	//Help Content
	public static final String HELP_CONTENT = "<h1>Test Data Generator Documentation</h1>"
	+ "<p>The DataGen is a tool which allows you to easily generate test data. It was primarily written for populating database for dev or test environments by providing values for names, addresses, email addresses, phone numbers, text, and dates.</p>"
	+ "<p>Read More....Coming soon</p>";
	
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
