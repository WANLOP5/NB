/** Clase utilidades
 * 	esta clase contiene los metodos cambiar CODEBASE,iniciar un registro y tumbar un registro del sistema.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.net.BindException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Utilidades {	
	// Nombre de la propiedad codebase
	private static final String CODEBASE = "java.rmi.server.codebase";

	// Añade la ruta de la clase c al codebase
	public static void cambiarCodeBase(Class<?> c) {
		String ruta = c.getProtectionDomain().getCodeSource()
						.getLocation().toString();
		
		String rutaPropiedad = System.getProperty(CODEBASE);
		
		if(rutaPropiedad != null && !rutaPropiedad.isEmpty()) {
			ruta = rutaPropiedad + " " + ruta;
		}
		
		System.setProperty(CODEBASE, ruta);
	}
	
	// Inicia el registro rmi en el sistema.
	public static Registry iniciarRegistro(int puerto) {
		Registry registroRMI = null;
		
		try { 
			registroRMI = LocateRegistry.createRegistry(puerto);
	    
		} catch (RemoteException e) {
			try {
				LocateRegistry.getRegistry(puerto);
			} catch(RemoteException e2) {
				System.err.println("(ERROR) NO SE PUDO CREAR NI LOCALIZAR EL REGISTRO RMI");
				System.exit(1);
			}
	    }
		
		return registroRMI;
			
    }
	
	// Tumbar el registro rmi del sistema.
	public static void tumbarRegistro(Registry registroRMI) {
		try {
			// Solo se puede tumbar el registro si no quedan servicios 
			if(registroRMI != null && registroRMI.list().length == 0)
				UnicastRemoteObject.unexportObject(registroRMI, true);			
		} catch (RemoteException e) {
			System.err.println("(ERROR) NO SE PUDO TUMBAR EL REGISTRO RMI");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
