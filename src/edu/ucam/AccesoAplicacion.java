package edu.ucam;

import java.util.Hashtable;

public class AccesoAplicacion {
    private Hashtable<String, String> usuarios;
    private Hashtable<String, Boolean> roles;

    public AccesoAplicacion() {
        usuarios = new Hashtable<>();
        roles = new Hashtable<>();

        // Crear un usuario administrador por defecto
        usuarios.put("admin", "admin");
        roles.put("admin", true);

        // Crear usuario no Administrador por defecto
        usuarios.put("pedro", "pedro");
        roles.put("pedro", false);
    }

    public boolean autenticarUsuario(String usuario, String contraseña) {
        return usuarios.containsKey(usuario) && usuarios.get(usuario).equals(contraseña);
    }

    public boolean esAdministrador(String usuario) {
        return roles.getOrDefault(usuario, false);
    }

    public void crearUsuario(String usuario, String contraseña) {
        if (!usuarios.containsKey(usuario)) {
            usuarios.put(usuario, contraseña);
            roles.put(usuario, false); // por defecto no es administrador
        } else {
            System.out.println("El usuario ya existe.");
        }
    }

    public void eliminarUsuario(String usuario) {
        if (usuarios.containsKey(usuario)) {
            usuarios.remove(usuario);
            roles.remove(usuario);
            System.out.println("Usuario eliminado exitosamente.");
        } else {
            System.out.println("El usuario no existe.");
        }
    }

    public void modificarUsuario(String usuario, String nuevaContraseña) {
        if (usuarios.containsKey(usuario)) {
            usuarios.put(usuario, nuevaContraseña);
            System.out.println("Contraseña del usuario " + usuario + " modificada exitosamente.");
        } else {
            System.out.println("El usuario no existe.");
        }
    }

    public void listarUsuarios() {
        System.out.println("--- Lista de Usuarios ---");
        for (String usuario : usuarios.keySet()) {
            String rol = roles.get(usuario) ? "Administrador" : "Usuario";
            System.out.println("Usuario: " + usuario + " - Rol: " + rol);
        }
    }
}