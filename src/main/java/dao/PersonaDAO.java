package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Especialidad;
import com.luciagf.modelo.Persona;


public class PersonaDAO {
	
	private Connection con;
	
	public PersonaDAO(Connection con) {
		this.con=con;
	}
		
	// INSERTAR una persona
    public boolean insertar(Persona persona) throws SQLException {
        String sql = "INSERT INTO persona (nombre, email, nacionalidad) VALUES (?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getEmail());
            ps.setString(3, persona.getNacionalidad());

            return ps.executeUpdate()>0;
        }
    }

    // ACTUALIZAR persona
    public boolean actualizar (Persona persona) throws SQLException {
        String sql = "UPDATE persona SET nombre = ?, email = ?, nacionalidad = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getEmail());
            ps.setString(3, persona.getNacionalidad());
            ps.setLong(4, persona.getId());

            return ps.executeUpdate() > 0;
        } 
        
    }

    // ELIMINAR
    public boolean eliminar(long id) throws SQLException {
        String sql = "DELETE FROM persona WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // BUSCAR POR ID
    public Persona buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM persona WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String perfil = rs.getString("perfil");

                    if ("artista".equalsIgnoreCase(perfil)) {
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
                            // convertir especialidad a enum
                            String especStr = rs.getString("especialidad");
                            Especialidad espec = null;
                            if (especStr != null && !especStr.isBlank()) {
                                try {
                                    espec = Especialidad.valueOf(especStr.trim().toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    System.out.println("Valor no válido en BD: " + especStr);
                                }
                            }

                            return new Artista(
                                rs.getLong("id"),              // persona.id
                                rs.getString("nombre"),        // persona.nombre
                                rs.getString("email"),         // persona.email
                                rs.getString("nacionalidad"),  // persona.nacionalidad
                                rs.getLong("idArt"),           // artista.idArt
                                rs.getString("apodo"),         // artista.apodo
                                espec                          // artista.especialidad
                            );
                        } else if ("coordinacion".equalsIgnoreCase(perfil)) {
                            return new Coordinacion(
                                rs.getLong("id"),              // persona.id
                                rs.getString("nombre"),        // persona.nombre
                                rs.getString("email"),         // persona.email
                                rs.getString("nacionalidad"),  // persona.nacionalidad
                                rs.getBoolean("senior"),       // coordinacion.senior
                                rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                            );
                        }
                    }
                }
            }
            return null;
        }


    /*
    // LISTAR todas las personas
    public List<Persona> listarTodas() throws SQLException {
        List<Persona> lista = new ArrayList<>();

        String sql = "SELECT * FROM persona";
        try (Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Persona p = new Persona(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad")
                );
                lista.add(p);
            }
        }
        return lista;
    }
    */
    }
}
