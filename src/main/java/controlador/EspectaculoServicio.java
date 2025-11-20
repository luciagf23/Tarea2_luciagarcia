package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Artista;
import com.luciagf.modelo.Espectaculo;
import com.luciagf.modelo.Numero;

import dao.EspectaculoDAO;

public class EspectaculoServicio {

	private EspectaculoDAO espectaculoDAO;

	public EspectaculoServicio(Connection con) {
		this.espectaculoDAO = new EspectaculoDAO(con);
	}

	// VER ESPECTACULO
	public void verEspectaculosBasico() {
		List<Espectaculo> lista = espectaculoDAO.listarTodos();
		System.out.println("\n=== LISTA DE ESPECTÁCULOS ===");
		for (Espectaculo e : lista) {
			System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Inicio: " + e.getFechaini()
					+ " | Fin: " + e.getFechafin());
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

            System.out.println("\n--- Números ---");
            for (Numero n : esp.getNumeros()) {
                System.out.println("Orden " + n.getOrden() + ": " + n.getNombre() +
                                   " (" + n.getDuracion() + " min)");

                System.out.println("   Artistas:");
                for (Artista a : n.getArtistas()) {
                    System.out.println("   - " + a.getNombre() +
                                       " (" + a.getNacionalidad() + ")" +
                                       (a.getApodo() != null ? " Apodo: " + a.getApodo() : ""));
                    if (a.getEspecialidad() != null) {
                        System.out.println("Especialidad: " + a.getEspecialidad());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ver espectáculo completo: " + e.getMessage());
        }
    }
	
	
	
}
