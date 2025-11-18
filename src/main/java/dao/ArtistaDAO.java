package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public void insertar(Artista artista) throws SQLException {
        // Primero insertamos la persona
        String sqlPersona = "INSERT INTO persona (id, nombre, email, nacionalidad, perfil) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlPersona)) {
            ps.setLong(1, artista.getId());
            ps.setString(2, artista.getNombre());
            ps.setString(3, artista.getEmail());
            ps.setString(4, artista.getNacionalidad());
            ps.setString(5, "artista");
            ps.executeUpdate();
        }

        // Luego insertamos el artista
        String sqlArtista = "INSERT INTO artista (idPersona, idArt, apodo, especialidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlArtista)) {
            ps.setLong(1, artista.getId());
            ps.setLong(2, artista.getId());
            ps.setString(3, artista.getApodo());
            ps.setString(4, artista.getEspecialidad().name()); //convierte a String
            ps.executeUpdate();
        }
    }

    // BUSCAR POR ID
    public Artista buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "JOIN artista a ON p.id = a.idPersona " +
                     "WHERE p.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Artista(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getLong("idArt"),
                        rs.getString("apodo"),
                        Especialidad.valueOf(rs.getString("especialidad").toUpperCase())
                    );
                }
            }
        }
        return null;
    }

    // BUSCAR POR EMAIL
    public Artista buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "JOIN artista a ON p.id = a.idPersona " +
                     "WHERE p.email = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Artista(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getLong("idArt"),
                        rs.getString("apodo"),
                        Especialidad.valueOf(rs.getString("especialidad").toUpperCase())
                    );
                }
            }
        }
        return null;
    }

    // LISTAR TODOS LOS ARTISTAS
    public List<Artista> listarTodos() throws SQLException {
        List<Artista> artistas = new ArrayList<>();
        String sql = "SELECT * FROM persona p " +
                     "JOIN artista a ON p.id = a.idPersona";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                artistas.add(new Artista(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("nacionalidad"),
                    rs.getLong("idArt"),
                    rs.getString("apodo"),
                    Especialidad.valueOf(rs.getString("especialidad").toUpperCase())
                ));
            }
        }
        return artistas;
    }
}
