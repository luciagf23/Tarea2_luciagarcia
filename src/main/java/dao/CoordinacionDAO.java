package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.luciagf.modelo.Coordinacion;

/**
* Clase CoordinacionDAO.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/

public class CoordinacionDAO {

	private Connection con;

    public CoordinacionDAO(Connection con) {
        this.con = con;
    }

    // INSERTAR COORDINACION
    public void insertar(Long idPersona, boolean senior, LocalDate fechaSenior) throws SQLException {
        // Primero insertamos la persona
        String sqlPersona = "INSERT INTO persona (id, nombre, email, nacionalidad, perfil) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlPersona)) {
            ps.setLong(1, idPersona);
            ps.setBoolean(2, senior);
            ps.setDate(3, Date.valueOf(fechaSenior));
            
            ps.executeUpdate();
        }

        // Luego insertamos la coordinación
        String sqlCoord = "INSERT INTO coordinacion (idPersona, senior, fechasenior) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlCoord)) {
            ps.setLong(1, idPersona);
            ps.setBoolean(2,senior);
            if (fechaSenior != null) {
                ps.setDate(3, Date.valueOf(fechaSenior));
            } else {
                ps.setDate(3, null);
            }
            ps.executeUpdate();
        }
    }

    // BUSCAR POR ID
    public Coordinacion buscarPorId(Long id) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "JOIN coordinacion c ON p.id = c.idPersona " +
                     "WHERE p.id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coordinacion(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getBoolean("senior"),
                        rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }

    // BUSCAR POR EMAIL
    public Coordinacion buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM persona p " +
                     "JOIN coordinacion c ON p.id = c.idPersona " +
                     "WHERE p.email = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coordinacion(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("nacionalidad"),
                        rs.getBoolean("senior"),
                        rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                    );
                }
            }
        }
        return null;
    }
    
    // ACTUALIZAR Coordinacion
    public void actualizar(long id, Boolean senior, LocalDate fechaSenior) throws SQLException {

        String sql = "UPDATE artista SET apodo = ?, especialidades = ? WHERE idPersona = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setBoolean(2, senior);
            //ps.setLocalDate(3, fechaSenior);

            ps.executeUpdate();
        }
    }

    // LISTAR TODAS LAS COORDINACIONES
    public List<Coordinacion> listarTodas() throws SQLException {
        List<Coordinacion> coordinaciones = new ArrayList<>();
        String sql = "SELECT * FROM persona p " +
                     "JOIN coordinacion c ON p.id = c.idPersona";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                coordinaciones.add(new Coordinacion(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("nacionalidad"),
                    rs.getBoolean("senior"),
                    rs.getDate("fechasenior") != null ? rs.getDate("fechasenior").toLocalDate() : null
                ));
            }
        }
        return coordinaciones;
    }

}

