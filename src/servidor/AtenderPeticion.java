package servidor;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
//
public class AtenderPeticion implements Runnable
{

	private PrintStream mensajesSalida;
	private DataInputStream mensajesEntrada;
	private List<File> cancionesServidor; 


	public AtenderPeticion(Socket servidorHaciaCliente) 
	{
		super();
		
		try {
			mensajesSalida = new PrintStream(servidorHaciaCliente.getOutputStream());
			mensajesEntrada = new DataInputStream(servidorHaciaCliente.getInputStream());
			this.cancionesServidor = new LinkedList<File>();
			File cancionesPredefinidas = new File("cancionesPredefinidas");
			File lista [] = cancionesPredefinidas.listFiles();
			for (File file : lista) 
			{
				this.cancionesServidor.add(file);
			}
		} catch (IOException e) 
		{
			
			e.printStackTrace();
		}
		

	}


	public void run() 
	{
		try 
		{
			
			boolean seguir = true;
			while(seguir)
			{
				String respuesta =mensajesEntrada.readLine();
				int opcion = Integer.parseInt(respuesta);
				switch (opcion) {
				
					case 0: //El cliente solicita la lista de canciones
					{
						this.listarCanciones();
						break;
					}
					
					case 1:  //El cliente solicita descargar una cancion
					{
						String eleccion = this.mensajesEntrada.readLine();
						this.mandarCancion(eleccion);
						break;
					}
					
					case 2: //El cliente desea subir una cancion
					{
						this.subirCancion();
						break;
					}
					
					case 3: //El cliente cierra la conexion
					{
						seguir = false;
						break;
					}
					
					case 4: //El cliente hace una busqueda y solicita las canciones que coincidan con la busqueda
					{
						this.listarCanciones(this.mensajesEntrada.readLine());
						break;
					}
					
					case 5: //El ciente solicita comprobar si la cancion existe en el servidor
					{
						this.comprobarSiExiste();
						break;
					}
						
				}
			}



		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	public void comprobarSiExiste() 
	//Comprobamos si la cancion que nos envia el usuario ya existe
	{
		int bytesIguales = 0;
		FileInputStream streamCancionYaExistente = null;
		
		try {
			String nombre = this.mensajesEntrada.readLine();
			long tamFich = Long.parseLong(mensajesEntrada.readLine()); //leemos el nombre de lo que nos quieren mandar y su tamaño
			
			File cancion = new File("cancionesPredefinidas/"+nombre);
			
			
			//Comprobamos si ya existe una cancion con el mismo nombre,
			//de ser asi, comprobamos la similitud que tiene con la cancion que
			//el cliente nos quiere enviar:
			//(Si son casi la misma canción, no se podra subir la cancion)
			
			if(cancion.exists()) 
			{
				mensajesSalida.write(1); //La cancion existe
				mensajesSalida.println(Math.min(tamFich, cancion.length())); //mandamos el tamaño minimo de los dos
				mensajesSalida.flush();
				
				
				streamCancionYaExistente = new FileInputStream(cancion);
				
				long cont = 0;
				//---------------------------------------------------------------------------------------------
				byte buffLlegada[] = new byte[700000];
				int leidosLlegada = mensajesEntrada.read(buffLlegada);
				
				byte buffFichero[] = new byte[700000];
				int leidosFichero = streamCancionYaExistente.read(buffFichero,0,leidosLlegada); //para que lean los mismos
				
				cont += leidosLlegada;
				boolean iguales = true;
				
				while(leidosLlegada!=-1 && iguales && (cont < Math.min(tamFich, cancion.length()))) 
				{ //Se supone que las dos inputStream acaban a la vez
					
					int byteComparado =0;
					while(iguales && (byteComparado<leidosLlegada))
					{
						if(!(buffFichero[byteComparado]==buffLlegada[byteComparado]))
						{
							iguales = false;
							this.mensajesSalida.println("false");
							this.mensajesSalida.flush();
							//break;
						}
						else
						{
							byteComparado++;
						}
					} 
					if(iguales)
					{
						this.mensajesSalida.println("true");
						this.mensajesSalida.flush(); 
					}
					else
					{
						break;
					}
					

					leidosLlegada = mensajesEntrada.read(buffLlegada);
					leidosFichero = streamCancionYaExistente.read(buffFichero,0,leidosLlegada);
					cont += leidosLlegada;
				}
				if(iguales)
				{
					this.mensajesSalida.println(iguales+""); //mandamos como ha quedado
				}
				
				
				if(iguales)
				{
					mensajesSalida.write(1);
					mensajesSalida.flush();//La cancion ya existe
				}
				else
				{
					mensajesSalida.write(0); 
					mensajesSalida.flush();//La cancion no existe
				}
				
			} 
			else {
				mensajesSalida.write(0);
				mensajesSalida.flush();//La cancion no existe
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if(streamCancionYaExistente != null) {
				try {
					streamCancionYaExistente.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
	
	public void listarCanciones(String s) 
	//Lista todas las canciones que contienen el string s en su nombre:
	{
			this.actualizarListaCanciones();
			
			List<String> listaCanciones = new ArrayList<String> ();
			
			for (File f : this.cancionesServidor) {
				if (f.getName().contains(s)) {
					listaCanciones.add(f.getName());
				}
			}
			
			this.mensajesSalida.println( listaCanciones.size());
			this.mensajesSalida.flush();
			
			for(String nombre : listaCanciones) {
				this.mensajesSalida.println(nombre);
				this.mensajesSalida.flush();
			}
	}

	public void listarCanciones()
	//Envia por el canal de comunicacion los nombres de las canciones que hay disponibles
	{
		this.actualizarListaCanciones();
		this.mensajesSalida.println(cancionesServidor.size());
		this.mensajesSalida.flush();

		
		for(File f : cancionesServidor)
		{
			this.mensajesSalida.println(f.getName());
			this.mensajesSalida.flush();	
		}

	}

	public void mandarCancion(String cancionSolicitada)  
	//Envia la cancion cuyo nombre corresponde con el string cancionSolicitada
	{
		String archivo= "cancionesPredefinidas/"+cancionSolicitada;
		FileInputStream fichero = null;
		
		try {
			File f = new File(archivo);
			fichero = new FileInputStream(f);
	
			this.mensajesSalida.println(f.length());
			this.mensajesSalida.flush();
	
			byte buff[] = new byte[300000];
			int leidos = fichero.read(buff);
			while(leidos!=-1)
			{
				this.mensajesSalida.write(buff, 0, leidos);
				this.mensajesSalida.flush();
				leidos = fichero.read(buff);
			}
			
		} catch(IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(fichero!=null) {
					fichero.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void subirCancion()
	//El usuario sube una cancion que se almacena junto al resto 
	{
		FileOutputStream f = null;
		
		
		try {
			
			String nombre = this.mensajesEntrada.readLine();
			long tamFich = Long.parseLong(mensajesEntrada.readLine());
			
			File cancion = new File("cancionesPredefinidas/"+nombre+".mp3");
			String nombreFinal = nombre;
			
			File cancionRename = new File("cancionesPredefinidas/"+nombre+".mp3");
			if(cancion.exists()) 
			//Si ya existe una cancion con ese nombre la guardamos con otro nombre
			{
				int cont=1;
				nombreFinal = nombre+"("+cont+")";
				cancionRename = new File("cancionesPredefinidas/"+nombreFinal+".mp3");
				while(cancionRename.exists()) 
				{
					cont++;
					nombreFinal = nombre+"("+cont+")";
					cancionRename = new File("cancionesPredefinidas/"+nombreFinal+".mp3");
				}
				cancionRename.renameTo(new File("cancionesPredefinidas/"+nombreFinal+".mp3"));
			}
					
			//Recibimos la cancion para almacenarla :
			f = new FileOutputStream(cancionRename, false);
				
			byte buff[] = new byte[300000];
			int leidos = mensajesEntrada.read(buff);
			long suma =0;
			suma += leidos;
			while(suma<tamFich)
			{
				f.write(buff,0,leidos);
				f.flush();
				leidos = mensajesEntrada.read(buff);
				suma += leidos;
			}																
								
			//Actualizamos la lista de canciones disponibles en el servidor:
			this.actualizarListaCanciones();
																		
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if(f!=null) {
					f.close();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
	}
	
	private void actualizarListaCanciones() 
	//Actualiza la lista de canciones del servidor 
	{
		this.cancionesServidor.clear(); 
		File cancionesPredefinidas = new File("cancionesPredefinidas");
		File lista [] = cancionesPredefinidas.listFiles();
		for (File file : lista) 
		{
			this.cancionesServidor.add(file);
		}
	}



}
