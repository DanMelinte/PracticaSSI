package com.ssn.practica.work.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Event {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
	private Date date;

	public Event() {

	}

	public Event(String title, Date date) {
		this.title = title;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Date getDate() {
		return date;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
