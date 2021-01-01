/** Implementacion de la interfaz ServicioSrOperadorInterface 
 *  Esta clase contiene un constructor por defecto y dos metodos: 
 *  bajarFichero para descargar un fichero del repositorio 
 *  y crear carpeta repositorio.
 *  
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.repositorio;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.ServicioSrOperadorInterface;
import nube.comun.Fichero;
import nube.comun.ServicioDiscoClienteInterface;

public class ServicioSrOperadorImpl extends UnicastRemoteObject implements ServicioSrOperadorInterface{
	// Objeto remoto del ServicioDiscoCliente para localizarlo mas tarde.
	ServicioDiscoClienteInterface discoCliente;

	private static final long serialVersionUID = 400066334857238973L;
	
	public ServicioSrOperadorImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
	}
	
	// Localiza el servicio discoCliente en la URL que se le indique
	private void localizarDiscoCliente(String URL) throws RemoteException, MalformedURLException, NotBoundException {
		discoCliente = (ServicioDiscoClienteInterface) Naming.lookup(URL);
	}

	// Bajada de un fichero utilizando el servicio discoCliente
	public void bajarFichero(String nombreFichero, int idCliente, String URLDiscoCliente) throws RemoteException  {
		Fichero fichero = new Fichero(""+idCliente,nombreFichero,""+idCliente);
		
		try {
			localizarDiscoCliente(URLDiscoCliente);
		} catch (MalformedURLException | NotBoundException e) {
			System.out.println(URLDiscoCliente);
			System.err.println("\n(ERROR) NO SE PUDO LOCALIZAR EL SERVICIO DISCO CLIENTE");
			return;
		} 
		
		if(discoCliente.bajarFicheroDisco(fichero)==false)
			System.out.println("\n(ERROR) NO SE PUDO BAJAR EL FICHERO "+nombreFichero+ 
							"DEL CLIENTE "+idCliente);
		
		System.out.println("\n[+] SE BAJO EL FICHERO "+ nombreFichero+ " DEL CLIENTE "+idCliente);

	}

	
	// Crea una carpeta para alojar los ficheros del cliente idCliente 
	public boolean crearCarpetaCliente(int idCliente) throws RemoteException {
		// Crear objeto archivo para la carpeta
		File carpeta = new File(""+idCliente);
		
		// Devolvera true si la operacion es exitosa
		boolean carpetaCreada = carpeta.mkdir(); 
		
		if(carpetaCreada) {			
			System.out.println("\n[+] LA CARPETA DEL CLIENTE "+ idCliente + " HA SIDO CREADA");
		
			// Ingresara la nueva carpeta a la lista del repositorio
			Repositorio.insertarCarpetaCliente(idCliente);
		}else {
			System.err.println("\n(ERROR) NO SE PUDO CREAR LA CARPETA PARA EL CLIENTE "+idCliente);
		}
		return carpetaCreada;
	}
	
}
