/**
* Clase Main.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/

package vista;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Credencial;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;
import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Persona;
import com.luciagf.modelo.Sesion;

import controlador.ArtistaServicio;
import controlador.EspectaculoServicio;
import controlador.NumeroServicio;
import controlador.PaisServicio;
import controlador.PersonaServicio;
import dao.ConexionBD;

public class Main {
	
private static Scanner teclado=new Scanner(System.in);
private static Sesion sesion = new Sesion(); 


public static void main(String[] args) {
	try {
	Connection con = ConexionBD.getConexion();

	
	PersonaServicio personaServicio = new PersonaServicio(con, sesion);
	NumeroServicio numeroServicio=new NumeroServicio(con);
	ArtistaServicio artistaServicio=new ArtistaServicio(con);

    EspectaculoServicio espectaculoServicio = new EspectaculoServicio(con);

    System.out.println("Bienvenidos al sistema de gestión del Circo de Lucía");
	System.out.println("--------------------------------------------------------");
	System.out.println("Aquí podrás gestionar espectáculos, artistas y mucho más");
	System.out.println();
	
	
    boolean salir = false;
    while (!salir) {
        mostrarMenu();
        System.out.print("Elige opción: ");
        int opcion = teclado.nextInt();
        teclado.nextLine(); 

        switch (sesion.getPerfilActual()) {
            case INVITADO:
                switch (opcion) {
                    case 1:
                        espectaculoServicio.verEspectaculosBasico();
                        break;
                    case 2:
                        System.out.print("Usuario: ");
                        String usuario = teclado.nextLine();
                        System.out.print("Contraseña: ");
                        String pass = teclado.nextLine();
                        personaServicio.login(usuario, pass);
                        break;
                    case 0:
                        salir = true;
                        break;
                }
                break;

            case ADMIN:
                switch (opcion) {
                
                	case 1:System.out.println("Registrar nueva persona");
                			
                			System.out.println("Nombre: ");
                			String nombre=teclado.nextLine();
                			
                			System.out.print("Email: ");
                	        String email = teclado.nextLine();

                	        System.out.print("Usuario: ");
                	        String usuario = teclado.nextLine();

                	        System.out.print("Contraseña: ");
                	        String password = teclado.nextLine();
                	        
                	        PaisServicio paisServicio=new PaisServicio();
                	        paisServicio.mostrarPaises();
                	        
                	        System.out.print("Introduce el id del país: ");
                	        int idPais = teclado.nextInt();
                	        teclado.nextLine();
                	        
                	        Persona persona=new Persona();
                	        persona.setNombre(nombre);
                	        persona.setEmail(email);
                	        
                	        Credencial credencial=new Credencial();
                	        credencial.setNombre(nombre);
                	        credencial.setPassword(password);
                	        
                	        //registrar persona
                	        boolean ok=personaServicio.registrarPersona(persona, credencial, idPais);
                	        
                	        if (!ok) {
                	            System.out.println("No se pudo registrar la persona");
                	        }
                	        
                	break;
                	case 2:System.out.println("Gestionar credenciales");
                			
                		
                	
                		break;
                		
                		
                		
                	case 3:System.out.println("Gestionar espectaculos");
                	
                		break;
                    case 4: // Ver todos los datos del circo (CU4)
                        System.out.print("ID del espectáculo: ");
                        Long idEsp = teclado.nextLong();
                        espectaculoServicio.verEspectaculoCompleto(idEsp);
                        break;
                    case 0:
                        personaServicio.logout();
                        break;
                        default:System.out.println("Opcion no válida");
                }
                break;

            case COORDINACION:
                switch (opcion) {
                	case 1:
                		System.out.println("Crear o modificar espectaculos");
                		
                		System.out.print("ID del espectáculo (0 si es nuevo): ");
                	    Long idEsp = teclado.nextLong();
                	    teclado.nextLine();

                	    System.out.print("Nombre del espectáculo: ");
                	    String nombre= teclado.nextLine();
                	    
                	 
                	    System.out.print("Fecha de inicio (YYYY-MM-DD): ");
                	    String fechaIni = teclado.nextLine();
                	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                	    LocalDate fechaInicio = null;
                	    try {
                	        fechaInicio = LocalDate.parse(fechaIni, formatter);
                	    } catch (DateTimeParseException e) {
                	        System.out.println("Formato de fecha incorrecto. Usa DD/MM/AAAA.");
                	        break;
                	    }
            
                	    
                	    System.out.print("Fecha fin (YYYY-MM-DD): ");
                	    String fechaFin = teclado.nextLine();
                	    LocalDate fechaFinal = null;
                	    try {
                	        fechaFinal = LocalDate.parse(fechaFin, formatter);
                	    } catch (DateTimeParseException e) {
                	        System.out.println("Formato de fecha incorrecto. Usa DD/MM/AAAA.");
                	        break;
                	    }
                	    
                	    Espectaculo espectaculo=new Espectaculo();
                	    espectaculo.setId(idEsp !=0 ? idEsp :null);
                	    espectaculo.setNombre(nombre);
                	    espectaculo.setFechaini(fechaInicio);
                	    espectaculo.setFechafin(fechaFinal);
                	    
                	    if (idEsp == 0) {
                	        espectaculoServicio.crearEspectaculo(espectaculo, sesion);
                	        System.out.println("Espectáculo creado correctamente.");
                	    } else {
                	        espectaculoServicio.modificarEspectaculo(espectaculo);
                	        System.out.println("Espectáculo modificado correctamente.");
                	    }
                	    
                		break;
                	case 2:
                		
                		boolean salirNumeros = false;
                	    while (!salirNumeros) {
                	        System.out.println("== Gestión de números ==");
                	        System.out.println("1. Crear número");
                	        System.out.println("2. Modificar número");
                	        System.out.println("3. Eliminar número");
                	        System.out.println("4. Listar números");
                	        System.out.println("0. Volver");

                	        System.out.print("Elige opción: ");
                	        int opcionNum = teclado.nextInt();
                	        teclado.nextLine();

                	        switch (opcionNum) {
                	            case 1:
                	                System.out.print("Nombre del número: ");
                	                String nombreNum = teclado.nextLine();
                	                
                	                System.out.print("Duración en minutos: ");
                	                int duracionNum = teclado.nextInt();
                	                teclado.nextLine();

                	                Numero numero = new Numero();
                	                numero.setNombre(nombreNum);
                	                numero.setDuracion(duracionNum);

                	                numeroServicio.crearNumero(numero);
                	                System.out.println("Número creado correctamente.");
                	                break;

                	            case 2:
                	                System.out.print("ID del número a modificar: ");
                	                Long idNum = teclado.nextLong();
                	                teclado.nextLine();

                	                System.out.print("Nombre: ");
                	                String nuevoNombre = teclado.nextLine();

                	                Numero numMod = new Numero();
                	                numMod.setId(idNum);
                	                numMod.setNombre(nuevoNombre);

                	                numeroServicio.modificarNumero(numMod);
                	                System.out.println("Número modificado correctamente.");
                	                break;

                	            case 3:
                	                System.out.print("ID del número a eliminar: ");
                	                Long idDel = teclado.nextLong();
                	                teclado.nextLine();

                	                numeroServicio.eliminarNumero(idDel, sesion);
                	                System.out.println("Número eliminado correctamente.");
                	                break;

                	            case 4:
                	            	numeroServicio.listarNumeros();
                	                break;

                	            case 0:
                	                salirNumeros = true;
                	                break;

                	            default:
                	                System.out.println("Opción no válida");
                	        }
                	    }
                		
                		break;
                		
                		
                	case 3:
                		System.out.println("Asignar artistas a números");
                		
                		System.out.println("ID del numero: ");
                		Long idNumAsignar=teclado.nextLong();
                		teclado.nextLine();
                		
                		System.out.print("ID del artista: ");
                	    Long idArtista = teclado.nextLong();
                	    teclado.nextLine();
                	    
                	    numeroServicio.asignarArtistaANumero(idNumAsignar, idArtista, sesion);
                	    System.out.println("Artista asigando correctamente");
                	    
                		break;
                		
                		
                    case 4: // Ver información completa de espectáculos (CU4)
                        System.out.print("ID del espectáculo: ");
                        Long idEspectaculo = teclado.nextLong();
                        espectaculoServicio.verEspectaculoCompleto(idEspectaculo);
                        break;
                    case 0:
                        personaServicio.logout();
                        break;
                    default:
                        	System.out.println("Opción no válida");
                }
                break;

            case ARTISTA:
                switch (opcion) {
                case 1:
                	System.out.println("== Ver mi ficha personal ==");
                	Long idArtistaSesion = sesion.getPersonaActual().getId();
                    artistaServicio.verFicha(idArtistaSesion);
                	break;
                case 2:
                	System.out.println("== Ver mis espectaculos y numeros ==");
                	Long idArtista = sesion.getPersonaActual().getId();
                    artistaServicio.verEspectaculosYNumeros(idArtista);
                	break;
                case 0:
                      personaServicio.logout();
                      break;
                default: System.out.println("Opción no válida");
                }
                break;
        }
    }
} catch (Exception e) {
    System.out.println("Error en la aplicación: " + e.getMessage());
	}
}

	
    public static void mostrarMenu() {
        Perfil perfilActual = sesion.getPerfilActual(); 

        if (perfilActual == Perfil.INVITADO) {
            System.out.println("== Menú Invitado ==");
            System.out.println("1. Ver espectáculos");
            System.out.println("2. Login");
            System.out.println("0. Salir");
            
        } else {
            switch (perfilActual) {
                case ADMIN:
                    System.out.println("== Menú Administrador ==");
                    System.out.println("1. Registrar nueva persona");
                    System.out.println("2. Gestionar credenciales");
                    System.out.println("3. Gestionar espectáculos");
                    System.out.println("4. Ver todos los datos del circo");
                    System.out.println("0. Cerrar sesión");
                    break;

                case COORDINACION:
                    System.out.println("== Menú Coordinación ==");
                    System.out.println("1. Crear o modificar espectáculos");
                    System.out.println("2. Gestionar números");
                    System.out.println("3. Asignar artistas a números");
                    System.out.println("4. Ver información completa de espectáculos");
                    System.out.println("0. Cerrar sesión");
                    break;
                    
                case ARTISTA: 
                System.out.println("== Menú Artista =="); 
                System.out.println("1. Ver mi ficha personal"); 
                System.out.println("2. Ver mis espectáculos y números"); 
                System.out.println("0. Cerrar sesión"); 
                break;   
                    
                    
            }
        }
    }
}

