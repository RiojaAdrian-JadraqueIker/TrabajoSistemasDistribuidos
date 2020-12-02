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

public class AtenderPeticion implements Runnable
{

	private PrintStream mensajesSalida;
	private DataInputStream mensajesEntrada;
	private List<File> cancionesServidor; //asi tenemos las mismas canciones en todos lados


	public AtenderPeticion(Socket servidorHaciaCliente) throws IOException {
		super();
		mensajesSalida = new PrintStream(servidorHaciaCliente.getOutputStream());
		mensajesEntrada = new DataInputStream(servidorHaciaCliente.getInputStream());
		this.cancionesServidor = new LinkedList<File>();
		File cancionesPredefinidas = new File("cancionesPredefinidas");
		File lista [] = cancionesPredefinidas.listFiles();
		for (File file : lista) 
		{
			this.cancionesServidor.add(file);
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
				case 0: 
				{
					this.mostrarCanciones();
					break;
				}
				case 1: 
				{
					this.mostrarCanciones();
					int eleccion = Integer.parseInt( this.mensajesEntrada.readLine());
					this.mandarCancion(eleccion);
					break;
				}
				case 2: 
				{
					this.subirCancion();
					break;
				}
				case 3:
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

	public void mostrarCanciones()
	{

		this.mensajesSalida.println(cancionesServidor.size());
		this.mensajesSalida.flush();

		int n =0;
		for(File f : cancionesServidor)
		{
			this.mensajesSalida.println(n +"- "+ f.getName());
			this.mensajesSalida.flush();
			n++;
		}

	}

	public void mandarCancion(int i) throws IOException 
	{
		File f = cancionesServidor.get(i);
		FileInputStream fichero = new FileInputStream(f);

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
	}

	public void subirCancion() throws NumberFormatException, IOException
	{
		String nombre = this.mensajesEntrada.readLine();
		long tamFich = Long.parseLong(mensajesEntrada.readLine());
		File cancion = new File("cancionesPredefinidas/"+nombre);
		FileOutputStream f = new FileOutputStream(cancion, false);
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

	}



}
