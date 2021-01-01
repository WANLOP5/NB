/** Clase cliente es la clase que contiene el metodo principal para los clientes
 *  hace uso de las funcionalidades de los servicios del servidor y del repositorio y 
 *  permite al usuario interactuar con la interfaz en consola para listar ficheros, listar
 *  clientes y subir,borrar y bajar ficheros de su repositorio
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.cliente;

import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import nube.comun.Fichero;
import nube.comun.IConsola;
import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioClOperadorInterface;
import nube.comun.ServicioDiscoClienteInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.Utilidades;

public class Cliente {
	// Puertos de los servicios en rmi
	private static int puertoCliente, puertoServidor, puertoRepositorio;
	
	// URLs de los servicios rmi a usar
	private static String URLAutenticador, URLGestor, 
			URLDiscoCliente, URLClienteOperador;
	
	// Objetos remotos para localizar los servicios rmi del servidor
	private static ServicioAutenticacionInterface srautenticador;
	private static ServicioGestorInterface srgestor;
	
	// Objetos remotos para localizar lo servicios rmi del repositorio
	private static ServicioClOperadorInterface clienteOperador;
	
	// Objetos remotos para iniciar los servicios del cliente
	private static ServicioDiscoClienteImpl discoCliente;
	
	
	// Datos de este cliente
	private static int idCliente, idRepositorioCliente;
		
	// Localiza el servicio autenticador en el registro e inicializa el objeto remoto
	private static void localizarAutenticador() {
		Utilidades.cambiarCodeBase(ServicioAutenticacionInterface.class);
		
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		try {
			srautenticador = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticador);
			
			System.out.println("[+] SERVICIO AUTENTICADOR LOCALIZADO EN EL SERVIDOR");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL AUTENTICADOR");
			
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio gestor en el registro e inicializa el objeto remoto
	private static void localizarGestor() {
		Utilidades.cambiarCodeBase(ServicioGestorInterface.class);
		
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		try {
			srgestor= (ServicioGestorInterface) Naming.lookup(URLGestor);
			System.out.println("[+] SERVICIO GESTOR LOCALIZADO EN EL SERVIDOR");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO GESTOR");
			
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio cliente operador en el registro e inicializa el objeto remoto
	private static void localizarClienteOperador() {
		Utilidades.cambiarCodeBase(ServicioClOperadorInterface.class);
		URLClienteOperador = "rmi://localhost:" + puertoRepositorio + "/clienteOperador/" + idRepositorioCliente;
		
		try {
			clienteOperador = (ServicioClOperadorInterface) Naming.lookup(URLClienteOperador);
			System.out.println("[+] SERVICIO CLIENTE OPERADOR LOCALIZADO EN EL REPOSITORIO");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO CLIENTE OPERADOR");
			
			System.exit(1);
		}
		
	}
	
	
	// Pone a correr el servicio del disco cliente y lo ingresa al registro rmi
	private static void iniciarDiscoCliente() {
		Utilidades.cambiarCodeBase(ServicioDiscoClienteInterface.class);
		URLDiscoCliente = "rmi://localhost:" + puertoCliente + "/discoCliente/"+idCliente;
		
		try {			
			discoCliente = new ServicioDiscoClienteImpl();
			Naming.rebind(URLDiscoCliente, discoCliente);
			System.out.println("[+] SERVICIO DISCO CLIENTE CORRIENDO");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO DISCO CLIENTE");
			
			System.exit(1);
		
		}
		
	}

	// Tumba el servicio disco cliente y lo saca del registro rmi
	private static void tumbarDiscoCliente() {
		try {
			Naming.unbind(URLDiscoCliente);
			System.out.println("[+] DISCO CLIENTE TUMBADO CON EXITO");
		} catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR TUMBANDO EL SERVICIO DISCO CLIENTE");
		} 
	}
	
	// Funcion que contiene bucle que alojara el menu de registro del programa 
	// hasta que se de por finalizado
	private static boolean bucleMenuRegistro() {
		boolean finalizado = false, autenticado = false;
		
		do {
			String opciones[] = {"Registrar un nuevo usuario", "Autenticarse en el sistema", 
								"Salir"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				try {
					String nombre = IConsola.pedirDato("NOMBRE");
					
					switch(srautenticador.registrarCliente(nombre)) {
					case -2 : 
						System.err.println("(ERROR) NO HAY REPOSITORIOS EN LINEA PARA "+nombre); 
						break;
					case -1: 
						System.err.println("\n(ERROR) YA EXISTE UN CLIENTE CON NOMBRE "+nombre); 
						break;
					default: 
						System.out.println("\n[+] "+nombre+" SE HA REGISTRADO EN EL SISTEMA"); 
						break;
					}

						
				} catch (RemoteException | MalformedURLException | NotBoundException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					
					System.exit(1);
				}
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 2: 
				
				try {	
					String nombre = IConsola.pedirDato("NOMBRE");					
					idCliente = srautenticador.autenticarCliente(nombre);
					
					// imprimir mensajes de error o exito segun haya devuelto el servicio autenticador
					switch(idCliente) {
					case -3 : 
						System.err.println("\n(ERROR) EL REPOSITORIO DEL CLIENTE NO ESTA EN LINEA"); 
						break;
					case -2 : 
						System.err.println("\n(ERROR) EL CLIENTE "+ nombre + " YA ESTA AUTENTICADO"); 
						break;
					case -1 : 
						System.err.println("\n(ERROR) EL CLIENTE "+ nombre + " NO ESTA REGISTRADO"); 
						break;
					default: 
						System.out.println("\n[+] "+nombre + " SE HA AUTENTICADO EN EL SISTEMA");
						
						// Inicializar los datos del cliente con el id retornado
						idRepositorioCliente = srgestor.buscarRepositorio(idCliente);
						
						// Ruta absoluta donde se crean las carpetas
						String rutaClientes = System.getProperty("user.dir");
						System.out.println("\n(AVISO) LAS CARPETAS DE LOS CLIENTES SE CREAN EN "+rutaClientes);
						
						autenticado = true;
						finalizado = true;
						break;
					}	
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR AUTENTICANDO EL USUARIO");
					System.exit(1);
				}
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			case 3: finalizado = true; break;
			}
			
			
		} while(!finalizado);
		return autenticado;
	}
	
	// Comprueba que el fichero no es una carpeta y que el fichero existe en el disco
	private static boolean comprobarFichero(String URIFichero) {		
		File ficheroDisco = new File(URIFichero);
				
		if(ficheroDisco.isDirectory() && !ficheroDisco.exists()) {
			System.err.println("(ERROR) EL FICHERO NO EXISTE O SE INTENTO SUBIR UNA CARPETA");
			return false;
		}
		
		return true;
	}
	
	// Funcion que contiene bucle que alojara el menu principal del programa hasta 
	// que se de por finalizado
	private static void bucleMenuPrincipal() {
		// Variable que mantendra el bucle corriendo
		boolean finalizado = false;
		
		do {
			String opciones[] = {"Subir fichero", "Bajar fichero", "Borrar fichero",
								"Listar ficheros", "Listar clientes del sistema", "Salir"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			
			// ######################################
			// Si se elige la opcion subir fichero
			// ######################################
			case 1:
				// Pide el nombre del fichero a subir
				String URIFichero = IConsola.pedirDato("NOMBRE DEL FICHERO");
				
				// Comprueba que el fichero existe en el disco y no es una carpeta
				if(!comprobarFichero(URIFichero)) break;
					
				try {
					Fichero nuevoFichero = new Fichero(URIFichero, ""+idCliente);
					
					// Utiliza el ServicioGestor para ingresar el fichero a la base de datos
					if(srgestor.subirFichero(idCliente, URIFichero) == -1) 
						System.err.println("\n(ERROR) EXISTE UN FICHERO CON EL MISMO NOMBRE");
					
					// Utiliza el cliente operador para alojar el fichero fisico en el repositorio
					if(!clienteOperador.subirFichero(nuevoFichero)) 
						System.err.println("\n(ERROR) NO SE PUDO SUBIR EL FICHERO");
					
					
				} catch(RemoteException e) {
					System.err.println("(ERROR) FUNCIONAMIENTO INESPERADO DE LOS SERVICIOS");
					System.exit(1);
				}
				
				System.out.println("\n[+] FICHERO SUBIDO AL REPOSITORIO CON EXITO");
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// ######################################
			// Si se elige la opcion bajar fichero  
			// ######################################	
			case 2: 
				try {
					// Obtener la lista de ficheros y la cantidad de ficheros del cliente
					String[] ficherosCliente = srgestor.listarFicheros(idCliente);
					// listaFicheros[0] es la lista de ficheros
					// listaFicheros[1] es la cantidad de ficheros
					String listaFicheros = ficherosCliente[0];
					int cantidadFicheros = Integer.parseInt(ficherosCliente[1]);

					// Mostrar la lista de ficheros disponibles para bajar
					System.out.println(listaFicheros);
					// Pedir que se elija un fichero de la lista
					int idFichero = IConsola.pedirOpcion(cantidadFicheros);
					
					// Indicarle al servicio gestor que baje el fichero
					String nombreFichero = srgestor.bajarFichero(idFichero, idCliente, URLDiscoCliente);
					
					// Si el nombre del fichero retornado es nulo no lo pudo encontrar
					if(nombreFichero == null) { 
						System.err.println("(ERROR) EL FICHERO QUE DESEA BAJAR NO EXISTE");
						break;
					}
					
					System.out.println("[+] FICHERO "+nombreFichero+" BAJADO CON EXITO");
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
			
			// ######################################
			// Si se elige la opcion borrar fichero
			// ######################################
			case 3: 
				try {
					String[] ficherosCliente = srgestor.listarFicheros(idCliente);
					// listaFicheros[0] es la lista de ficheros
					// listaFicheros[1] es la cantidad de ficheros
					String listaFicheros = ficherosCliente[0];
					int cantidadFicheros = Integer.parseInt(ficherosCliente[1]);

					// Imprimir la lista de ficheros por pantalla
					System.out.println(listaFicheros);
					int idFichero = IConsola.pedirOpcion(cantidadFicheros);
					
					// Obtener el nombre del fichero del cliente con el ServicioGestor
					String nombreFichero = srgestor.buscarMetadatos(idFichero).getNombre();
					// El nombre de la carpeta es el nombre del cliente al que le pertenece
					String carpetaFichero = "" + idCliente;
					
					if(nombreFichero == null) { 
						System.err.println("(ERROR) EL FICHERO QUE DESEA BAJAR NO EXISTE");
						break;
					}
					
					// Borrar el fichero de la base de datos utilizando el ServicioGestor
					if(srgestor.borrarFichero(idFichero, idCliente) == -1) {
						System.err.println("(ERROR) EL ARCHIVO NO LE PERTENECE AL CLIENTE");
						break;
					}
					
					// Borrar el fichero del disco utilizando el ServicioClienteOperador
					if(!clienteOperador.borrarFichero(nombreFichero, carpetaFichero)) {
						System.err.println("(ERROR) NO SE PUDO BORRAR EL FICHERO DEL DISCO DEL CLIENTE");
						break;
					}
					
					System.out.println("[+] FICHERO "+nombreFichero+ ", BORRADO CON EXITO DE "
							+ "LA CARPETA DEL CLIENTE "+ idCliente);
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// ######################################
			// Si se elige la opcion listar ficheros
			// ######################################
			case 4: 
				try {
					// Listar los ficheros del cliente utilizando el ServicioGestor
					String listaFicheros = srgestor.listarFicheros(idCliente)[0];
					System.out.println(listaFicheros);
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// ######################################
			// Si se elige la opcion listar clientes
			// ######################################
			case 5: 
				try {
					// Listar los clientes del sistema utilizando el ServicioGestor
					System.out.println(srgestor.listarClientes());
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				} 
				IConsola.pausar();
				IConsola.limpiarConsola();
				break;
				
			// ######################################
			// Si se elige la opcion salir
			// ######################################
			case 6:
				try { 
					// Llama al autenticador para desconectar el cliente borrando la sesion
					srautenticador.desconectarCliente(idCliente);
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO AUTENTICADOR");
				}
				
				// Finalizado pasa a ser true para salir del bucle while y terminar la funcion
				finalizado = true; 
				break;
			}
			
			
		} while(!finalizado);
	}
	
	// Metodo principal de la clase 
	public static void main(String[] args) {
		// Inicializar los puertos de los distintos servicios.
		puertoServidor = 9091;
		puertoRepositorio = 9092;
		puertoCliente = 9093;
		
		// Localizar el ServicioAutenticacion del servidor para registrar y autenticar
		localizarAutenticador();
		// Localizar el ServicioGestor del servidor para interactuar con la base de datos 
		localizarGestor();
		
		// Imprimir el bucle de registro y comprobar si el cliente se autentica o no
		boolean autenticado = bucleMenuRegistro();
		
		// Iniciar el registroRMI para los servicios del cliente
		Registry registroRMI = Utilidades.iniciarRegistro(puertoCliente);
			
		// Insertar el ServicioDiscoCliente en el registro rmi
		iniciarDiscoCliente();
		// Localizar el ServicioClienteOperador del repositorio en el registro rmi
		localizarClienteOperador();
		
		// Si el cliente esta autenticado iniciar el bucle con el menu principal
		if(autenticado) bucleMenuPrincipal();
		
		// Tumbar el servicio iniciado en el registro rmi por el cliente
		tumbarDiscoCliente();
		
		// Intentar tumbar el registro rmi usado por el cliente
		Utilidades.tumbarRegistro(registroRMI);
		// Cerrar el programa indicandole al sistema que acabo sin errores
		System.exit(0);
	}
}
