package edu.ucam;

import java.util.Hashtable;

public class ConfiguracionIMAP {
    private Hashtable<String, String[]> configuracionesIMAP;

    public ConfiguracionIMAP() {
        configuracionesIMAP = new Hashtable<>();
    }

    public void configurarServidorIMAP(String usuario, String usuarioIMAP, String contraseñaIMAP) {
        String servidorIMAP = "imap.gmail.com";
        int puertoIMAP = 993;
        configuracionesIMAP.put(usuario, new String[]{servidorIMAP, String.valueOf(puertoIMAP), usuarioIMAP, contraseñaIMAP});
    }

    public boolean tieneConfiguracion(String usuario) {
        return configuracionesIMAP.containsKey(usuario);
    }

    public String[] getConfiguracionIMAP(String usuario) {
        return configuracionesIMAP.get(usuario);
    }
}