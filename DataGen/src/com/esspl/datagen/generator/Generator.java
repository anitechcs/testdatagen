/**
 * 
 */
package com.esspl.datagen.generator;

import java.util.ArrayList;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.GeneratorBean;

/**
 * @author tapasj
 *
 */
public interface Generator {
	
	/**
	 * Uses Command Design pattern. Concrete implementation should provide the logic for 
	 * specific data generation. e.g. sql, xml, excel.
	 */
	public String generate(DataGenApplication dataGenApplication, ArrayList<GeneratorBean> rowList);
	
}
