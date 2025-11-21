package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Persona;

import dao.ArtistaDAO;
import dao.CoordinacionDAO;
import dao.PersonaDAO;

public class CoordinacionServicio {

	private CoordinacionDAO coordinacionDAO;
	private PersonaDAO personaDAO;
	private ArtistaDAO artistaDAO;

	public CoordinacionServicio(Connection con) {
		this.coordinacionDAO = new CoordinacionDAO(con);
		this.personaDAO = new PersonaDAO(con);
		this.artistaDAO = new ArtistaDAO(con);

	}

	// CREAR COORDINADOR
	public void crearCoordinador(Long idPersona, boolean senior, LocalDate fechaSenior) throws SQLException {

		// Validar persona
		Persona p = personaDAO.buscarPorId(idPersona);
		if (p == null) {
			throw new SQLException("No existe persona con id " + idPersona);
		}

		// Validar que no sea artista
		if (artistaDAO.buscarPorPersona(idPersona) != null) {
			throw new SQLException("Esta persona es artista. No puede ser coordinador.");
		}

		// Validar que no sea ya coordinador
		if (coordinacionDAO.buscarPorId(idPersona) != null) {
			throw new SQLException("Esta persona ya es coordinador.");
		}

		// Crear coordinador
		coordinacionDAO.insertar(idPersona, senior, fechaSenior);
		System.out.println("Coordinador creado correctamente.");
	}

	// ACTUALIZAR COORDINADOR
	public void actualizarCoordinador(Long idPersona, boolean senior, LocalDate fechaSenior) throws SQLException {

		Coordinacion c = coordinacionDAO.buscarPorId(idPersona);
		if (c == null) {
			throw new SQLException("Esta persona no es coordinador.");
		}

		coordinacionDAO.actualizar(idPersona, senior, fechaSenior);
		System.out.println("Coordinador actualizado correctamente.");
	}

	// MARCAR COMO SENIOR
	public void marcarSenior(Long idPersona, LocalDate fechaSenior) throws SQLException {

		Coordinacion c = coordinacionDAO.buscarPorId(idPersona);
		if (c == null) {
			throw new SQLException("Esta persona no es coordinador.");
		}

		coordinacionDAO.actualizar(idPersona, true, fechaSenior);
		System.out.println("Coordinador marcado como senior.");
	}

	// QUITAR SENIOR (poner false)
	public void quitarSenior(Long idPersona) throws SQLException {

		Coordinacion c = coordinacionDAO.buscarPorId(idPersona);
		if (c == null) {
			throw new SQLException("Esta persona no es coordinador.");
		}

		coordinacionDAO.actualizar(idPersona, false, null);
		System.out.println("Estado senior eliminado.");
	}

	// BUSCAR COORDINADOR POR PERSONA
	public Coordinacion buscarPorIdPersona(Long idPersona) throws SQLException {
		return coordinacionDAO.buscarPorId(idPersona);
	}

	// LISTAR COORDINADORES
	public List<Coordinacion> listarCoordinadores() throws SQLException {
		return coordinacionDAO.listarTodas();
	}

}
