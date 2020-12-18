package nube.comun;

public class Utilidades {
	// Nombre de la propiedad codebase
	public static final String CODEBASE = "java.rmi.server.codebase";

	// Añade la ruta de la clase c al codebase
	public static void cambiarCodeBase(Class<?> c) {
		String rutaClase = c.getProtectionDomain().getCodeSource()
					   .getLocation().toString(); // Obtiene la ruta de la clase
		
		// Obtiene la propiedad del codebase
		String propiedadRuta = System.getProperty(CODEBASE); 
		
		/* Si la propiedad no está vacía concatena la ruta de la clase con el contenido
		 * que tenía la propiedad */
		if (propiedadRuta != null && !propiedadRuta.isEmpty()) { 
			propiedadRuta = propiedadRuta + " " + rutaClase; 
		} 
		
		System.setProperty(CODEBASE, propiedadRuta);
	}
}
