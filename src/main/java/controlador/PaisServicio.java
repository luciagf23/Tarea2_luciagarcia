package controlador;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PaisServicio {

	private Map<Integer, String> paises = new HashMap<>();

	public PaisServicio() {
		cargarPaises();
	}

	private void cargarPaises() {
		try {
			File file = new File("paises.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);

			NodeList listaPaises = doc.getElementsByTagName("pais");

			for (int i = 0; i < listaPaises.getLength(); i++) {
				Element pais = (Element) listaPaises.item(i);
				int id = Integer.parseInt(pais.getAttribute("id"));
				String nombre = pais.getElementsByTagName("nombre").item(0).getTextContent();
				paises.put(id, nombre);
			}
		} catch (Exception e) {
			System.out.println("Error al leer paises.xml: " + e.getMessage());
		}
	}

	// Mostrar todos los países
	public void mostrarPaises() {
		System.out.println("=== Lista de países ===");
		for (Map.Entry<Integer, String> entry : paises.entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue());
		}
	}

	// Validar si existe un país por id
	public boolean existePais(int id) {
		return paises.containsKey(id);
	}

	// Obtener nombre del país por id
	public String obtenerPaisPorId(int id) {
		return paises.get(id);
	}

}
