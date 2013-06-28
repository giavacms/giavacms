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

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.common.annotation.LogOperation;
import org.giavacms.common.repository.AbstractRepository;
import org.giavacms.errors.model.ErrorPage;
import org.giavacms.errors.model.type.HttpError;

@Named
@Stateless
@LocalBean
public class ErrorPageRepository extends AbstractRepository<ErrorPage> {

	private static final long serialVersionUID = 1L;

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
	public List<ErrorPage> getAllList() {
		List<ErrorPage> ErrorPage = new ArrayList<ErrorPage>();
		for (HttpError httpError : HttpError.values()) {
			ErrorPage.add(new ErrorPage(httpError));
		}
		return ErrorPage;
	}

	@Override
	public ErrorPage fetch(Object key) {
		try {
			ErrorPage p = new ErrorPage();
			HttpError httpError = HttpError.valueOf((String) key);
			File f = new File(ResourceUtils.getRealPath() + p.getPath()
					+ File.separator + httpError.name() + ".html");
			if (f.exists() && f.isDirectory())
				throw new Exception("file could not be accessed!");
			if (!f.exists()) {
				p.setHttpError(httpError);
				p.setPageContent(httpError.getDefaultContent());
				return persist(p);
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
				p.setHttpError(httpError);
				p.setPageContent(sb.toString());
				return p;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@LogOperation
	public ErrorPage persist(ErrorPage object) {
		// TODO Auto-generated method stub
		update(object);
		return object;
	}

	@Override
	public boolean update(ErrorPage errorPage) {
		try {
			File d = new File(ResourceUtils.getRealPath() + errorPage.getPath());
			if (!d.exists()) {
				if (!d.mkdir()) {
					throw new Exception("errors creating path!");
				}
			} else if (d.exists() && !d.isDirectory()) {
				throw new Exception("path could not be created!");
			}
			File f = new File(ResourceUtils.getRealPath() + errorPage.getPath()
					+ File.separator + errorPage.getHttpError().name()
					+ ".html");
			if (f.exists() && f.isDirectory()) {
				throw new Exception("file could not be written!");
			} else if (f.exists()) {
				f.delete();
			}
			f = new File(ResourceUtils.getRealPath() + errorPage.getPath()
					+ File.separator + errorPage.getHttpError().name()
					+ ".html");
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(errorPage.getPageContent().getBytes());
			fos.close();
			return true;
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}

}
