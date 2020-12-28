package nube.cliente;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import nube.comun.Fichero;
import nube.comun.IConsola;
import nube.comun.ServicioAutenticacionInterface;
import nube.comun.ServicioClOperadorInterface;
import nube.comun.ServicioDatosInterface;
import nube.comun.ServicioDiscoClienteInterface;
import nube.comun.ServicioGestorInterface;
import nube.comun.Utilidades;
import nube.servidor.ServicioAutenticacionImpl;
import nube.servidor.ServicioDatosImpl;
import nube.servidor.ServicioGestorImpl;
import nube.repositorio.ServicioClOperadorImpl;

public class Cliente {
	// Puertos de los servicios en rmi
	private static int puertoCliente, puertoServidor, puertoRepositorio;
	
	// URLs de los servicios rmi a usar
	private static String URLAutenticador, URLGestor, 
			URLDiscoCliente, URLClienteOperador;
	
	// Objetos remotos para localizar los servicios rmi
	private static ServicioAutenticacionInterface srautenticador;
	private static ServicioGestorInterface srgestor;
	private static ServicioDiscoClienteInterface discoCliente;
	private static ServicioClOperadorInterface clienteOperador;
	
	// Objeto de registro rmi
	private static Registry registroRMI;
	
	// Datos de este cliente
	private static String nombreCliente;
	private static int idCliente, idRepositorioCliente;
		
