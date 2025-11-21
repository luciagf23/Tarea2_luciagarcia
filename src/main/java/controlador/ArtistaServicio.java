package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;
import com.luciagf.modelo.Persona;

import dao.ArtistaDAO;
import dao.NumeroDAO;
import dao.PersonaDAO;;

public class ArtistaServicio {

	private ArtistaDAO artistaDAO;
	private PersonaDAO personaDAO;
	private NumeroDAO numeroDAO;

	public ArtistaServicio(Connection con) {
		this.artistaDAO = new ArtistaDAO(con);
		this.personaDAO = new PersonaDAO(con);
		this.numeroDAO = new NumeroDAO(con);
	}

	// Crear artista
	public void crearArtista(Long id, String apodo, Especialidad especialidad) throws SQLException {

		Persona p = personaDAO.buscarPorId(id);
		if (p == null) {
			throw new SQLException("No existe persona con id " + id);

		}

		// Validar que no sea ya artista
		if (artistaDAO.buscarPorPersona(id) != null) {
			throw new SQLException("Esta persona ya es artista");
		}

		// Crear artista
		artistaDAO.insertar(id, apodo, especialidad);
		System.out.println("Artista creado correctamente");
	}

	// Actualizar artista
	public void actualizarArtista(Long id, String apodo, Especialidad especialidad) throws SQLException {

		Artista a = artistaDAO.buscarPorPersona(id);
		if (a == null) {
			throw new SQLException("Esta persona no es artista");
		}
		artistaDAO.actualizar(id, apodo, especialidad);
		System.out.println("Artista actualizado correctamente.");

	}

	// Asignar numero a artista
	public void asignarNumero(Long id, Long idNumero) throws SQLException {

		// Validar artista
		Artista a = artistaDAO.buscarPorPersona(id);
		if (a == null) {
			throw new SQLException("La persona no es artista");
		}

		// Validar numero
		if (numeroDAO.buscarPorId(idNumero) == null) {
			throw new SQLException("No existe número con id " + idNumero);
		}

		// Asignar numero
		artistaDAO.asignarNumero(id, idNumero);
		System.out.println("Número asignado correctamente");
	}

	// Buscar Artista

	public Artista buscarPorIdPersona(Long id) throws SQLException {
		return artistaDAO.buscarPorPersona(id);
	}

	// LISTAR ARTISTAS
	public List<Artista> listarArtistas() throws SQLException {
		return artistaDAO.listarTodos();
	}

	// VER FICHA
	public void verFicha(Long idArtista) {
		try {
			Artista artista = artistaDAO.obtenerArtistaPorId(idArtista);
			if (artista == null) {
				System.out.println("No se encontró el artista");
				return;
			}

			System.out.println("===== FICHA DEL ARTISTA =====");
			System.out.println("Nombre: " + artista.getNombre());
			System.out.println("Email: " + artista.getEmail());
			System.out.println("Nacionalidad: " + artista.getNacionalidad());
			if (artista.getApodo() != null) {
				System.out.println("Apodo: " + artista.getApodo());
			}
			System.out.println("Especialidades: " + artista.getEspecialidades());

			System.out.println("\nTrayectoria en el circo:");
			List<String> trayectoria = artistaDAO.obtenerTrayectoria(idArtista);
			if (trayectoria.isEmpty()) {
				System.out.println("Este artista aún no ha participado en ningún número");
			} else {
				for (String t : trayectoria) {
					System.out.println("- " + t);
				}
			}
			System.out.println("=============================");

		} catch (SQLException e) {
			System.out.println("Error al mostrar ficha: " + e.getMessage());
		}
	}

	// Ver espectáculos y números asignados
	public void verEspectaculosYNumeros(Long idArtista) {
		List<Espectaculo> espectaculos = artistaDAO.obtenerEspectaculosDeArtista(idArtista);
		List<Numero> numeros = artistaDAO.obtenerNumerosDeArtista(idArtista);

		System.out.println("== Espectáculos del artista ==");
		if (espectaculos.isEmpty()) {
			System.out.println("No tiene espectáculos asignados.");
		} else {
			for (Espectaculo e : espectaculos) {
				System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre());
			}
		}

		System.out.println("== Números del artista ==");
		if (numeros.isEmpty()) {
			System.out.println("No tiene números asignados.");
		} else {
			for (Numero n : numeros) {
				System.out.println("ID: " + n.getId() + " | Nombre: " + n.getNombre());
			}
		}
	}

}
