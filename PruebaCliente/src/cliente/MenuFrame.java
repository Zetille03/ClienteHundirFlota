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
    JButton salirBoton = new JButton("Salir");
    JButton volverAPartidaBoton = new JButton("Volver a la partida");
    FramePartida nuevaPartidaFrame;
    SeleccionContrincanteFrame seleccionarFrame;
    ReplayFrame replayFrame;
    ArrayList<JFrame> arrayFrames = new ArrayList<JFrame>();
    MenuFrame menu = this;
     
   public MenuFrame(ObjectOutputStream salida) {
//	   nuevaPartidaFrame = new FramePartida(this, salida);
	   arrayFrames.add(nuevaPartidaFrame);
	   arrayFrames.add(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       setLayout(new GridLayout(4,1));
       setSize(new Dimension(200,400));
       
      
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
       
       verPartidasTerminadasBoton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				salida.writeObject("R@L");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
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
       
       volverAPartidaBoton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(nuevaPartidaFrame!=null) {
				if(nuevaPartidaFrame.isVisible()==false) {
					nuevaPartidaFrame.setVisible(true);

				}
			}
		}
	});
       
       add(nuevaPartidaBoton);
       add(volverAPartidaBoton);
       add(verPartidasTerminadasBoton);
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
