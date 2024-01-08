package cliente;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SeleccionContrincanteFrame extends JFrame {
	JTextArea chatArea;
	JTextField mensajeField;
	
	SeleccionContrincanteFrame(ObjectOutputStream salida){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        chatArea = new JTextArea();
        
        chatArea.setEditable(false);

        mensajeField = new JTextField();
        mensajeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                enviarMensaje();
            }
        });

        JButton enviarButton = new JButton("Enviar");
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                	String mensaje = mensajeField.getText();
					salida.writeObject("P@C@"+mensaje);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(mensajeField, BorderLayout.CENTER);
        inputPanel.add(enviarButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);
	}

	public void cargarDatos(HashMap<Integer,String> jugadoresEsperando) {
		for(Map.Entry<Integer,String> entry: jugadoresEsperando.entrySet()) {
			String usuario = entry.getValue();
			int idUsuario = entry.getKey();
			SwingUtilities.invokeLater(() -> {
				chatArea.append(""+idUsuario+".-"+usuario+" "+"\n");
			});
		}
	}
	
	public void esperarConexion() {
		chatArea.append("Espera a que se conecte otro usuario");
	}
}
