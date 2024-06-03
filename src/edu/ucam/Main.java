package edu.ucam;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AccesoAplicacion accesoAplicacion = new AccesoAplicacion();
        ConfiguracionIMAP configuracionIMAP = new ConfiguracionIMAP();

        // Control de acceso a la aplicación
        System.out.println("--- INICIO DE SESIÓN ---");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contraseña = scanner.nextLine();

        // Verificar las credenciales del usuario
        if (accesoAplicacion.autenticarUsuario(usuario, contraseña)) {
            System.out.println("Inicio de sesión exitoso.");

            // Verificar si el usuario es administrador
            if (accesoAplicacion.esAdministrador(usuario)) {
                // Mostrar el menú para el administrador
                mostrarMenuAdministrador(scanner, accesoAplicacion);
            } else {
                // Mostrar el menú para el usuario regular
                mostrarMenuUsuario(scanner, configuracionIMAP, usuario);
            }
        } else {
            // Mensaje de error si las credenciales son incorrectas
            System.out.println("Nombre de usuario o contraseña incorrectos.");
        }

        scanner.close();
    }

    // Método para mostrar el menú del administrador
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
            System.out.println("3. Salir");

            System.out.print("Seleccione una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("--- CONFIGURACIÓN DEL SERVIDOR IMAP ---");
                    System.out.print("Ingrese el servidor IMAP: ");
                    String servidorIMAP = scanner.nextLine();
                    System.out.print("Ingrese el puerto: ");
                    int puertoIMAP = Integer.parseInt(scanner.nextLine());
                    System.out.print("Ingrese el nombre de usuario IMAP: ");
                    String usuarioIMAP = scanner.nextLine();
                    System.out.print("Ingrese la contraseña IMAP: ");
                    String contraseñaIMAP = scanner.nextLine();

                    configuracionIMAP.configurarServidorIMAP(usuario, servidorIMAP, puertoIMAP, usuarioIMAP, contraseñaIMAP);
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
                    System.out.println("Cerrando sesión de usuario...");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }
    }
}
