package net.poweroak.entity;

import java.io.Serializable;

public class Demo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object id;
	private String name;
	private Double value;
	private String date;
	public Demo(Object id, String name, Double value, String date) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.date = date;
	}
	public Demo() {
		super();
	}
	public Object getId() {
		return id;
	}
	public void setId(Object id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Demo [id=" + id + ", name=" + name + ", value=" + value + ", date=" + date + "]";
	}
	
	
}
