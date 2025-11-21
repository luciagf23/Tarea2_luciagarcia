package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;

/**
 * Clase NumeroDAO.java
 *
 * author LUCÍA GARCÍA FERNÁNDEZ version 1.0
 */

public class NumeroDAO {

	private Connection con;
	private final ArtistaDAO artistaDAO;

	public NumeroDAO(Connection con) {
		this.con = con;
		this.artistaDAO = new ArtistaDAO(con);
	}

	// INSERTAR numero
	public void insertar(Numero numero) throws SQLException {
		String sql = "INSERT INTO numero(orden, nombre, duracion, idEspectaculo) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(2, numero.getOrden());
			ps.setString(3, numero.getNombre());
			ps.setDouble(4, numero.getDuracion());
			ps.setLong(5, numero.getIdEspectaculo());
			ps.executeUpdate();

			System.out.println("Numero insertado correctamente");

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					numero.setId(rs.getLong(1));
				}
			}
		}
	}

	// BUSCAR POR ID
	public Numero buscarPorId(long id) throws SQLException {

		String sql = "SELECT * FROM numero WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Numero(rs.getLong("id"), rs.getInt("orden"), rs.getString("nombre"),
						rs.getDouble("duracion"), rs.getLong("idEspectaculo")

				);
			}
		}
		return null;
	}

	public List<Numero> buscarNumerosPorEspectaculo(Long idEspectaculo) throws SQLException {
		List<Numero> numeros = new ArrayList<>();
		String sql = "SELECT id, orden, nombre, duracion FROM numero WHERE idEspectaculo = ? ORDER BY orden";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, idEspectaculo);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Numero num = new Numero(rs.getLong("id"), rs.getInt("orden"), rs.getString("nombre"),
							rs.getDouble("duracion"), rs.getLong("idCoord"));

					List<Artista> artistas = artistaDAO.buscarArtistasPorNumero(num.getId());
					num.setArtistas(artistas);

					numeros.add(num);
				}
			}
		}
		return numeros;
	}

	// ACTUALIZAR
	public void actualizar(Numero numero) throws SQLException {

		String sql = "UPDATE numero SET nombre = ?, orden = ?, duracion = ?, idEspectaculo = ? " + "WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, numero.getNombre());
			ps.setInt(2, numero.getOrden());
			ps.setDouble(3, numero.getDuracion());
			ps.setLong(4, numero.getIdEspectaculo());
			ps.setLong(5, numero.getId());

			ps.executeUpdate();
		}
	}

	// CONTAR NUMEROS
	public int contarPorEspectaculo(Long idEspectaculo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM numero WHERE idEspectaculo=?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, idEspectaculo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return rs.getInt(1);
			}
		}
		return 0;
	}

	// ASIGNAR ARTISTA
	public void asignarArtista(Long idNumero, Long idArtista) {
		String sql = "INSERT INTO numero_artista (id_numero, id_artista) VALUES (?, ?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, idNumero);
			ps.setLong(2, idArtista);

			int filas = ps.executeUpdate();

			if (filas > 0) {
				System.out.println("Artista asignado correctamente al número");
			} else {
				System.out.println("No se pudo asignar el artista al número");
			}

		} catch (SQLException e) {
			System.out.println("Error al asignar artista a número: " + e.getMessage());
		}
	}

	// LISTAR TODOS LOS NUMEROS
	public List<Numero> listarTodos() throws SQLException {
		List<Numero> numeros = new ArrayList<>();
		String sql = "SELECT n.id, n.orden, n.nombre, n.duracion, "
				+ "e.id AS idEspectaculo, e.nombre AS nombreEspectaculo, e.fechaIni, e.fechaFin, "
				+ "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " + "c.senior, c.fechasenior "
				+ "FROM numero n " + "JOIN espectaculo e ON n.idEspectaculo = e.id "
				+ "JOIN coordinacion c ON e.idCoordinacion = c.idPersona " + "JOIN persona p ON c.idPersona = p.id";

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {

				Coordinacion coord = new Coordinacion(rs.getLong("idPersona"), rs.getString("nombrePersona"),
						rs.getString("email"), rs.getString("nacionalidad"), rs.getBoolean("senior"),
						rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null);

				Espectaculo esp = new Espectaculo(rs.getLong("idEspectaculo"), rs.getString("nombreEspectaculo"),
						rs.getDate("fechaIni").toLocalDate(), rs.getDate("fechaFin").toLocalDate(), coord

				);

				Numero num = new Numero(rs.getLong("id"), rs.getInt("orden"), rs.getString("nombre"),
						rs.getDouble("duracion"), rs.getLong("idEspectaculo"));

				numeros.add(num);
			}
		} catch (SQLException e) {
			System.out.println("Error al listar números: " + e.getMessage());
			e.printStackTrace();
		}
		return numeros;
	}

	// LISTAR NUMEROS POR ESPECTACULO
	public List<Numero> listarPorEspectaculo(long idEspectaculo) throws SQLException {

		List<Numero> lista = new ArrayList<>();

		String sql = "SELECT * FROM numero WHERE idEspectaculo = ? ORDER BY orden";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, idEspectaculo);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Numero n = new Numero(rs.getLong("id"), rs.getInt("orden"), rs.getString("nombre"),
						rs.getDouble("duracion"), rs.getLong("idEspectaculo"));

				lista.add(n);
			}
		}

		return lista;
	}

	// ELIMINAR NUMERO
	public boolean eliminar(long id) throws SQLException {

		String sql = "DELETE FROM numero WHERE id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, id);
			return ps.executeUpdate() > 0;
		}
	}

}