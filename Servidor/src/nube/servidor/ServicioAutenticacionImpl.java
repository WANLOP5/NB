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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioSrOperadorInterface;

//implementacion de la interfaz ServicioAutenticacionInterface
public class ServicioAutenticacionImpl extends UnicastRemoteObject 
implements ServicioAutenticacionInterface {
	
	
	private static final long serialVersionUID = 8325119378408210655L; // numero serial generado por eclipse
	private static int sesion = 1; //identificador unico para los clientes y los repositorios
	private static int puerto = 9090; // numero de puerto para el servicio los servicios de los clientes y los repositorios
	private static ServicioDatosInterface baseDatos; // almacen de datos 
	
	// constructor generado por la clase, sirve para buscar en el almacen de datos
	public ServicioAutenticacionImpl() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		
		String URLRegistro = "rmi://localhost:" + puerto + "/baseDatos";
		baseDatos = (ServicioDatosInterface) Naming.lookup(URLRegistro);
	}
	
	/* metodo para autenticar un cliente en el sistema.
	 * requiere el nombre del cliente que se quiere autenticar en el sistema.
	 * el identificador del cliente que se haya autenticado. 
	 * */
	@Override
	public int autenticarCliente(String nombre) throws RemoteException {
		int sesionCliente = getSesion();
		int identificador = baseDatos.comprobarCliente(nombre, sesionCliente);
		switch (identificador) {
		case -2 : System.out.println("Este cliente"+ nombre + "no esta registrado en el sistema"); 
		break;
		case -1 : System.out.println("el repositorio no esta en linea, se ha cancelado la autenticacion del cliente"+nombre);
		break;
		case 0 : System.out.println("Este cliente"+ nombre + "ya esta autenticado en el sistema");
		break;
		default : System.out.println("Este cliente"+ nombre + "se ha autenticado en el sistema");
		break;
		}
		
		return identificador;
	}

	
	/* metodo para autenticar un repositorio en el sistema
	 * requiere el nombre del repositorio
	 * el identificador de la sesion del repositorio
	 * */
	@Override
	public int autenticarRepositorio(String nombre) throws RemoteException {
		int sesionRepositorio = getSesion();
		int identificador = baseDatos.comprobarRepositorio(nombre, sesionRepositorio);
		switch(identificador) {
		case -1 : System.out.println("EL repositorio"+ nombre +"no esta registrado en el sistema");
		break;
		case 0 : System.out.println("El repositorio"+ nombre + "ya esta autenticado en el sistema");
		break;
		default : System.out.println(" se ha autenticado el repositorio"+nombre+"en el sistema");
		}
		return identificador;
	}

	/* metodo para registrar un cliente en el sistema
	 * requiere el nombre del cliente que se va a registrar en el sistema.
	 * */
	@Override
	public int registrarCliente(String nombre) throws RemoteException, NotBoundException, MalformedURLException {
		int sesion = getSesion();
		int identificador = baseDatos.insertarCliente(nombre,sesion);
		switch(identificador) {
		case -1 : System.out.println("El repositorio no esta en linea, no es posible el registro del cliente"+nombre);
		break;
		case 0 : System.out.println("Este cliente "+nombre+"ya esta registrado en el sistema");
		break;
		default : System.out.println("Este cliente"+nombre+"se ha registrado en el sistema");
		break;
		}
		
		return identificador;
	}

	/* metodo para registrar un repositorio en el sistema
	 * registra un repositorio con el nombre
	 * */
	@Override
	public int registrarRepositorio(String nombre) throws RemoteException {
		int sesion = getSesion();
		int identificador = baseDatos.insertarRepositorio(nombre, sesion);
		if(identificador != 0)
			System.out.println("El repositorio"+ nombre +"se encuentra registrado en el sistema");
		else
			System.out.println("No se puede crear dos veces el mismo repositorio"+nombre+"esta operacion ha sido cancelada");
		return identificador;
	}
	
	// devuelve el id de sesion y el contador de sesiones
	private int getSesion() {
		return sesion++;
	}
}
