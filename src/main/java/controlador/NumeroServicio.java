package controlador;

import java.sql.Connection;

import dao.NumeroDAO;

public class NumeroServicio {

	
	private NumeroDAO numeroDAO;
	
	public NumeroServicio(Connection con) {
		this.numeroDAO=new NumeroDAO(con);
	}
	
	
	
}
