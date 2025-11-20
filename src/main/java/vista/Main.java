/**
* Clase Main.java
*
* author LUCÍA GARCÍA FERNÁNDEZ
* version 1.0
*/

package vista;

import com.luciagf.modelo.Perfil;
import com.luciagf.modelo.Sesion;

public class Main {

	
	private static Sesion sesion = new Sesion(); 

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
