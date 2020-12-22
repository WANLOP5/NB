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

//implementacion de la interfaz ServicioClOperadorInterface
public class ServicioClOperadorImpl extends UnicastRemoteObject implements ServicioClOperadorInterface {
	

	// identificador generado por el eclipse al utilizar la clase UnicastRemoteObject
	private static final long serialVersionUID = -7480727944970116419L;

	// constructor de la clase ServicioClOperadorImpl
	public ServicioClOperadorImpl() throws RemoteException {
		super();
		
	}
	
	/* metodo para borrar un fichero de la carpeta repositorio
	 * este metodo borra el fichero que se indicada de la carpeta del repositorio.
	 * */
	@Override
	public boolean borrarFichero(String fichero, String carpeta) throws RemoteException {
		File f = new File(carpeta + File.separator + fichero);
		boolean ficheroBorrado = f.delete();
		if(ficheroBorrado)
			System.out.println("El fichero se ha borrado correctamente" + carpeta + File.separator + fichero);
		else 
			System.out.println("El fichero no se ha podido borrar" + carpeta + File.separator + fichero);
		
		return ficheroBorrado;
	}
	
	/* metodo para subir un fichero a la carpeta repositorio
	 * este metodo se encarga de subir los ficheros a la carpeta del repositorio 
	 * */
	@Override
	public boolean subirFichero(Fichero fichero) throws RemoteException {
		OutputStream os;
		String Fnombre = fichero.obtenerPropietario() + File.separator + fichero.obtenerNombre();
		
		try {
			os = new FileOutputStream(Fnombre);
			if(fichero.escribirEn(os)==false) {
				os.close();
				return false;
			}
			os.close();
			System.out.println("El Fichero"+ Fnombre +"ha sido guardado");
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	

}
