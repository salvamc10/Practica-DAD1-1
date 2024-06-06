package edu.ucam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

//Correo de pruebas 1: correopruebasdad@gmail.com
//Contraseña: ukevzowqnrrmstsm 
///Users/salvamc/Downloads/cosas

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
            while ((response = reader.readLine()) != null) {
                System.out.println(response);
                if (response.startsWith("A3 OK")) {
                    break;
                }
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
    
    //Correo de pruebas 1: correopruebasdad@gmail.com
    //Contraseña: ukevzowqnrrmstsm 
    ///Users/salvamc/Downloads/cosas
    public void descargarAdjuntos(int numMensaje, String rutaDescarga) {
        try {
            // Seleccionar el buzón INBOX
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            String response;
            while (!(response = reader.readLine()).startsWith("A3 OK")) {
                System.out.println(response);
            }

            // Obtener el cuerpo completo del mensaje
            writer.write("A4 FETCH " + numMensaje + " BODY.PEEK[]\r\n");
            writer.flush();
            StringBuilder mensajeCompleto = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                mensajeCompleto.append(response).append("\n");
                if (response.startsWith("A4 OK")) {
                    break;
                }
            }

            // Parsear el mensaje MIME y extraer los adjuntos
            String[] lineas = mensajeCompleto.toString().split("\n");
            boolean esAdjunto = false;
            boolean esBase64 = false;
            String nombreArchivo = null;
            StringBuilder contenidoAdjunto = new StringBuilder();
            Pattern pattern = Pattern.compile("filename=\"(.*?)\"");
            for (String linea : lineas) {
                if (linea.startsWith("Content-Disposition: attachment;")) {
                    esAdjunto = true;
                    esBase64 = false;  // Resetear el flag de base64
                    Matcher matcher = pattern.matcher(linea);
                    if (matcher.find()) {
                        nombreArchivo = matcher.group(1);
                    }
                } else if (esAdjunto && linea.startsWith("Content-Transfer-Encoding: base64")) {
                    esBase64 = true;
                } else if (esAdjunto && esBase64) {
                    if (linea.startsWith("--")) {
                        // Guardar el adjunto cuando se encuentra un boundary
                        if (contenidoAdjunto.length() > 0) {
                            guardarArchivoAdjunto(nombreArchivo, contenidoAdjunto.toString(), rutaDescarga);
                            contenidoAdjunto.setLength(0);
                        }
                        esAdjunto = false;
                        esBase64 = false;
                        nombreArchivo = null;
                    } else {
                        contenidoAdjunto.append(linea);
                    }
                } else if (esAdjunto && !esBase64) {
                    if (linea.startsWith("--")) {
                        esAdjunto = false;
                    }
                }
            }
            // Asegurarse de guardar cualquier adjunto pendiente
            if (contenidoAdjunto.length() > 0 && nombreArchivo != null) {
                guardarArchivoAdjunto(nombreArchivo, contenidoAdjunto.toString(), rutaDescarga);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarArchivoAdjunto(String nombreArchivo, String contenidoBase64, String rutaDescarga) {
        try {
            // Guardar el contenido Base64 directamente en un archivo
            try (OutputStream out = new FileOutputStream(rutaDescarga + "/" + nombreArchivo)) {
                out.write(contenidoBase64.getBytes());
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