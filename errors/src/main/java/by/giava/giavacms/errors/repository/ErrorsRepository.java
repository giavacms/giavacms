package by.giava.giavacms.errors.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import by.giava.common.repository.AbstractRepository;
import by.giava.giavacms.base.common.util.FileUtils;
import by.giava.giavacms.errors.model.Errors;

@Named
@Stateless
@LocalBean
public class ErrorsRepository extends AbstractRepository<Errors> {

	private static final long serialVersionUID = 1L;

	private static final String TYPE_PATH = "errors";

	@PersistenceContext
	EntityManager em;

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
	public Errors fetch(Object key) {
		try {
			Errors error = find(key);
			File f = new File(FileUtils.getRealPath() + TYPE_PATH
					+ File.separator + error.getPage());
			if (f.exists() && f.isDirectory())
				throw new Exception("file could not be accessed!");
			if (!f.exists()) {
				error.setPageContent("<html><body>Errore</body></html>");
				return error;
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
				error.setPageContent(sb.toString());
				return error;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean update(Errors error) {
		try {
			File f = new File(FileUtils.getRealPath() + TYPE_PATH
					+ File.separator + error.getPage());
			if (f.exists() && f.isDirectory())
				throw new Exception("file could not be written!");
			if (f.exists())
				f.delete();
			f = new File(FileUtils.getRealPath() + TYPE_PATH + File.separator
					+ error.getPage());
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(error.getPageContent().getBytes());
			fos.close();
			return super.update(error);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}

}
