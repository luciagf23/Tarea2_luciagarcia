package com.luciagf.modelo;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Persona{

	protected Long id;
	protected String nombre;
	protected String email;
	protected String nacionalidad;

	public Persona() {
		super();
	}

	public Persona(Long id, String nombre, String email, String nacionalidad) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.nacionalidad = nacionalidad;
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", email=" + email + ", nacionalidad=" + nacionalidad + "]";
	}

	
	

}