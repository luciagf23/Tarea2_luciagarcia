package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EspectaculoDAO {
	
	
	//INSERTAR espectaculo
	public void insertar(String nombre,Date fechaIni, Date fechaFin) {
	String sql = "INSERT INTO espectaculo (nombre, fechaIni, fechaFin) VALUES (?, ?, ?)";
	try (Connection con = ConexionBD.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setString(1, nombre);
           ps.setDate(2, fechaIni);
           ps.setDate(3,fechaFin);
           ps.executeUpdate();
           System.out.println("Espectaculo insertado correctamente");

       } catch (SQLException e) {
           System.out.println("Error al insertar espectaculo");
           e.printStackTrace();
       }
		
	}
	
	
	
}
