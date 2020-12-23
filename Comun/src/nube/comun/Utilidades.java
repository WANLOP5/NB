/** Clase utilidades
 * 	esta clase contiene los metodos cambiar CODEBASE,iniciar un registro y tumbar un registro del sistema.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

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
	
	// Inicia el registro rmi en el sistema.
	public static Registry iniciarRegistro(int puerto) {
		Registry registroRMI = null;
		try {
			try { // Si el registro no se puede crear se ignora la accion por la excepcion.
				registroRMI = LocateRegistry.createRegistry(puerto);
	        } catch (ExportException e) {}
			
			registroRMI = LocateRegistry.getRegistry(puerto);
			
			System.out.println("[+] REGISTRO RMI INICIALIZADO");
		} catch(Exception e) {
			System.err.println("(ERROR) NO SE PUDO INICIAR EL REGISTRO RMI");
			System.exit(1);
		}
		
		return registroRMI;
        
    }
	
	// Tumbar el registro rmi del sistema.
	public static void tumbarRegistro(int puerto) {
		try {
			Registry registroRMI = LocateRegistry.getRegistry(puerto);
			UnicastRemoteObject.unexportObject(registroRMI, true);
			System.out.println("[+] REGISTRO RMI TUMBADO");
		} catch (RemoteException e) {
			System.err.println("(ERROR) NO SE PUDO TUMBAR EL REGISTRO RMI");
			System.exit(1);
		}
	}
}
