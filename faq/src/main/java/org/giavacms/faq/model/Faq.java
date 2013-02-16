package org.giavacms.faq.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Faq implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String question;
	private String answer;
	private FaqCategory faqCategory;
	private Date date;
	private boolean active = true;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	public FaqCategory getFaqCategory() {
		if (this.faqCategory == null)
			this.faqCategory = new FaqCategory();
		return faqCategory;
	}

	public void setFaqCategory(FaqCategory faqCategory) {
		this.faqCategory = faqCategory;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Lob
	@Column(length = 1024)
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Lob
	@Column(length = 1024)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Faq [id=" + id + ", question=" + question + ", answer="
				+ answer + ", faqCategory=" + faqCategory.getName()
				+ ", active=" + active + "]";
	}

}
