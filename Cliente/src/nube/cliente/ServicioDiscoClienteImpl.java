/** Implementacion de la interfaz ServicioDiscoClienteInterface
 * 	Esta clase contiene un metodo que recibe un fichero y lo baja al disco.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.cliente;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.Fichero;
import nube.comun.ServicioDiscoClienteInterface;

public class ServicioDiscoClienteImpl extends UnicastRemoteObject implements ServicioDiscoClienteInterface {

	// identificador generado por eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = 2960530139594587988L;
	
	// constructor por defecto de la clase ServiciDiscoClienteImpl
	public ServicioDiscoClienteImpl() throws RemoteException {
		super();
		
	}

	// recibe un fichero, el uso es el que explico el profesor en el foro con un ejemplo.
	@Override
	public boolean bajarFicheroDisco(Fichero objetoFichero) throws RemoteException {
		boolean operacionExitosa = false;
		
		
		OutputStream salidaFichero;
		String nombreFichero = objetoFichero.obtenerNombre();
		
		try {
			salidaFichero = new FileOutputStream(nombreFichero);
			if(!objetoFichero.escribirEn(salidaFichero)) {
				salidaFichero.close();
			}
			salidaFichero.close();
			operacionExitosa = true;
			System.out.println("\n[+] FICHERO "+ nombreFichero +" RECIBIDO Y GUARDADO");
			
		}catch(FileNotFoundException e) {
			System.err.println("\n(ERROR) NO SE ENCONTRO EL FICHERO A BAJAR");
		}catch(IOException e) {
			System.err.println("\n(ERROR) NO SE PUDO BAJAR EL FICHERO AL DISCO");
		}
		return operacionExitosa;
		
	}
}
