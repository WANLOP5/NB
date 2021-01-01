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
	public String bajarFichero(int idFichero, int idCliente, String URLDiscoCliente) throws RemoteException;
	// Devuelve la direccion del repositorio de un cliente
	public String localizarURLRepositorio(int idCliente) throws RemoteException;
	// Borra un fichero de la base de datos y devuelve el id del repositorio donde se encontraba 
	public int borrarFichero(int idFichero, int idCliente) throws RemoteException;
	
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) throws RemoteException;
	// Busca el nombre del id de un cliente
	public String buscarNombreCliente(int idCliente) throws RemoteException;
	// Busca los metadatos del id de un fichero
	public Metadatos buscarMetadatos(int idFichero) throws RemoteException;
	
	// Lista los clientes del sistema
	public String listarClientes() throws RemoteException;
	// Lista los repositorios del sistema
	public String listarRepositorios() throws RemoteException;
	// Lista los clientes y sus repositorios
	public String listarClientesRepositorios() throws RemoteException;
	// Lista los ficheros del cliente pasado por parametro
	public String[] listarFicheros(int idCliente) throws RemoteException;
}
