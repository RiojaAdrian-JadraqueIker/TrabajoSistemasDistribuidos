package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import reproductor.Reproductor;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;


//
public class Cliente
{

	private String host;
	private int puerto;
	private Reproductor reproductor;
	private DataInputStream mensajesEntrada;
	private PrintStream mensajesSalida;
	private Socket cliente;


	public Cliente(String host, int puerto) {
		
		this.host = host;
		this.puerto = puerto;
		this.reproductor= new Reproductor();

	}
	
	public void conectar()  //falta cerrar las cosas (metodoDesconectar())
	{
		try {
			this.cliente = new Socket(this.host, this.puerto);
			this.mensajesEntrada = new DataInputStream(cliente.getInputStream());
			this.mensajesSalida = new PrintStream(cliente.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void desconetar() 
	{
		/*if(this.cliente!=null)
		{
			try
			{
				this.cliente.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}*/
		this.mensajesSalida.println(3);
		this.mensajesSalida.flush();
	}
	

	/*public void conectar() //para sin interfaz
	{
		this.mensajesEntrada=null;
		this.mensajesSalida =null;
		
		
		try(Socket cliente = new Socket(this.host, this.puerto);
			DataInputStream in = new DataInputStream(cliente.getInputStream());
			PrintStream	out = new PrintStream(cliente.getOutputStream()); )
		{
			this.mensajesEntrada = in;
			this.mensajesSalida = out;

			
			boolean seguir = true;
			while(seguir)
			{
				int opcion = this.opcionesMenu();
				mensajesSalida.println(opcion);
				mensajesSalida.flush();
				switch (opcion) {
				case 0: //Solicitamos la lista de canciones al servidor
				{
					this.mostrarCanciones();
					break;
				}
				case 1: 
				{
					this.elegirCancion();
					break;
				}
				case 2: 
				{
					
					File f = this.seleccionarArchivo();
					if(f!=null)
					{
						this.subirCancion(f);
					}
					else
					{
						System.out.println("No se ha seleccionado ningun archivo");
					}
					
					break;
				}
				case 3:
				{
					seguir = false;
					break;
				}

				}
			}

		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	
	
	public List<String> listaCanciones() 
	//Devuelve una lista de canciones enviada por el servidor
	{
		List<String> lista = new ArrayList<String> ();
		
		try {
			//es el caso 0 del servidor luego mandamos la opcion 0
			this.mensajesSalida.println(0);
			this.mensajesSalida.flush();
			
			int tam = Integer.parseInt(mensajesEntrada.readLine());
			
			for(int i =0; i<tam;i++)
			{
				String s = mensajesEntrada.readLine();
				lista.add(s);
			} 	
			
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} 
		
		return lista;	
	}
	
	public void subirCancion(File f) 
	//Envia al servidor el fichero y este lo almacena en su "BD"
	{
		try(FileInputStream fichero = new FileInputStream(f);)
		{
			
			this.mensajesSalida.println(2);
			this.mensajesSalida.println(f.getName());
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
			//this.listaCanciones();
			
		} catch (FileNotFoundException e1 ) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void descargarCancion(String s) 
	//Pide al servidor la cancion dada por parametro y la descarga en el archivo mp3 del reproductor
	{
		File cancion = new File("cancion.mp3");
		
		try(FileOutputStream f = new FileOutputStream(cancion, false))
		{
			this.mensajesSalida.println(1);
			this.mensajesSalida.flush();
			
			//Le decimos la cancion que queremos:
			this.mensajesSalida.println(s);
			this.mensajesSalida.flush();
			
			
			//Descarga la cancion solicitada y la almacena en el fichero
			long tamFich = Long.parseLong(mensajesEntrada.readLine());
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
			System.out.println();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reproducirCancion() 
	//Reproduce la cancion que tenemos disponible en el momento
	{
		this.reproductor.abrirCancion(new File("cancion.mp3"));
		this.reproductor.play();	
	}
	
	public void pausarCancion() 
	//Pausa la cancion que esta sonando 
	{	
		this.reproductor.pause();
	}
	
	public void reanudarCancion() 
	//Continua reproduciendo una canción previamente pausada
	{
		this.reproductor.continuar();
	}
	
	public void regularVolumen(double n) 
	//Asigna a la cancion que se esta reproduciendo el volumen n
	{
		this.reproductor.regularVolumen(n);
	}
	
	public double getVolumen() 
	//Obtiene el valor del volumen de la canción que se está reproduciendo
	{
		return this.reproductor.getVolumen();
	}

	/*public void elegirCancion() throws NumberFormatException, IOException, BasicPlayerException
	//Yo creo que esto no lo tenemos que usar por que utiliza la consola 
	{
		File cancion = new File("cancion.mp3");
		try(	Scanner entrda = new Scanner(System.in);
				FileOutputStream f = new FileOutputStream(cancion, false);
			)
		{
			this.mostrarCanciones();
			System.out.println("Elige una cancion");
			
			String s = entrda.next();
			this.mensajesSalida.println(s);
			this.mensajesSalida.flush();

			//Descarga una cancion de las que posee el servidor y la suena
			long tamFich = Long.parseLong(mensajesEntrada.readLine());
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


			this.reproductor.abrirCancion(cancion);
			this.reproductor.play();

			//cancion.deleteOnExit(); //borra el ficehro al terminar
		}
		

	}*/
	
	
	/*public void mostrarCanciones() throws NumberFormatException, IOException
	{
		List<String> lista = this.listaCanciones();
		
		System.out.println();
		System.out.println();
		System.out.println("-------------------CANCIONES-------------------");
		
		for(String s : lista) {
			System.out.println(s);
		}
		
		System.out.println("----------------------------------------------");
	}*/
	
	/*public int opcionesMenu() throws IOException
	//Esto tambien creo que lo tenemos que hacr en la interfaz gráfica
	{
		System.out.println("Elige una opcion:");
		System.out.println("0 - Mostrar la lista de canciones del servidor");
		System.out.println("1 - Elegir y escuchar una cancion");
		System.out.println("2 - Subir una cancion al servidor");
		System.out.println("3 - Salir");
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		boolean b = false;
		int devuelto =0;
		while(!b)
		{
			try
			{
				System.out.print("Opcion:- ");
				String s = teclado.readLine();
				System.out.println();
				devuelto = Integer.valueOf(s);
				if(devuelto<0 || devuelto>3) //numero opciones menu
				{
					devuelto = -1;
				}
			}
			catch(NumberFormatException e)
			{
				devuelto = -1;
			}
			finally 
			{
				if(devuelto != -1)
				{
					b = true;
				}
				else
				{
					System.out.println("Escribe algo valido");
				}
			}
		}

		return devuelto;
	}*/

}
