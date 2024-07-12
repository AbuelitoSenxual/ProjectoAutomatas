package compilador;

import java.util.ArrayList;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.checkerframework.checker.units.qual.C;

public class AnalizadorSemantico {

    static Nodo NodoIni;
    static String NombrePrograma;
    static Nodo Aux;
    static boolean StatusError;
    static NodoIdentificadores CabezaIdentificadores;
    static String CadenaOperaciones = "";

    public static void main(String[] args) {
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        /// retorna un true si la ejecuccion fue exitosa
        Aux = NodoIni.NodoSig;
        NodoIdentificadores AuxiliarIdentificador = CabezaIdentificadores;
        NombrePrograma = Aux.Token;
        Aux = Aux.NodoSig;
        // ID 100 , palabra reservada var 201
        do {

            // Captura el caso donde se incialice una lista de variables
            if ("201".equals(Aux.Campo2) && StatusError != true) {
                definicion();
            }
            // Captura el caso donde incie una operacion(con un identificador)
            if ("100".equals(Aux.Campo2) && StatusError != true) {
                // Verifica que la operacion inicie de manera correcta con := 118
                Nodo Aux2 = Aux.NodoSig;
                if ("118".equals(Aux2.Campo2) && StatusError != true) {
                    // captura el caso donde no exita la variable
                    if (ExisteID(Aux.Token) != true) {
                        StatusError = true;
                        System.out.println("Error Semantico no se a inicializado la variable: " + Aux.Token);
                        break;
                    }
                    // Si existe
                    else {
                        AuxiliarIdentificador = BuscaIdentificadorxNombre(Aux.Token);
                        // Se avanza al siguiente token despues del identificador (:=)
                        Aux = Aux.NodoSig;
                        VerificarNull();
                        CadenaOperaciones = CadenaOperaciones +AuxiliarIdentificador.Nombre + " = ";
                        AuxiliarIdentificador.Valor = Operacion(AuxiliarIdentificador.TipoDato);

                        if (StatusError != true) {
                            ImprimirIdentificadores();
                        }
                        
                    }
                }
            }
            Aux = Aux.NodoSig;
        } while (Aux != null && StatusError != true);
        System.out.println(convertirATresDirecciones(CadenaOperaciones));

    }

    public static Object OperacionInteger() {
        String operacion = "";
        Object Valor = null;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        while (Aux.Campo2 != "111" && StatusError != true) {
            Aux = Aux.NodoSig;
            // Verifica caso donde es un identificador
            if (Aux.Campo2 == "100") {
                // verifica que el dato sea de tipo integer
                if (BuscaIdentificadorxNombre(Aux.Token).TipoDato != "203") {
                    StatusError = true;
                    System.out.println("Error Semantico el identificador: " + Aux.Token + " no es de tipo integer");
                    break;
                } else {
                    operacion = operacion + BuscaIdentificadorxNombre(Aux.Token).Valor;
                    CadenaOperaciones = CadenaOperaciones + Aux.Token;
                }
            }
            // verifica en caso donde se un numero entero
            if (Aux.Campo2 == "101") {
                operacion = operacion + Aux.Token;
                CadenaOperaciones = CadenaOperaciones + Aux.Token;

            }
            // verifica en caso donde se un operador / = 106, + = 103 , - = 104 , * = 105
            if (Aux.Campo2 == "106" || Aux.Campo2 == "103" || Aux.Campo2 == "104" || Aux.Campo2 == "105") {
                operacion = operacion + Aux.Token;
                CadenaOperaciones = CadenaOperaciones + Aux.Token;
            }

            // captura el caso donde no sea nada de lo de arriba y genera un error
            if (Aux.Campo2 != "100" && Aux.Campo2 != "101" && Aux.Campo2 != "106" && Aux.Campo2 != "103"
                    && Aux.Campo2 != "104" && Aux.Campo2 != "105" && Aux.Campo2 != "111") {
                StatusError = true;
                System.out.println("Error Semantico no se puede realizar la operacion Linea: " + Aux.NumLinea);
                break;
            }

        }

        CadenaOperaciones += ";";
        // 111 es el punto y coma ;
        if (!StatusError) {
            try {
                Valor = (int) engine.eval(operacion);

            } catch (ScriptException e) {
                StatusError = true;
                System.out.println("Error evaluando la operación: " + operacion);
            }
        }

        return Valor;
    }




    
    public static String convertirATresDirecciones(String cadena) {
        StringBuilder codigoTresDirecciones = new StringBuilder();
        
        // Separar las instrucciones por ;
        String[] instrucciones = cadena.split(";");
        
        int contadorTemporal = 1; // Contador para generar nombres únicos para temporales
        
        for (String instruccion : instrucciones) {
            // Limpiar espacios en blanco alrededor de la instrucción
            instruccion = instruccion.trim();
            
            if (instruccion.isEmpty()) {
                continue; // Si es una línea vacía, pasar a la siguiente
            }
            
            // Separar por el operador =
            String[] partes = instruccion.split("=");
            
            // Si la instrucción no tiene el operador = correctamente, continuar con la siguiente instrucción
            if (partes.length != 2) {
                continue;
            }
            
            // Primer parte es la variable a asignar
            String variable = partes[0].trim();
            
            // Segunda parte es la expresión
            String expresion = partes[1].trim();
            
            // Verificar si la expresión es una asignación directa o una operación
            if (expresion.matches("\\d+")) {
                // Es una asignación directa de valor entero
                codigoTresDirecciones.append(variable).append(" = ").append(expresion).append("\n");
            } else {
                // Es una operación que requiere temporales
                // Generar un nombre único para la temporal
                String temporal = "t" + contadorTemporal++;
                codigoTresDirecciones.append(temporal).append(" = ").append(expresion).append("\n");
                codigoTresDirecciones.append(variable).append(" = ").append(temporal).append("\n");
            }
        }
        
        return codigoTresDirecciones.toString();
    }
    










