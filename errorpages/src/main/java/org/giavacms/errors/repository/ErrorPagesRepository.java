package org.giavacms.errors.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.giavacms.base.common.util.FileUtils;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.errors.model.type.ErrorPages;

@Named
@Stateless
@LocalBean
public class ErrorPagesRepository extends AbstractRepository<ErrorPages> {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_PATH = "errors";

	EntityManager em = null;

	@Override
	public EntityManager getEm() {
		return em;
	}

	@Override
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	protected String getDefaultOrderBy() {
		return "id";
	}

	@Override
	public List<ErrorPages> getAllList() {
		List<ErrorPages> errorPages = new ArrayList<ErrorPages>();
		for (ErrorPages knownError : ErrorPages.values()) {
			errorPages.add(knownError);
		}
		return errorPages;
	}

	@Override
	public ErrorPages fetch(Object key) {
		try {
			ErrorPages knownError = ErrorPages.valueOf((String) key);
			File f = new File(FileUtils.getRealPath() + TYPE_PATH
					+ File.separator + knownError.name() + ".html");
			if (f.exists() && f.isDirectory())
				throw new Exception("file could not be accessed!");
			if (!f.exists()) {
				knownError.setPageContent("<html><body>Errore</body></html>");
				return knownError;
			}
			StringBuffer sb = new StringBuffer();
			Scanner scanner = null;
			boolean failure = false;
			try {
				scanner = new Scanner(f);
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine());
				}
			} catch (Exception e) {
				e.printStackTrace();
				failure = true;
			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
			if (failure) {
				return null;
			} else {
				knownError.setPageContent(sb.toString());
				return knownError;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean update(ErrorPages knownError) {
		try {
			File f = new File(FileUtils.getRealPath() + TYPE_PATH
					+ File.separator + knownError.name() + ".html");
			if (f.exists() && f.isDirectory())
				throw new Exception("file could not be written!");
			if (f.exists())
				f.delete();
			f = new File(FileUtils.getRealPath() + TYPE_PATH + File.separator
					+ knownError.name() + ".html");
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(knownError.getPageContent().getBytes());
			fos.close();
			return true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}

}
