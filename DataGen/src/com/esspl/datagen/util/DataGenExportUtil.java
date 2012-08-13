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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Tapas
 * 
 * Utility class for Export feature
 *
 */
public class DataGenExportUtil {
	
	private static final Logger log = Logger.getLogger(DataGenExportUtil.class);
	
	public void doExport(String argOption){
		log.debug("DataGenExportUtil--->doExport() Called.");
		
		//Export stuff goes here
		
		if(!StringUtils.isBlank(argOption)){
			if(argOption.equalsIgnoreCase("sql")){
				doSqlExport();
			}else {
				doExcelExport();
			}
		}else{
			log.error("DataGenExportUtil--->doExport()----- :  argOption is blank!");
		}
	}
	
	public void doSqlExport(){
		log.debug("DataGenExportUtil--->doSqlExport() Called.");
		
		//sql stuff here
		
	}
	
	public void doExcelExport(){
		log.debug("DataGenExportUtil--->doExcelExport() Called.");
		
		//Excel stuff here
		
	}
	
}
