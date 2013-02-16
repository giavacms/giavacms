package org.giavacms.company.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.giavacms.base.model.attachment.Image;

@Entity
public class Company implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String spot;
	private String address;
	private String city;
	private String province;
	private String cap;
	private String telephone;
	private String fax;
	private String email;
	private String emailNewsletter;
	private String vat;
	private String lat;
	private String lon;
	private String preview;
	private String description;
	private String secondaryAddress;
	private String telephoneNumbers;
	private Image image;
	private Image newImage;
	private Image logo;
	private Image newLogo;
	private boolean active = true;
	private boolean principal;
	private String video;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "image_id", unique = true, nullable = true, insertable = true, updatable = true)
	public Image getImage() {
		if (image == null)
			this.image = new Image();
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Transient
	public Image getNewImage() {
		if (newImage == null)
			this.newImage = new Image();
		return newImage;
	}

	public void setNewImage(Image newImage) {
		this.newImage = newImage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getSecondaryAddress() {
		return secondaryAddress;
	}

	public void setSecondaryAddress(String secondaryAddress) {
		this.secondaryAddress = secondaryAddress;
	}

	public String getTelephoneNumbers() {
		return telephoneNumbers;
	}

	public void setTelephoneNumbers(String telephoneNumbers) {
		this.telephoneNumbers = telephoneNumbers;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	@Lob
	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "logo_id", unique = true, nullable = true, insertable = true, updatable = true)
	public Image getLogo() {
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}

	@Transient
	public Image getNewLogo() {
		if (newLogo == null)
			this.newLogo = new Image();
		return newLogo;
	}

	public void setNewLogo(Image newLogo) {
		this.newLogo = newLogo;
	}

   public String getEmailNewsletter()
   {
      return emailNewsletter;
   }

   public void setEmailNewsletter(String emailNewsletter)
   {
      this.emailNewsletter = emailNewsletter;
   }

}
