/** Interfaz Servicio Autenticacion Interface.
 * La interfaz con sus respectivos metodos para registrar y autenticar 
 * tanto clientes como repositorios.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.comun;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioAutenticacionInterface extends Remote{
	
	// Metodo para registrar un cliente en el servidor
	public int registrarCliente(String nombre) throws RemoteException, NotBoundException, MalformedURLException;
	
	// Metodo para autenticar un cliente en el servidor
	public int autenticarCliente(String nombre) throws RemoteException;
	
	// Metodo para desconectar un cliente
	public void desconectarCliente(int idCliente) throws RemoteException;
		
	// Metodo para registrar un repositorio en el servidor
	public int registrarRepositorio(String nombre) throws RemoteException;
	
	// Metodo para autenticar un repositorio en el servidor
	public int autenticarRepositorio(String nombre) throws RemoteException;
	
	// Metodo para desconectar un repositorio 
	public void desconectarRepositorio(int idRepositorio) throws RemoteException;
	
	
}
