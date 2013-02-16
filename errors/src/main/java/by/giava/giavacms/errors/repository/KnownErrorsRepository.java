package by.giava.giavacms.errors.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;

import by.giava.common.repository.AbstractRepository;
import by.giava.giavacms.base.common.util.FileUtils;
import by.giava.giavacms.errors.model.type.KnownErrors;

@Named
@Stateless
@LocalBean
public class KnownErrorsRepository extends AbstractRepository<KnownErrors> {

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
	public List<KnownErrors> getAllList() {
		List<KnownErrors> knownErrors = new ArrayList<KnownErrors>();
		for (KnownErrors knownError : KnownErrors.values()) {
			knownErrors.add(knownError);
		}
		return knownErrors;
	}

	@Override
	public KnownErrors fetch(Object key) {
		try {
			KnownErrors knownError = KnownErrors.valueOf((String) key);
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
	public boolean update(KnownErrors knownError) {
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
