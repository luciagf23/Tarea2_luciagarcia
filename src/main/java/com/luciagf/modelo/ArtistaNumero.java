package com.luciagf.modelo;

public class ArtistaNumero {

	private Long idArtista;
	private Long idNumero;

	public ArtistaNumero() {

	}

	public ArtistaNumero(Long idArtista, Long idNumero) {
		this.idArtista = idArtista;
		this.idNumero = idNumero;
	}

	public Long getIdArtista() {
		return idArtista;
	}

	public void setIdArtista(Long idArtista) {
		this.idArtista = idArtista;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long idNumero) {
		this.idNumero = idNumero;
	}

}
