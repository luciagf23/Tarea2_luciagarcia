
package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Persona;

/**
 * Clase PersonaDAO.java
 *
 * author LUCÍA GARCÍA FERNÁNDEZ version 1.0
 */

public class PersonaDAO {

	private Connection con;
	PersonaDAO personaDAO = new PersonaDAO(con);

	public PersonaDAO(Connection con) {
		this.con = con;
	}

	// INSERTAR PERSONA
	public void insertar(Persona persona) throws SQLException {
		String sql = "INSERT INTO persona (nombre, email, nacionalidad) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(2, persona.getNombre());
			ps.setString(3, persona.getEmail());
			ps.setString(4, persona.getNacionalidad());

			ps.executeUpdate();

			// Recuperar el id
			try (ResultSet rSet = ps.getGeneratedKeys()) {
				if (rSet.next()) {
					persona.setId(rSet.getLong(1));
				}
			}
		}
	}

	public Persona buscarPorCredenciales(String username, String password) throws SQLException {
	    String sql = "SELECT p.id, p.nombre, p.email, p.nacionalidad, c.senior, c.fechasenior, "
	               + "a.apodo, a.especialidad, a.idPersona AS idArt, p.perfil "
	               + "FROM persona p "
	               + "JOIN credencial u ON p.id = u.idPersona "
	               + "LEFT JOIN coordinacion c ON p.id = c.idPersona "
	               + "LEFT JOIN artista a ON p.id = a.idPersona "
	               + "WHERE u.username = ? AND u.password = ?";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, username.toLowerCase());
	        ps.setString(2, password);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Long id = rs.getLong("id");
	                String nombre = rs.getString("nombre");
	                String email = rs.getString("email");
	                String nacionalidad = rs.getString("nacionalidad");
	                String perfilStr = rs.getString("perfil");
	                Perfil perfil = Perfil.valueOf(perfilStr.toUpperCase()); 

	                if (perfil == Perfil.COORDINACION) {
	                    boolean senior = rs.getBoolean("senior");
	                    LocalDate fechaSenior = rs.getDate("fechasenior") != null
	                            ? rs.getDate("fechasenior").toLocalDate(): null;
	                            
	                    return new Coordinacion(id, nombre, email, nacionalidad, senior, fechaSenior);
	                } else if (perfil == Perfil.ARTISTA) {
	                    String apodo = rs.getString("apodo");
	                    String especialidadStr = rs.getString("especialidad");
	                    Long idArt = rs.getLong("idArt");
	                    Especialidad especialidad = Especialidad.valueOf(especialidadStr.toUpperCase());
	                    return new Artista(id, nombre, email, nacionalidad, idArt, apodo, especialidad);
	                } else {
	                    return new Persona(id, nombre, email, nacionalidad, perfil);
	                }
	            }
	        }
	    }
	    return null;
	}

	

	public Coordinacion seleccionarCoordinador() throws SQLException {
		// consulta que devuelve solo personas con perfil COORDINACION
		String sql = "SELECT id, nombre, email, nacionalidad FROM persona WHERE perfil = 'COORDINACION'";
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				Coordinacion c = new Coordinacion();
				c.setId(rs.getLong("id"));
				c.setNombre(rs.getString("nombre"));
				c.setEmail(rs.getString("email"));
				c.setNacionalidad(rs.getString("nacionalidad"));
				return c;
			}
		}
		return null;
	}

	// BUSCAR POR ID
	public Persona buscarPorId(Long id) throws SQLException {
		String sql = "SELECT * FROM persona p " + "LEFT JOIN artista a ON p.id = a.idPersona "
				+ "LEFT JOIN coordinacion c ON p.id = c.idPersona " + "WHERE p.id = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String perfil = rs.getString("perfil");

					if ("artista".equalsIgnoreCase(perfil)) {

						return new Artista(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"), rs.getLong("idArt"), rs.getString("apodo"),
								Especialidad.valueOf(rs.getString("especialidad").toUpperCase())

						);
					} else if ("coordinacion".equalsIgnoreCase(perfil)) {
						return new Coordinacion(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"), rs.getBoolean("senior"),
								rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null);
					} else {
						return new Persona(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"));
					}
				}
			}
		}
		return null;
	}

	// BUSCAR POR USUARIO
	public Persona buscarPorUsuario(String usuario, String password) throws SQLException {
		String sql = "SELECT p.id, p.nombre, p.email, p.nacionalidad, c.usuario, c.password, p.perfil "
				+ "FROM persona p JOIN credencial c ON p.idCredenciales = c.id "
				+ "WHERE c.usuario = ? AND c.password = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Persona persona = new Persona();
					persona.setId(rs.getLong("id"));
					persona.setNombre(rs.getString("nombre"));
					persona.setEmail(rs.getString("email"));
					persona.setNacionalidad(rs.getString("nacionalidad"));
					persona.setPerfil(Perfil.valueOf(rs.getString("perfil")));

					return persona;
				}
			}
		}
		return null;
	}

	// BUSCAR POR EMAIL
	public Persona buscarPorEmail(String email) throws SQLException {
		String sql = "SELECT * FROM persona p " + "LEFT JOIN artista a ON p.id = a.idPersona "
				+ "LEFT JOIN coordinacion c ON p.id = c.idPersona " + "WHERE p.email = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String perfil = rs.getString("perfil");

					if ("artista".equalsIgnoreCase(perfil)) {
						String especStr = rs.getString("especialidad");
						Especialidad espec = null;

						return new Artista(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"), rs.getLong("idArt"), rs.getString("apodo"), espec);
					} else if ("coordinacion".equalsIgnoreCase(perfil)) {
						return new Coordinacion(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"), rs.getBoolean("senior"),
								rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null);
					} else {
						return new Persona(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
								rs.getString("nacionalidad"));
					}
				}
			}
		}
		return null;
	}

	// LISTAR ARTISTAS
	public List<Persona> listarArtistas() throws SQLException {
		List<Persona> artistas = new ArrayList<>();

		String sql = "SELECT id, nombre, email, nacionalidad " + "FROM persona " + "WHERE perfil = 'ARTISTA'";

		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Persona artista = new Persona();
				artista.setId(rs.getLong("id"));
				artista.setNombre(rs.getString("nombre"));
				artista.setEmail(rs.getString("email"));
				artista.setNacionalidad(rs.getString("nacionalidad"));
				artista.setPerfil(Perfil.ARTISTA);

				artistas.add(artista);
			}
		}

		return artistas;
	}

	// LISTAR TODAS LAS PERSONAS
	public List<Persona> listarTodas() throws SQLException {
		List<Persona> personas = new ArrayList<>();
		String sql = "SELECT * FROM persona p " + "LEFT JOIN artista a ON p.id = a.idPersona "
				+ "LEFT JOIN coordinacion c ON p.id = c.idPersona";

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

			while (rs.next()) {
				String perfil = rs.getString("perfil");

				if ("artista".equalsIgnoreCase(perfil)) {
					String especStr = rs.getString("especialidad");
					Especialidad espec = null;

					personas.add(new Artista(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
							rs.getString("nacionalidad"), rs.getLong("idArt"), rs.getString("apodo"), espec));
				} else if ("coordinacion".equalsIgnoreCase(perfil)) {
					personas.add(new Coordinacion(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
							rs.getString("nacionalidad"), rs.getBoolean("senior"),
							rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null));
				} else {
					personas.add(new Persona(rs.getLong("id"), rs.getString("nombre"), rs.getString("email"),
							rs.getString("nacionalidad")));
				}
			}
		}
		return personas;
	}

	// ACTUALIZAR persona
	public void actualizar(Persona persona) throws SQLException {
		String sql = "UPDATE persona SET nombre = ?, email = ?, nacionalidad = ?, idCredenciales = ? WHERE id = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, persona.getNombre());
			ps.setString(2, persona.getEmail());
			ps.setString(3, persona.getNacionalidad());
			ps.setLong(4, persona.getIdCredenciales());
			ps.setLong(5, persona.getId());
			ps.executeUpdate();
		}
	}

	// ELIMINAR persona por ID
	public void eliminar(Long id) throws SQLException {
		String sql = "DELETE FROM persona WHERE id = ?";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}

}
