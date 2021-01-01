/** Interfaz cliente operador 
 *  La interfaz cuenta con dos metodos, subir fichero y borrar fichero del repositorio.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioClOperadorInterface extends Remote{
	
	// metodo para subir un fichero al repositorio
	public boolean subirFichero(Fichero objetoFichero) throws RemoteException;
	
	// metodo para borrar un fichero del repositorio
	public boolean borrarFichero(String nombreFichero, String carpetaCliente) throws RemoteException;

}
