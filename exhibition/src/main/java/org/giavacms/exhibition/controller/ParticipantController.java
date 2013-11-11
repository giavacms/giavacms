package org.giavacms.exhibition.controller;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.giavacms.base.common.util.ResourceUtils;
import org.giavacms.base.model.attachment.Image;
import org.giavacms.common.annotation.BackPage;
import org.giavacms.common.annotation.EditPage;
import org.giavacms.common.annotation.ListPage;
import org.giavacms.common.annotation.OwnRepository;
import org.giavacms.common.annotation.ViewPage;
import org.giavacms.common.controller.AbstractLazyController;
import org.giavacms.exhibition.model.Artist;
import org.giavacms.exhibition.model.Association;
import org.giavacms.exhibition.model.Center;
import org.giavacms.exhibition.model.Institute;
import org.giavacms.exhibition.model.Museum;
import org.giavacms.exhibition.model.Participant;
import org.giavacms.exhibition.model.Sponsor;
import org.giavacms.exhibition.model.Testimonial;
import org.giavacms.exhibition.producer.ExhibitionProducer;
import org.giavacms.exhibition.repository.ArtistRepository;
import org.giavacms.exhibition.repository.AssociationRepository;
import org.giavacms.exhibition.repository.CenterRepository;
import org.giavacms.exhibition.repository.InstituteRepository;
import org.giavacms.exhibition.repository.MuseumRepository;
import org.giavacms.exhibition.repository.ParticipantRepository;
import org.giavacms.exhibition.repository.TestimonialRepository;

