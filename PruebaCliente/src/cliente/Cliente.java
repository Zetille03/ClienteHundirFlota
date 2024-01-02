package cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

public class Cliente {

    private static final String SERVIDOR_IP = "localhost";
    private static final int PUERTO = 12345;
    Socket socket;
    
    private JFrame registroFrame;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;

    private FramePartida partidaFrame;
    private MenuFrame menuFrame;
    private ObjectOutputStream outputStream;

    private int idUsuario;
    private String nombreUsuario;
    private String passwordUsuario;
    private boolean usuarioDuplicado=false;
//    private String infoLogeo;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cliente().iniciarRegistro());
    }

    private void iniciarRegistro() {
    	conectarAlServidor();
        registroFrame = new JFrame("Registro de Usuario");
        registroFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registroFrame.setSize(300, 150);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField();
        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaField = new JPasswordField();
        JButton registrarButton = new JButton("Registrar");

        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        panel.add(usuarioLabel);
        panel.add(usuarioField);
        panel.add(contrasenaLabel);
        panel.add(contrasenaField);
        panel.add(new JLabel());  // Espacio en blanco
        panel.add(registrarButton);

        registroFrame.getContentPane().add(panel);
        registroFrame.setVisible(true);
    }

    private void registrarUsuario() {
        String usuario = usuarioField.getText();
        char[] contrasena = contrasenaField.getPassword();

        
        if (!usuario.isEmpty() && contrasena.length > 0) {
        	
            nombreUsuario = usuario;
            passwordUsuario = new String(contrasena);

            String mensaje = ("L@"+nombreUsuario+"@"+passwordUsuario);
            
            enviarMensajeLogeo(mensaje);
            
            SwingUtilities.invokeLater(() -> {
                registroFrame.setVisible(false);
                iniciarMenu();
            });
        } else {
            JOptionPane.showMessageDialog(registroFrame, "Por favor, complete todos los campos.");
        }
    }

    private void iniciarMenu() {
//        chatFrame = new JFrame("Chat Cliente - " + nombreUsuario);
//        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        chatFrame.setSize(400, 300);
//
//        chatArea = new JTextArea();
//        chatArea.setEditable(false);
//
//        mensajeField = new JTextField();
//        mensajeField.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	enviarMensajeTextInput();
//            }
//        });
//
//        JButton enviarButton = new JButton("Enviar");
//        enviarButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//            	enviarMensajeTextInput();
//            }
//        });
//
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
//
//        JPanel inputPanel = new JPanel(new BorderLayout());
//        inputPanel.add(mensajeField, BorderLayout.CENTER);
//        inputPanel.add(enviarButton, BorderLayout.EAST);
//
//        panel.add(inputPanel, BorderLayout.SOUTH);
//
//        chatFrame.getContentPane().add(panel);
//        chatFrame.setVisible(true);

    	
    	menuFrame = new MenuFrame(outputStream);
    	menuFrame.setVisible(true);
    }

    private void conectarAlServidor() {
        try {
            socket = new Socket(SERVIDOR_IP, PUERTO);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    String mensaje;
                    String[] mensajeArray;
                    
                    mensaje = (String) inputStream.readObject();
                    mensajeArray = mensaje.split("@");
                    if(mensajeArray[1].equals("0")) {
                        throw new Exception("datos de registro incorrecto");
                    }else if(mensajeArray[1].equals("R")) {
                    	usuarioDuplicado = true;
                    	throw new Exception("usuario ya registrado");
                    }else {
                    	System.out.println(mensajeArray[2]);
                    	idUsuario = Integer.parseInt(mensajeArray[2]);
                    }
                    
                    
                    do {
                    	mensaje = null;
                    	mensajeArray = null;
                        mensaje = (String) inputStream.readObject();
                        mensajeArray = mensaje.split("@");
                        switch(mensajeArray[0]) {
                        case "D":
                        	System.out.println(mensaje);
                        	break;
                        case "P":
                        	switch(mensajeArray[1]) {
                        	case "H":
                        		if(mensajeArray[2].equals("{}")==false) {
                        			System.out.println(mensajeArray[2]);
                        			HashMap<Integer,String> hashmapEsperando = stringToHashMap(mensajeArray[2]);
                        			menuFrame.seleccionarFrame.setVisible(true);
                        			menuFrame.seleccionarFrame.cargarDatos(hashmapEsperando);
                        		}else {
                        			System.out.println(mensajeArray[2]);
                        			menuFrame.seleccionarFrame.setVisible(true);
                        			menuFrame.seleccionarFrame.esperarConexion();
                        		}
                        	case "E":
                        		
                        	}
                        	break;
                        default:
//                        	 System.out.println(mensaje);
                        	 break;
                        }
                       
                    } while (!mensaje.equals("bye"));

                    socket.close();
                    System.exit(0);  // Cerrar la aplicación cuando se desconecta
                } catch (IOException | ClassNotFoundException e) {
                    // Manejar la excepción de conexión reset
                    if (e instanceof SocketException && e.getMessage().equals("Connection reset")) {
                        System.out.println("Conexión con el servidor perdida.");
                        System.exit(0);
                    }else {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
					if(e.getMessage().equals("datos de registro incorrecto")) {
						partidaFrame.setVisible(false);
			            JOptionPane.showMessageDialog(registroFrame, "No se pudo conectar al servidor. Datos erroneos.");
			            System.exit(0);
					}else if(e.getMessage().equals("usuario ya registrado")) {
						partidaFrame.setVisible(false);
			            JOptionPane.showMessageDialog(registroFrame, "No se pudo conectar al servidor. Usuario ya registrado.");
			            System.exit(0);
					}
				}
            }).start();
        } catch (ConnectException e) {
        	partidaFrame.setVisible(false);
            JOptionPane.showMessageDialog(registroFrame, "No se pudo conectar al servidor. Verifica la conexión.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void enviarMensajeLogeo(String message) {
        try {
            String mensaje = message;
            if (!mensaje.isEmpty()) {
        		outputStream.writeObject(mensaje);
            }
        } catch (IOException e) {
            // Manejar la excepción de conexión reset
            if (e instanceof SocketException && e.getMessage().equals("Connection reset")) {
                System.out.println("Conexión con el servidor perdida.");
                System.exit(0);
            }else {
                e.printStackTrace();
            }
        }
    }
    
    private static HashMap<Integer, String> stringToHashMap(String str) {
        HashMap<Integer, String> hashMap = new HashMap<>();
        // Eliminar los corchetes y espacios en blanco del String
        String[] pairs = str.substring(1, str.length() - 1).split(", ");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            hashMap.put(Integer.parseInt(keyValue[0]), keyValue[1]);
        }
        return hashMap;
    }
}
