package com.luciagf.modelo;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Artista extends Persona {

	private Long idArt;
	private String apodo = null;
	private Especialidad especialidad;

	public Artista() {
		super();

	}

	public Artista(Long idPersona, String nombre, String email, String nacionalidad, Long idArt, String apodo,
			Especialidad especialidad) {
		super(idPersona, nombre, email, nacionalidad);
		this.idArt = idArt;
		this.apodo = apodo;
		this.especialidad = especialidad;
	}
	
	public Artista(Long idPersona, String nombre, String email, String nacionalidad, String apodo,
			Especialidad especialidad) {
		super(idPersona, nombre, email, nacionalidad);
		this.apodo = apodo;
		this.especialidad = especialidad;
	}

	public Long getId() {
		return idArt;
	}

	public void setId(Long id) {
		this.idArt = id;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	@Override
	public String toString() {
		return "Artista [idArt=" + idArt + ", apodo=" + apodo + ", especialidad=" + especialidad + ", id=" + id
				+ ", nombre=" + nombre + ", email=" + email + ", nacionalidad=" + nacionalidad + "]";
	}

}