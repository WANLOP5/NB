/** Servidor es la clase principal que contiene el main del servidor.
 *  Hace uso de sus servicios ya implementados y proporciona la interfaz de consola
 *  para el usuario.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es		
 */

package nube.servidor;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.Utilidades;
import nube.comun.IConsola;

public class Servidor {
	// Puerto para los servicios del servidor
	private static int puertoServidor;
	// URLs para los servicios del servidor
	private static String URLBaseDatos, URLAutenticador, URLGestor;
	// Objetos para los servicios del servidor.
	private static ServicioDatosImpl baseDatos;
	private static ServicioAutenticacionImpl autenticador;
	private static ServicioGestorImpl gestor;	
	
	// Pone a correr el servicio base de datos y lo ingresa al registro rmi
	private static void iniciarBaseDatos() {
		Utilidades.cambiarCodeBase(ServicioDatosInterface.class);
		URLBaseDatos = "rmi://localhost:" + puertoServidor + "/baseDatos";
		
		try {			
			baseDatos = new ServicioDatosImpl();
			Naming.rebind(URLBaseDatos, baseDatos);
			System.out.println("[+] SERVICIO DE BASE DE DATOS CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO LA BASE DE DATOS");
			System.exit(1);
		
		}
	}
	
	// Pone a correr el servicio de autenticacion y lo ingresa al registro rmi
	private static void iniciarAutenticador() {
		Utilidades.cambiarCodeBase(ServicioAutenticacionInterface.class);
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		
		try {
			autenticador = new ServicioAutenticacionImpl();
			Naming.rebind(URLAutenticador, autenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO AUTENTICADOR");
			System.exit(1);
		
		}
		
	}
	
	// Pone a correr el servicio gestor y lo ingresa al registro rmi
	private static void iniciarGestor() {
		Utilidades.cambiarCodeBase(ServicioGestorInterface.class);
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		
		try {
			gestor = new ServicioGestorImpl();
			Naming.rebind(URLGestor, gestor);
			System.out.println("[+] SERVICIO GESTOR CORRIENDO");
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO GESTOR");
			System.exit(1);
		
		}
	
	}
	
	// Tumba el servicio base de datos y lo saca del registro rmi
	private static void tumbarBaseDatos() {
		try {
			Naming.unbind(URLBaseDatos);
			System.out.println("[+] BASE DE DATOS TUMBADA CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO LA BASE DE DATOS");
		} 
	}
	
	// Tumba el servicio autenticador y lo saca del registro rmi
	private static void tumbarAutenticador() {
		try {
			Naming.unbind(URLAutenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO AUTENTICADOR");
		} 
	}

	// Tumba el servicio gestor y lo saca del registro rmi
	private static void tumbarGestor() {
		try {
			Naming.unbind(URLGestor);
			System.out.println("[+] SERVICIO GESTOR TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO GESTOR");
			System.exit(1);
		}  
	}

	// Funcion que contiene bucle que alojara el menu principal del programa hasta 
	// que se de por finalizado
	public static void bucleMenuPrincipal() {
		// Variable que mantendra el bucle corriendo
		boolean finalizado = false;
		
		do {
			String[] opciones = {"Listar clientes", "Listar repositorios", 
					"Listar parejas cliente-repositorio", "Salir"};
			int opcion = IConsola.desplegarMenu("Servidor", opciones);
			
			// ######################################
			// Si se elige la opcion listar clientes
			// ######################################
			switch(opcion) {
			case 1:
				try {
					System.out.println("\nDATOS: (NOMBRE_CLIENTE=ID_CLIENTE)");
					System.out.println(gestor.listarClientes());
				} catch (RemoteException e) {
					System.err.println("(ERROR) ERROR EN LA CONEXION CON EL SERVICIO GESTOR");
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// #####################################
			// Si se elige la opcion listar repositorios
			// #####################################	
			case 2: 
				try {
					System.out.println("\nDATOS: (NOMBRE_REPOSITORIO=ID_REPOSITORIO)");
					System.out.println(gestor.listarRepositorios());
				} catch (RemoteException e) {
					System.err.println("(ERROR) ERROR EN LA CONEXION CON EL SERVICIO GESTOR");
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			
			// ####################################
			// Si se elige la opcion listar parejas clientes-repositorios
			// ####################################
			case 3: 
				try {
					System.out.println("\nDATOS: (ID_CLIENTE=ID_REPOSITORIO)");
					System.out.println(gestor.listarClientesRepositorios());
				} catch (RemoteException e) {
					System.err.println("(ERROR) ERROR EN LA CONEXION CON EL SERVICIO GESTOR");
				} 
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// ###################################
			// Si se elige la opcion salir
			// ###################################
			case 4: finalizado = true; break;
			}
			
			
		} while(!finalizado);
	}
	
	// Metodo principal de la clase 
	public static void main(String[] args) {
		// Inicializar el puerto del servidor
		puertoServidor = 9091;
		
		// Iniciar el registroRMI para los servicios del servidor
		Registry registroRMI = Utilidades.iniciarRegistro(puertoServidor);
		
		// Insertar el ServicioDatos en el registro rmi
		iniciarBaseDatos();
		// Insertar el ServicioAutenticacion en el registro rmi
		iniciarAutenticador();
		// Insertar el ServicioGestor en el registro rmi
		iniciarGestor();
	
		// Inicia el bucle del menu principal del servidor
		bucleMenuPrincipal();
		
		// Tumbar el ServicioDatos del registro rmi 
		tumbarBaseDatos();
		// Tumbar el ServicioAutenticacion del registro rmi 
		tumbarAutenticador();
		// Tumbar el ServicioGestor del registro rmi 
		tumbarGestor();
		// Intentar tumbar el registro rmi usado por el servidor
		Utilidades.tumbarRegistro(registroRMI);
		// Cerrar el programa indicandole al sistema que acabo sin errores
		System.exit(0);
	}
}
