package com.esspl.datagen.generator.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.esspl.datagen.DataGenApplication;
import com.esspl.datagen.GeneratorBean;
import com.esspl.datagen.generator.Generator;

/**
 * @author tapasj
 *
 */
public class ExcelDataGenerator implements Generator{

	private static final Logger log = Logger.getLogger(ExcelDataGenerator.class);
	
	@Override
	public String generate(DataGenApplication dataGenApplication, ArrayList<GeneratorBean> rowList) {
		log.debug("ExcelDataGenerator - generate() method start");
		// TODO Auto-generated method stub
		return "This Feature is not implemented Yet.";
	}

}
