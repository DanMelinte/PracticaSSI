package com.ssn.practica.work.App;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Store {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private String name;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Price> prices = new ArrayList<>();

	public Store() {

	}

	public Store(String name) {
		super();
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Price> getPrices() {
		return prices;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return "Store [id=" + id + ", name=" + name + "]";
	}

}
