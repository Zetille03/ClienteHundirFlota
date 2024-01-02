package cliente;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuFrame extends JFrame {
	JButton nuevaPartidaBoton = new JButton("Nueva partida");
    JButton verPartidasTerminadasBoton = new JButton("Ver partidas terminadas");
    JButton verPartidasNoTerminadasBoton = new JButton("Ver partidas sin terminar");
    JButton salirBoton = new JButton("Salir");
    FramePartida nuevaPartidaFrame;
    SeleccionContrincanteFrame seleccionarFrame;
    ArrayList<JFrame> arrayFrames = new ArrayList<JFrame>();
    MenuFrame menu = this;
     
   public MenuFrame(ObjectOutputStream salida) {
	   seleccionarFrame = new SeleccionContrincanteFrame(salida);
	   nuevaPartidaFrame = new FramePartida(this, salida);
	   arrayFrames.add(nuevaPartidaFrame);
	   arrayFrames.add(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       setLayout(new GridLayout(4,1));
       setSize(new Dimension(200,300));
       
      
       nuevaPartidaBoton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				salida.writeObject("P@N");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
    	   
       });
       
       
       salirBoton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
    	   
       });
       
       add(nuevaPartidaBoton);
       add(verPartidasTerminadasBoton);
       add(verPartidasNoTerminadasBoton);
       add(salirBoton);
       
       setVisible(true);
   }
   
   public void esconderFrames() {
	   for(JFrame frame: arrayFrames) {
		   frame.setVisible(false);
	   }
   }
   
   public void crearSeleccionContricantes() {
	   
   }
}
