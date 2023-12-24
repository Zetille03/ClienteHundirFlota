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

public class Cliente {

    private static final String SERVIDOR_IP = "localhost";
    private static final int PUERTO = 12345;

    private JFrame registroFrame;
    private JTextField usuarioField;
    private JPasswordField contrasenaField;

    private JFrame chatFrame;
    private JTextArea chatArea;
    private JTextField mensajeField;
    private ObjectOutputStream outputStream;

    private String nombreUsuario;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cliente().iniciarRegistro());
    }

    private void iniciarRegistro() {
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

        registroFrame.add(panel);
        registroFrame.setVisible(true);
    }

    private void registrarUsuario() {
        String usuario = usuarioField.getText();
        char[] contrasena = contrasenaField.getPassword();

        if (!usuario.isEmpty() && contrasena.length > 0) {
            nombreUsuario = usuario;

            SwingUtilities.invokeLater(() -> {
                registroFrame.setVisible(false);
                iniciarInterfaz();
            });
        } else {
            JOptionPane.showMessageDialog(registroFrame, "Por favor, complete todos los campos.");
        }
    }

    private void iniciarInterfaz() {
        chatFrame = new JFrame("Chat Cliente - " + nombreUsuario);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setSize(400, 300);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        mensajeField = new JTextField();
        mensajeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        JButton enviarButton = new JButton("Enviar");
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(mensajeField, BorderLayout.CENTER);
        inputPanel.add(enviarButton, BorderLayout.EAST);

        panel.add(inputPanel, BorderLayout.SOUTH);

        chatFrame.add(panel);
        chatFrame.setVisible(true);

        conectarAlServidor();
    }

    private void conectarAlServidor() {
        try {
            Socket socket = new Socket(SERVIDOR_IP, PUERTO);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            // Hilo para recibir mensajes del servidor
            new Thread(() -> {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    String mensaje;
                    do {
                        mensaje = (String) inputStream.readObject();
                        mostrarMensaje(mensaje);
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
                }
            }).start();
        } catch (ConnectException e) {
        	chatFrame.setVisible(false);
            JOptionPane.showMessageDialog(registroFrame, "No se pudo conectar al servidor. Verifica la conexión.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje() {
        try {
            String mensaje = mensajeField.getText();
            if (!mensaje.isEmpty()) {
        		outputStream.writeObject(mensaje);
                mensajeField.setText("");
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

    private void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(mensaje + "\n");
        });
    }
}
