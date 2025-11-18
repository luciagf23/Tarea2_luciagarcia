package controlador;

import java.sql.Connection;

import com.luciagf.modelo.Artista;

import dao.ArtistaDAO;;

public class ArtistaServicio {

	private ArtistaDAO artistaDAO;
	
	public ArtistaServicio(Connection con) {
        this.artistaDAO = new ArtistaDAO(con);
    }
	
	
	
	
	
	
	
	
	
}
