
package com.luciagf.modelo;

/**
 * @author Lucía García Fernández
 * @version 1.0
 * @since 2025
 */

public class Sesion {

	private Long id;
	private String nombreUsuario;
	private String password;
	private Perfil perfilActual;
	private Persona personaActual;

	public Sesion(Long id, Perfil perfilActual) {
		super();
		this.id = id;
		this.perfilActual = perfilActual;
	}
	

	public Sesion() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Perfil getPerfilActual() {
		return perfilActual;
	}

	public void setPerfilActual(Perfil perfilActual) {
		this.perfilActual = perfilActual;
	}
	
	 public Persona getPersonaActual() {
	        return personaActual;
	    }

	    public void setPersonaActual(Persona personaActual) {
	        this.personaActual = personaActual;
	    }
	
	// Métodos de login/logout
    public void login(Long id, String nombreUsuario, String password, Perfil perfil) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.perfilActual = perfil;
    }
	
    public void logout() {
        this.id = null;
        this.nombreUsuario = null;
        this.password = null;
        this.perfilActual = Perfil.INVITADO;
        this.personaActual=null;
    }
    
    // Saber si hay sesión activa
    public boolean isAutenticado() {
        return perfilActual != Perfil.INVITADO;
    }

	@Override
	public String toString() {
		return "Sesion [id=" + id + ", nombreUsuario=" + nombreUsuario + ", password=" + password + ", perfilActual="
				+ perfilActual + "]";
	}

}