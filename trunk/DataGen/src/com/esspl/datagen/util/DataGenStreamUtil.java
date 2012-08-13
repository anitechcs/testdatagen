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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;

/**
 * @author Tapas
 *
 */
@SuppressWarnings("serial")
public class DataGenStreamUtil extends StreamResource {

	private static final Logger log = Logger.getLogger(DataGenStreamUtil.class);
	
	private final String filename;
	private String contentType;

	public DataGenStreamUtil(Application application, String fileName, String contentType, File tempFile) throws FileNotFoundException {
		super(new FileStreamResource(tempFile), fileName, application);
		this.filename = fileName;
		this.contentType = contentType;
	}

	public DownloadStream getStream() {
		log.debug("DataGenStreamUtil - getStream() method start");
		DownloadStream stream = new DownloadStream(getStreamSource().getStream(), contentType, filename);
		stream.setParameter("Content-Disposition", "attachment;filename=" + filename);
		// This magic incantation should prevent anyone from caching the data
		stream.setParameter("Cache-Control", "private,no-cache,no-store");    
		// In theory <=0 disables caching. In practice Chrome, Safari (and, apparently, IE) all ignore <=0. Set to 1s 
		stream.setCacheTime(1000);
		log.debug("DataGenStreamUtil - getStream() method end");
		return stream;
	}

	private static class FileStreamResource implements StreamSource {
		private final InputStream inputStream;
		public FileStreamResource(File fileToDownload) throws FileNotFoundException {
			inputStream = new DeletingFileInputStream(fileToDownload);
		}

		public InputStream getStream() {
			return inputStream;
		}
	}
}
