package nube.comun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class IGrafica {
	// Scanner utilizado para la entrada de datos.
	private static Scanner entrada;
	
	// Constructor que inicializa el Scanner a usar para la interfaz
	public IGrafica() {
		entrada = new Scanner(System.in);
	}
	// Funcion para desplegar un menu, dado su titulo y las opciones a desplegar
	public static int desplegarMenu(String name, String[] entradas) {
		int opcionElegida=0; 
		System.out.println("==========================");
		System.out.println(" Menu " + name); // Titulo del menu
		System.out.println("==========================");
		for(int i=0; i<entradas.length; i++) { // Imprime las opciones pasadas como argumentos enumeradas.
			System.out.println((i+1) + ") " + entradas[i]);
		}
		try { 
			do {  // Pedir opcion al usuario
				System.out.print("\nElija una de las opciones > ");
				opcionElegida = entrada.nextInt();
				System.out.print("\n\n");
				
			} while(opcionElegida < 0 && opcionElegida>entradas.length); // Si la opcion no existe repetir 
		}
		catch(Exception e){ 
			System.out.println("\nOcurrio un error eligiendo la opcion.");
		}
		return opcionElegida; 
	}
	// Metodo para limpiar la consola imprimiendo lineas vacias.
	public static void limpiarConsola()  { 
        for(int i = 0; i < 50; i++) System.out.println("\b");
	}
	
	// Funcion para pedir un dato (en formato String) desde la consola.
	public static String pedirDato(String dato) {
		String respuesta=null;
		System.out.print(dato + ": ");
		try {
			respuesta = entrada.nextLine();
		}
		catch (Exception e) {
			System.out.println("Ocurrio un error introduciendo datos.");
		}
		return respuesta;
	}
}
