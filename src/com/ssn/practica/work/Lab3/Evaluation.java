package com.ssn.practica.work.Lab3;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Evaluation {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private long id;

	private int nota;

	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@ManyToOne
	@JoinColumn(name = "trainee_id", nullable = false)
	private Trainee trainee;

	public Evaluation() {

	}

	public Evaluation(int nota, Course course, Trainee trainee) {
		super();
		this.nota = nota;
		this.course = course;
		this.trainee = trainee;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Trainee getTrainee() {
		return trainee;
	}

	public void setTrainee(Trainee trainee) {
		this.trainee = trainee;
	}
}
