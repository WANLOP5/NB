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

public class ServicioDiscoClienteImpl extends UnicastRemoteObject {

	// identificador generado por eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = 2960530139594587988L;
	
	// constructor por defecto de la clase ServiciDiscoClienteImpl
	public ServicioDiscoClienteImpl() throws RemoteException {
		super();
		
	}

	// recibe un fichero, el uso es el que explico el profesor en el foro con un ejemplo.
	public boolean bajarFicheroDisco(Fichero fichero,int identificador) throws RemoteException {
		OutputStream os;
		String Fnombre = fichero.obtenerNombre() + "." +identificador; // para saber de que cliente es el fichero.
		
		try {
			os = new FileOutputStream(Fnombre);
			if(fichero.escribirEn(os)==false) {
				os.close();
				return false;
			}
			os.close();
			System.out.println("El fichero"+ Fnombre +" ha sido recibido y se ha guardado");
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
}
