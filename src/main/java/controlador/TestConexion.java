package controlador;

import java.sql.Connection;

import dao.ConexionBD;

public class TestConexion {
	public static void main(String[] args) {
		try {
			Connection con = ConexionBD.getConexion();

			if (con != null && !con.isClosed()) {
				System.out.println("CONEXIÓN CORRECTA: Conectado a la base de datos.");
			} else {
				System.out.println("FALLO: La conexión es nula o está cerrada.");
			}

		} catch (Exception e) {
			System.out.println("ERROR AL CONECTAR");
			e.printStackTrace();
		}
	}
}
