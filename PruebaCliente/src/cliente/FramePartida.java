package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;

import infocompartida.Barco;
import infocompartida.Boton;



public class FramePartida extends JFrame{
	public Tablero tableroJugador;
	public Tablero tableroEnemigo;
	public JLabel mensajeAyuda = new JLabel("");
	public MenuFrame menuFrame;
	public FramePartida partidaFrame=this;
	public ArrayList<Barco> barcosJugador;
	public ArrayList<Barco> barcosEnemigo;
	public ObjectOutputStream salida;
	public int id_partida;
	public boolean esMiTurno;
	String nombreUsuario;
	String nombreContrincante;
	JButton volverMenuBoton;
	JButton renunciarPartida;
	JButton salirAplicacionBoton;
	
	FramePartida(MenuFrame menuFrame, ObjectOutputStream salida, int id_partida, String contrincante, boolean primeroEnEmpezar){
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		this.menuFrame = menuFrame;
		this.salida = salida;
		this.id_partida = id_partida;
		this.nombreContrincante = contrincante;
		this.esMiTurno = primeroEnEmpezar;
	}
	
	public void iniciarInterfaz(String nombreUsuario) throws InterruptedException {
		menuFrame.setVisible(false);
		this.nombreUsuario = nombreUsuario;
		setTitle("Partida " + nombreUsuario+";        ID_PARTIDA: "+this.id_partida);
		JPanel tablerosPanel = new JPanel();
		JPanel botonesPanel = new JPanel();
		botonesPanel.setLayout(new FlowLayout());
		GridLayout layout = new GridLayout(1,2);
		tablerosPanel.setLayout(layout);
		
		volverMenuBoton = new JButton("Volver al menu");
		renunciarPartida = new JButton("Renunciar");
		salirAplicacionBoton = new JButton("Salir");
		
		volverMenuBoton.addActionListener(new ActionListener() {
			MenuFrame menu= menuFrame;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				partidaFrame.setVisible(false);
				menu.setVisible(true);
			}
		});
		
		salirAplicacionBoton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		botonesPanel.add(volverMenuBoton);
		botonesPanel.add(renunciarPartida);
		botonesPanel.add(salirAplicacionBoton);
		
		while(barcosJugador==null || barcosEnemigo==null) {
			Thread.sleep(1);
		}
		
		tableroJugador = new Tablero(true, "Tablero jugador",salida,barcosJugador,this);
		tableroEnemigo = new Tablero(false, "Tablero Enemigo",salida,barcosEnemigo,this);
		
		tablerosPanel.add(tableroJugador);
		tablerosPanel.add(tableroEnemigo);
		
		
		
		
		setBounds(100,100,1000,600);
		add(botonesPanel);
		add(tablerosPanel);
		setLocationRelativeTo(null);
		setVisible(true);
		System.out.println("todo añadido");
	}
	public static void mostrarArray(ArrayList<Barco> arrayBarcos) {
		for(Barco barco: arrayBarcos) {
			System.out.print("Barco: ");
			for(Boton b : barco.getBotonesBarco()) {
				System.out.print(b.getPosicionTablero()+",");
			}
			System.out.println("");
		}
	}
	
	public void recibirDisparo(int casilla, ArrayList<Barco> array) {
		Boton b = this.tableroJugador.boton[casilla];
		if(b.getAgua()==true) {
			b.setFallido(true);
			b.setColorFallido();
		}else {
			b.setColorTocado();
			b.setTocado(true);
			Barco barco = seleccionarBarco(b,array);
			boolean hundido = comprobacionBarcoHundido(barco);
			if(hundido==true) {
				hundirBarco(barco,tableroJugador);
			}
			
		}
		this.esMiTurno=true;
	}
	
	public static Barco seleccionarBarco(Boton b, ArrayList<Barco> array) {
		for(Barco barco: array) {
			for(Boton boton: barco.getBotonesBarco()) {
				if(boton.getPosicionTablero()==b.getPosicionTablero()) {
					boton.setTocado(true);
					return barco;
				}
			}
		}
		return null;
	}
	
	public static boolean comprobacionBarcoHundido(Barco barco) {
		for(Boton b: barco.getBotonesBarco()) {
			if(b.getTocado()==false) {
				return false;
			}
		}
		return true;
	}
	
	public static void hundirBarco(Barco barco,Tablero tablero) {
		barco.setHundido(true);
		for(Boton b: barco.getBotonesBarco()) {
			tablero.boton[b.getPosicionTablero()].setColorHundido();
			tablero.boton[b.getPosicionTablero()].setHundido(true);
		}
	}
}
