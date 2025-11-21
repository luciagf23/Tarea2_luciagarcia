package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Persona;

public class NumeroArtistaDAO {

	
	private Connection con;
	
	public NumeroArtistaDAO(Connection con) {
		this.con=con;
	}
	
	// Asignar un artista a un número
    public void asignarArtista(Long idNumero, Long idArtista) throws SQLException {
        String sql = "INSERT INTO numero_artista (idNumero, idArtista) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idNumero);
            ps.setLong(2, idArtista);
            ps.executeUpdate();
        }
    }
    
 //Eliminar asignación
    public void eliminarAsignacion(Long idNumero, Long idArtista) throws SQLException {
        String sql = "DELETE FROM numero_artista WHERE idNumero=? AND idArtista=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idNumero);
            ps.setLong(2, idArtista);
            ps.executeUpdate();
        }
    }
    
 //Listar artistas de un número
    public List<Persona> listarArtistasPorNumero(Long idNumero) throws SQLException {
        List<Persona> artistas = new ArrayList<>();
        String sql = "SELECT p.id, p.nombre, p.email, p.nacionalidad " +
                     "FROM persona p JOIN numero_artista na ON p.id = na.idArtista " +
                     "WHERE na.idNumero=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idNumero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Persona artista = new Persona();
                    artista.setId(rs.getLong("id"));
                    artista.setNombre(rs.getString("nombre"));
                    artista.setEmail(rs.getString("email"));
                    artista.setNacionalidad(rs.getString("nacionalidad"));
                    artistas.add(artista);
                }
            }
        }
        return artistas;
    }
    
    
}
