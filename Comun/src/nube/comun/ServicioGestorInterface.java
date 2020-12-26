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
	/* Utiliza la base de datos para insertar un nuevo fichero al sistema
	* y devuelve el id del repositorio donde se sube */
	public int subirFichero(int idCliente, String nombreFichero) throws RemoteException;
	// Inicia la operacion de bajada de fichero y llama al ServicioServidorOperador.
	public Metadatos bajarFichero(int idFichero, int idCliente) throws RemoteException;
	// Devuelve la direccion del repositorio de un cliente
	public String localizarURLRepositorio(int idCliente) throws RemoteException;
	// Borra un fichero de la base de datos y devuelve el id del repositorio donde se encontraba 
	public int borrarFichero(int idFichero, int idCliente) throws RemoteException;
	
	// Lista los clientes del sistema
	public void listarClientes() throws RemoteException;
	// Lista los repositorios del sistema
	public void listarRepositorios() throws RemoteException;
	// Lista los clientes y sus repositorios
	public void listarClientesRepositorios() throws RemoteException;
	// Lista los ficheros del cliente pasado por parametro
	public void listarFicheros(int idCliente) throws RemoteException;
}
