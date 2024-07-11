package compilador;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

public class AnalizadorLexico {
    public static Nodo NodoIni = null;
    public static String RutaTxt = "compilador1\\src\\main\\resources\\TextPrueba.txt";

    // Crear (biMap) lista de Palabras receravadas
    public static BiMap<String, String> palabrasReservadas = HashBiMap.create();
    
    // Crear (biMap) lista de Lexema Simples (1 Caracter)
    public static Map<String, String> Lexema = new HashMap<>();
    
    // Crear (biMap) lista de Errores
    public static Map<String, String> Errores = new HashMap<>();

    private static boolean EstaEnAlfabeto(char caracter) {
        if (Character.isLetter(caracter)) {
            return true;
        }

        return false;
    }

    private static boolean EsteaEnDigito(char Digito) {
        if (Character.isDigit(Digito)) {
            return true;
        }
        return false;
    }

    private void CrearNodo(String Token, String Campo2, int NumLinea) {
        Nodo NuevoNodo = new Nodo(Token, Campo2, NumLinea);
        Nodo Aux = NodoIni;
        if (NodoIni != null) {
            while (Aux.NodoSig != null) {
                Aux = Aux.NodoSig;
            }
            Aux.NodoSig = NuevoNodo;
        }
        if (NodoIni == null) {
            NodoIni = NuevoNodo;
        }

    }

    public static void RecorrerTokens() {
        Nodo Aux = NodoIni;
        while (Aux != null) {
            System.out.println(Aux.Token + "  " + Aux.Campo2 + "  Linea:" + Aux.NumLinea);
            Aux = Aux.NodoSig;
        }
    }

