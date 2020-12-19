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
	
	// Busca un repositorio que esté disponible
	public int obtenerRepositorioDisponible() throws RemoteException;
	// Busca el repositorio del id de un cliente
	public int buscarRepositorio(int idCliente) throws RemoteException;
	// Busca los metadatos de un id de fichero
	public int buscarMetadatos(int idFichero) throws RemoteException;
	// Busca el id del cliente con su sesión.
	public int buscarCliente(int sesionCliente) throws RemoteException;
	
	// Autentica un cliente utilizando las tablas correspondientes.
	public byte autenticarClienteTabla(String nombre, int idCliente) throws RemoteException;
	// Registrar un cliente utilizando las tablas correspondientes. 
	public byte registrarClienteTabla(String nombre, int idCliente) throws RemoteException;
	// Desconecta un cliente eliminando sus datos de las tablas de sesión.
	public String desconectarClienteTabla(int sesionCliente) throws RemoteException;
	
	// Autentica un repositorio utilizando las tablas correspondientes.
	public byte autenticarRepositorioTabla(String nombre, int idCliente) throws RemoteException;
	// Registrar un repositorio utilizando las tablas correspondientes. 
	public byte registrarRepositorioTabla(String nombre, int idCliente) throws RemoteException;
	// Desconecta un repositorio eliminando sus datos de las tablas de sesión.
	public String desconectarRepositorioTabla(int sesionCliente) throws RemoteException;
	
	// Agrega un fichero a la lista de ficheros
	public int insertarFichero(String nombre, int sesionCliente) throws RemoteException;
	// Elimina un fichero a la lista de ficheros
	public int eliminarFichero(int idFichero, int sesionCliente) throws RemoteException;
	// Comprueba si el fichero pertenece al cliente y devuelve sus metadatos
	public int descargarFichero(int idFichero, int sesionCliente) throws RemoteException;
	
}
