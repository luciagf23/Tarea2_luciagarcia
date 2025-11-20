/**
* Clase Main.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/

package vista;

import java.sql.Connection;
import java.util.Scanner;

import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Sesion;

import controlador.EspectaculoServicio;
import controlador.PersonaServicio;
import dao.ConexionBD;

public class Main {
	
private static Scanner teclado=new Scanner(System.in);
private static Sesion sesion = new Sesion(); 

public static void main(String[] args) {
	try {
	Connection con = ConexionBD.getConexion();

	
	PersonaServicio personaServicio = new PersonaServicio(con, sesion);
    EspectaculoServicio espectaculoServicio = new EspectaculoServicio(con);

    boolean salir = false;
    while (!salir) {
        mostrarMenu();
        System.out.print("Elige opción: ");
        int opcion = teclado.nextInt();
        teclado.nextLine(); // limpiar buffer

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
                    case 4: // Ver todos los datos del circo (CU4)
                        System.out.print("ID del espectáculo: ");
                        Long idEsp = teclado.nextLong();
                        espectaculoServicio.verEspectaculoCompleto(idEsp);
                        break;
                    case 0:
                        personaServicio.logout();
                        break;
                }
                break;

            case COORDINACION:
                switch (opcion) {
                    case 4: // Ver información completa de espectáculos (CU4)
                        System.out.print("ID del espectáculo: ");
                        Long idEsp = teclado.nextLong();
                        espectaculoServicio.verEspectaculoCompleto(idEsp);
                        break;
                    case 0:
                        personaServicio.logout();
                        break;
                }
                break;

            case ARTISTA:
                switch (opcion) {
                    case 0:
                        personaServicio.logout();
                        break;
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

