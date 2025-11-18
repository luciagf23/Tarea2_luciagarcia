package controlador;


import dao.PersonaDAO;

public class PersonaServicio {

	//private PersonaDAO dao = new PersonaDAO();

    public void agregarPersona(String nombre, int edad, String email, String nacionalidad) {
        if (nombre == null || nombre.isEmpty() || edad < 0) {
            System.out.println("Datos inválidos"); 
            return;
        }
        //dao.insertar(nombre, edad, email, nacionalidad);
    }

    
    
    
}