    public static Nodo ObtenerNodoIniLexemas() {
        // Inicializa lista de Palabras receravadas
        palabrasReservadas.put("program", "200");
        palabrasReservadas.put("var", "201");
        palabrasReservadas.put("string", "202");
        palabrasReservadas.put("integer", "203");
        palabrasReservadas.put("real", "204");
        palabrasReservadas.put("boolean", "205");
        palabrasReservadas.put("begin", "206");
        palabrasReservadas.put("end", "207");
        palabrasReservadas.put("read", "208");
        palabrasReservadas.put("write", "209");
        palabrasReservadas.put("if", "210");
        palabrasReservadas.put("then", "211");
        palabrasReservadas.put("else", "212");
        palabrasReservadas.put("while", "213");
        palabrasReservadas.put("do", "214");
        palabrasReservadas.put("or", "215");
        palabrasReservadas.put("and", "216");
        palabrasReservadas.put("not", "217");
        // Inicializa lista de Lexema Simples (1 Caracter)
        Lexema.put("+", "103");
        Lexema.put("-", "104");
        Lexema.put("*", "105");
        Lexema.put("(", "107");
        Lexema.put(")", "108");
        Lexema.put(".", "109");
        Lexema.put(",", "110");
        Lexema.put(";", "111");
        Lexema.put(":", "119");
        // Inicializa lista de Errores
        Map<String, String> Errores = new HashMap<>();
        Errores.put("500", "Se esperaba un digito");
        Errores.put("501", "Cierre inesperado de comentario");
        Errores.put("502", "Se esperaba un =");
        
        int CaracterLeido;
        String Acumulado = "";
        int SaltosLinea = 1;
        try (PushbackReader Lector = new PushbackReader(new FileReader(RutaTxt));) {
            AnalizadorLexico AnalizadorLexico = new AnalizadorLexico();
            while ((CaracterLeido = Lector.read()) != -1) {
                // Contar Saltos de linea
                if (CaracterLeido == '\n') {
                    SaltosLinea++;
                }
                // Capturar Numeros Enteros y Numeros Racionales
                if (EsteaEnDigito((char) CaracterLeido)) {
                    boolean NumeroRacional = false;
                    Acumulado = ((char) CaracterLeido) + "";

                    do {
                        CaracterLeido = Lector.read();
                        if (CaracterLeido == '\n') {
                            SaltosLinea++;
                        }
                        if (EsteaEnDigito((char) CaracterLeido)) {
                            Acumulado = Acumulado + ((char) CaracterLeido);
                        }
                        if ('.' == ((char) CaracterLeido) && NumeroRacional == false) {
                            Acumulado = Acumulado + ((char) CaracterLeido);
                            NumeroRacional = true;
                            CaracterLeido = Lector.read();
                            if (CaracterLeido == '\n') {
                                SaltosLinea++;
                            }
                            if (!EsteaEnDigito((char) CaracterLeido)) {
                                AnalizadorLexico.CrearNodo("Error:500", Errores.get("500"), SaltosLinea);
                                CaracterLeido = Lector.read();
                                NumeroRacional = false;
                                if (CaracterLeido == '\n') {
                                    SaltosLinea++;
                                }
                                Acumulado = "";
                                if (!EsteaEnDigito((char) CaracterLeido)) {
                                    break;
                                }

                            }
                            if (EsteaEnDigito((char) CaracterLeido)) {
                                Acumulado = Acumulado + ((char) CaracterLeido);
                            }

                        }

                        if (!".".equals((char) CaracterLeido) && !EsteaEnDigito((char) CaracterLeido)) {
                            if (NumeroRacional) {
                                AnalizadorLexico.CrearNodo(Acumulado, "102", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            if (!NumeroRacional) {
                                AnalizadorLexico.CrearNodo(Acumulado, "101", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                        }

                    } while (CaracterLeido != -1);
                }
                // Caputara IDs y Palabras reservadas
                if (EstaEnAlfabeto((char) CaracterLeido)) {

                    Acumulado = ((char) CaracterLeido) + "";
                    do {
                        CaracterLeido = Lector.read();
                        if (CaracterLeido == '\n') {
                            SaltosLinea++;
                        }
                        if (EstaEnAlfabeto((char) CaracterLeido) || EsteaEnDigito((char) CaracterLeido)) {

                            if (palabrasReservadas.get(Acumulado) == null) {
                                Acumulado = Acumulado + ((char) CaracterLeido);
                            }
                            if (palabrasReservadas.get(Acumulado) != null) {
                                AnalizadorLexico.CrearNodo("Palabra reservada " + Acumulado, (palabrasReservadas.get(Acumulado)), SaltosLinea);

                                Acumulado = "";
                                break;
                            }

                        }

                        if (!EstaEnAlfabeto((char) CaracterLeido) && !EsteaEnDigito((char) CaracterLeido)) {
                            AnalizadorLexico.CrearNodo(Acumulado, "100", SaltosLinea);

                            Acumulado = "";
                            break;
                        }

                    } while (CaracterLeido != -1);

                }
                // Capturar comentarios
                if ('/' == ((char) CaracterLeido)) {
                    Acumulado = ((char) CaracterLeido) + "";
                    CaracterLeido = Lector.read();
                    if (CaracterLeido == '\n') {
                        SaltosLinea++;
                    }
                    if (((char) CaracterLeido) != '*') {
                        AnalizadorLexico.CrearNodo("/", "106", SaltosLinea);
                        Acumulado = "";
                        Lector.unread(CaracterLeido);

                    }
                    if ('*' == ((char) CaracterLeido)) {
                        Acumulado = Acumulado + ((char) CaracterLeido);
                        do {
                            CaracterLeido = Lector.read();
                            if (CaracterLeido == '\n') {
                                SaltosLinea++;
                                AnalizadorLexico.CrearNodo("501", Errores.get("501"), SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            if (((char) CaracterLeido) == '*') {
                                CaracterLeido = Lector.read();
                                if (CaracterLeido == '\n') {
                                    SaltosLinea++;
                                }
                                if (CaracterLeido == '/') {
                                    Acumulado = "";
                                    break;
                                } else {
                                    Acumulado = Acumulado + ((char) CaracterLeido);
                                }
                            }
                            if (CaracterLeido == -1) {
                                AnalizadorLexico.CrearNodo("501", Errores.get("501"), SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            if (((char) CaracterLeido) != '*') {
                                Acumulado = Acumulado + ((char) CaracterLeido);
                            }

                        } while (CaracterLeido != -1);

                    }

                }
                // Capturar cadenas
                if ('"' == ((char) CaracterLeido)) {
                    Acumulado = ((char) CaracterLeido) + "";
                    do {
                        CaracterLeido = Lector.read();
                        if (CaracterLeido == '\n') {
                            SaltosLinea++;
                        }
                        if (CaracterLeido == -1) {
                            AnalizadorLexico.CrearNodo("Error:501", Errores.get("501"), SaltosLinea);
                            Acumulado = "";
                            break;
                        }
                        if ((char) CaracterLeido != '"') {
                            Acumulado = Acumulado + ((char) CaracterLeido);
                        }
                        if ((char) CaracterLeido == '"') {
                            Acumulado = Acumulado + ((char) CaracterLeido);
                            AnalizadorLexico.CrearNodo("Cadenan " + Acumulado, "119", SaltosLinea);
                            Acumulado = "";
                            break;

                        }
                    } while (CaracterLeido != -1);

                }
                // Capturar Lexemas Compuestos
                if ((((char) CaracterLeido) == '<') || (((char) CaracterLeido) == '=')|| (((char) CaracterLeido) == '>') || (((char) CaracterLeido) == ':')) {
                    Acumulado = (char) CaracterLeido + "";
                    do {

                        if ((char) CaracterLeido == '>') {
                            CaracterLeido = Lector.read();
                            if (CaracterLeido == '\n') {
                                SaltosLinea++;
                            }
                            if ((char) CaracterLeido != '=') {
                                AnalizadorLexico.CrearNodo(Acumulado, "114", SaltosLinea);
                                Lector.unread(CaracterLeido);
                                Acumulado = "";
                                break;
                            }
                            if ((char) CaracterLeido == '=') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "115", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                        }
                        if ((char) CaracterLeido == '<') {
                            CaracterLeido = Lector.read();
                            if (CaracterLeido == '\n') {
                                SaltosLinea++;
                            }
                            if ((char) CaracterLeido != '=' && (char) CaracterLeido != '>') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "112", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            if ((char) CaracterLeido == '=') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "113", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            if ((char) CaracterLeido == '>') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "117", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                        }
                        if ((char) CaracterLeido == '=') {
                            CaracterLeido = Lector.read();
                            if (CaracterLeido == '\n') {
                                SaltosLinea++;
                            }
                            if ((char) CaracterLeido != '=') {
                                AnalizadorLexico.CrearNodo("Error:502", Errores.get("502"), SaltosLinea);
                                Lector.unread(CaracterLeido);
                                Acumulado = "";
                                break;
                            }
                            if ((char) CaracterLeido == '=') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "116", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                        }
                        if ((char) CaracterLeido == ':') {

                            CaracterLeido = Lector.read();
                            if ((char) CaracterLeido == '=') {
                                Acumulado = Acumulado + (char) CaracterLeido;
                                AnalizadorLexico.CrearNodo(Acumulado, "118", SaltosLinea);
                                Acumulado = "";
                                break;
                            }
                            
                            if ((char) CaracterLeido != '=') {
                                AnalizadorLexico.CrearNodo(Acumulado, "119", SaltosLinea);
                                Lector.unread(CaracterLeido);
                                Acumulado = "";
                                break;
                            }
                            
                        }
                    } while (CaracterLeido != -1);
                }
                // Cpturar Lexemas Simples
                if (Lexema.get(((char) CaracterLeido) + "") != null) {
                    Acumulado = ((char) CaracterLeido) + "";
                    AnalizadorLexico.CrearNodo(Acumulado,Lexema.get(Acumulado), SaltosLinea);
                    Acumulado = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return NodoIni;
    }

}