@Named
@SessionScoped
public class ParticipantController extends AbstractLazyController<Participant> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------------------
	@BackPage
	public static String BACK = "/private/administration.xhtml";
	@ViewPage
	public static String VIEW = "/private/exhibition/participant/view.xhtml";
	@ListPage
	public static String LIST = "/private/exhibition/participant/list.xhtml";
	@EditPage
	public static String EDIT = "/private/exhibition/participant/edit.xhtml";

	public static String EDIT_IMAGE = "/private/exhibition/participant/edit-image.xhtml";

	public static String EDIT_NEW_SUBJECT = "/private/exhibition/participant/editNewSubject1.xhtml";

	// ------------------------------------------------

	@Inject
	@OwnRepository(ParticipantRepository.class)
	ParticipantRepository participantRepository;

	@Inject
	ArtistRepository artistRepository;

	@Inject
	InstituteRepository instituteRepository;

	@Inject
	MuseumRepository museumRepository;

	@Inject
	TestimonialRepository testimonialRepository;

	@Inject
	ExhibitionProducer exhibitionProducer;

	@Inject
	AssociationRepository associationRepository;

	@Inject
	CenterRepository centerRepository;

	@Override
	public void initController() {
	}

	@Override
	public Object getId(Participant t) {
		// TODO Auto-generated method stub
		return t.getId();
	}

	@Override
	public String update() {
		saveImage();
		return super.update();
	}

	public String updateAndModifyImage() {
		update();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	@Override
	public String save() {
		saveImage();
		return super.save();
	}

	public String saveAndModifyImage() {
		save();
		setEditMode(true);
		setReadOnlyMode(false);
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	@Override
	public String delete() {
		super.delete();
		return listPage();
	}

	public String deleteImg() {
		getElement().setImage(null);
		participantRepository.update(getElement());
		return listPage();
	}

	public String modImage() {
		// TODO Auto-generated method stub
		super.modElement();
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	public String modImageCurrent() {
		// TODO Auto-generated method stub
		super.modCurrent();
		return EDIT_IMAGE + REDIRECT_PARAM;
	}

	private void saveImage() {
		if (getElement().getNewImage().getUploadedData() != null
				&& getElement().getNewImage().getUploadedData().getContents() != null
				&& getElement().getNewImage().getUploadedData().getFileName() != null
				&& !getElement().getNewImage().getUploadedData().getFileName()
						.isEmpty()) {
			logger.info("carico nuova immagine: "
					+ getElement().getNewImage().getUploadedData()
							.getFileName());
			Image img = new Image();
			img.setData(getElement().getNewImage().getUploadedData()
					.getContents());
			img.setType(getElement().getNewImage().getUploadedData()
					.getContentType());
			String filename = ResourceUtils.createImage_("img", getElement()
					.getNewImage().getUploadedData().getFileName(),
					getElement().getNewImage().getUploadedData().getContents());
			img.setFilename(filename);
			getElement().setImage(img);
			getElement().setNewImage(null);
		} else {
			logger.info("non c'e' nuova immagine");
		}
	}

	public String addArtistElement() {
		addElement();
		getElement().getSubject().setType(Artist.TYPE);

		return editPage();
	}

	public String addAssociationElement() {
		addElement();
		getElement().getSubject().setType(Association.TYPE);

		return editPage();
	}

	public String addCenterElement() {
		addElement();
		getElement().getSubject().setType(Center.TYPE);

		return editPage();
	}

	public String addInstituteElement() {
		addElement();
		getElement().getSubject().setType(Institute.TYPE);
		return editPage();
	}

	public String addMuseumElement() {
		addElement();
		getElement().getSubject().setType(Museum.TYPE);
		return editPage();
	}

	public String addTestimonialElement() {
		addElement();
		getElement().getSubject().setType(Testimonial.TYPE);
		return editPage();
	}

	public String addSponsorElement() {
		addElement();
		getElement().getSubject().setType(Sponsor.TYPE);
		return editPage();
	}

	public String addNewSubject() {
		addElement();
		return EDIT_NEW_SUBJECT + REDIRECT_PARAM;
	}

	public String clone() {
		Participant t = (Participant) getModel().getRowData();
		t = getRepository().fetch(getId(t));
		addElement();
		getElement().setActive(true);
		getElement().setAddress(t.getAddress());
		getElement().setArtistName(t.getArtistName());
		getElement().setCity(t.getCity());
		getElement().setDate(t.getDate());
		getElement().setDescription(t.getDescription());
		getElement().setDimensions(t.getDimensions());
		getElement().setDiscipline(t.getDiscipline());
		getElement().setEmail(t.getEmail());
		getElement().setExternalImage(t.getExternalImage());
		getElement().setFacebookAccount(t.getFacebookAccount());
		getElement().setImage(new Image());
		getElement().getImage().setActive(true);
		getElement().getImage().setData(t.getImage().getData());
		getElement().getImage().setDescription(t.getImage().getDescription());
		getElement().getImage().setFilename(t.getImage().getFilename());
		getElement().getImage().setName(t.getImage().getName());
		getElement().getImage().setType(t.getImage().getType());
		getElement().setInstagramAccount(t.getInstagramAccount());
		getElement().setMaterial(t.getMaterial());
		getElement().setNewImage(t.getNewImage());
		getElement().setOeuvreTitle(t.getOeuvreTitle());
		getElement().setParticipationType(t.getParticipationType());
		getElement().setPhone(t.getPhone());
		getElement().setProvince(t.getProvince());
		getElement().setSubject(t.getSubject());
		getElement().setSummary(t.getSummary());
		getElement().setTwitterAccount(t.getTwitterAccount());
		getElement().setWebSite(t.getWebSite());
		return EDIT + REDIRECT_PARAM;

	}

	public String saveNewSubject() {
		if (Artist.TYPE.equals(getElement().getSubject().getType())) {
			Artist artist = new Artist();
			artist.setName(getElement().getSubject().getName());
			artist.setSurname(getElement().getSubject().getSurname());
			artist.setArtistName(getElement().getArtistName());
			artistRepository.persist(artist);
			exhibitionProducer.resetItemsForClass(Artist.class);
			addElement();
			getElement().setSubject(artist);
		} else if (Institute.TYPE.equals(getElement().getSubject().getType())) {
			Institute institute = new Institute();
			institute.setName(getElement().getSubject().getName());
			institute.setSurname(getElement().getSubject().getSurname());
			instituteRepository.persist(institute);
			exhibitionProducer.resetItemsForClass(Institute.class);

			getElement().setSubject(institute);
		} else if (Association.TYPE.equals(getElement().getSubject().getType())) {
			Association association = new Association();
			association.setName(getElement().getSubject().getName());
			association.setSurname(getElement().getSubject().getSurname());
			associationRepository.persist(association);
			exhibitionProducer.resetItemsForClass(Association.class);

			getElement().setSubject(association);
		} else if (Center.TYPE.equals(getElement().getSubject().getType())) {
			Center center = new Center();
			center.setName(getElement().getSubject().getName());
			center.setSurname(getElement().getSubject().getSurname());
			centerRepository.persist(center);
			exhibitionProducer.resetItemsForClass(Center.class);

			getElement().setSubject(center);
		} else if (Museum.TYPE.equals(getElement().getSubject().getType())) {
			Museum museum = new Museum();
			museum.setName(getElement().getSubject().getName());
			museum.setSurname(getElement().getSubject().getSurname());
			museumRepository.persist(museum);
			exhibitionProducer.resetItemsForClass(Museum.class);
			addElement();
			getElement().setSubject(museum);
		} else if (Testimonial.TYPE.equals(getElement().getSubject().getType())) {
			Testimonial testimonial = new Testimonial();
			testimonial.setName(getElement().getSubject().getName());
			testimonial.setSurname(getElement().getSubject().getSurname());
			testimonial.setArtistName(getElement().getArtistName());
			testimonialRepository.persist(testimonial);
			exhibitionProducer.resetItemsForClass(Testimonial.class);
			addElement();
			getElement().setSubject(testimonial);
		}
		return EDIT + REDIRECT_PARAM;
	}
}
