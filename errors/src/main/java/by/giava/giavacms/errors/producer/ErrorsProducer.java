package by.giava.giavacms.errors.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.logging.Logger;

import by.giava.giavacms.errors.model.Errors;
import by.giava.giavacms.errors.repository.ErrorsRepository;

@Singleton
public class ErrorsProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	private List<Errors> allErrors = new ArrayList<Errors>();

	@Inject
	private ErrorsRepository errorRepository;

	@Produces
	@Named
	public List<Errors> getErrors() {
		return allErrors;
	}

	@PostConstruct
	public void reset() {
		// svuoto la lista e la riempio senza riassegnala, in modo che chi se
		// l'è già injettata continui a riferirla per riferimento
		List<Errors> oldErrors = new ArrayList<Errors>();
		for (Errors error : allErrors) {
			oldErrors.add(error);
		}
		for (Errors error : oldErrors) {
			allErrors.remove(error);
		}
		List<Errors> errors = errorRepository.getAllList();
		if (errors != null) {
			for (Errors error : errors) {
				allErrors.add(error);
			}
		}
	}
}
