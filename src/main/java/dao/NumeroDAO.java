package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;

/**
* Clase NumeroDAO.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/

public class NumeroDAO {

	private Connection con;

    public NumeroDAO(Connection con) {
        this.con = con;
    }
	
	
	//INSERTAR numero
	public void insertar(Numero numero) {
		String sql = "INSERT INTO numero(id, orden, nombre, duracion, idEspectaculo) VALUES (?, ?, ?, ?, ?, ?)";
		try(PreparedStatement ps = con.prepareStatement(sql)) {
				ps.setLong(1, numero.getId());
				ps.setInt(2, numero.getOrden());
	            ps.setString(3, numero.getNombre());
	            ps.setDouble(4, numero.getDuracion());
	            ps.setLong(5, numero.getEspectaculo().getId()); 
	            ps.executeUpdate();
	           
	           System.out.println("Numero insertado correctamente");

	       } catch (SQLException e) {
	           System.out.println("Error al insertar numero");
	           e.printStackTrace();
	       }	
	}
	
	// BUSCAR POR ID
	public List<Numero> listarTodos() throws SQLException {
	    List<Numero> numeros = new ArrayList<>();
	    String sql = "SELECT n.id, n.orden, n.nombre, n.duracion, " +
	                 "e.id AS idEspectaculo, e.nombre AS nombreEspectaculo, e.fechaIni, e.fechaFin, " +
	                 "p.id AS idPersona, p.nombre AS nombrePersona, p.email, p.nacionalidad, " +
	                 "c.senior, c.fechasenior " +
	                 "FROM numero n " +
	                 "JOIN espectaculo e ON n.idEspectaculo = e.id " +
	                 "JOIN coordinacion c ON e.idCoordinacion = c.idPersona " +
	                 "JOIN persona p ON c.idPersona = p.id";

	    try (Statement st = con.createStatement();
	         ResultSet rs = st.executeQuery(sql)) {

	        while (rs.next()) {
	            
	            Coordinacion coord = new Coordinacion(
	                rs.getLong("idPersona"),
	                rs.getString("nombrePersona"),
	                rs.getString("email"),
	                rs.getString("nacionalidad"),
	                rs.getBoolean("senior"),
	                rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
	            );

	            
	            Espectaculo esp = new Espectaculo(
	                rs.getLong("idEspectaculo"),
	                rs.getString("nombreEspectaculo"),
	                rs.getDate("fechaIni").toLocalDate(),
	                rs.getDate("fechaFin").toLocalDate(),
	                coord
	            );

	           
	            Numero num = new Numero(
	                rs.getLong("id"),
	                rs.getInt("orden"),
	                rs.getString("nombre"),
	                rs.getDouble("duracion"),
	                esp
	            );

	            numeros.add(num);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al listar números: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return numeros;
	}


	}