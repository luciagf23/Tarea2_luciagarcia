package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Persona;

public class PersonaDAO {

    private Connection con;

    public PersonaDAO(Connection con) {
        this.con = con;
    }

    // INSERTAR PERSONA
    public void insertar(Persona persona) throws SQLException {
        String sql = "INSERT INTO persona (id, nombre, email, nacionalidad) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, persona.getId());
            ps.setString(2, persona.getNombre());
            ps.setString(3, persona.getEmail());
            ps.setString(4, persona.getNacionalidad());
           
            ps.executeUpdate();
        }
    }

    // BUSCAR POR ID
    public Persona buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "LEFT JOIN artista a ON p.id = a.idPersona " +
                     "LEFT JOIN coordinacion c ON p.id = c.idPersona " +
                     "WHERE p.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String perfil = rs.getString("perfil");

                   if("artista".equalsIgnoreCase(perfil)) {

                        return new Artista(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad"),
                            rs.getLong("idArt"),
                            rs.getString("apodo"),
                            Especialidad.valueOf(rs.getString("especialidad").toUpperCase())
                           
                        );
                    } else if ("coordinacion".equalsIgnoreCase(perfil)) {
                        return new Coordinacion(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad"),
                            rs.getBoolean("senior"),
                            rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                        );
                    } else {
                        return new Persona(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad")
                        );
                    }
                }
            }
        }
        return null;
    }

    // BUSCAR POR EMAIL
    public Persona buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "LEFT JOIN artista a ON p.id = a.idPersona " +
                     "LEFT JOIN coordinacion c ON p.id = c.idPersona " +
                     "WHERE p.email = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String perfil = rs.getString("perfil");

                    if ("artista".equalsIgnoreCase(perfil)) {
                        String especStr = rs.getString("especialidad");
                        Especialidad espec = null;
                        if (especStr != null && !especStr.isBlank()) {
                            espec = Especialidad.valueOf(especStr.trim().toUpperCase());
                        }

                        return new Artista(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad"),
                            rs.getLong("idArt"),
                            rs.getString("apodo"),
                            espec
                        );
                    } else if ("coordinacion".equalsIgnoreCase(perfil)) {
                        return new Coordinacion(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad"),
                            rs.getBoolean("senior"),
                            rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                        );
                    } else {
                        return new Persona(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("nacionalidad")
                        );
                    }
                }
            }
        }
        return null;
    }

    // LISTAR TODAS LAS PERSONAS
    public List<Persona> listarTodas() throws SQLException {
        List<Persona> personas = new ArrayList<>();
        String sql = "SELECT * FROM persona p " +
                     "LEFT JOIN artista a ON p.id = a.idPersona " +
                     "LEFT JOIN coordinacion c ON p.id = c.idPersona";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String perfil = rs.getString("perfil");

                if ("artista".equalsIgnoreCase(perfil)) {
                    String especStr = rs.getString("especialidad");
                    Especialidad espec = null;
                    if (especStr != null && !especStr.isBlank()) {
                        espec = Especialidad.valueOf(especStr.trim().toUpperCase());
                    }

                    personas.add(new Artista(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getLong("idArt"),
                        rs.getString("apodo"),
                        espec
                    ));
                } else if ("coordinacion".equalsIgnoreCase(perfil)) {
                    personas.add(new Coordinacion(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getBoolean("senior"),
                        rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                    ));
                } else {
                    personas.add(new Persona(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad")
                    ));
                }
            }
        }
        return personas;
    }
    
 // ACTUALIZAR persona
    public void actualizar(Persona persona) throws SQLException {
        String sql = "UPDATE persona SET nombre = ?, email = ?, nacionalidad = ?, idCredenciales = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getEmail());
            ps.setString(3, persona.getNacionalidad());
            ps.setLong(4, persona.getIdCredenciales()); 
            ps.setLong(5, persona.getId());
            ps.executeUpdate();
        }
    }
    
    // ELIMINAR persona por ID
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    
    
}
