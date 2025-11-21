package com.luciagf.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Artista extends Persona {

	private Long idArt;
	private String apodo = null;
	private List<Especialidad> especialidades;

	public Artista() {
		super();

	}

	public Artista(Long idPersona, String nombre, String email, String nacionalidad, Long idArt, String apodo,
			Especialidad especialidad) {
		super(idPersona, nombre, email, nacionalidad);
		this.idArt = idArt;
		this.apodo = apodo;
		this.especialidades = new ArrayList<Especialidad>();
	}

	public Artista(Long idPersona, String nombre, String email, String nacionalidad, String apodo,
			Especialidad especialidad) {
		super(idPersona, nombre, email, nacionalidad);
		this.apodo = apodo;
		this.especialidades = new ArrayList<>();
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

	public List<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	@Override
	public String toString() {
		return "Artista [idArt=" + idArt + ", apodo=" + apodo + ", especialidades=" + especialidades + "]";
	}

}
