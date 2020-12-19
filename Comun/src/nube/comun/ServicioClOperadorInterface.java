package nube.comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicioClOperadorInterface extends Remote{
	
	// metodo para subir un fichero al repositorio
	public boolean subirFichero(Fichero fichero) throws RemoteException;

	// metodo para borrar un fichero del repositorio
	public boolean borrarFichero(String fichero,String carpeta) throws RemoteException;

}
