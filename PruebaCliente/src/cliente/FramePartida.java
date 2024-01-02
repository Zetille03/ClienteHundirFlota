package cliente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.*;

public class FramePartida extends JFrame{
	Tablero tableroJugador;
	Tablero tableroEnemigo;
	MenuFrame menuFrame;
	FramePartida partidaFrame=this;
	
	FramePartida(MenuFrame menuFrame, ObjectOutputStream salida){
		this.menuFrame = menuFrame;
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel tablerosPanel = new JPanel();
		JPanel botonesPanel = new JPanel();
		botonesPanel.setLayout(new FlowLayout());
		GridLayout layout = new GridLayout(1,2);
		tablerosPanel.setLayout(layout);
		
		JButton volverMenuBoton = new JButton("Volver al menu");
		JButton guardarPartida = new JButton("Guardar");
		JButton renunciarPartida = new JButton("Renunciar");
		JButton salirAplicacionBoton = new JButton("Salir");
		
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
		botonesPanel.add(guardarPartida);
		botonesPanel.add(renunciarPartida);
		botonesPanel.add(salirAplicacionBoton);
		
		tableroJugador = new Tablero(true, "Tablero jugador",salida);
		tableroEnemigo = new Tablero(false, "Tablero Enemigo",salida);
		
		tablerosPanel.add(tableroJugador);
		tablerosPanel.add(tableroEnemigo);
		
		
		
		
		setBounds(100,100,1000,600);
		add(botonesPanel);
		add(tablerosPanel);
		setLocationRelativeTo(null);
	}
}
