
package com.ssn.practica.work.Lab3;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Course {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	private String nume;

	@ManyToMany(mappedBy = "courses")
	private List<Trainee> traines = new ArrayList<>();

	@OneToMany(mappedBy = "course")
	private List<Evaluation> evaluations = new ArrayList<>();

	public Course() {

	}

	public List<Trainee> getTraines() {
		return traines;
	}

	public void setTraines(List<Trainee> traines) {
		this.traines = traines;
	}

	public List<Evaluation> getEvaluations() {
		return evaluations;
	}

	public void setEvaluations(List<Evaluation> evaluations) {
		this.evaluations = evaluations;
	}

	public Course(String nume) {
		this.nume = nume;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Override
	public String toString() {
		return "Course [id=" + id + ", nume=" + nume + ", traines=" + traines + "]";
	}

}
