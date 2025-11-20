package controlador;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Credencial;
import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Persona;
import com.luciagf.modelo.Sesion;

import dao.CredencialDAO;
import dao.PersonaDAO;

public class PersonaServicio {

	private PersonaDAO personaDAO;
	private CredencialDAO credencialDAO;
	private Credencial usuarioActual;
	private Sesion sesion;

	public PersonaServicio(Connection con, Sesion sesion) {
        this.personaDAO = new PersonaDAO(con);
        this.credencialDAO=new CredencialDAO(con);
        this.usuarioActual=null;
        this.sesion=sesion;
    }
	
	//LOGIN
	public boolean login(String nombreUsuario, String password) throws SQLException {
		try {
		if(nombreUsuario==null || nombreUsuario.trim().isEmpty() || password==null
				|| password.trim().isEmpty()) {
			
			System.out.println("usuario y contraseña no pueden estar vacios");
			return false;
		}
		
		if(nombreUsuario.equals("admin")&& password.equals("admin")) {
			usuarioActual = new Credencial(0L, "admin", "admin", Perfil.ADMIN);
            System.out.println("Login correcto como ADMIN.");
            return true;
		}
		
		//Buscar credencial
		Credencial credencial=credencialDAO.buscarPorUsuario(nombreUsuario);
		if(credencial !=null && credencial.getPassword().equals(password)) {
			
			sesion.login(credencial.getId(), 
					credencial.getNombre(),
                    credencial.getPassword(), 
                    credencial.getPerfil());
			System.out.println("Login correcto");
			return true;
			
		}else {
			System.out.println("Error: credenciales incorrectas");
			return false;
		}
		
		}catch (SQLException e) {
			System.out.println("Error en login: " +e.getMessage());
			return false;
		}
	}
	
	
	//LOGOUT
	public void logout() {
	    if (sesion.isAutenticado()) {
	        sesion.logout();
	        System.out.println("Sesión cerrada. Ahora eres Invitado.");
	    } else {
	        System.out.println("No hay sesión activa.");
	    }
	}
	
	// Obtener perfil actual
    public Perfil verPerfilActual() {
        return sesion.getPerfilActual();
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

	        // Insertar credencial 
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

	//Buscar persona por id
	public Persona buscarPersonaPorId(Long id) {
	    try {
	        return personaDAO.buscarPorId(id);
	    } catch (SQLException e) {
	        System.out.println("Error al buscar persona: " + e.getMessage());
	        return null;
	    }
	}

	
	//Listar personas
	public List<Persona> listarPersonas() {
	    try {
	        return personaDAO.listarTodas();
	    } catch (SQLException e) {
	        System.out.println("Error al listar personas: " + e.getMessage());
	        return null;
	    }
	}
	
	
	
	//Actualizar persona
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

