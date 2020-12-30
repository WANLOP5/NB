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
	public String listarClientesRepositorios() throws RemoteException;
	// Devuelve los ficheros de un cliente 
	public String listarFicherosCliente(int idCliente) throws RemoteException;
	
	// Busca un repositorio aleatorio disponible
	public int buscarRepositorioAleatorio() throws RemoteException;
	
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) throws RemoteException;
	// Busca el nombre del id de un cliente
	public String buscarNombreCliente(int idCliente) throws RemoteException;
	// Busca los metadatos de un id de fichero
	public Metadatos buscarMetadatos(int idFichero) throws RemoteException;
	// Busca el id del cliente con su sesionn.
	public int buscarIDCliente(int sesionCliente) throws RemoteException;
	// Busca el id repositorio con su sesion.
	public int buscarIDRepositorio(int sesionRepositorio) throws RemoteException;
	// Busca el id del repositorio con su nombre
	public int buscarIDRepositorio(String nombreRepositorio) throws RemoteException;
	
	public int insertarSesionCliente(String nombre, int idCliente) throws RemoteException;
	// Inserta un cliente nuevo para registrarlo
	public int insertarCliente(String nombre, int idCliente) throws RemoteException;
	
	// Inserta nueva sesion para el repositorio
	public int insertarSesionRepositorio(String nombre, int idRepositorio) throws RemoteException;
	// Inserta un repositorio nuevo para registrarlo
	public int insertarRepositorio(String nombre, int idCliente) throws RemoteException;
	
	// Agrega un fichero a la lista de ficheros
	public int insertarFichero(String nombre, int idCliente) throws RemoteException;
	// Elimina un fichero a la lista de ficheros
	public int eliminarFichero(int idFichero, int idCliente) throws RemoteException;
	// Comprueba si el fichero pertenece al cliente y devuelve sus metadatos
	public Metadatos obtenerDatosFichero(int idFichero, int idCliente) throws RemoteException;
	
}
