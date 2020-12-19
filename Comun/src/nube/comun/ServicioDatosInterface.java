/** ServicioDatosInterface contiene los métodos para gestionar los datos guardados 
 * en el servidor 
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioDatosInterface extends Remote {
	// Devuelve todos los clientes del sistema y sus datos.
	public String listarClientes() throws RemoteException;
	// Devuelve todos los repositorios del sistema y sus datos.
	public String listarRepositorios() throws RemoteException;
	// Devuelve los clientes con sus repositorios
	public String listarTClientesRepositorios() throws RemoteException;
	// Devuelve los ficheros de un cliente 
	public String listarFicherosCliente(int idCliente) throws RemoteException;
	
	// Busca un repositorio que esté disponible
	public int buscarRepositorioDisponible() throws RemoteException;
	
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) throws RemoteException;
	// Busca los metadatos de un id de fichero
	public int buscarMetadatos(int idFichero) throws RemoteException;
	// Busca el id del cliente con su sesión.
	public int buscarIDCliente(int sesionCliente) throws RemoteException;
	
	// Comprueba el cliente en el sistema para autenticarlo
	public byte comprobarCliente(String nombre, int idCliente) throws RemoteException;
	// Inserta un cliente nuevo para registrarlo
	public byte insertarCliente(String nombre, int idCliente) throws RemoteException;
	
	// Comprueba el repositorio en el sistema para autenticalo
	public byte comprobarRepositorio(String nombre, int idCliente) throws RemoteException;
	// Inserta un repositorio nuevo para registrarlo
	public byte insertarRepositorio(String nombre, int idCliente) throws RemoteException;
	
	// Agrega un fichero a la lista de ficheros
	public int insertarFichero(String nombre, int sesionCliente) throws RemoteException;
	// Elimina un fichero a la lista de ficheros
	public int eliminarFichero(int idFichero, int sesionCliente) throws RemoteException;
	// Comprueba si el fichero pertenece al cliente y devuelve sus metadatos
	public int descargarFichero(int idFichero, int sesionCliente) throws RemoteException;
	
}
