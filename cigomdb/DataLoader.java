/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cigomdb;

import database.Transacciones;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public class DataLoader {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String database = "cigomdb";
        String user = "root";
        String host = "localhost";
        String password = "amorphis";
        String input = "C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\4 db\\swissprot\\uniref100.5k.xml";
        String mapPrefix = "gen_id_";
        String idPrefix = "M1SE3";
        String gffIn = "C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\8 Metagenomas\\results_func\\genes_prediction\\metagolfos_FGS.gff";
        String ncIn = "C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\8 Metagenomas\\results_func\\genes_prediction\\metagolfos_FGS.ffn";
        String aaIn = "C:\\Users\\Alejandro\\Documents\\Projects\\pemex\\8 Metagenomas\\results_func\\genes_prediction\\metagolfos_FGS.faa";
        boolean debug = false;
        ArrayList<String> modes = new ArrayList<String>();
        modes.add("swiss");
        modes.add("genes");
        modes.add("");
        String mode = "genes";
        for (int i = 0; i < args.length; i++) {
            if (i == 0 && (!args[i].equals("-h") && !args[i].equals("-help"))) {
                mode = args[i];
                if (!modes.contains(mode)) {
                    System.out.println("Opcion no valida\n\n");
                    printHelp();
                    System.exit(1);
                }
            }
            if (args[i].equals("-i")) {
                try {
                    input = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion i - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-db")) {
                try {
                    database = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion db - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-u")) {
                try {
                    user = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion user - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-host")) {
                try {
                    host = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion host - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-pass")) {
                try {
                    password = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion pass - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-d")) {
                debug = true;
            } else if (args[i].equals("-h") || args[i].equals("-help")) {
                printHelp();
                System.exit(1);
            } else if (args[i].equals("-idpre")) {

                try {
                    idPrefix = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion idpre - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-gff")) {

                try {
                    gffIn = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion gff - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-nc")) {
                try {
                    ncIn = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion nc - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-aa")) {
                try {
                    aaIn = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion aa - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            } else if (args[i].equals("-mapre")) {
                try {
                    mapPrefix = args[i + 1];
                    i++;
                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    System.out.println("Opcion mapre - Se esperaba un argumento\n\n");
                    printHelp();
                    System.exit(1);
                }
            }
        }
        Transacciones transacciones = new Transacciones(database, user, host, password);
        transacciones.setDebug(debug);
        if (transacciones.testConnection()) {
            String log = "";
            long start = System.currentTimeMillis();
            if (mode.equals("swiss")) {
                SwissProt swiss = new SwissProt(transacciones);
                log = swiss.loadSwissProtFromXML(input, debug);
                System.out.println("END: " + log);
            } else if (mode.equals("genes")) {
                if (gffIn.length() > 0 && (aaIn.length() + ncIn.length()) > 0) {
                    GeneFuncLoader loader = new GeneFuncLoader(transacciones);
                    log += loader.loadFragileScanFiles(idPrefix, gffIn, ncIn, aaIn, mapPrefix);
                } else {
                    System.out.println("Para correr el programa gen se espera minimo un archivo gff y un archivo fasta");
                    printHelp();
                    System.exit(1);
                }
            }
            long end = System.currentTimeMillis() - start;
            System.out.println(end / 1000 + " s.");
            System.out.println("\nLog and Messages\n" + log);
        } else {
            System.out.println("No hay conexion con la BD.\nAsegurese de introducir de manera correcta los parametros de conexion.\nDataLoader -h para mas ayuda.");
        }
    }

    private static void printHelp() {
        System.out.println("*********CIGOM DATABASE LOADER***************");
        System.out.println("**@author:  Alejandro Abdala              **");
        System.out.println("********************************************");
        System.out.println("uso:DataLoader <mode> <opt>");
        System.out.println("#modes:");
        System.out.println("\tswiss.\tCarga la bd de swissprot a partir del xml");
        System.out.println("\t\tEl único parámetro que toma este programa es -i. Input file con XML swissprot");
        System.out.println("\tgen.\tCarga a la bd resultados de prediccion de genes");
        System.out.println("\n\t\tEsta opción necesita los siguientes parametros: ");
        System.out.println("\n\t\t\t -idpre\t Prefijo (+ numero consecutivo) para el ID que tendrá cada gen predicho.\n\t\t\t\t Se espera que cumpla con alguna nomencaltura"
                + "\n\t\t\t -gff\t Archivo gff que tiene las coordenadas de cada gen en el archivo de contigs "
                + "\n\t\t\t -nc\t Archivo fasta con los genes predichos - secuencias de nucleótidos"
                + "\n\t\t\t -aa\t Archivo fasta con las proteínas predichas - secuencias de aminoácidos"
                + "\n\t\t\t -mapre\t Prefijo para mapear futras predicciones. Default = gene_id_#");
        System.out.println("\n--------------------------------------------------");
        System.out.println("\n             --------Options--------");
        System.out.println("-d\t debug = true");
        System.out.println("-db\t database");
        System.out.println("-h\t help menu");
        System.out.println("-host\t db host");
        System.out.println("-i\t set inputfile");
        System.out.println("-pass\t db password");
        System.out.println("-u\t db user");

    }
}
