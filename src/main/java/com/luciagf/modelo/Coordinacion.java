package com.luciagf.modelo;

import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Coordinacion extends Persona {

	private Long idCoord;
	private boolean senior = false;
	private LocalDate fechaSenior = null;

	public Coordinacion() {

	}

	public Coordinacion(Long idPersona, String nombre, String email, String nacionalidad, boolean senior,
			LocalDate fechaSenior) {
		super(idPersona, nombre, email, nacionalidad);
		this.senior = senior;
		this.fechaSenior = fechaSenior;
	}

	public Long getId() {
		return idCoord;
	}

	public void setId(Long id) {
		this.idCoord = id;
	}

	public boolean isSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public LocalDate getFechaSenior() {
		return fechaSenior;
	}

	public void setFechaSenior(LocalDate fechaSenior) {
		this.fechaSenior = fechaSenior;
	}

	@Override
	public String toString() {
		return "Coordinacion [id=" + idCoord + ", senior=" + senior + ", fechaSenior=" + fechaSenior;

	}
}
