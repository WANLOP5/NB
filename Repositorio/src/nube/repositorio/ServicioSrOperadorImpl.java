/** Implementacion de la interfaz ServicioSrOperadorInterface 
 *  Esta clase contiene un constructor por defecto y dos metodos bajar fichero para descargar un fichero del repositorio 
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
	private int puertoDiscocliente;
	private String URLDiscoCliente;
	ServicioDiscoClienteInterface discoCliente;

	
	// identificador generado por eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = 400066334857238973L;
	
	// constructor de la clase ServicioSrOperadorImpl
	public ServicioSrOperadorImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		puertoDiscocliente = 9092;
		URLDiscoCliente = "rmi://localhost:" + puertoDiscocliente + "/discoCliente";
		discoCliente = (ServicioDiscoClienteInterface) Naming.lookup(URLDiscoCliente);
	}

	// metodo para descargar un fichero, envia un fichero del repositorio en la URL enviada
	@Override
	public void bajarFichero(String Fnombre, int idCliente) throws RemoteException  {
		
		Fichero fichero = new Fichero(""+idCliente,Fnombre,""+idCliente);
		
		if(discoCliente.bajarFicheroDisco(Fnombre,fichero)==false)
		{
			System.out.println("ha ocurrido un error en el envio (fallo en el checksum), debe intentarlo de nuevo");
		}
		else {
			System.out.println("Fichero bajado"+ Fnombre);
		}
	}

	
	/** crea la carpeta del cliente, no creamos carpeta de la repo, ya que no tiene sentido
	 *  puesto que el servicio Datos es quien conoce la relacion entre repos clientes
	 *  si hay varias repos registradas nos da igual, incluso si hay varias repos autenticadas
	 *  a si que las carpetas de los clientes se crean en la carpeta*/
	@Override
	public boolean crearCarpetaRepositorio(int idCliente) throws RemoteException {
			File carpeta = new File(""+idCliente);
			boolean carpetaCreada = carpeta.mkdir(); // true si se ha creado.
			
			if(carpetaCreada) {
				System.out.println("la carpeta se ha creado"+ idCliente + "en la ruta" + System.getProperty("user.dir"));
				Repositorio.listaDeCarpetas.add(""+idCliente);
			}else {
				System.out.println("no se ha podido crear la carpeta"+ idCliente +"en la ruta"+ System.getProperty("user.dir"));
			}
		return carpetaCreada;
	}
	
}
