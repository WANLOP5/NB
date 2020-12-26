/** ServicioGestorInterface gestiona las transferencias de ficheros para el cliente
 * y proporciona métodos para la interaccion con el ServicioDatos
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.Metadatos;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.ServicioSrOperadorInterface;

public class ServicioGestorImpl extends UnicastRemoteObject implements ServicioGestorInterface {
	private static final long serialVersionUID = 1L;
	/* Atributos para localizar la base de datos y 
	* el ServicioServidorOperador en el rmiregistry */
	private int puertoBaseDatos, puertoSrOperador;
	private String URLBaseDatos, URLServidorOperador;
	private ServicioDatosInterface baseDatos;
	private ServicioSrOperadorInterface servidorOperador;
	
	
	// Inicializa y localiza los servicios baseDatos y servidorOperador en el rmiregistry
	public ServicioGestorImpl() throws RemoteException, MalformedURLException, NotBoundException { 
		super();
	
		// Localiza el servicio baseDatos
		puertoBaseDatos = 9090;
		URLBaseDatos = "rmi://localhost:"+puertoBaseDatos+"/baseDatos";
		localizarBaseDatos(URLBaseDatos);
		// Localiza el servicio servidorOperador
		puertoSrOperador = 9090;
		URLServidorOperador = "rmi://localhost:"+puertoSrOperador+"/servidorOperador";
		localizarServidorOperador(URLServidorOperador);
	}
	
	private void localizarServidorOperador(String URL) throws RemoteException, MalformedURLException, NotBoundException {
		servidorOperador = (ServicioSrOperadorInterface) Naming.lookup(URL);
	}
	
	private void localizarBaseDatos(String URL) throws MalformedURLException, RemoteException, NotBoundException {
		baseDatos = (ServicioDatosInterface) Naming.lookup(URL);
	}
	
	/* Utiliza la base de datos para insertar un nuevo fichero al sistema
	* y devuelve el id del repositorio donde se sube */
	public int subirFichero(int idCliente, String nombreFichero) throws RemoteException {
		int codError = baseDatos.insertarFichero(nombreFichero, idCliente);
		
		if(codError == -1) System.out.println("(ERROR) Existe un fichero con el mismo nombre");
		else System.out.println("Se ha subido el fichero a la base de datos");
		
		return baseDatos.buscarRepositorio(idCliente);
		
	}
	// Inicia la operacion de bajada de fichero y llama al ServicioServidorOperador.
	public Metadatos bajarFichero(int idFichero, int idCliente) throws RemoteException {
		Metadatos mFichero = baseDatos.buscarMetadatos(idFichero);
		
		if(mFichero == null) return null;
		
		// Busca el repositorio del cliente.
		int repositorioCliente = baseDatos.buscarIDCliente(idCliente);
		// URL para localizar la carpeta del repositorio.
		String URLRepositorio = URLServidorOperador+"/"+ repositorioCliente;
		
		try {
			// Localizar la carpeta del repositorio en el servicio servidor operador.
			localizarServidorOperador(URLRepositorio);
			
			// Baja el fichero con el servidorOperador.
			servidorOperador.bajarFichero(mFichero.getNombre(), idCliente);
			
			// El Servidor Operador vuelve a la URL original.
			localizarServidorOperador(URLServidorOperador);

		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		
		return mFichero;
		
		
	}
	/* Borra un fichero de la base de datos y devuelve el id del repositorio 
	/ donde se encontraba */ 
	public int borrarFichero(int idFichero, int idCliente) throws RemoteException {
		int codError = baseDatos.eliminarFichero(idFichero, idCliente);
		
		if(codError == -1) System.out.println("(ERROR) El archivo no le pertenece al cliente");
		else System.out.println("Se ha borrado el fichero con id "+idFichero+
				 				" del cliente "+idCliente);
		
		return baseDatos.buscarRepositorio(idCliente);
		
	}
	
	// Devuelve la direccion del repositorio de un cliente
	public String localizarURLRepositorio(int idCliente) throws RemoteException {
		return URLBaseDatos + "/" + baseDatos.buscarRepositorio(idCliente);
	}
	
	// Lista los clientes del sistema
	public void listarClientes() throws RemoteException {
		System.out.println(baseDatos.listarClientes());
	}
	// Lista los repositorios del sistema
	public void listarRepositorios() throws RemoteException {
		System.out.println(baseDatos.listarRepositorios());
	}
	// Lista los clientes y sus repositorios
	public void listarClientesRepositorios() throws RemoteException {
		System.out.println(baseDatos.listarClientesRepositorios());
	}
	
	// Lista los ficheros del cliente pasado por parametro
	public void listarFicheros(int idCliente) throws RemoteException {
		System.out.println(baseDatos.listarFicherosCliente(idCliente));
	}

}
