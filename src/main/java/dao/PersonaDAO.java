package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PersonaDAO {
	
		
	 // INSERTAR una persona
    public void insertar(String nombre, int edad, String email, String nacionalidad) {
        String sql = "INSERT INTO persona (nombre, edad, email, nacionalidad) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, edad);
            ps.setString(3,email);
            ps.setString(4, nacionalidad);
            ps.executeUpdate();
            System.out.println("Persona insertada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al insertar persona");
            e.printStackTrace();
        }
    }
    
    //ACTUALIZAR persona
    public void actualizar(int id, String nombre, int edad, String email, String nacionalidad) {
        String sql = "UPDATE persona SET nombre = ?, edad = ? , email=? , nacionalidad=? WHERE id = ?";
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, edad);
            ps.setInt(3, id);
            ps.setString(3,email);
            ps.setString(4, nacionalidad);
            ps.executeUpdate();
            int filas = ps.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error al actualizar persona");
            e.printStackTrace();
        }
    }
    
    
    // LISTAR todas las personas
    
    
      
		
	
}
