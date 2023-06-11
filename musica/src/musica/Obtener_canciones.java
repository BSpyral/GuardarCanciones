package musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Obtener_canciones {

	public static void main(String[] args) {
		boolean finalizar=false,salir=false;
		List<String> nombres_archivos=new ArrayList<String>();
		List<String> rutas_archivos=new ArrayList<String>();
		int opcion_elegida;
		String ruta="",rutaGuardar="",nombreArchivoCanciones;
		
		do {
			opcion_elegida = menu_opciones();
			if (opcion_elegida==1) {			
				do {
					ruta=obtener_ubicacionCarpeta();
					rutaGuardar=obtener_ubicacionCarpetaGuardar();
						try {
							nombreArchivoCanciones=crear_archivo(ruta,rutaGuardar);
							nombres_archivos.add(nombreArchivoCanciones);
							rutas_archivos.add(rutaGuardar);
						} catch (IOException e) {
							System.out.println("No se pudo crear el archivo");
							e.printStackTrace();
						}
					finalizar = se_sigueEjecutando("Quiere obtener canciones en otra ubicacion?");
				} while (finalizar==true);
			}
			else if (opcion_elegida==2) {
				int num_archivo=0;
				String[] archivos=new String[2];
				if (nombres_archivos.size()<=2)	{
					while(nombres_archivos.size()<2) {
						ruta=obtenerRutaArchivosExistentes(nombres_archivos);
						rutas_archivos.add(ruta);
					}
					archivos[0]=rutas_archivos.get(0)+"\\"+nombres_archivos.get(0);	
					archivos[1]=rutas_archivos.get(1)+"\\"+nombres_archivos.get(1);	
				}
				else if (nombres_archivos.size()>2) {
					num_archivo=elegir_archivosComparar(nombres_archivos);
					archivos[0]=rutas_archivos.get(num_archivo)+"\\"+nombres_archivos.get(num_archivo);	
					num_archivo=elegir_archivosComparar(nombres_archivos);
					archivos[1]=rutas_archivos.get(num_archivo)+"\\"+nombres_archivos.get(num_archivo);		
				}
					
				try {
					obtener_musicaFaltante(archivos,rutas_archivos.get(num_archivo));
					System.out.println("Se ha comparado el archivo "+nombres_archivos.get(0)+" "
									+ "con el archivo "+nombres_archivos.get(1)+" correctamente.");
				} catch (FileNotFoundException e) {
					System.out.println("Archivos Inexistentes");
				} catch (IOException e) {
					System.out.println("No se pudo leer el archivo");
				}
			}
			else {
				System.out.println("No eligio una opcion, vuelva a ejecutar");
				salir=true;
			}
			
			if (salir==false) {
				salir=se_sigueEjecutando("Quiere salir del programa?");
			}
		}while(salir==false);
		
	}

	private static int menu_opciones() {
		int opcion_elegida;
		System.out.println("1. Obtener nombre de canciones en una carpeta");
		System.out.println("2. Comparar nombres de canciones diferentes entre 2 archivos");
		System.out.println("Elija una opcion");
		Scanner eleccion=new Scanner (System.in);
		try {
			opcion_elegida=eleccion.nextInt();
		} catch (Exception e) {
			opcion_elegida=3;
		}
		return opcion_elegida;
	}

	private static String obtener_nombreCarpeta(String ruta) {		
		String carpeta;
		int verificarExistencia=0;
		do {
			verificarExistencia=ruta.indexOf("\\");
			verificarExistencia++;
			if (verificarExistencia!=0) {
				ruta=ruta.substring(verificarExistencia);
			}
		} while (verificarExistencia!=0);

		carpeta=ruta;
		return carpeta;
	}
	
	private static char obtener_nombreDisco(String ruta) {
		return ruta.charAt(0);
	}
	
	private static String obtener_ubicacionCarpeta() {
		@SuppressWarnings("resource")
		Scanner entrada=new Scanner(System.in);
		System.out.println("Escriba la ruta donde se encuentran las canciones"); //Example: C:\Users\Bar\Music\CARPE
		String ubicacionCarpeta=entrada.nextLine();
		return ubicacionCarpeta;
	}
	
	private static String obtener_ubicacionCarpetaGuardar() {
		@SuppressWarnings("resource")
		Scanner entrada=new Scanner(System.in);
		System.out.println("Escriba la ruta donde guardar el archivo"); //Example: C:\Users\Bar\Music\CARPE
		String ubicacionCarpeta=entrada.nextLine();
		return ubicacionCarpeta;
	}

	private static String crear_archivo(String ruta,String rutaGuardar) throws IOException {
		String nombre_carpeta=obtener_nombreCarpeta(ruta);
		String nombre_archivo="Canciones en "+nombre_carpeta+" de disco "+ 
								obtener_nombreDisco(ruta)+".txt";
		rutaGuardar+="\\"+nombre_archivo;
		File nombre_canciones=new File(rutaGuardar);
			if (!nombre_canciones.exists())	nombre_canciones.createNewFile();
		FileWriter escribir_archivo = new FileWriter(nombre_canciones);
		
		File carpeta_canciones=new File(ruta);		
		llenar_archivoCanciones(carpeta_canciones,escribir_archivo);
		
		return nombre_archivo;
	}

	private static void llenar_archivoCanciones(File carpeta_canciones,FileWriter escribir_archivo) throws IOException {
		int cantidad_archivos=0;
		File[] canciones=carpeta_canciones.listFiles() ;
		if (canciones!=null) {
			for(File can:canciones) {
				try {
					if (can.isFile() && !can.isHidden()) {
						escribir_archivo.write(can.getName());   
						escribir_archivo.write("\n");
						cantidad_archivos++;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
			System.out.println(canciones.length+" Elementos");
			System.out.println(cantidad_archivos+" Archivos");
		}
		else {
			System.out.println("No hay archivos");
		}
		
		escribir_archivo.close();
	}
	
	private static int elegir_archivosComparar(List<String> archivos) {
		int iterador=0,eleccion=0;
		Scanner entrada=new Scanner (System.in);
		System.out.println("¿Que archivo elige comparar?");
		
		do { 
			while (archivos.size()>iterador) {
				System.out.println((iterador+1)+". "+archivos.get(iterador));
				iterador++;
			}
			
			try {
				eleccion=entrada.nextInt();
			}
			catch (Exception e){
				eleccion=iterador+5;
			}
			
			if (eleccion>iterador || eleccion<=0) System.out.println("Tiene que elegir una opcion\n");
		}while (eleccion>iterador);
		
		return --eleccion;
	}
	
	private static String obtenerRutaArchivosExistentes(List<String> nombres_archivos) {	//Podria ser mucho mejor
		List<String> archivos_carpeta=new ArrayList<String>();
		System.out.println("Escriba el nombre de la carpeta donde se guardan los nombres de los archivos");
		Scanner entrada=new Scanner (System.in);
		String ruta=entrada.nextLine();
		
		File carpeta=new File(ruta);
		File[] archivos=carpeta.listFiles();
		for (File auxiliar:archivos) {
			if (auxiliar.isFile()&&!auxiliar.isHidden())   archivos_carpeta.add(auxiliar.getName());
		}
		
		int numArchivo=elegir_archivosComparar(archivos_carpeta);
		nombres_archivos.add(archivos_carpeta.get(numArchivo));
		
		return ruta;
	}
	
	private static void obtener_musicaFaltante(String[] archivos, String rutaGuardado) throws FileNotFoundException,IOException {
			
		BufferedReader leer_archivo1=new BufferedReader(new FileReader(archivos[0]));
		BufferedReader leer_archivo2=new BufferedReader(new FileReader(archivos[1]));
		List<String> canciones=new ArrayList<String>();
		List<String> canciones2=new ArrayList<String>();
		List<String> canciones_faltantes=new ArrayList<String>();
		boolean valorDuplicado=false;
		
		leer_archivo(leer_archivo1, canciones);
		leer_archivo(leer_archivo2, canciones2);

		for(int contador=0;contador<canciones.size();contador++) {
			for(int contador2=0;contador2<canciones2.size();contador2++) {
				if (canciones.get(contador).compareToIgnoreCase(canciones2.get(contador2))==0) {
					valorDuplicado=true;
					break;
				}
			}
			if (!valorDuplicado) canciones_faltantes.add(canciones.get(contador));
			valorDuplicado=false;
		}	
		guardar_archivoFaltantes(canciones_faltantes,rutaGuardado);
	}

	private static void guardar_archivoFaltantes(List<String> canciones_faltantes, String rutaGuardado) throws IOException {
		String ruta;
		
		ruta=rutaGuardado+"\\ArchivosDiferentes="+canciones_faltantes.size()+".txt";
		File faltanteMusica=new File(ruta);
		FileWriter archivo=new FileWriter(faltanteMusica);
		
		int contador=0;
		while (contador<canciones_faltantes.size()) {
			archivo.write(canciones_faltantes.get(contador));
			archivo.write("\n");
			contador++;
		}
		archivo.close();
	}

	private static void leer_archivo(BufferedReader leer_archivo, List<String> canciones) throws IOException {
		int contador=-1;
		
		do {
			contador++;
			canciones.add(leer_archivo.readLine());
		}while(canciones.get(contador)!=null);
		canciones.remove(contador);
		
		leer_archivo.close();
	}	

	private static boolean se_sigueEjecutando(String texto_salida) {
		String finalizar;
		@SuppressWarnings("resource")
		Scanner elegirSalir=new Scanner (System.in);
		System.out.println(texto_salida);
		finalizar=elegirSalir.nextLine();
			if (finalizar.toLowerCase().compareTo("si")==0 || finalizar.toLowerCase().compareTo("s")==0)	return true;
			else return false;
	}
}
