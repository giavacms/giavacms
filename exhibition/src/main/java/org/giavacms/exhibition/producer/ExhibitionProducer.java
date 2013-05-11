package org.giavacms.exhibition.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.common.model.Search;
import org.giavacms.exhibition.enums.ParticipationType;
import org.giavacms.exhibition.model.Artist;
import org.giavacms.exhibition.model.Association;
import org.giavacms.exhibition.model.Center;
import org.giavacms.exhibition.model.Discipline;
import org.giavacms.exhibition.model.Exhibition;
import org.giavacms.exhibition.model.Institute;
import org.giavacms.exhibition.model.Museum;
import org.giavacms.exhibition.model.Sponsor;
import org.giavacms.exhibition.model.Subject;
import org.giavacms.exhibition.model.Testimonial;
import org.giavacms.exhibition.repository.ArtistRepository;
import org.giavacms.exhibition.repository.AssociationRepository;
import org.giavacms.exhibition.repository.CenterRepository;
import org.giavacms.exhibition.repository.DisciplineRepository;
import org.giavacms.exhibition.repository.ExhibitionRepository;
import org.giavacms.exhibition.repository.InstituteRepository;
import org.giavacms.exhibition.repository.MuseumRepository;
import org.giavacms.exhibition.repository.SponsorRepository;
import org.giavacms.exhibition.repository.TestimonialRepository;
import org.jboss.logging.Logger;

@SessionScoped
@Named
public class ExhibitionProducer implements Serializable {

	Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Inject
	DisciplineRepository disciplineRepository;

	@Inject
	ExhibitionRepository exhibitionRepository;

	@Inject
	ArtistRepository artistRepository;

	@Inject
	InstituteRepository instituteRepository;

	@Inject
	MuseumRepository museumRepository;

	@Inject
	TestimonialRepository testimonialRepository;

	@Inject
	SponsorRepository sponsorRepository;

	@Inject
	AssociationRepository associationRepository;

	@Inject
	CenterRepository centerRepository;

	@SuppressWarnings("rawtypes")
	private Map<Class, SelectItem[]> items = null;

	public ExhibitionProducer() {
		// TODO Auto-generated constructor stub
	}

	@Produces
	@Named
	public SelectItem[] getDisciplineItems() {
		if (items.get(Discipline.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona disciplina..."));
			for (Discipline t : disciplineRepository.getList(
					new Search<Discipline>(Discipline.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getName()));
			}
			items.put(Discipline.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Discipline.class);
	}

	@Produces
	@Named
	public SelectItem[] getParticipationTypeItems() {
		if (items.get(ParticipationType.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona partecipazione..."));
			for (ParticipationType t : ParticipationType.values()) {
				valori.add(new SelectItem(t, t.name()));
			}
			items.put(ParticipationType.class,
					valori.toArray(new SelectItem[] {}));
		}
		return items.get(ParticipationType.class);
	}

	@Produces
	@Named
	public SelectItem[] getExhibitionItems() {
		if (items.get(Exhibition.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona esibizione..."));
			for (Exhibition t : exhibitionRepository.getList(
					new Search<Exhibition>(Exhibition.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getName()));
			}
			items.put(Exhibition.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Exhibition.class);
	}

	@Produces
	@Named
	public SelectItem[] getArtistItems() {
		if (items.get(Artist.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona artista..."));
			for (Artist t : artistRepository.getList(new Search<Artist>(
					Artist.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Artist.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Artist.class);
	}

	@Produces
	@Named
	public SelectItem[] getInstituteItems() {
		if (items.get(Institute.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona istituti..."));
			for (Institute t : instituteRepository.getList(
					new Search<Institute>(Institute.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Institute.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Institute.class);
	}

	@Produces
	@Named
	public SelectItem[] getAssociationItems() {
		if (items.get(Association.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona associazione..."));
			for (Association t : associationRepository.getList(
					new Search<Association>(Association.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Association.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Association.class);
	}

	@Produces
	@Named
	public SelectItem[] getCenterItems() {
		if (items.get(Center.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona centri..."));
			for (Center t : centerRepository.getList(new Search<Center>(
					Center.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Center.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Center.class);
	}

	@Produces
	@Named
	public SelectItem[] getMuseumItems() {
		if (items.get(Museum.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona museo..."));
			for (Museum t : museumRepository.getList(new Search<Museum>(
					Museum.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Museum.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Museum.class);
	}

	@Produces
	@Named
	public SelectItem[] getTestimonialItems() {
		if (items.get(Testimonial.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona testimonial..."));
			for (Testimonial t : testimonialRepository.getList(
					new Search<Testimonial>(Testimonial.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Testimonial.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Testimonial.class);
	}

	@Produces
	@Named
	public SelectItem[] getSponsorItems() {
		if (items.get(Sponsor.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona sponsor..."));
			for (Sponsor t : sponsorRepository.getList(new Search<Sponsor>(
					Sponsor.class), 0, 0)) {
				valori.add(new SelectItem(t.getId(), t.getNameSurname()));
			}
			items.put(Sponsor.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Sponsor.class);
	}

	@Produces
	@Named
	public SelectItem[] getSubjectTypeItems() {
		if (items.get(Subject.class) == null) {
			List<SelectItem> valori = new ArrayList<SelectItem>();
			valori.add(new SelectItem(null, "seleziona tipo soggetto..."));
			valori.add(new SelectItem(Artist.TYPE, "artista"));
			valori.add(new SelectItem(Association.TYPE, "associazione"));
			valori.add(new SelectItem(Center.TYPE, "centro"));
			valori.add(new SelectItem(Institute.TYPE, "istituto"));
			valori.add(new SelectItem(Museum.TYPE, "museo"));
			valori.add(new SelectItem(Testimonial.TYPE, "testimonial"));
			valori.add(new SelectItem(Sponsor.TYPE, "sponsor"));
			items.put(Subject.class, valori.toArray(new SelectItem[] {}));
		}
		return items.get(Subject.class);
	}

	public void resetItemsForClass(Class clazz) {
		if (items.containsKey(clazz)) {
			items.remove(clazz);
		}
	}

	// ==============================================================================

	@PostConstruct
	public void reset() {
		items = new HashMap<Class, SelectItem[]>();
	}

}
