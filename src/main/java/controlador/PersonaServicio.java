package controlador;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Credencial;
import com.luciagf.modelo.Persona;

import dao.CredencialDAO;
import dao.PersonaDAO;

public class PersonaServicio {

	private PersonaDAO personaDAO;
	private CredencialDAO credencialDAO;

	public PersonaServicio(Connection con) {
        this.personaDAO = new PersonaDAO(con);
    }
	
	
	
	public boolean registrarPersona(Persona persona, Credencial credencial) {
	    try {
	        // Validar email único
	        if (personaDAO.buscarPorEmail(persona.getEmail()) != null) {
	            System.out.println("Error: el email ya existe en el sistema");
	            return false;
	        }

	        // Validar nombre de usuario único
	        if (credencialDAO.buscarPorUsuario(credencial.getNombre()) != null) {
	            System.out.println("Error: el nombre de usuario ya existe en el sistema");
	            return false;
	        }

	        // Insertar credencial primero
	        credencialDAO.insertar(credencial);

	        // Vincular credencial a persona
	        persona.setIdCredenciales(credencial.getId());

	        // Insertar persona con su idCredenciales
	        personaDAO.insertar(persona);

	        System.out.println("Persona registrada correctamente con ID: " + persona.getId());
	        return true;

	    } catch (SQLException e) {
	        System.out.println("Error al registrar persona: " + e.getMessage());
	        return false;
	    }
	}

	public Persona buscarPersonaPorId(Long id) {
	    try {
	        return personaDAO.buscarPorId(id);
	    } catch (SQLException e) {
	        System.out.println("Error al buscar persona: " + e.getMessage());
	        return null;
	    }
	}

	
	public List<Persona> listarPersonas() {
	    try {
	        return personaDAO.listarTodas();
	    } catch (SQLException e) {
	        System.out.println("Error al listar personas: " + e.getMessage());
	        return null;
	    }
	}
	
	public boolean actualizarPersona(Persona persona, Credencial credencial) {
	    try {
	        personaDAO.actualizar(persona);
	        credencialDAO.actualizar(credencial);
	        System.out.println("Persona y credencial actualizadas correctamente");
	        return true;
	    } catch (SQLException e) {
	        System.out.println("Error al actualizar: " + e.getMessage());
	        return false;
	    }
	}
	// Eliminar persona y credencial asociada
	public boolean eliminarPersona(Long idPersona) {
	    try {
	        Persona persona = personaDAO.buscarPorId(idPersona);
	        if (persona != null) {
	            credencialDAO.eliminar(persona.getIdCredenciales());
	            personaDAO.eliminar(idPersona);
	            System.out.println("Persona y credencial eliminadas correctamente");
	            return true;
	        }
	        return false;
	    } catch (SQLException e) {
	        System.out.println("Error al eliminar persona: " + e.getMessage());
	        return false;
	    }
	}


    
}

