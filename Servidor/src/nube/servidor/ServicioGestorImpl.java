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
	// Atributo generado por eclipse al heredar de UnicastRemoteObject
	private static final long serialVersionUID = 1L;
	/* Atributos para localizar la base de datos y 
	* el ServicioServidorOperador en el rmiregistry */
	private int puertoBaseDatos, puertoSrOperador;
	// URLs RMI para localizar el ServicioDatos y el ServicioSrOperador
	private String URLBaseDatos, URLServidorOperador;
	// Objetos remotos para alojar los servicios localizados en el registro RMI
	private ServicioDatosInterface baseDatos;
	private ServicioSrOperadorInterface servidorOperador;

	
	// Inicializa y localiza los servicios baseDatos y servidorOperador en el rmiregistry
	public ServicioGestorImpl() throws RemoteException, MalformedURLException, NotBoundException { 
		super();
	
		// Localiza el el ServicioDatos
		puertoBaseDatos = 9091;
		URLBaseDatos = "rmi://localhost:"+puertoBaseDatos+"/baseDatos";
		localizarBaseDatos(URLBaseDatos);
		// Atributos para localizar el servicio servidorOperador
		puertoSrOperador = 9092;
		URLServidorOperador = "rmi://localhost:"+puertoSrOperador+"/servidorOperador";
		
	}
	
	// Localiza el ServicioSrOperador pasandole la URL RMI
	private void localizarServidorOperador(String URL) throws RemoteException, MalformedURLException, NotBoundException {
		servidorOperador = (ServicioSrOperadorInterface) Naming.lookup(URL);
	}
	
	// Localiza el ServicioDatos pasandole la URL RMI
	private void localizarBaseDatos(String URL) throws MalformedURLException, RemoteException, NotBoundException {
		baseDatos = (ServicioDatosInterface) Naming.lookup(URL);
	}
	
	/* Utiliza la base de datos para insertar un nuevo fichero al sistema
	* y devuelve el id del repositorio donde se sube */
	public int subirFichero(int idCliente, String nombreFichero) throws RemoteException {
		int codError = baseDatos.insertarFichero(nombreFichero, idCliente);

		return codError;
		
	}
	// Inicia la operacion de bajada de fichero y llama al ServicioServidorOperador.
	public String bajarFichero(int idFichero, int idCliente, String URLDiscoCliente) {
		Metadatos mFichero = null;
		
		try {
			mFichero = baseDatos.buscarMetadatos(idFichero);
			
			if(mFichero == null) return null;
			
			// Busca el repositorio del cliente.
			int repositorioCliente = baseDatos.buscarRepositorio(idCliente);
			// URL para localizar la carpeta del repositorio.
			String URLRepositorio = URLServidorOperador+"/"+ repositorioCliente;
			// Localizar la carpeta del repositorio en el servicio servidor operador.
			localizarServidorOperador(URLRepositorio);
			
			// Baja el fichero con el servidorOperador.
			servidorOperador.bajarFichero(mFichero.getNombre(), idCliente, URLDiscoCliente);

		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.out.println("\n(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVIDOR OPERADOR");
			return null;
		}
		
		return mFichero.getNombre();
		
		
	}
	/* Borra un fichero de la base de datos y devuelve el id del repositorio 
	/ donde se encontraba */ 
	public int borrarFichero(int idFichero, int idCliente) throws RemoteException {
		int codError = baseDatos.eliminarFichero(idFichero, idCliente);
		
		return codError;
	}
	
	// Devuelve la direccion del repositorio de un cliente
	public String localizarURLRepositorio(int idCliente) throws RemoteException {
		return URLBaseDatos + "/" + baseDatos.buscarRepositorio(idCliente);
	}
	
	// Busca un repositorio con el id del cliente
	public int buscarRepositorio(int idCliente) throws RemoteException{
		return baseDatos.buscarRepositorio(idCliente);
	}
	
	// Busca el nombre de un cliente con el id 
	public String buscarNombreCliente(int idCliente) throws RemoteException {
		return baseDatos.buscarNombreCliente(idCliente);
	}
	
	// Busca los metadatos del id de un fichero
	public Metadatos buscarMetadatos(int idFichero) throws RemoteException {
		return baseDatos.buscarMetadatos(idFichero);
	}
	
	// Lista los clientes del sistema
	public String listarClientes() throws RemoteException {
		return baseDatos.listarClientes();
	}
	// Lista los repositorios del sistema
	public String listarRepositorios() throws RemoteException {
		return baseDatos.listarRepositorios();
	}
	// Lista los clientes y sus repositorios
	public String listarClientesRepositorios() throws RemoteException {
		return baseDatos.listarClientesRepositorios();
	}
	
	// Lista los ficheros del cliente pasado por parametro
	public String listarFicheros(int idCliente) throws RemoteException {
		return baseDatos.listarFicherosCliente(idCliente);
	}

}
