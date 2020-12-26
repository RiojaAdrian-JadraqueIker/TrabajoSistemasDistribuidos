package reproductor;

import java.io.*;
import javazoom.jlgui.basicplayer.*;



public class Reproductor extends Thread
{
	//ATRIBUTOS
	private BasicPlayer reproductor;
	
	
	//METODOS:
	
	
	public Reproductor() 
	{
		this.reproductor = new BasicPlayer();
	}
	
	public void abrirCancion(File f) 
	{
		try {
			this.reproductor.open(f);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	} 
	public void abrirCancion(InputStream in)  
	{
		try {
			this.reproductor.open(in);
		} catch (BasicPlayerException e2) {
			e2.printStackTrace();
		}
	}
	
	public void play()
	{
		try {
			this.reproductor.play();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void pause() 
	{
		try 
		{
			this.reproductor.pause();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void continuar()  
	{
		try 
		{
			this.reproductor.resume();
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void regularVolumen(double n) 
	{
		try 
		{
			this.reproductor.setGain(n);
		} 
		catch (BasicPlayerException e) 
		{
			e.printStackTrace();
		}
	}
	public double getVolumen()
	{
		return this.reproductor.getGainValue();
	}
	
	public void finalizarCancion()
	{
		try {
			this.reproductor.stop();
		} catch (BasicPlayerException e) {
		
			e.printStackTrace();
		}
	}

	
	
	

}
