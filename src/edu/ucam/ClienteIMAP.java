package edu.ucam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Base64;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//Correo de pruebas 1: correopruebasdad@gmail.com
//Contraseña: ukevzowqnrrmstsm 

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
    
    public void leerContenidoMensaje(int numMensaje) {
        try {
            // Seleccionar el buzón INBOX
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A3 OK")) {
                System.out.println(response);
            }

            // Ahora fetch el contenido del mensaje
            writer.write("A4 FETCH " + numMensaje + " BODY[TEXT]\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A4 OK")) {
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarMensaje(int numMensaje) {
        try {
            // Seleccionar el buzón INBOX
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A3 OK")) {
                System.out.println(response);
            }

            // Enviar el comando para marcar el mensaje como eliminado
            writer.write("A5 STORE " + numMensaje + " +FLAGS \\Deleted\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A5 OK")) {
                System.out.println(response);
            }

            // Enviar el comando para expurgar el mensaje marcado
            writer.write("A6 EXPUNGE\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A6 OK")) {
                System.out.println(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearCarpeta(String nombreCarpeta) {
        try {
            writer.write("A7 CREATE " + nombreCarpeta + "\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A7 OK")) {
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCarpeta(String nombreCarpeta) {
        try {
            writer.write("A8 DELETE " + nombreCarpeta + "\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A8 OK")) {
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCorreosCarpeta(String nombreCarpeta) {
        try {
            writer.write("A9 SELECT " + nombreCarpeta + "\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A9 OK")) {
                System.out.println(response);
            }
            writer.write("A10 FETCH 1:* (BODY[HEADER.FIELDS (SUBJECT DATE FROM)])\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A10 OK")) {
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void moverMensaje(String origenCarpeta, String destinoCarpeta, int numMensaje) {
        try {
            // Seleccionar la carpeta de origen
            writer.write("A11 SELECT " + origenCarpeta + "\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A11 OK")) {
                System.out.println(response);
            }

            // Copiar el mensaje a la carpeta de destino
            writer.write("A12 COPY " + numMensaje + " " + destinoCarpeta + "\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A12 OK")) {
                System.out.println(response);
            }

            // Marcar el mensaje original como eliminado
            writer.write("A13 STORE " + numMensaje + " +FLAGS \\Deleted\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A13 OK")) {
                System.out.println(response);
            }

            // Expurgar el mensaje marcado como eliminado
            writer.write("A14 EXPUNGE\r\n");
            writer.flush();
            while (!(response = reader.readLine()).startsWith("A14 OK")) {
                System.out.println(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void descargarAdjuntos(int numMensaje, String carpetaDestino) {
        try {
            // Seleccionar el buzón INBOX
            writer.write("A15 SELECT INBOX\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A15 OK")) {
                System.out.println(response);
            }

            // Fetch la estructura del mensaje para obtener los adjuntos
            writer.write("A16 FETCH " + numMensaje + " BODYSTRUCTURE\r\n");
            writer.flush();
            StringBuilder bodyStructure = new StringBuilder();
            while (!(response = reader.readLine()).startsWith("A16 OK")) {
                bodyStructure.append(response).append("\n");
            }
            String bodyStructureString = bodyStructure.toString();
            
            // Analizar la estructura del cuerpo para encontrar los adjuntos
            // Este es un ejemplo simplificado; una implementación real necesitaría manejar casos más complejos
            if (bodyStructureString.contains("attachment")) {
                String[] parts = bodyStructureString.split("\\*");
                for (String part : parts) {
                    if (part.contains("attachment")) {
                        String[] lines = part.split("\n");
                        String partId = lines[0].split(" ")[1].replace(".", "");

                        // Obtener la parte que contiene el adjunto
                        writer.write("A17 FETCH " + numMensaje + " BODY[" + partId + "]\r\n");
                        writer.flush();
                        StringBuilder attachmentData = new StringBuilder();
                        while (!(response = reader.readLine()).startsWith("A17 OK")) {
                            attachmentData.append(response).append("\n");
                        }

                        // Decodificar los datos base64 y guardarlos en un archivo
                        String base64Data = attachmentData.toString();
                        byte[] decodedData = Base64.getDecoder().decode(base64Data);

                        // Escribir los datos decodificados en un archivo
                        FileOutputStream fos = new FileOutputStream(carpetaDestino + "/adjunto_" + partId + ".dat");
                        fos.write(decodedData);
                        fos.close();
                        System.out.println("Archivo adjunto guardado en: " + carpetaDestino + "/adjunto_" + partId + ".dat");
                    }
                }
            } else {
                System.out.println("No se encontraron archivos adjuntos en el mensaje.");
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