/** Implementacion de la interfaz servicio autenticacion
 *  Esta clase contiene metodos para registrar y autenticar tanto un cliente como un repositorio en el sistema.
 * 
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioDatosInterface;

// implementacion de la interfaz ServicioAutenticacionInterface
public class ServicioAutenticacionImpl extends UnicastRemoteObject 
implements ServicioAutenticacionInterface {
	// Atributo generado por eclipse al heredar de UnicastRemoteObject
	private static final long serialVersionUID = 1L;
	// Identificador unico de servidores y repositorios
	private static int sesion = 1;
	// Atributos para localizar el la base de datos en el registro RMI
	private static int puertoServidor; 
	private static String URLBaseDatos;
	// Objeto remoto para localizar la base de datos en el registro RMI
	private static ServicioDatosInterface baseDatos; 
	
	
	// Constructor que inicializa los atributos para localizar el servicioDatos
	public ServicioAutenticacionImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		
		puertoServidor = 9091;
		URLBaseDatos = "rmi://localhost:" + puertoServidor + "/baseDatos";		
		localizarBaseDatos(URLBaseDatos);
	}
	
	// Localiza el ServicioDatos pasandole la URL RMI
	private void localizarBaseDatos(String URL) throws MalformedURLException, RemoteException, NotBoundException {
		baseDatos = (ServicioDatosInterface) Naming.lookup(URL);
	}
	
	/* metodo para autenticar un cliente en el sistema.
	 * requiere el nombre del cliente que se quiere autenticar en el sistema.
	 * el identificador del cliente que se haya autenticado. 
	 * */
	@Override
	public int autenticarCliente(String nombre) throws RemoteException {
		int nuevaSesion = getSesion();
		int identificador = baseDatos.insertarSesionCliente(nombre, nuevaSesion);
		
		return identificador;
	}

	
	/* metodo para autenticar un repositorio en el sistema
	 * requiere el nombre del repositorio
	 * el identificador de la sesion del repositorio
	 * */
	@Override
	public int autenticarRepositorio(String nombre) throws RemoteException {
		int nuevaSesion = getSesion();
		int identificador = baseDatos.insertarSesionRepositorio(nombre, nuevaSesion);

		return identificador;
	}

	/* metodo para registrar un cliente en el sistema
	 * requiere el nombre del cliente que se va a registrar en el sistema.
	 * */
	@Override
	public int registrarCliente(String nombre) throws RemoteException, NotBoundException, MalformedURLException {
		int identificador = baseDatos.insertarCliente(nombre, getSesion());
		
		return identificador;
	}

	/* metodo para registrar un repositorio en el sistema
	 * registra un repositorio con el nombre
	 * */
	@Override
	public int registrarRepositorio(String nombre) throws RemoteException {
		int identificador = baseDatos.insertarRepositorio(nombre, getSesion());
		
		return identificador;
	}
	
	// metodo para desconectar un cliente borrando su sesion
	public void desconectarCliente(int idCliente) throws RemoteException{
		baseDatos.eliminarSesionCliente(idCliente);
	}
	
	// metodo para desconectar un repositorio borrando su sesion
	public void desconectarRepositorio(int idRepositorio) throws RemoteException{
		baseDatos.eliminarSesionRepositorio(idRepositorio);
	}
	
	
	// devuelve el id de sesion y el contador de sesiones
	private int getSesion() {
		return sesion++;
	}
}
