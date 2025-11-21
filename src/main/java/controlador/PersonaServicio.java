package controlador;


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.luciagf.modelo.Credencial;
import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Persona;
import com.luciagf.modelo.Sesion;

import dao.CredencialDAO;
import dao.PersonaDAO;

public class PersonaServicio {

	private PersonaDAO personaDAO;
	private PaisServicio paisServicio;
	private CredencialDAO credencialDAO;
	private Credencial usuarioActual;
	private Sesion sesion;
	private String usuarioAdmin;
    private String passwordAdmin;

	public PersonaServicio(Connection con, Sesion sesion) {
        this.personaDAO = new PersonaDAO(con);
        this.credencialDAO=new CredencialDAO(con);
        this.usuarioActual=null;
        this.sesion=sesion;
    }
	
	 public PersonaServicio() {
	        cargarCredenciales();
	    }
	 
	
	 // Método para cargar las credenciales desde el fichero properties
    private void cargarCredenciales() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("application.properties")) {
            props.load(fis);
            usuarioAdmin = props.getProperty("usuarioAdmin");
            passwordAdmin = props.getProperty("passwordAdmin");
        } catch (IOException e) {
            System.out.println("Error al cargar credenciales: " + e.getMessage());
        }
    }
    
	//LOGIN
	public boolean login(String usuario, String password) {
	    try {
	    	
	    	//Login admin
	    	if(usuarioAdmin!=null && passwordAdmin!=null &&
	    			usuarioAdmin.equals(usuario) && passwordAdmin.equals(password)) {
	    	usuarioActual = new Credencial(usuario, password);	
	    	Persona admin=new Persona();	
	    	admin.setNombre("Administrador");
	    	admin.setPerfil(Perfil.ADMIN);
	    	
	    	sesion.setPersonaActual(admin);
	    	sesion.setPerfilActual(admin.getPerfil());
	    	
	    	System.out.println("Login correcto. Bienvenido/a Administrador");
	    	}
	    	
	    	
	        // Buscar persona por usuario y contraseña
	        Persona persona = personaDAO.buscarPorUsuario(usuario, password);
	        if (persona != null) {
	        usuarioActual = new Credencial(usuario, password);
	            // Guardar la persona en la sesión
	            sesion.setPersonaActual(persona);

	            // Guardar el perfil de esa persona en la sesión
	            sesion.setPerfilActual(persona.getPerfil());

	            System.out.println("Login correcto. Bienvenido/a " + persona.getNombre() +
	                               " con perfil " + persona.getPerfil());
	            return true;
	        } else {
	            System.out.println("Usuario o contraseña incorrectos.");
	            return false;
	        }

	    } catch (SQLException e) {
	        System.out.println("Error en login: " + e.getMessage());
	        return false;
	    }
	}

	
	//LOGOUT
	public void logout() {
	    if (sesion.isAutenticado()) {
	        sesion.logout();
	        System.out.println("Sesión cerrada. Ahora eres Invitado");
	        usuarioActual = null;
	    } else {
	        System.out.println("No hay sesión activa");
	    }
	}
	
	// Obtener perfil actual
    public Perfil verPerfilActual() {
        return sesion.getPerfilActual();
    }

	
	//REGISTRAR PERSONA
	public boolean registrarPersona(Persona persona, Credencial credencial, int idPais) {
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
	        
	        //Validar pais
	        PaisServicio paisServicio=new PaisServicio();
	        if(!paisServicio.existePais(idPais)) {
	        	System.out.println("El id del país no es válido");
	        	return false;
	        }
	        String nacionalidad = paisServicio.obtenerPaisPorId(idPais);
	        persona.setNacionalidad(nacionalidad);

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

