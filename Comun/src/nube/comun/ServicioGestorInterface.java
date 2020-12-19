/** ServicioGestorInterface gestiona las transferencias de ficheros para el cliente
 * y proporciona métodos para la interaccion con el ServicioDatos
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface ServicioGestorInterface extends Remote {
	// Devuelve la direccion del repositorio de un cliente
	public String localizarURLRepositorio(int idCliente) throws RemoteException;
	// Inicia la bajada de un fichero utilizando los servicios del repositorio
	public void bajarFichero(String nombreFichero, int idCliente) throws RemoteException;
	
	// Lista los clientes del sistema	
	public HashMap<String, Integer> listarClientes() throws RemoteException;
	// Lista los ficheros de un cliente
	public List<String> listarFicheros(int idCliente) throws RemoteException;
	// Lista los metadatos de los ficheros de un cliente.
	public List<Fichero> listaMetadatos(int idCliente) throws RemoteException;
}