    public static boolean ExisteID(String ID) {
        NodoIdentificadores AuxiliarIdentificador = CabezaIdentificadores;
        while (AuxiliarIdentificador != null) {
            if (AuxiliarIdentificador.Nombre.equals(ID)) {
                return true;
            }
            AuxiliarIdentificador = AuxiliarIdentificador.SigIdentificador;
        }
        return false;
    }

    public static void definicion() {
        ArrayList<String> ListaIdentificadores = new ArrayList<String>();
        String TipoDato = "";

        do {
            Aux = Aux.NodoSig;
            // Es identificador
            if ("100".equals(Aux.Campo2)) {
                // verifica que no se igual al nombre del programa
                if (NombrePrograma.equals(Aux.Token)) {
                    StatusError = true;
                    System.out.println("Error Semantico no se puede usar el  nombre del programa como identificador");
                    break;
                }
                // Verifica si ya se inicializo
                if (ExisteID(Aux.Token) || ListaIdentificadores.contains(Aux.Token)) {
                    StatusError = true;
                    System.out.println("Error el identificadore :" + Aux.Token + " ya se habia inicializado");
                    break;
                }
                ListaIdentificadores.add(Aux.Token);

            }
            // ES : (LE sigue el tipo de datos)
            if ("119".equals(Aux.Campo2)) {
                Aux = Aux.NodoSig;
                TipoDato = Aux.Campo2;
                break;
            }
        } while (StatusError != true);
        // Rellena lista de identificadores
        for (String identificador : ListaIdentificadores) {
            añadirNodoIdenticador(identificador, TipoDato, null);
        }

    }

    public static void añadirNodoIdenticador(String nombre, String tipoDato, Object valor) {
        NodoIdentificadores nuevoNodo = new NodoIdentificadores(nombre, tipoDato, valor);

        if (CabezaIdentificadores == null) {
            // Si CabezaIdentificadores es nulo, el nuevo nodo se convierte en la cabeza.
            CabezaIdentificadores = nuevoNodo;
        } else {
            // Si CabezaIdentificadores no es nulo, recorre la lista hasta el final.
            NodoIdentificadores current = CabezaIdentificadores;
            while (current.SigIdentificador != null) {
                current = current.SigIdentificador;
            }
            current.SigIdentificador = nuevoNodo;
        }
    }

    public static NodoIdentificadores BuscaIdentificadorxNombre(String Nombre) {
        NodoIdentificadores AuxiliarIdentificador = CabezaIdentificadores;
        while (AuxiliarIdentificador != null) {
            if (AuxiliarIdentificador.Nombre.equals(Nombre)) {
                return AuxiliarIdentificador;
            }
            AuxiliarIdentificador = AuxiliarIdentificador.SigIdentificador;
        }
        return null; // Return null if the identifier is not found
    }

    public static Object Operacion(String TipoDato) {

        Object Valor = null;
        switch (TipoDato) {
            // integer 203
            case "203":
                Valor = OperacionInteger();
                break;
            // real 204
            case "204":
                Valor = Float.parseFloat(Aux.NodoSig.Token);
                break;
            // boolean 205
            case "205":
                Valor = Boolean.parseBoolean(Aux.NodoSig.Token);
                break;
            // string 206
            case "202":
                Valor = Boolean.parseBoolean(Aux.NodoSig.Token);
                break;

        }
        return Valor;
    }

    public static Object OperacionReal() {
        Object Valor = null;
        return Valor;
    }

    public static Object OperacionBoolean() {
        Object Valor = null;
        return Valor;
    }

    public static void VerificarNull() {
        // verificar que todos que el campo valor de todos los identificadores sea
        // diferente a null
        Nodo Aux2 = Aux;
        do {
            Aux2 = Aux2.NodoSig;
            if (Aux2.Campo2 == "100") {
                if (BuscaIdentificadorxNombre(Aux2.Token).Valor == null) {
                    StatusError = true;
                    System.out.println("Error Semantico no se a inicializado la variable: " + Aux2.Token);
                    break;
                }
            }
            if (Aux2.Campo2 == "111") {
                break;

            }

        } while (StatusError);
    }

    public static void ImprimirIdentificadores() {
        NodoIdentificadores AuxiliarIdentificador = CabezaIdentificadores;

        while (AuxiliarIdentificador != null) {
            String tipoDato = AnalizadorLexico.palabrasReservadas.inverse().get(AuxiliarIdentificador.TipoDato);
            System.out.println("Identificador:" + AuxiliarIdentificador.Nombre + " TipoDato:" + tipoDato
                    + " Valore:" + AuxiliarIdentificador.Valor);
            AuxiliarIdentificador = AuxiliarIdentificador.SigIdentificador;
        }
        System.out.println();
    }

}
