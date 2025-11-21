package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Coordinacion;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;
import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Persona;
import com.luciagf.modelo.Sesion;

import dao.EspectaculoDAO;
import dao.PersonaDAO;

public class EspectaculoServicio {

	private EspectaculoDAO espectaculoDAO;
	private PersonaDAO personaDAO;

	public EspectaculoServicio(Connection con) {
		this.espectaculoDAO = new EspectaculoDAO(con);
		this.personaDAO=new PersonaDAO(con);
	}

	
	//CREAR
	public boolean crearEspectaculo(Espectaculo espectaculo, Sesion sesion) {
        try {
        	        	
        	if(espectaculo.getNombre().length()>25) {
        		System.out.println("Error: el nombre no puede superar los 25 caracteres");
        		return false;
        	}
        	
        	 if (espectaculoDAO.buscarPorNombre(espectaculo.getNombre()) != null) {
                 System.out.println("Error: ya existe un espectáculo con ese nombre.");
                 return false;
             }

             // Validar fechas
             long dias = ChronoUnit.DAYS.between(espectaculo.getFechaini(), espectaculo.getFechafin());
             if (dias > 365) {
                 System.out.println("Error: el periodo no puede superar 1 año");
                 return false;
             }

             // Asignar coordinación
             if (sesion.getPerfilActual() == Perfil.COORDINACION) {
                 espectaculo.setCoordinacion((Coordinacion)sesion.getPersonaActual());
             } else if (sesion.getPerfilActual() == Perfil.ADMIN) {
                 Coordinacion coordinador = personaDAO.seleccionarCoordinador();
                 espectaculo.setCoordinacion((Coordinacion)coordinador);
             }

             // Insertar espectáculo
             espectaculoDAO.insertar(espectaculo);
             System.out.println("Espectáculo creado con ID: " + espectaculo.getId());

             // Paso siguiente: CU5B (gestionar números)
             return true;

         } catch (SQLException e) {
             System.out.println("Error al crear espectáculo: " + e.getMessage());
             return false;
         }
     }
	
	//MODIFICAR
	public boolean modificarEspectaculo(Espectaculo espectaculo) {
	    try {
	        
	        if (espectaculo.getNombre().length() > 25) {
	            System.out.println("Error: el nombre no puede superar 25 caracteres.");
	            return false;
	        }

	        long dias = ChronoUnit.DAYS.between(espectaculo.getFechaini(), espectaculo.getFechafin());
	        if (dias > 365) {
	            System.out.println("Error: el periodo de vigencia no puede superar 1 año.");
	            return false;
	        }

	        espectaculoDAO.actualizar(espectaculo);
	        System.out.println("Espectáculo actualizado correctamente.");
	        return true;

	    } catch (SQLException e) {
	        System.out.println("Error al modificar espectáculo: " + e.getMessage());
	        return false;
	    }
	}

	
	
	
	
	// VER ESPECTACULO BASICO
	public void verEspectaculosBasico() {
		try {
		List<Espectaculo> lista = espectaculoDAO.listarBasicos();
		if(lista.isEmpty()) {
			System.out.println("No hay espectaculos registrados");
		}
		System.out.println("\n=== LISTA DE ESPECTÁCULOS ===");
		for (Espectaculo e : lista) {
			System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Inicio: " + e.getFechaini()
					+ " | Fin: " + e.getFechafin());
		}
	}	catch(Exception e) {
			System.out.println("Error al listar espectaculos");
	}
}
		
		
	
	// VER ESPECTACULOS COMPLETOS
	public void verEspectaculoCompleto(Long idEspectaculo) {
        try {
            Espectaculo esp = espectaculoDAO.buscarEspectaculoCompleto(idEspectaculo);
            if (esp == null) {
                System.out.println("No se encontró el espectáculo con ID " + idEspectaculo);
                return;
            }
            if(esp.getCoordinacion()!=null) {
            System.out.println("\n=== ESPECTÁCULO COMPLETO ===");
            System.out.println("ID: " + esp.getId() + " | Nombre: " + esp.getNombre());
            System.out.println("Fechas: " + esp.getFechaini() + " - " + esp.getFechafin());

            System.out.println("\n--- Coordinación ---");
            System.out.println("Nombre: " + esp.getCoordinacion().getNombre());
            System.out.println("Email: " + esp.getCoordinacion().getEmail());
            System.out.println("Nacionalidad: " + esp.getCoordinacion().getNacionalidad());
            System.out.println("Senior: " + esp.getCoordinacion().isSenior());
            if (esp.getCoordinacion().getFechaSenior() != null) {
                System.out.println("Fecha Senior: " + esp.getCoordinacion().getFechaSenior());
            }
          }

            if(esp.getNumeros()!= null && !esp.getNumeros().isEmpty()) {
            System.out.println("\n--- Números ---");
            for (Numero n : esp.getNumeros()) {
                System.out.println("Orden " + n.getOrden() + ": " + n.getNombre() +
                                   " (" + n.getDuracion() + " min)");

                System.out.println("   Artistas:");
                for (Artista a : n.getArtistas()) {
                    System.out.println("   - " + a.getNombre() +
                                       " (" + a.getNacionalidad() + ")" +
                                       (a.getApodo() != null ? " Apodo: " + a.getApodo() : ""));
                    if (a.getEspecialidades() != null) {
                        System.out.println("Especialidad: " + a.getEspecialidades());
                    }
                }
            }
            }else {
            	System.out.println("Este espectaculo no tiene numeros");
            }
        } catch (SQLException e) {
            System.out.println("Error al ver espectáculo completo: " + e.getMessage());
        }
    }
	
	
}
