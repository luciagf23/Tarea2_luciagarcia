package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.luciagf.modelo.Numero;
import com.luciagf.modelo.Sesion;

import dao.NumeroDAO;

public class NumeroServicio {

	
	private NumeroDAO numeroDAO;
	
	public NumeroServicio(Connection con) {
		this.numeroDAO=new NumeroDAO(con);
	}
	
	
	public boolean crearNumero(Numero numero) {
        try {
            int cantidad = numeroDAO.contarPorEspectaculo(numero.getIdEspectaculo());
            numero.setOrden(cantidad + 1); 

            numeroDAO.insertar(numero);
            System.out.println("Número creado con ID: " + numero.getId());
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear número: " + e.getMessage());
            return false;
        }
    }
	
	
	//MODIFICAR
	public boolean modificarNumero(Numero numero) {
        try {
            numeroDAO.actualizar(numero);
            System.out.println("Número actualizado correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar número: " + e.getMessage());
            return false;
        }
    }
	
	
	//VALIDAR DURACION
	public static boolean validarDuracion(String duracionStr) {
	    if (!duracionStr.matches("\\d+,[05]")) return false;
	    return true;
	}
	
	
	
	public void listarNumeros() throws SQLException {
	    List<Numero> numeros = numeroDAO.listarTodos();

	    if (numeros.isEmpty()) {
	        System.out.println("No hay números registrados.");
	    } else {
	        System.out.println("== Lista de números ==");
	        for (Numero n : numeros) {
	            System.out.println("ID: " + n.getId() +
	                               " | Nombre: " + n.getNombre() +
	                               " | Duración: " + n.getDuracion() + " min");
	        }
	    }
	}

	
	public void eliminarNumero(Long idNumero, Sesion sesion) throws SQLException {
        numeroDAO.eliminar(idNumero);
    }
	
	public void asignarArtistaANumero(Long idNumero, Long idArtista, Sesion sesion) {
       
        numeroDAO.asignarArtista(idNumero, idArtista);
    }
	
	
	
}
