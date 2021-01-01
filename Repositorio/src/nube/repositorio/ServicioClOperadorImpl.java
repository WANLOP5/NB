/** Implementacion de la interfaz Servicio cliente operador
 *  Esta clase contiene la implementacion de los metodos borrarFichero y subirFichero.
 *  borrarFichero para borrar un fichero de la carpeta repositorio y subirFichero para subir un fichero a la carpeta del repositorio.
 * 
 * @author Wanderson López Veras, wan_lop05@outlook.es
 */

package nube.repositorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nube.comun.Fichero;
import nube.comun.ServicioClOperadorInterface;

public class ServicioClOperadorImpl extends UnicastRemoteObject implements ServicioClOperadorInterface {
	// identificador generado por eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = -7480727944970116419L;

	// constructor de la clase ServicioClOperadorImpl
	public ServicioClOperadorImpl() throws RemoteException {
		super();
		
	}
	
	/* metodo para borrar un fichero de la carpeta repositorio
	 * este metodo borra el fichero que se indicada de la carpeta del repositorio.
	 * */
	@Override
	public boolean borrarFichero(String nombreFichero, String carpetaCliente) throws RemoteException {
		File f = new File(carpetaCliente + File.separator + nombreFichero);
		
		boolean ficheroBorrado = f.delete();
		if(ficheroBorrado)
			System.out.println("\n[+] EL FICHERO "+nombreFichero +
							" DEL CLIENTE "+carpetaCliente+" HA SIDO BORRADO");
		else 
			System.err.println("\n(ERROR) EL FICHERO "+ nombreFichero +
							" DEL CLIENTE "+carpetaCliente+" NO SE HA PODIDO BORRAR");
		
		return ficheroBorrado;
	}
	
	/* metodo para subir un fichero a la carpeta repositorio
	 * este metodo se encarga de subir los ficheros a la carpeta del repositorio 
	 * */
	@Override
	public boolean subirFichero(Fichero objetoFichero) throws RemoteException {
		// Cliente propietario del fichero obtenido a traves del objeto Fichero
		String carpetaCliente = objetoFichero.obtenerPropietario();
		String nombreFichero = objetoFichero.obtenerNombre();
		// La URI donde subir el fichero es: CARPETA_CLIENTE_PROPIETARIO/NOMBRE_FICHERO
		String rutaFichero = carpetaCliente + File.separator + nombreFichero;
		
		// Objeto stream para la salida de datos (escritura) 
		OutputStream salidaFichero;
		
		try {
			// Inicializacion del objeto stream como un stream de salidas para archivos
			// Se inicializa con la ruta y el nombre donde se escribira
			salidaFichero = new FileOutputStream(rutaFichero);
			
			// Escribir todos los datos del fichero en el objeto stream de salida
			// Devuelve true si la operacion es exitosa.
			if(objetoFichero.escribirEn(salidaFichero)==false) {
				System.err.println("\n(ERROR) NO SE PUDO GUARDAR EL FICHERO "+ nombreFichero +
								" DEL CLIENTE "+carpetaCliente);
				// Cerrar el objeto stream del fichero
				salidaFichero.close();
				return false;
			}
			// Cerrar el objeto stream del fichero
			salidaFichero.close();
			System.out.println("\n[+] SE SUBIO EL FICHERO "+ nombreFichero +
							" DEL CLIENTE "+carpetaCliente);
		}catch(FileNotFoundException e){
			System.err.print("\n(ERROR) NO SE PUDO ENCONTRAR EL FICHERO");
		}catch(IOException e) {
			System.err.print("\n(ERROR) OCURRIO UN ERROR ESCRIBIENDO AL FICHERO");
		}
		
		return true;
	}
	
	

}
