package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class ConexionBD {
    private static Connection conexion;

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar el archivo de propiedades
                Properties props = new Properties();
                InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("application.properties");
                if (input == null) {
                    throw new RuntimeException("No se encontró el archivo application.properties");
                }
                props.load(input);

                // Obtener valores
                String url = props.getProperty("application.url");
                String usuario = props.getProperty("application.user", "root"); 
                String password = props.getProperty("application.password", ""); 

                // Cargar driver MySQL 
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crear la conexión
                conexion = DriverManager.getConnection(url, usuario, password);

            } catch (ClassNotFoundException e) {
                System.out.println("Driver de MySQL no encontrado.");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error al conectar con la base de datos.");
                e.printStackTrace();
                throw new SQLException(e);
            }
        }
        return conexion;
    }
}
