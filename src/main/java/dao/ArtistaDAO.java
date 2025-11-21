package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Persona;

/**
* Clase ArtistaDAO.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/
public class ArtistaDAO {

    private Connection con;

    public ArtistaDAO(Connection con) {
        this.con = con;
    }

  
    // INSERTAR ARTISTA 
    public void insertar(long idPersona, String apodo, Especialidad especialidad) throws SQLException {

        String sql = "INSERT INTO artista (apodo, idNumero, idPersona, especialidades) " +
                     "VALUES (?, NULL, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, apodo);
            ps.setLong(2, idPersona);
            ps.setString(3, especialidad.name());

            ps.executeUpdate();
        }
    }

   
    // BUSCAR ARTISTA POR IDPERSONA
    public Artista buscarPorPersona(long idPersona) throws SQLException {

        String sql = "SELECT * FROM artista WHERE idPersona = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idPersona);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Artista(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("nacionalidad"),
                    rs.getLong("idArt"),
                    rs.getString("apodo"),
                    Especialidad.valueOf(rs.getString("especialidades"))
                );
            }
        }
        return null;
    }

    
    //BUSCAR ARTISTA POR ID
    public Artista obtenerArtistaPorId(Long idArtista) throws SQLException {
        String sql = "SELECT id, nombre, email, nacionalidad, apodo, especialidades " +
                     "FROM persona WHERE id=? AND perfil='ARTISTA'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idArtista);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Artista artista = new Artista();
                    artista.setId(rs.getLong("id"));
                    artista.setNombre(rs.getString("nombre"));
                    artista.setEmail(rs.getString("email"));
                    artista.setNacionalidad(rs.getString("nacionalidad"));
                   
                    String apodo=rs.getString("apodo");
                   
                   if(apodo!=null && !apodo.trim().isEmpty()) {
                	   artista.setApodo(apodo);
                   }else {
                	   artista.setApodo("sin apodo");
                   }
                   
                   return artista;
                }
            }
        }
        return null;
    }
    
    
    // TRAYECTORIA
    public List<String> obtenerTrayectoria(Long idArtista) throws SQLException {
        List<String> trayectoria = new ArrayList<>();
        String sql = "SELECT e.id AS idEspectaculo, e.nombre AS nombreEspectaculo, " +
                     "n.id AS idNumero, n.nombre AS nombreNumero " +
                     "FROM espectaculo e " +
                     "JOIN numero n ON e.id = n.idEspectaculo " +
                     "JOIN numero_artista na ON n.id = na.idNumero " +
                     "WHERE na.idArtista=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idArtista);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    trayectoria.add("Espectáculo [" + rs.getLong("idEspectaculo") + "] " +
                                    rs.getString("nombreEspectaculo") +
                                    " Número [" + rs.getLong("idNumero") + "] " +
                                    rs.getString("nombreNumero"));
                }
            }
        }
        return trayectoria;
    }
    

    // BUSCAR ARTISTA POR EMAIL
    
    public Artista buscarPorEmail(String email) throws SQLException {

        String sql = "SELECT a.*, p.nombre, p.email, p.nacionalidad " +
                     "FROM artista a " +
                     "JOIN persona p ON a.idPersona = p.id " +
                     "WHERE p.email = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Artista(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("nacionalidad"),
                    rs.getLong("idArt"),
                    rs.getString("apodo"),
                    Especialidad.valueOf(rs.getString("especialidades"))
                );
            }
        }
        return null;
    }

    //BUSCAR ARTISTA POR NUMERO
    public List<Artista> buscarArtistasPorNumero(Long idNumero) throws SQLException {
        List<Artista> artistas = new ArrayList<>();
        String sql = "SELECT a.idArt, p.nombre, p.nacionalidad, a.apodo, ea.especialidad " +
                     "FROM participa pa " +
                     "JOIN artista a ON pa.idArt = a.idArt " +
                     "JOIN persona p ON a.idArt = p.id " +
                     "JOIN especialidad_artista ea ON a.idArt = ea.idArt " +
                     "WHERE pa.idNumero = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idNumero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artista art = new Artista(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getLong("idArt"),
                        rs.getString("apodo"),
                        Especialidad.valueOf(rs.getString("especialidades"))
                        
                    );
                    
                    artistas.add(art);
                }
            }
        }
        return artistas;
    }

    
    // LISTAR TODOS LOS ARTISTAS
        public List<Artista> listarTodos() throws SQLException {

        List<Artista> lista = new ArrayList<>();

        String sql = "SELECT a.*, p.nombre, p.email, p.nacionalidad " +
                     "FROM artista a " +
                     "JOIN persona p ON a.idPersona = p.id";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Artista artista = new Artista(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("nacionalidad"),
                    rs.getLong("idArt"),
                    rs.getString("apodo"),
                    Especialidad.valueOf(rs.getString("especialidades"))
                );

                lista.add(artista);
            }
        }
        return lista;
    }

    // ACTUALIZAR ARTISTA
    public void actualizar(long idPersona, String apodo, Especialidad especialidad) throws SQLException {

        String sql = "UPDATE artista SET apodo = ?, especialidades = ? WHERE idPersona = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, apodo);
            ps.setString(2, especialidad.name());
            ps.setLong(3, idPersona);

            ps.executeUpdate();
        }
    }

   
    // ELIMINAR ARTISTA
    public boolean eliminar(long idPersona) throws SQLException {

        String sql = "DELETE FROM artista WHERE idPersona = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idPersona);

            return ps.executeUpdate() > 0;
        }
    }

    // ASIGNAR NÚMERO AL ARTISTA
    public void asignarNumero(long idPersona, long idNumero) throws SQLException {

        String sql = "UPDATE artista SET idNumero = ? WHERE idPersona = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idNumero);
            ps.setLong(2, idPersona);

            ps.executeUpdate();
        }
    }
}
