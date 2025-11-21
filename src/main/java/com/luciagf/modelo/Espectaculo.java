package com.luciagf.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */
public class Espectaculo {

	private Long id;
	private String nombre;
	private LocalDate fechaIni;
	private LocalDate fechaFin;
	private Coordinacion coordinacion;
	private List<Numero> numeros;

	public Espectaculo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Espectaculo(Long id, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
	    this.id = id;
	    this.nombre = nombre;
	    this.fechaIni = fechaInicio;
	    this.fechaFin = fechaFin;
	}

	public Espectaculo(Long id, String nombre, LocalDate fechaIni, LocalDate fechaFin, Coordinacion coordinacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		this.coordinacion = coordinacion;
        this.numeros = new ArrayList<>();
		
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

	public LocalDate getFechaini() {
		return fechaIni;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaIni = fechaini;
	}

	public LocalDate getFechafin() {
		return fechaFin;
	}

	public void setFechafin(LocalDate fechafin) {
		this.fechaFin = fechafin;
	}



	public Coordinacion getCoordinacion() {
		return coordinacion;
	}


	public void setCoordinacion(Coordinacion coordinacion) {
		this.coordinacion = coordinacion;
	}


	public List<Numero> getNumeros() { 
		return numeros; 
	}
    public void setNumeros(List<Numero> numeros) { 
    	this.numeros = numeros; 
    }
    
    //Metodos
    public void addNumero(Numero numero) {
    	numeros.add(numero);
    }


	@Override
	public String toString() {
		return "Espectaculo [id=" + id + ", nombre=" + nombre + ", fechaIni=" + fechaIni + ", fechaFin=" + fechaFin
				+ ", coordinacion=" + coordinacion + ", numeros=" + numeros + "]";
	}


    

	
}