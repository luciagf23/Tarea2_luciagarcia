package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;

/**
 * Clase EspectaculoDAO.java
 *
 * author LUCÍA GARCÍA FERNÁNDEZ 
 * version 1.0
 */

public class EspectaculoDAO {

	private Connection con;
	

	public EspectaculoDAO(Connection con) {
		this.con = con;
	}

	// INSERTAR espectaculo
	public void insertar(String nombre, Date fechaIni, Date fechaFin, Long idCoordinacion) {
		String sql = "INSERT INTO espectaculo (nombre, fechaIni, fechaFin, idCoord) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, nombre);
			ps.setDate(2, fechaIni);
			ps.setDate(3, fechaFin);
			ps.setLong(4, idCoordinacion);
			ps.executeUpdate();
			System.out.println("Espectaculo insertado correctamente");

		} catch (SQLException e) {
			System.out.println("Error al insertar espectaculo");
			e.printStackTrace();
		}

	}

	//BUSCAR POR NOMBRE
	public Espectaculo buscarPorNombre(String nombre) throws SQLException {
	    String sql = "SELECT id, nombre, fechaInicio, fechaFin, idCoordinacion FROM espectaculo WHERE nombre = ?";
	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, nombre);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Espectaculo e = new Espectaculo();
	                e.setId(rs.getLong("id"));
	                e.setNombre(rs.getString("nombre"));
	                e.setFechaini(rs.getDate("fechaInicio").toLocalDate());
	                e.setFechafin(rs.getDate("fechaFin").toLocalDate());
	                
	                return e;
	            }
	        }
	    }
	    return null; 
	    }
	

	// BUSCAR ESPECTÁCULO POR ID
	public Espectaculo buscarEspectaculoCompleto(Long id) throws SQLException {
	    Espectaculo espectaculo = null;

	    // Buscar espectáculo + coordinación
	    String sql = "SELECT e.id, e.nombre, e.fechaIni, e.fechaFin, " +
	                 "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " +
	                 "c.senior, c.fechasenior " +
	                 "FROM espectaculo e " +
	                 "JOIN coordinacion c ON e.idCoordinacion = c.idPersona " +
	                 "JOIN persona p ON c.idPersona = p.id " +
	                 "WHERE e.id = ?";

	    try (PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setLong(1, id);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                Coordinacion coord = new Coordinacion(
	                        rs.getLong("idPersona"),
	                        rs.getString("nombrePersona"),
	                        rs.getString("email"),
	                        rs.getString("nacionalidad"),
	                        rs.getBoolean("senior"),
	                        rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
	                );

	                espectaculo = new Espectaculo(
	                        rs.getLong("id"),
	                        rs.getString("nombre"),
	                        rs.getDate("fechaIni").toLocalDate(),
	                        rs.getDate("fechaFin").toLocalDate(),
	                        coord
	                );
	            }
	        }
	    }

	    if (espectaculo == null) 
	    	return null;

	    // Buscar números del espectáculo
	    String sqlNumeros = "SELECT idNumero, nombre, duracion, orden FROM numero WHERE idEspectaculo = ?";
	    try (PreparedStatement ps = con.prepareStatement(sqlNumeros)) {
	        ps.setLong(1, id);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Numero numero = new Numero(
	                        rs.getLong("idNumero"),
	                        rs.getInt("orden"),
	                        rs.getString("nombre"),
	                        rs.getDouble("duracion"),
	                        rs.getLong("idEspectaculo")
	                        
	                        
	                );

	                // Buscar artistas de cada número
	                String sqlArtistas = "SELECT a.idPersona, p.nombre, p.email, p.nacionalidad, a.apodo, a.especialidad " +
	                                     "FROM artista a " +
	                                     "JOIN persona p ON a.idPersona = p.id " +
	                                     "JOIN numero_artista na ON a.idPersona = na.idArtista " +
	                                     "WHERE na.idNumero = ?";
	                try (PreparedStatement psArt = con.prepareStatement(sqlArtistas)) {
	                    psArt.setLong(1, numero.getId());
	                    try (ResultSet rsArt = psArt.executeQuery()) {
	                        while (rsArt.next()) {
	                        	Artista artista = new Artista(
	                        		    rsArt.getLong("idPersona"),
	                        		    rsArt.getString("nombre"),
	                        		    rsArt.getString("email"),
	                        		    rsArt.getString("nacionalidad"),
	                        		    rsArt.getString("apodo"),
	                        		    Especialidad.valueOf(rsArt.getString("especialidad").toUpperCase())
	                        		);
	                        		numero.addArtista(artista);
	                        }
	                    }
	                }

	                espectaculo.addNumero(numero);
	            }
	        }
	    }

	    return espectaculo;
	}


	
	
	
	//LISTAR BASICOS
	public List<Espectaculo> listarBasicos() throws SQLException {
	    List<Espectaculo> espectaculos = new ArrayList<>();

	    String sql = "SELECT idEspectaculo, nombre, fechaInicio, fechaFin FROM espectaculo";

	    try (PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	            Long id = rs.getLong("idEspectaculo");
	            String nombre = rs.getString("nombre");
	            LocalDate inicio = rs.getDate("fechaInicio").toLocalDate();
	            LocalDate fin = rs.getDate("fechaFin").toLocalDate();

	            // Constructor básico de Espectaculo (solo con id, nombre y fechas)
	            Espectaculo e = new Espectaculo(id, nombre, inicio, fin);
	            espectaculos.add(e);
	        }
	    }

	    return espectaculos;
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
	
	
	//INSERTAR
		public void insertar(Espectaculo espectaculo) throws SQLException {
			String sql = "INSERT INTO espectaculo (nombre, fechaInicio, fechaFin, idCoordinacion) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            ps.setString(1, espectaculo.getNombre());
	            ps.setDate(2, Date.valueOf(espectaculo.getFechaini()));
	            ps.setDate(3, Date.valueOf(espectaculo.getFechafin()));
	            ps.setLong(4, espectaculo.getCoordinacion().getId());

	            ps.executeUpdate();

	            try (ResultSet rs = ps.getGeneratedKeys()) {
	                if (rs.next()) {
	                    espectaculo.setId(rs.getLong(1));
	                }
	            }
	        }
	      
		}
		
		// Actualizar espectáculo
	        public void actualizar(Espectaculo espectaculo) throws SQLException {
	            String sql = "UPDATE espectaculo SET nombre=?, fechaInicio=?, fechaFin=?, idCoordinacion=? WHERE id=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setString(1, espectaculo.getNombre());
	                ps.setDate(2, Date.valueOf(espectaculo.getFechaini()));
	                ps.setDate(3, Date.valueOf(espectaculo.getFechafin()));
	                ps.setLong(4, espectaculo.getCoordinacion().getId());
	                ps.setLong(5, espectaculo.getId());

	                ps.executeUpdate();
	            }
	        }
		
	     // Eliminar espectáculo
	        public void eliminar(Long id) throws SQLException {
	            String sql = "DELETE FROM espectaculo WHERE id=?";
	            try (PreparedStatement ps = con.prepareStatement(sql)) {
	                ps.setLong(1, id);
	                ps.executeUpdate();
	            }
	        }
}
