package edu.ucam;

import java.io.*;

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
        writer.write("A1 LOGIN " + email + " " + contraseña + "\r\n");
        writer.flush();
        leerRespuesta("A1 OK");
    }

    public void listarCorreosBuzonPrincipal() {
        try {
            writer.write("A2 SELECT INBOX\r\n");
            writer.flush();
            leerRespuesta("A2 OK");

            writer.write("A3 FETCH 1:* (BODY[HEADER.FIELDS (SUBJECT DATE FROM)])\r\n");
            writer.flush();
            leerRespuesta("A3 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leerContenidoMensaje(int numMensaje) {
        try {
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            leerRespuesta("A3 OK");

            writer.write("A4 FETCH " + numMensaje + " BODY[TEXT]\r\n");
            writer.flush();
            leerRespuesta("A4 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarMensaje(int numMensaje) {
        try {
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            leerRespuesta("A3 OK");

            writer.write("A5 STORE " + numMensaje + " +FLAGS \\Deleted\r\n");
            writer.flush();
            leerRespuesta("A5 OK");

            writer.write("A6 EXPUNGE\r\n");
            writer.flush();
            leerRespuesta("A6 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crearCarpeta(String nombreCarpeta) {
        try {
            writer.write("A7 CREATE " + nombreCarpeta + "\r\n");
            writer.flush();
            leerRespuesta("A7 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCarpeta(String nombreCarpeta) {
        try {
            writer.write("A8 DELETE " + nombreCarpeta + "\r\n");
            writer.flush();
            leerRespuesta("A8 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarCorreosCarpeta(String nombreCarpeta) {
        try {
            writer.write("A9 SELECT " + nombreCarpeta + "\r\n");
            writer.flush();
            leerRespuesta("A9 OK");

            writer.write("A10 FETCH 1:* (BODY[HEADER.FIELDS (SUBJECT DATE FROM)])\r\n");
            writer.flush();
            leerRespuesta("A10 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moverMensaje(String origenCarpeta, String destinoCarpeta, int numMensaje) {
        try {
            writer.write("A11 SELECT " + origenCarpeta + "\r\n");
            writer.flush();
            leerRespuesta("A11 OK");

            writer.write("A12 COPY " + numMensaje + " " + destinoCarpeta + "\r\n");
            writer.flush();
            leerRespuesta("A12 OK");

            writer.write("A13 STORE " + numMensaje + " +FLAGS \\Deleted\r\n");
            writer.flush();
            leerRespuesta("A13 OK");

            writer.write("A14 EXPUNGE\r\n");
            writer.flush();
            leerRespuesta("A14 OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void descargarAdjuntos(int numMensaje, String rutaDestino) {
        try {
            writer.write("A3 SELECT INBOX\r\n");
            writer.flush();
            leerRespuesta("A3 OK");

            writer.write("A4 FETCH " + numMensaje + " BODY[HEADER]\r\n");
            writer.flush();

            String respuesta = leerRespuestaCompleta("A4 OK");

            String boundary = obtenerBoundary(respuesta);
            if (boundary == null) {
                System.out.println("No se pudo obtener el boundary del mensaje.");
                return;
            }

            writer.write("A5 FETCH " + numMensaje + " BODY[]\r\n");
            writer.flush();
            respuesta = leerRespuestaCompleta("A5 OK");

            guardarAdjuntos(respuesta, boundary, rutaDestino);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtenerBoundary(String response) {
        String boundaryPrefix = "boundary=\"";
        int boundaryIndex = response.indexOf(boundaryPrefix);
        if (boundaryIndex != -1) {
            int startIndex = boundaryIndex + boundaryPrefix.length();
            int endIndex = response.indexOf("\"", startIndex);
            return response.substring(startIndex, endIndex);
        } else {
            boundaryPrefix = "boundary=";
            boundaryIndex = response.indexOf(boundaryPrefix);
            if (boundaryIndex != -1) {
                int startIndex = boundaryIndex + boundaryPrefix.length();
                int endIndex = response.indexOf("\r\n", startIndex);
                return response.substring(startIndex, endIndex).replace("\"", "");
            }
        }
        return null;
    }

    private void guardarAdjuntos(String response, String boundary, String rutaDestino) throws IOException {
        String[] parts = response.split("--" + boundary);
        for (String part : parts) {
            if (part.contains("Content-Disposition: attachment")) {
                String[] lines = part.split("\r\n");
                String filename = null;
                StringBuilder fileContent = new StringBuilder();
                boolean contentStarted = false;

                for (String line : lines) {
                    if (line.startsWith("Content-Disposition: attachment;")) {
                        int filenameIndex = line.indexOf("filename=\"");
                        if (filenameIndex != -1) {
                            int startIndex = filenameIndex + "filename=\"".length();
                            int endIndex = line.indexOf("\"", startIndex);
                            filename = line.substring(startIndex, endIndex);
                        }
                    } else if (contentStarted) {
                        fileContent.append(line).append("\r\n");
                    } else if (line.isEmpty()) {
                        contentStarted = true;
                    }
                }

                if (filename != null) {
                    File file = new File(rutaDestino, filename);
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                        bw.write(fileContent.toString());
                    }
                    System.out.println("Archivo adjunto guardado: " + filename);
                }
            }
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

    private void leerRespuesta(String esperado) throws Exception {
        String response;
        while (!(response = reader.readLine()).startsWith(esperado)) {
            System.out.println(response);
        }
    }

    private String leerRespuestaCompleta(String esperado) throws Exception {
        StringBuilder respuestaCompleta = new StringBuilder();
        String response;
        while (!(response = reader.readLine()).startsWith(esperado)) {
            respuestaCompleta.append(response).append("\r\n");
        }
        return respuestaCompleta.toString();
    }
}