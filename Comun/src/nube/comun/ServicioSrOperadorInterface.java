/** ServicioSrOperadorInterface gestiona los lugares de almacenamiento del cliente
 * y permite la bajada de ficheros del repositorio.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioSrOperadorInterface extends Remote{
	// Crea una carpeta para el cliente en el repositorio.
	public boolean crearCarpetaCliente(int idCliente) throws RemoteException;
	// Baja un fichero para el cliente. 
	public void bajarFichero(String nombreFichero, int idCliente, String URLDiscoCliente) throws RemoteException;
}
