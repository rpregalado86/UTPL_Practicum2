/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author Walter Mora
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

public abstract class ManejoArchivo {

    public static final String CARPETA_ARCHIVOS = "/archivos/";
    public static final String DELIMITADOR_CAMPOS = "|";
    public static final String NOMBRE_ARC_ACCESOS = "accesos.txt";
    public static final String NOMBRE_ARC_RUTAS = "rutas.txt";
    public static final String NOMBRE_ARC_BOLETOS = "boletos.txt";
    public static final String PREFIJO_TEMPORAL = "tmp_";

    public String cabeceraArcAccesos = "USERNAME" + DELIMITADOR_CAMPOS
            + "NOMBRES" + DELIMITADOR_CAMPOS
            + "APELLIDOS" + DELIMITADOR_CAMPOS
            + "PASSWORD";

    public String cabeceraArcRutas = "CODIGO" + DELIMITADOR_CAMPOS
            + "NOMBRE" + DELIMITADOR_CAMPOS
            + "ORIGEN" + DELIMITADOR_CAMPOS
            + "DESTINO" + DELIMITADOR_CAMPOS
            + "COSTO";
    
    public String cabeceraArcBoletos = "CODIGO" + DELIMITADOR_CAMPOS
            + "IDENTIFICACION_CLIENTE" + DELIMITADOR_CAMPOS
            + "NOMBRES_CLIENTE" + DELIMITADOR_CAMPOS
            + "APELLIDOS_CLIENTE" + DELIMITADOR_CAMPOS
            + "FECHA_NACIMIENTO_CLIENTE" + DELIMITADOR_CAMPOS
            + "TIPO_CLIENTE" + DELIMITADOR_CAMPOS
            + "CODIGO_RUTA" + DELIMITADOR_CAMPOS
            + "FECHA" + DELIMITADOR_CAMPOS
            + "HORA" + DELIMITADOR_CAMPOS
            + "TOTAL" + DELIMITADOR_CAMPOS
            + "USUARIO";
    
    public File obtieneArchivo(String nombre) throws Exception {
        File archivo = null;

        URL url = getClass().getResource(CARPETA_ARCHIVOS + nombre);

        if (url == null) {
            archivo = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), CARPETA_ARCHIVOS + nombre);
            archivo.createNewFile();

            BufferedWriter bw = null;
            bw = new BufferedWriter(new FileWriter(archivo));
            
            if (NOMBRE_ARC_ACCESOS.equalsIgnoreCase(nombre) || (PREFIJO_TEMPORAL + NOMBRE_ARC_ACCESOS).equalsIgnoreCase(nombre)) {
                bw.write(cabeceraArcAccesos);
                bw.newLine();
            } else if (NOMBRE_ARC_RUTAS.equalsIgnoreCase(nombre) || (PREFIJO_TEMPORAL + NOMBRE_ARC_RUTAS).equalsIgnoreCase(nombre)) {
                bw.write(cabeceraArcRutas);
                bw.newLine();
            } else if (NOMBRE_ARC_BOLETOS.equalsIgnoreCase(nombre) || (PREFIJO_TEMPORAL + NOMBRE_ARC_BOLETOS).equalsIgnoreCase(nombre)) {
                bw.write(cabeceraArcBoletos);
                bw.newLine();
            }
            bw.close();
        } else {
            archivo = new File(url.getFile());
        }
        return archivo;
    }

    public String[] obtieneRegistroArchivo(String nombreArchivo, String codigo, int posCampo) throws Exception {
        String linea;
        String[] registroAux;
        String[] registro = null;
        boolean encontrado;

        File archivo = obtieneArchivo(nombreArchivo);
        if (archivo != null) {
            BufferedReader bf = new BufferedReader(new FileReader(archivo));
            encontrado = false;
            //Se recorre el archivo hasta encontrar el codigo
            while ((linea = bf.readLine()) != null && encontrado == false) {
                registroAux = linea.split("\\" + DELIMITADOR_CAMPOS);
                if (registroAux[posCampo].equals(codigo)) {
                    encontrado = true;
                    registro = registroAux;
                }
            }
            bf.close();
        }
        return registro;
    }

    public int obtenerNumeroLineasArchivo(String nombreArchivo) throws Exception {
        int numeroLineas = 0;
        File archivo = obtieneArchivo(nombreArchivo);

        if (archivo != null) {
            FileReader fr = new FileReader(archivo);
            BufferedReader bf = new BufferedReader(fr);

            while ((bf.readLine()) != null) {
                numeroLineas++;
            }
            bf.close();
        }
        //Se resta uno por la cabecera del archivo
        return numeroLineas - 1;
    }
}
