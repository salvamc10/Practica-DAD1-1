package edu.ucam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//Correo de pruebas 1: correopruebasdad@gmail.com
//Contraseña: dneditxmosyhffjf
public class ClienteIMAP {
    private SSLSocket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public void conectar(String servidor, int puerto, String email, String contraseña) {
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket) factory.createSocket(servidor, puerto);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Autenticación con el servidor IMAP
            login(email, contraseña);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void login(String email, String contraseña) throws Exception {
        // Enviar el comando de inicio de sesión
        writer.write("A1 LOGIN " + email + " " + contraseña + "\r\n");
        writer.flush();
        // Leer la respuesta del servidor
        String response = reader.readLine();
        System.out.println(response);
    }

    public void listarCorreosBuzonPrincipal() {
        try {
            // Enviar el comando para seleccionar el buzón INBOX
            writer.write("A2 SELECT INBOX\r\n");
            writer.flush();
            // Leer la respuesta del servidor
            String response;
            while (!(response = reader.readLine()).startsWith("A2 OK")) {
                System.out.println(response);
            }

            // Enviar el comando para obtener información sobre los correos en la bandeja de entrada
            writer.write("A3 FETCH 1:* (BODY[HEADER.FIELDS (SUBJECT DATE FROM)])\r\n");
            writer.flush();
            // Leer la respuesta del servidor
            response = reader.readLine();
            while (!response.equals("A3 OK FETCH completed.")) {
                System.out.println(response);
                response = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) {
                writer.write("A4 LOGOUT\r\n");
                writer.flush();
                reader.readLine();
                socket.close();
                System.out.println("Desconectado del servidor IMAP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
