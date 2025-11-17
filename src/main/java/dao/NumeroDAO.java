package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NumeroDAO {

	
	//INSERTAR numero
	public void insertar(String nombre, double duracion) {
		String sql = "INSERT INTO numero(nombre, duracion VALUES (?, ?)";
		try (Connection con = ConexionBD.getConexion();
	            PreparedStatement ps = con.prepareStatement(sql)) {

	           ps.setString(1, nombre);
	           ps.setDouble(2, duracion);
	           
	           ps.executeUpdate();
	           System.out.println("Numero insertado correctamente");

	       } catch (SQLException e) {
	           System.out.println("Error al insertar numero");
	           e.printStackTrace();
	       }	
	}
}