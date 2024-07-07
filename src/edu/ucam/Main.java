package edu.ucam;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccesoAplicacion accesoAplicacion = new AccesoAplicacion();
        ConfiguracionIMAP configuracionIMAP = new ConfiguracionIMAP();

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
                    mostrarMenuAdministrador(scanner, accesoAplicacion, configuracionIMAP, usuario);
                } else {
                    mostrarMenuUsuario(scanner, configuracionIMAP, usuario);
                }
            } else {
                System.out.println("Nombre de usuario o contraseña incorrectos.");
            }
        }
    }

    private static void mostrarMenuAdministrador(Scanner scanner, AccesoAplicacion accesoAplicacion, ConfiguracionIMAP configuracionIMAP, String usuario) {
        ClienteIMAP clienteIMAP = new ClienteIMAP();
        System.out.println("¡Bienvenido, Administrador!");

        while (true) {
            System.out.println("--- MENÚ ADMINISTRADOR ---");
            System.out.println("1. Crear nuevo usuario");
            System.out.println("2. Eliminar usuario");
            System.out.println("3. Modificar usuario");
            System.out.println("4. Listar usuarios");
            System.out.println("5. Configurar servidor IMAP");
            System.out.println("6. Listar correos del buzón principal");
            System.out.println("7. Leer contenido de un mensaje");
            System.out.println("8. Eliminar mensajes");
            System.out.println("9. Crear carpeta");
            System.out.println("10. Eliminar carpeta");
            System.out.println("11. Listar correos de una carpeta");
            System.out.println("12. Mover mensaje entre carpetas");
            System.out.println("13. Descargar archivos adjuntos");
            System.out.println("14. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            try {
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
                        configurarServidorIMAP(scanner, configuracionIMAP, usuario);
                        break;
                    case 6:
                        listarCorreosBuzonPrincipal(configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 7:
                        leerContenidoMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 8:
                        eliminarMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 9:
                        crearCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 10:
                        eliminarCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 11:
                        listarCorreosCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 12:
                        moverMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 13:
                        descargarAdjuntos(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 14:
                        System.out.println("Cerrando sesión de administrador...");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

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
            System.out.println("9. Descargar archivos adjuntos");
            System.out.println("10. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcion) {
                    case 1:
                        configurarServidorIMAP(scanner, configuracionIMAP, usuario);
                        break;
                    case 2:
                        listarCorreosBuzonPrincipal(configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 3:
                        leerContenidoMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 4:
                        eliminarMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 5:
                        crearCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 6:
                        eliminarCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 7:
                        listarCorreosCarpeta(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 8:
                        moverMensaje(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 9:
                        descargarAdjuntos(scanner, configuracionIMAP, clienteIMAP, usuario);
                        break;
                    case 10:
                        System.out.println("Saliendo del programa...");
                        return;
                    default:
                        System.out.println("Opción no válida, por favor intente de nuevo.");
                }
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        }
    }

    private static void configurarServidorIMAP(Scanner scanner, ConfiguracionIMAP configuracionIMAP, String usuario) {
        try {
            System.out.println("--- CONFIGURACIÓN DEL SERVIDOR IMAP ---");
            System.out.print("Ingrese el correo IMAP: ");
            String usuarioIMAP = scanner.nextLine();
            System.out.print("Ingrese la contraseña IMAP: ");
            String contraseñaIMAP = scanner.nextLine();

            configuracionIMAP.configurarServidorIMAP(usuario, usuarioIMAP, contraseñaIMAP);
            System.out.println("Servidor IMAP configurado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al configurar el servidor IMAP: " + e.getMessage());
        }
    }

    private static void listarCorreosBuzonPrincipal(ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- LISTADO DE CORREOS ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                clienteIMAP.listarCorreosBuzonPrincipal();
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al listar los correos: " + e.getMessage());
        }
    }

    private static void leerContenidoMensaje(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- LEER CONTENIDO DE UN MENSAJE ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el número del mensaje a leer: ");
                int numMensaje = Integer.parseInt(scanner.nextLine());
                clienteIMAP.leerContenidoMensaje(numMensaje);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al leer el mensaje: " + e.getMessage());
        }
    }

    private static void eliminarMensaje(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- ELIMINAR MENSAJE ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el número del mensaje a eliminar: ");
                int numMensaje = Integer.parseInt(scanner.nextLine());
                clienteIMAP.eliminarMensaje(numMensaje);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el mensaje: " + e.getMessage());
        }
    }

    private static void crearCarpeta(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- CREAR CARPETA ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el nombre de la nueva carpeta: ");
                String nombreCarpeta = scanner.nextLine();
                clienteIMAP.crearCarpeta(nombreCarpeta);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear la carpeta: " + e.getMessage());
        }
    }

    private static void eliminarCarpeta(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- ELIMINAR CARPETA ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el nombre de la carpeta a eliminar: ");
                String nombreCarpeta = scanner.nextLine();
                clienteIMAP.eliminarCarpeta(nombreCarpeta);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar la carpeta: " + e.getMessage());
        }
    }

    private static void listarCorreosCarpeta(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- LISTAR CORREOS DE UNA CARPETA ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el nombre de la carpeta: ");
                String nombreCarpeta = scanner.nextLine();
                clienteIMAP.listarCorreosCarpeta(nombreCarpeta);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al listar los correos de la carpeta: " + e.getMessage());
        }
    }

    private static void moverMensaje(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- MOVER MENSAJE ENTRE CARPETAS ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el número del mensaje a mover: ");
                int numMensaje = Integer.parseInt(scanner.nextLine());
                System.out.print("Ingrese la carpeta de origen: ");
                String carpetaOrigen = scanner.nextLine();
                System.out.print("Ingrese la carpeta de destino: ");
                String carpetaDestino = scanner.nextLine();
                clienteIMAP.moverMensaje(carpetaOrigen, carpetaDestino, numMensaje);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al mover el mensaje: " + e.getMessage());
        }
    }

    private static void descargarAdjuntos(Scanner scanner, ConfiguracionIMAP configuracionIMAP, ClienteIMAP clienteIMAP, String usuario) {
        try {
            System.out.println("--- DESCARGAR ARCHIVOS ADJUNTOS ---");
            if (configuracionIMAP.tieneConfiguracion(usuario)) {
                String[] configuracion = configuracionIMAP.getConfiguracionIMAP(usuario);
                clienteIMAP.conectar(configuracion[0], Integer.parseInt(configuracion[1]), configuracion[2], configuracion[3]);
                System.out.print("Ingrese el número del mensaje: ");
                int numMensaje = Integer.parseInt(scanner.nextLine());
                System.out.print("Ingrese la ruta de destino para guardar el adjunto: ");
                String rutaDestino = scanner.nextLine();
                clienteIMAP.descargarAdjuntos(numMensaje, rutaDestino);
                clienteIMAP.desconectar();
            } else {
                System.out.println("No hay configuración IMAP disponible para el usuario.");
            }
        } catch (Exception e) {
            System.out.println("Error al descargar los adjuntos: " + e.getMessage());
        }
    }
}