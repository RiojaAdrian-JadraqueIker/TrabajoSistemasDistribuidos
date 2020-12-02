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
import java.util.Scanner;

import javazoom.jlgui.basicplayer.BasicPlayerException;
import reproductor.Reproductor;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;



public class Cliente
{

	private String host;
	private int puerto;
	private Reproductor reproductor;
	private DataInputStream mensajesEntrada;
	private PrintStream mensajesSalida;


	public Cliente(String host, int puerto) {
		super();
		this.host = host;
		this.puerto = puerto;
		this.reproductor= new Reproductor();

	}

	public void conectar()
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
				case 0: 
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
	}

	public void mostrarCanciones() throws NumberFormatException, IOException
	{
		System.out.println();
		System.out.println();
		System.out.println("-------------------CANCIONES-------------------");
		int tam = Integer.parseInt(mensajesEntrada.readLine());
		for(int i =0; i<tam;i++)
		{
			String s = mensajesEntrada.readLine();
			System.out.println(s);
		} 
		System.out.println("----------------------------------------------");
	}

	public void elegirCancion() throws NumberFormatException, IOException, BasicPlayerException
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
		

	}

	public void subirCancion(File f) throws FileNotFoundException, IOException
	{
		try(FileInputStream fichero = new FileInputStream(f);)
		{
			
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
		}
		
	}
	
	public File seleccionarArchivo()
	{
		File fichero = null;
		JFileChooser seleccionador = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Musica mp3", "mp3");
        seleccionador.setFileFilter(filter);
		int n = seleccionador.showOpenDialog(seleccionador);
		if (n == JFileChooser.APPROVE_OPTION) 
		{
			fichero = new File(seleccionador.getSelectedFile().getAbsolutePath());
		}
		
		return fichero;
		
	}

	public int opcionesMenu() throws IOException
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
	}

}
