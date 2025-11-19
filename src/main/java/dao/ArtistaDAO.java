package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Especialidad;

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
