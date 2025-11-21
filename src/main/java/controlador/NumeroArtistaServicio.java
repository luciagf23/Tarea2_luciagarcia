package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Persona;

import dao.NumeroArtistaDAO;

public class NumeroArtistaServicio {

	private NumeroArtistaDAO numeroArtistaDAO;

	public NumeroArtistaServicio(Connection con) {
		this.numeroArtistaDAO = new NumeroArtistaDAO(con);
	}

	public void asignarArtistas(Long idNumero, List<Long> idsArtistas) {
		try {
			for (Long idArtista : idsArtistas) {
				numeroArtistaDAO.asignarArtista(idNumero, idArtista);
			}
			System.out.println("Artistas asignados correctamente al número " + idNumero);
		} catch (SQLException e) {
			System.out.println("Error al asignar artistas: " + e.getMessage());

		}
	}

	public void mostrarArtistasDeNumero(Long idNumero) {
		try {
			List<Persona> artistas = numeroArtistaDAO.listarArtistasPorNumero(idNumero);
			System.out.println("Artistas en el número " + idNumero + ":");
			for (Persona p : artistas) {
				System.out.println("- " + p.getNombre());
			}
		} catch (SQLException e) {
			System.out.println("Error al listar artistas: " + e.getMessage());
		}
	}

}
