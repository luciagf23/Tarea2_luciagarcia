package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Espectaculo;

/**
 * Clase EspectaculoDAO.java
 *
 * author LUCÍA GARCÍA FERNÁNDEZ version 1.0
 */

public class EspectaculoDAO {

	private Connection con;
	

	public EspectaculoDAO(Connection con) {
		this.con = con;
	}

	// INSERTAR espectaculo
	public void insertar(String nombre, Date fechaIni, Date fechaFin, Long idCoord) {
		String sql = "INSERT INTO espectaculo (nombre, fechaIni, fechaFin, idCoord) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombre);
			ps.setDate(2, fechaIni);
			ps.setDate(3, fechaFin);
			ps.setLong(4, idCoord);
			ps.executeUpdate();
			System.out.println("Espectaculo insertado correctamente");

		} catch (SQLException e) {
			System.out.println("Error al insertar espectaculo");
			e.printStackTrace();
		}

	}

	// BUSCAR ESPECTACULO
	public Espectaculo buscarEspectaculoCompleto(Long idEspectaculo) throws SQLException {
		String sql = "SELECT e.id AS idEspectaculo, e.nombre AS nombreEspectaculo, e.fechaIni, e.fechaFin, "
				+ "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " + "c.senior, c.fechasenior "
				+ "FROM espectaculo e " + "JOIN coordinacion c ON e.idCoordinacion = c.idPersona "
				+ "JOIN persona p ON c.idPersona = p.id " + "WHERE e.id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, idEspectaculo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Coordinacion coord = new Coordinacion(
							rs.getLong("idPersona"), 
							rs.getString("nombrePersona"),
							rs.getString("email"), 
							rs.getString("nacionalidad"), 
							rs.getBoolean("senior"),
							rs.getDate("fechasenior") != null ? 
							rs.getDate("fechasenior").toLocalDate() : null);

					Espectaculo esp = new Espectaculo(
							rs.getLong("idEspectaculo"), 
							rs.getString("nombreEspectaculo"),
							rs.getDate("fechaIni").toLocalDate(), 
							rs.getDate("fechaFin").toLocalDate(),
							coord
					);

					// Recuperar números y artistas
					NumeroDAO numeroDAO = new NumeroDAO(con);
	                esp.setNumeros(numeroDAO.buscarNumerosPorEspectaculo(idEspectaculo));
					return esp;
				}
			}
		}
		return null;
	}

	// BUSCAR ESPECTÁCULO POR ID
	public Espectaculo buscarPorId(Long id) {
		String sql = "SELECT e.id, e.nombre, e.fechaIni, e.fechaFin, "
				+ "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " + "c.senior, c.fechasenior "
				+ "FROM espectaculo e " + "JOIN coordinacion c ON e.idCoordinacion = c.idPersona "
				+ "JOIN persona p ON c.idPersona = p.id " + "WHERE e.id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Construimos la Coordinacion
					Coordinacion coord = new Coordinacion(
							rs.getLong("idPersona"), 
							rs.getString("nombrePersona"),
							rs.getString("email"), 
							rs.getString("nacionalidad"), 
							rs.getBoolean("senior"),
							rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null);

					return new Espectaculo(
							rs.getLong("id"), 
							rs.getString("nombre"),
							rs.getDate("fechaIni").toLocalDate(), 
							rs.getDate("fechaFin").toLocalDate(),
							coord
					);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error al buscar espectáculo por ID: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	// LISTAR TODOS LOS ESPECTÁCULOS
	public List<Espectaculo> listarTodos() {
		List<Espectaculo> espectaculos = new ArrayList<>();
		String sql = "SELECT e.id, e.nombre, e.fechaIni, e.fechaFin, "
				+ "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " + "c.senior, c.fechasenior "
				+ "FROM espectaculo e " + "JOIN coordinacion c ON e.idCoordinacion = c.idPersona "
				+ "JOIN persona p ON c.idPersona = p.id";

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				Coordinacion coord = new Coordinacion(rs.getLong("idPersona"), rs.getString("nombrePersona"),
						rs.getString("email"), rs.getString("nacionalidad"), rs.getBoolean("senior"),
						rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null);

				Espectaculo esp = new Espectaculo(
						rs.getLong("id"), 
						rs.getString("nombre"),
						rs.getDate("fechaIni").toLocalDate(), 
						rs.getDate("fechaFin").toLocalDate(),
						coord
					);

				espectaculos.add(esp);
			
			}
		} catch (SQLException e) {
			System.out.println("Error al listar espectáculos: " + e.getMessage());
			e.printStackTrace();
		}
		return espectaculos;
	}
}
