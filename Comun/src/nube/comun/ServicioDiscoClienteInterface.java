/** Interfaz disco cliente
 * 	La interfaz cuenta con un unico metodo para descargar un fichero al disco duro.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDiscoClienteInterface extends Remote{

	// metodo para descargar un fichero al disco duro
	public boolean bajarFicheroDisco(String nombreFichero,Fichero fichero) throws RemoteException;

}
