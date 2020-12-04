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
				}
			}



		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void listarCanciones()
	//Envia por el canal de comunicacion los nombres de las canciones que hay disponibles
	{

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
			
			File cancion = new File("cancionesPredefinidas/"+nombre);
			f = new FileOutputStream(cancion, false);
			
			
			
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
			this.cancionesServidor.add(new File(nombre));
		
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



}
