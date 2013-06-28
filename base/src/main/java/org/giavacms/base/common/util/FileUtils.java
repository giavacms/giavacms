/*
 * Copyright 2013 GiavaCms.org.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.giavacms.base.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;

import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jboss.logging.Logger;

public class FileUtils {

	protected static Logger logger = Logger
			.getLogger(FileUtils.class.getName());

	static String beginRegExpr = "/(\\.|\\/)(";
	static String separatorRegExpr = "|";
	static String endRegExpr = ")$/";

	static String[] imgRegExpr = new String[] { "gif", "jpe?g", "png" };
	static String[] docRegExpr = new String[] { "pdf", "doc", "docx", "xls",
			"xlsx" };
	static String cssRegExpr = "css";
	static String jsRegExpr = "js";
	static String swfRegExpr = "swf";
	static String[] fontRegExpr = new String[] { "otf", "eot", "svg", "ttf",
			"woff" };

	public static String DOCS = "docs";
	public static String IMG = "img";
	public static String CSS = "css";
	public static String JS = "js";
	public static String SWF = "swf";
	public static String FONT = "font";
	public static String ALL = "ALL";

	static String[] docsExtensions = new String[] { "pdf", "PDF", "doc", "DOC",
			"docx", "DOCX", "xls", "XLS", "xlsx", "XLSX", "p7m", "P7M", "txt" };
	static String[] imgExtensions = new String[] { "gif", "GIF", "jpg", "JPG",
			"jpeg", "JPEG", "png", "PNG" };
	static String[] cssExtensions = new String[] { "css", "CSS" };
	static String[] jsExtensions = new String[] { "js", "JS" };
	static String[] swfExtensions = new String[] { "swf", "SWF" };
	static String[] fontExtensions = new String[] { "otf", "OTF", "eot", "EOT",
			"svg", "SVG", "ttf", "TTF", "woff", "WOFF" };

	public static String getRegExpByTypes(String[] types) {
		StringBuffer result = new StringBuffer(separatorRegExpr);
		for (String type : types) {
			if (type.equals(IMG) || type.equals(ALL)) {
				for (String img : imgRegExpr) {
					result.append(separatorRegExpr + img);
				}
			} else if (type.equals(DOCS) || type.equals(ALL)) {
				for (String doc : docsExtensions) {
					result.append(separatorRegExpr + doc);
				}
			} else if (type.equals(CSS) || type.equals(ALL)) {
				result.append(separatorRegExpr + cssRegExpr);
			} else if (type.equals(JS) || type.equals(ALL)) {
				result.append(separatorRegExpr + jsRegExpr);
			} else if (type.equals(SWF) || type.equals(ALL)) {
				result.append(separatorRegExpr + swfRegExpr);
			} else if (type.equals(FONT) || type.equals(ALL)) {
				for (String font : fontRegExpr) {
					result.append(separatorRegExpr + font);
				}
			}
		}
		if (result.length() > separatorRegExpr.length()) {
			return beginRegExpr
					+ (result.toString().substring(1).concat(endRegExpr));
		}
		logger.info("nessun tipo corrispondente");
		return "";
	}

	public static List<String> getFilesName(String directory,
			String[] extensions) {
		File rootDir = new File(getRealPath() + directory);
		IOFileFilter filesFilter = new SuffixFileFilter(extensions);
		IOFileFilter notDirectory = new NotFileFilter(
				DirectoryFileFilter.INSTANCE);
		FilenameFilter fileFilter = new AndFileFilter(filesFilter, notDirectory);
		String[] resultFiles = rootDir.list(fileFilter);
		Arrays.sort(resultFiles);
		if (resultFiles.length > 0) {
			return Arrays.asList(resultFiles);
		}
		return new ArrayList<String>();
	}

	public static String getType(String fileName) {
		String type = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (Arrays.asList(docsExtensions).contains(type)) {
			return DOCS;
		}
		if (Arrays.asList(imgExtensions).contains(type)) {
			return IMG;
		}
		if (Arrays.asList(cssExtensions).contains(type)) {
			return CSS;
		}
		if (Arrays.asList(jsExtensions).contains(type)) {
			return JS;
		}
		if (Arrays.asList(swfExtensions).contains(type)) {
			return SWF;
		}
		if (Arrays.asList(fontExtensions).contains(type)) {
			return FONT;
		}
		return null;
	}

	// pdf, p7m, doc, docx, xls, xlsx
	public static List<String> getPdfFiles() {
		return getFilesName(DOCS, docsExtensions);
	}

	public static List<String> getCssFiles() {
		return getFilesName(CSS, cssExtensions);
	}

	public static List<String> getJsFiles() {
		return getFilesName(JS, jsExtensions);
	}

	public static List<String> getImgFiles() {
		return getFilesName(IMG, imgExtensions);
	}

	public static List<String> getFlashFiles() {
		return getFilesName(SWF, swfExtensions);
	}

	public static List<String> getFontFiles() {
		return getFilesName(FONT, fontExtensions);
	}

	public static String createImage_(String folder, String imageFileName,
			byte[] data) {
		try {
			String actualFileName = getUniqueName(getRealPath() + folder,
					imageFileName);
			FileImageOutputStream imageOutput;
			imageOutput = new FileImageOutputStream(new File(actualFileName));
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
			return actualFileName.substring(actualFileName
					.lastIndexOf(File.separator) + 1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String createFile_(String folder, String fileName, byte[] data) {
		try {
			String actualFileName = getUniqueName(getRealPath() + folder,
					fileName);
			FileOutputStream fos = new FileOutputStream(
					new File(actualFileName));
			fos.write(data);
			fos.close();
			return actualFileName.substring(actualFileName
					.lastIndexOf(File.separator) + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getUniqueName(String folder, String fileName) {
		String est = fileName.substring(fileName.indexOf(".") + 1);
		String nome = fileName.substring(0, fileName.indexOf(".")).replaceAll(
				" ", "");
		String finalName = fileName.replaceAll(" ", "");
		boolean trovato = false;
		int i = 0;
		while (!trovato) {
			logger.info("finalName: " + finalName);
			File file = new File(folder + File.separator + finalName);
			logger.info("found: " + finalName);
			if (file != null && file.exists()) {
				i++;
				finalName = nome + "_" + i + "." + est;
			} else {
				trovato = true;
				return folder + File.separator + finalName;
			}
		}
		return folder + File.separator + finalName;
	}

	public static String getRealPath() {
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		String folder = servletContext.getRealPath("") + File.separator;
		return folder;
	}

	public static String getFileContent(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}
}
