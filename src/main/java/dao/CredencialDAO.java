package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Credencial;
import com.luciagf.modelo.Perfil;

public class CredencialDAO {
	
	 private Connection con;

	    public CredencialDAO(Connection con) {
	        this.con = con;
	    }

	    // INSERTAR CREDENCIAL
	    public void insertar(Credencial credencial) throws SQLException {
	        String sql = "INSERT INTO credencial (idPersona, usuario, password) VALUES (?, ?, ?)";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setLong(1, credencial.getId());
	            ps.setString(2, credencial.getNombre());
	            ps.setString(3, credencial.getPassword());
	            ps.executeUpdate();
	        }
	    }

	    // BUSCAR POR ID PERSONA
	    public Credencial buscarPorIdPersona(Long idPersona) throws SQLException {
	        String sql = "SELECT * FROM credencial WHERE idPersona = ?";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setLong(1, idPersona);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return new Credencial(
	                        rs.getLong("id"),
	                        rs.getString("nombreUsuario"),
	                        rs.getString("password"),
	                        Perfil.valueOf(rs.getString("perfil").toUpperCase())
	                        
	                    );
	                }
	            }
	        }
	        return null;
	    }

	    // BUSCAR POR USUARIO
	    public Credencial buscarPorUsuario(String usuario) throws SQLException {
	        String sql = "SELECT * FROM credencial WHERE usuario = ?";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, usuario);

	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    return new Credencial(
	                        rs.getLong("id"),
	                        rs.getString("nombreUsuario"),
	                        rs.getString("password"),
	                        Perfil.valueOf(rs.getString("perfil").toUpperCase())
	                        
	                    );
	                }
	            }
	        }
	        return null;
	    }

	    // VALIDAR LOGIN
	    public boolean validarLogin(String usuario, String password) throws SQLException {
	        String sql = "SELECT * FROM credencial WHERE usuario = ? AND password = ?";
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setString(1, usuario);
	            ps.setString(2, password);

	            try (ResultSet rs = ps.executeQuery()) {
	                return rs.next(); // true si existe una fila con esas credenciales
	            }
	        }
	    }

	    // LISTAR TODAS LAS CREDENCIALES
	    public List<Credencial> listarTodas() throws SQLException {
	        List<Credencial> credenciales = new ArrayList<>();
	        String sql = "SELECT * FROM credencial";

	        try (Statement st = con.createStatement();
	             ResultSet rs = st.executeQuery(sql)) {

	            while (rs.next()) {
	                credenciales.add(new Credencial(
	                    rs.getLong("idPersona"),
	                    rs.getString("usuario"),
	                    rs.getString("password"),
	                    Perfil.valueOf(rs.getString("perfil").toUpperCase())
	                ));
	            }
	        }
	        return credenciales;
	    }
	
}
