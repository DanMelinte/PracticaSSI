package com.ssn.practica.work.Lab3;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Trainee {

	@Id
	@GeneratedValue()
	@GenericGenerator(name = "increment", strategy = "increment")
	private Long id;

	private String name;

	private int Age;

	@ManyToMany(cascade = { CascadeType.ALL })
	@JoinTable(name = "Trainee_Course", joinColumns = { @JoinColumn(name = "trainee_id") }, inverseJoinColumns = {
			@JoinColumn(name = "course_id") })
	private List<Course> courses = new ArrayList<Course>();

	public Trainee() {

	}

	public Trainee(String name, int age) {
		this.name = name;
		this.Age = age;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

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

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", Age=" + Age + "]";
	}

}
