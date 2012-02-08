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
public class DataGenExportUtility extends StreamResource {

	private static final Logger log = Logger.getLogger(DataGenExportUtility.class);
	private final String filename;
	private String contentType;

	public DataGenExportUtility(Application application, String fileName, String contentType, File tempFile) throws FileNotFoundException {
		super(new FileStreamResource(tempFile), fileName, application);
		this.filename = fileName;
		this.contentType = contentType;
	}

	public DownloadStream getStream() {
		log.debug("DataGenExportUtility - getStream() method start");
		DownloadStream stream = new DownloadStream(getStreamSource().getStream(), contentType, filename);
		stream.setParameter("Content-Disposition", "attachment;filename=" + filename);
		// This magic incantation should prevent anyone from caching the data
		stream.setParameter("Cache-Control", "private,no-cache,no-store");    
		// In theory <=0 disables caching. In practice Chrome, Safari (and, apparently, IE) all ignore <=0. Set to 1s 
		stream.setCacheTime(1000);
		log.debug("DataGenExportUtility - getStream() method end");
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