	// Localiza el servicio autenticador en el registro e inicializa el objeto remoto
	private static void localizarAutenticador() {
		URLAutenticador = "rmi://localhost:" + puertoServidor + "/autenticador";
		try {
			srautenticador = (ServicioAutenticacionInterface) Naming.lookup(URLAutenticador);
			System.out.println("[+] SERVICIO AUTENTICADOR LOCALIZADO EN EL SERVIDOR");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL AUTENTICADOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio gestor en el registro e inicializa el objeto remoto
	private static void localizarGestor() {
		URLGestor = "rmi://localhost:" + puertoServidor + "/gestor";
		try {
			srgestor= (ServicioGestorInterface) Naming.lookup(URLGestor);
			System.out.println("[+] SERVICIO GESTOR LOCALIZADO EN EL SERVIDOR");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO GESTOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	// Localiza el servicio cliente operador en el registro e inicializa el objeto remoto
	private static void localizarClienteOperador(String URLRepositorio) {
		URLClienteOperador = "rmi://localhost:" + puertoRepositorio + "/clienteOperador";
		
		if(URLRepositorio != null) 
			URLClienteOperador.concat(URLRepositorio);
		
		try {
			clienteOperador = (ServicioClOperadorInterface) Naming.lookup(URLClienteOperador);
			System.out.println("[+] SERVICIO CLIENTE OPERADOR LOCALIZADO EN EL REPOSITORIO");
			
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			System.out.println("(ERROR) OCURRIO UN ERROR LOCALIZANDO EL SERVICIO CLIENTE OPERADOR");
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	
	// Pone a correr el servicio del disco cliente y lo ingresa al registro rmi
	private static void iniciarDiscoCliente() {
		Utilidades.cambiarCodeBase(ServicioDiscoClienteInterface.class);
		URLDiscoCliente = "rmi://localhost:" + puertoCliente + "/discoCliente";
		
		try {			
			discoCliente = new ServicioDiscoClienteImpl();
			Naming.rebind(URLDiscoCliente, discoCliente);
			System.out.println("[+] SERVICIO DISCO CLIENTE CORRIENDO");
		} catch (RemoteException | MalformedURLException e) {
			System.err.println("(ERROR) OCURRIO UN ERROR INICIANDO EL SERVICIO DISCO CLIENTE");
			e.printStackTrace();
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
					
					srautenticador.registrarCliente(nombre);
					System.out.println("[+] USUARIO REGISTRADO");
				} catch (RemoteException | MalformedURLException | NotBoundException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}
				break;
			case 2: 
				
				try {	
					String nombre = IConsola.pedirDato("NOMBRE");
					
					idCliente = srautenticador.autenticarCliente(nombre);
					// Inicializar los datos del cliente con el id retornado
					nombreCliente = srgestor.buscarNombreCliente(idCliente);
					idRepositorioCliente = srgestor.buscarRepositorio(idCliente);
					
					System.out.println("[+] USUARIO AUTENTICADO");
				} catch (RemoteException e) {
					System.out.println("(ERROR) OCURRIO UN ERROR REGISTRANDO EL USUARIO");
					System.exit(1);
				}
				break;
			case 3: finalizado = true; break;
			}
			
			
		} while(!finalizado);
		return autenticado;
	}
	
	private static boolean comprobarFichero(String URIFichero) {		
		File ficheroDisco = new File(URIFichero);
				
		if(ficheroDisco.isDirectory() && !ficheroDisco.exists()) {
			System.err.println("(ERROR) EL FICHERO NO EXISTE / SE INTENTO SUBIR UNA CARPETA");
			return false;
		}
		
		return true;
	}
	
	// Funcion que contiene bucle que alojara el menu principal del programa hasta 
	// que se de por finalizado
	private static void bucleMenuPrincipal() {
		boolean finalizado = false;
		
		do {
			String opciones[] = {"Subir fichero", "Bajar fichero", "Borrar fichero",
								"Listar ficheros", "Listar clientes"};
			int opcion = IConsola.desplegarMenu("Cliente", opciones);
			
			switch(opcion) {
			case 1: 
				IConsola.limpiarConsola();
				
				String URIFichero = IConsola.pedirDato("NOMBRE DEL FICHERO");
				String propietario = IConsola.pedirDato("PROPIETARIO DEL FICHERO");
				
				comprobarFichero(URIFichero);
				
				try {
					Fichero nuevoFichero = new Fichero(nombreCliente, propietario);
					if(srgestor.subirFichero(idCliente, URIFichero) != 0) break;
				
					// Localizar el repositorio en el cliente operador 
					localizarClienteOperador("/" + idRepositorioCliente);
					
					if(!clienteOperador.subirFichero(nuevoFichero)) {
						System.err.println("(ERROR) NO SE PUDO SUBIR EL FICHERO");
						break;
					}
					
					// Asignar el URLClienteOperador predeterminado
					localizarClienteOperador(null);
					
				} catch(RemoteException e) {
					System.err.println("(ERROR) INESPERADO FUNCIONAMIENTO DE LOS SERVICIOS");
					System.exit(1);
				}
				
				System.out.println("[+] FICHERO SUBIDO AL REPOSITORIO CON EXITO");
				
				break;
			case 2: 
				IConsola.limpiarConsola();
				
				try {
					srgestor.listarFicheros(idCliente);
					int idFichero = Integer.parseInt(IConsola.pedirDato("ELIJA ID DE FICHERO"));
					
					String URLFicheroDisco = URLDiscoCliente + "/" + idCliente;
					String nombreFichero = srgestor.bajarFichero(idFichero, idCliente, URLFicheroDisco);
					
					if(nombreFichero == null) { 
						System.err.println("(ERROR) NO SE PUDO BAJAR EL FICHERO");
						break;
					}
					
					System.out.println("[+] FICHERO "+nombreFichero+" BAJADO CON EXITO");
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				break;
			case 3: 
				IConsola.limpiarConsola();
			
				try {
					srgestor.listarFicheros(idCliente);
					int idFichero = Integer.parseInt(IConsola.pedirDato("ELIJA ID DE FICHERO"));
					String nombreFichero = srgestor.buscarMetadatos(idFichero).getNombre();
					String carpetaFichero = "" + idCliente;
					
					if(srgestor.borrarFichero(idFichero, idCliente) == -1) break;
					
					if(!clienteOperador.borrarFichero(nombreFichero, carpetaFichero)) break;
					
					System.out.println("[+] FICHERO CON ID "+idFichero + " BORRADO CON EXITO DE "
							+ "LA CARPETA DE REPOSITORIO /"+ idCliente);
					break;
					
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				
				
				break;
			case 4: 
				IConsola.limpiarConsola();
				
				try {
					srgestor.listarFicheros(idCliente);
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				}
				break;
			case 5: 
				IConsola.limpiarConsola();
				
				try {
					srgestor.listarClientes();
				} catch (RemoteException e) {
					System.err.println("(ERROR) OCURRIO UN ERROR CON EL SERVICIO GESTOR");
					System.exit(1);
				} 
				break;
			case 6: finalizado = true; break;
			}
			
			
		} while(!finalizado);
	}
	
	public static void main(String[] args) {
		// Inicializando los puertos de los distintos servicios.
		puertoServidor = 9091;
		puertoRepositorio = 9092;
		puertoCliente = 9093;
		
		registroRMI = Utilidades.iniciarRegistro(puertoCliente);
			
		iniciarDiscoCliente();
		localizarAutenticador();
		localizarGestor();
		localizarClienteOperador(null);
		
		boolean autenticado = bucleMenuRegistro();
		if(autenticado) bucleMenuPrincipal();
		
		tumbarDiscoCliente();
		
		Utilidades.tumbarRegistro(puertoCliente);
		System.exit(0);
	}
}
