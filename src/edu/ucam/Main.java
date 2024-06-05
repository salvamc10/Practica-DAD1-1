package edu.ucam;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccesoAplicacion accesoAplicacion = new AccesoAplicacion();
        ConfiguracionIMAP configuracionIMAP = new ConfiguracionIMAP();
        ClienteIMAP clienteIMAP = new ClienteIMAP();

        while (true) {
            // Control de acceso a la aplicación
            System.out.println("--- INICIO DE SESIÓN ---");
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contraseña = scanner.nextLine();

            if (accesoAplicacion.autenticarUsuario(usuario, contraseña)) {
                System.out.println("Inicio de sesión exitoso.");

                if (accesoAplicacion.esAdministrador(usuario)) {
                    mostrarMenuAdministrador(scanner, accesoAplicacion);
                } else {
                    mostrarMenuUsuario(scanner, configuracionIMAP, usuario);
                }
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
            }
        }
    }

    private static void mostrarMenuAdministrador(Scanner scanner, AccesoAplicacion accesoAplicacion) {
        System.out.println("¡Bienvenido, Administrador!");

        while (true) {
            System.out.println("--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Crear nuevo usuario");
            System.out.println("2. Eliminar usuario");
            System.out.println("3. Modificar usuario");
            System.out.println("4. Listar usuarios");
            System.out.println("5. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de usuario nuevo: ");
                    String nuevoUsuario = scanner.nextLine();
                    System.out.print("Ingrese la contraseña del nuevo usuario: ");
                    String nuevaContraseña = scanner.nextLine();
                    accesoAplicacion.crearUsuario(nuevoUsuario, nuevaContraseña);
                    System.out.println("Usuario creado exitosamente.");
                    break;
                case 2:
                    System.out.print("Ingrese el nombre de usuario a eliminar: ");
                    String usuarioEliminar = scanner.nextLine();
                    accesoAplicacion.eliminarUsuario(usuarioEliminar);
                    System.out.println("Usuario eliminado exitosamente.");
                    break;
                case 3:
                    System.out.print("Ingrese el nombre de usuario a modificar: ");
                    String usuarioModificar = scanner.nextLine();
                    System.out.print("Ingrese la nueva contraseña del usuario: ");
                    String nuevaContraseñaModificar = scanner.nextLine();
                    accesoAplicacion.modificarUsuario(usuarioModificar, nuevaContraseñaModificar);
                    break;
                case 4:
                    accesoAplicacion.listarUsuarios();
                    break;
                case 5:
                    System.out.println("Cerrando sesión de administrador...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }

    // Método para mostrar el menú del usuario regular
    private static void mostrarMenuUsuario(Scanner scanner, ConfiguracionIMAP configuracionIMAP, String usuario) {
        ClienteIMAP clienteIMAP = new ClienteIMAP();

        while (true) {
            System.out.println("¡Bienvenido, Usuario!");

            System.out.println("--- MENÚ USUARIO ---");
            System.out.println("1. Configurar servidor IMAP");
            System.out.println("2. Listar correos del buzón principal");
            System.out.println("3. Leer contenido de un mensaje");
            System.out.println("4. Eliminar mensajes");
            System.out.println("5. Crear carpeta");
            System.out.println("6. Eliminar carpeta");
            System.out.println("7. Listar correos de una carpeta");
            System.out.println("8. Mover mensaje entre carpetas");
            System.out.println("9. ");
            System.out.println("9. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("--- CONFIGURACIÓN DEL SERVIDOR IMAP ---");
                    System.out.print("Ingrese el correo IMAP: ");
                    String usuarioIMAP = scanner.nextLine();
                    System.out.print("Ingrese la contraseña IMAP: ");
                    String contraseñaIMAP = scanner.nextLine();

                    configuracionIMAP.configurarServidorIMAP(usuario, usuarioIMAP, contraseñaIMAP);
                    System.out.println("Servidor IMAP configurado exitosamente.");
                    break;
                case 2:
                    System.out.println("--- LISTADO DE CORREOS ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        clienteIMAP.listarCorreosBuzonPrincipal();
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                case 3:
                    System.out.println("--- LEER CONTENIDO DE UN MENSAJE ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el número del mensaje: ");
                        int numMensaje = Integer.parseInt(scanner.nextLine());
                        clienteIMAP.leerContenidoMensaje(numMensaje);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                case 4:
                    System.out.println("--- ELIMINAR MENSAJES ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el número del mensaje a eliminar: ");
                        int numMensajeEliminar = Integer.parseInt(scanner.nextLine());
                        clienteIMAP.eliminarMensaje(numMensajeEliminar);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                case 5:
                    System.out.println("--- CREAR CARPETA ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el nombre de la nueva carpeta: ");
                        String nuevaCarpeta = scanner.nextLine();
                        clienteIMAP.crearCarpeta(nuevaCarpeta);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                case 6:
                    System.out.println("--- ELIMINAR CARPETA ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el nombre de la carpeta a eliminar: ");
                        String carpetaEliminar = scanner.nextLine();
                        clienteIMAP.eliminarCarpeta(carpetaEliminar);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                case 7:
                    System.out.println("--- LISTAR CORREOS DE UNA CARPETA ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el nombre de la carpeta: ");
                        String carpeta = scanner.nextLine();
                        clienteIMAP.listarCorreosCarpeta(carpeta);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                    
                case 8:
                    System.out.println("--- MOVER MENSAJE ENTRE CARPETAS ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese la carpeta de origen: ");
                        String origenCarpeta = scanner.nextLine();
                        System.out.print("Ingrese la carpeta de destino: ");
                        String destinoCarpeta = scanner.nextLine();
                        System.out.print("Ingrese el número del mensaje a mover: ");
                        int numMensajeMover = Integer.parseInt(scanner.nextLine());
                        clienteIMAP.moverMensaje(origenCarpeta, destinoCarpeta, numMensajeMover);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                    
                case 9:
                    System.out.println("--- DESCARGAR ARCHIVOS ADJUNTOS ---");
                    if (configuracionIMAP.tieneConfiguracion(usuario)) {
                        String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                        clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                        System.out.print("Ingrese el número del mensaje: ");
                        int numMensajeAdjunto = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese la carpeta de destino para los adjuntos: ");
                        String carpetaDestino = scanner.nextLine();
                        clienteIMAP.descargarAdjuntos(numMensajeAdjunto, carpetaDestino);
                        clienteIMAP.desconectar();
                    } else {
                        System.out.println("No hay configuración IMAP disponible para el usuario.");
                    }
                    break;
                    
                case 10:
                    System.out.println("Cerrando sesión de usuario...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }
}