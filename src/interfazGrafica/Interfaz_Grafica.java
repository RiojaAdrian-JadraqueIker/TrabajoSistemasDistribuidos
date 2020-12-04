package interfazGrafica;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import cliente.Cliente;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListModel;

import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class Interfaz_Grafica extends JFrame {

	private JPanel contentPane;
	private Cliente cliente;
	private JTextField tbNombreCancionActual;
	
	
	
	
	public Interfaz_Grafica(Cliente c) throws UnknownHostException, IOException 
	{
		File f = new File("cancion.mp3");
		if(f.exists())
		{
			f.delete();
		}
		
		setTitle("Reproductor musica");
		this.cliente=c;
		this.cliente.conectar();
		//this.cliente.conectar();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 762, 407);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btPLay = new JButton("Play");
		btPLay.setToolTipText("Hace sonar la cancion actual");
		btPLay.setBounds(307, 303, 89, 23);
		contentPane.add(btPLay);
		
		
		JButton btPause = new JButton("Pausa");
		btPause.setToolTipText("Hace parar la cancion que esta sonando");
		btPause.setBounds(406, 303, 89, 23);
		contentPane.add(btPause);
		
		JProgressBar pbDuracionCancion = new JProgressBar();
		pbDuracionCancion.setToolTipText("Progreso de la cancion");
		pbDuracionCancion.setBounds(208, 343, 386, 14);
		contentPane.add(pbDuracionCancion);
		
		tbNombreCancionActual = new JTextField();
		tbNombreCancionActual.setToolTipText("Nombre de la cancion actual");
		tbNombreCancionActual.setEditable(false);
		tbNombreCancionActual.setBounds(324, 272, 210, 20);
		contentPane.add(tbNombreCancionActual);
		tbNombreCancionActual.setColumns(10);
		
		JPanel panelAcciones = new JPanel();
		panelAcciones.setBounds(10, 11, 228, 281);
		contentPane.add(panelAcciones);
		panelAcciones.setLayout(null);
		
		JLabel lbAcciones = new JLabel("ACCIONES");
		lbAcciones.setFont(new Font("Arial Black", Font.PLAIN, 16));
		lbAcciones.setBounds(64, 11, 102, 23);
		panelAcciones.add(lbAcciones);
		
		JButton btMostrarCanciones = new JButton("Mostrar canciones");
		btMostrarCanciones.setBounds(38, 45, 156, 23);
		panelAcciones.add(btMostrarCanciones);
		
		JButton btSubirCancion = new JButton("Subir cancion");
		btSubirCancion.setBounds(38, 79, 156, 23);
		panelAcciones.add(btSubirCancion);
		
		JRadioButton btAleatorio = new JRadioButton("Aleatorio");
		btAleatorio.setToolTipText("Se reproduce de forma aleatoria");
		btAleatorio.setBounds(600, 303, 78, 23);
		contentPane.add(btAleatorio);
		
		JSlider volumen = new JSlider();
		volumen.setOrientation(SwingConstants.VERTICAL);
		volumen.setToolTipText("Sirve para regular el volumen");
		volumen.setBounds(639, 46, 65, 195);
		contentPane.add(volumen);
		
		JTextPane tpanelGrande = new JTextPane();
		tpanelGrande.setEditable(false);
		tpanelGrande.setBounds(279, 11, 308, 49);
		contentPane.add(tpanelGrande);
		
		JButton btAnterior = new JButton("Anterior");
		btAnterior.setToolTipText("Se pasa a la cancion anterior");
		btAnterior.setBounds(208, 303, 89, 23);
		contentPane.add(btAnterior);
		
		JButton btSiguiente = new JButton("Siguiente");
		btSiguiente.setToolTipText("Pasa a la siguiente cancion");
		btSiguiente.setBounds(505, 303, 89, 23);
		contentPane.add(btSiguiente);
		
		java.awt.List lista = new java.awt.List();
		lista.setBounds(279, 87, 308, 154);
		contentPane.add(lista);
		
		
		//-----------------------------------
		//---------------EVENTOS-------------
		btPLay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				clickPlay();
			}
		});
		
		
		btPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				clickPause();
			}
		});
		
		btMostrarCanciones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					clickMostrarCanciones(lista);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		
		btSubirCancion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				File fichero = seleccionarArchivo();
				if(fichero!=null)
				{
					cliente.subirCancion(fichero);
				}
				
			}
		});
		
		btSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				siguienteCancion(lista);
			}
		});
		
		btAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				anteriorCancion(lista);
			}
		});
		
		volumen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				volumen(volumen);
			}
		});
		
		//--------------
		
	}
	
	
	public void volumen(JSlider volumen)
	{
		File f = new File("cancion.mp3");
		
		if(f.exists()) 
		{
			int valorActual = volumen.getValue();
		}
	}
	
	public void siguienteCancion(java.awt.List lista)
	{
		if(lista.getItemCount()!=0)
		{
			if(lista.getSelectedIndex()<lista.getItemCount()-1)
			{
				lista.select(lista.getSelectedIndex()+1);
			}
			else
			{
				lista.select(0);
			}
			String s =lista.getSelectedItem();
			cliente.pausarCancion();
			cliente.descargarCancion(s);
			cliente.reproducirCancion();
		}
		
	}
	
	public void anteriorCancion(java.awt.List lista)
	{
		if(lista.getItemCount()!=0)
		{
			if(lista.getSelectedIndex()>0)
			{
				lista.select(lista.getSelectedIndex()-1);
			}
			else
			{
				lista.select(lista.getItemCount()-1);
			}
			String s =lista.getSelectedItem();
			cliente.pausarCancion();
			cliente.descargarCancion(s);
			cliente.reproducirCancion();
		}
	}
	
	public void mostrarInterfaz() 
	{
		this.setVisible(true);
		
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
	
	
	private void clickPlay()
	{
		File f = new File("cancion.mp3");
		
		if(f.exists()) 
		{
			this.cliente.reanudarCancion();
		}
		
	}
	
	
	private void clickPause()
	{
		this.cliente.pausarCancion();
	}
	
	
	private void clickMostrarCanciones(java.awt.List lista ) throws NumberFormatException, IOException
	{
		List<String> listaCanciones = this.cliente.listaCanciones();

		lista.clear(); // limpìamos la lista por si habia algo ya para asi actualizar
		for(String s: listaCanciones)
		{
			lista.add(s);
		}
		lista.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s =lista.getSelectedItem();
				cliente.descargarCancion(s);
				cliente.reproducirCancion();
			}
		});

	}
	
	
	
}
