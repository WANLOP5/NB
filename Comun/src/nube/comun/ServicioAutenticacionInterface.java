/** Interfaz Servicio Autenticacion Interface.
 * La interfaz con sus respectivos metodos para registrar,autenticar,desconectar tanto clientes como repositorios.
 * 
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote{
	
	// metodo para registrar un cliente en el servidor
	public int registrarCliente(String nombre) throws RemoteException, NotBoundException, MalformedURLException;
	
	// metodo para autenticar un cliente en el servidor
	public int autenticarCliente(String nombre) throws RemoteException;
	
	// metodo para registrar un repositorio en el servidor
	public int registrarRepositorio(String nombre) throws RemoteException;
	
	// metodo para autenticar un repositorio en el servidor
	public int autenticarRepositorio(String nombre) throws RemoteException;
	
	// metodo para desconectar el cliente pasando el identificador de la sesion
	public void desconectarCliente(int sesion) throws RemoteException;
	
	// metodo para desconectar el repositorio pasando el identificador de la sesion
	public void desconectarRepositorio(int sesion) throws RemoteException;
	
	
}
