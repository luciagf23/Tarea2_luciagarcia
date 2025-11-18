package com.luciagf.modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Numero {

	private Long id;
	private int orden;
	private String nombre;
	private double duracion;
	private Espectaculo espectaculo;

	public Numero() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Numero(Long id, int orden, String nombre, double duracion, Espectaculo espectaculo) {
        this.id = id;
        this.orden = orden;
        this.nombre = nombre;
        this.duracion = duracion;
        this.espectaculo = espectaculo;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	public Espectaculo getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}

	@Override
	public String toString() {
		return "Numero [id=" + id + ", orden=" + orden + ", nombre=" + nombre + ", duracion=" + duracion + ", espectaculo=" + espectaculo + "]";
	}

}