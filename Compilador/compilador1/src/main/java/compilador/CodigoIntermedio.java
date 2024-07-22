package compilador;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodigoIntermedio {

    static Nodo NodoIni;
    static Nodo Aux;
    static int ContadorEtiquetasTemp=1;
    static int ContadorSalidaCodigo=1;
    static NodoIdentificadores IdentificadorInicial;

    public static void main(String[] args) {

        
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        Aux = NodoIni.NodoSig.NodoSig;

        GenerarCodigoIntermedio();

    }

    public static void GenerarCodigoIntermedio() {
        // Recorre codigo buscando Operaciones Asignaciones y codicionales
        do {
            // Captura el caso donde incie una inicializacion de una lista de variable con palabra reservada var 201
            if ("201".equals(Aux.Campo2)) {
                definicion();
            }

            // Captura el caso donde incie una operacion(con un identificador)
            if ("100".equals(Aux.Campo2)) {
                String CadenaOP = "";
                //Almacena los elementos de la operacion
                do {
                    CadenaOP += Aux.Token;
                    Aux = Aux.NodoSig;
                } while (!"111".equals(Aux.Campo2));

                Operaciones(CadenaOP);
            }
            //Captura el caso donde encuentre una condicional 210 if
            if ("210".equals(Aux.Campo2)) {
                System.out.println();
                String Arg1,Arg2;
                String OpBooleano;
                //Almacena los elementos de la operacion booleanad del if
                Aux = Aux.NodoSig;
                Arg1 =Aux.Token;
                Aux = Aux.NodoSig;
                OpBooleano = Aux.Token;
                Aux = Aux.NodoSig;
                Arg2 =Aux.Token;

                System.out.println(String.format("(%s , %s , %s , T%d)", OpBooleano,Arg1,Arg2,ContadorEtiquetasTemp));
                System.out.println(String.format("(if_False , T%d , null , L%d)", ContadorEtiquetasTemp,ContadorSalidaCodigo+1));
                System.out.println(String.format("(label, L%d , null , null)", ContadorSalidaCodigo));
                ContadorEtiquetasTemp++;
                ContadorSalidaCodigo++;
                GenerarCodigoIntermedio();
                System.out.println(String.format(("(goto , null , null , L%d"), ContadorSalidaCodigo));
                System.out.println(String.format("(label, L%d , null , null)", ContadorSalidaCodigo));
                ContadorSalidaCodigo++;
            }                                                       

            // Cptura el caso donde se termine el bloque de enunciados END 207
            if ("207".equals(Aux.Campo2)) {
                System.out.println();
                Aux = Aux.NodoSig;
                return;
            }

            Aux = Aux.NodoSig;
        } while (Aux != null);
    }

    public static void definicion(){  
        ArrayList<String> ListaIdentificadores = new ArrayList<String>();
        String TipoDato="";

        do {
            Aux= Aux.NodoSig;
            //Es identificador
            if ("100".equals(Aux.Campo2)) {
                
                ListaIdentificadores.add(Aux.Token);

            }
            // ES : (LE sigue el tipo de datos)
            if ("119".equals(Aux.Campo2)) {
                Aux= Aux.NodoSig;
                TipoDato = AnalizadorLexico.palabrasReservadas.inverse().get(Aux.Campo2);
                break;
            }
        } while (!"119".equals(Aux.Campo2));
        //Rellena lista de identificadores
        for(String identificador :ListaIdentificadores){
            AnalizadorSemantico.añadirNodoIdenticador( identificador, TipoDato, null);
            System.out.println(String.format("(Declare , %s , %s , null)", TipoDato,identificador));
        }
        System.out.println();
    }
    public static void añadirNodoIdenticador(String nombre, String tipoDato, Object valor) {
        NodoIdentificadores nuevoNodo = new NodoIdentificadores(nombre, tipoDato, valor);
        
        if (IdentificadorInicial == null) {
            // Si IdentificadorInicial es nulo, el nuevo nodo se convierte en la cabeza.
            IdentificadorInicial = nuevoNodo;
        } else {
            // Si IdentificadorInicial no es nulo, recorre la lista hasta el final.
            NodoIdentificadores current = IdentificadorInicial;
            while (current.SigIdentificador != null) {
                current = current.SigIdentificador;
            }
            current.SigIdentificador = nuevoNodo;
        }
    }
    public static void Operaciones(String CadenaOP) {
        String Resultado;
        ArrayList<String> OpIzq = new ArrayList<>();
        ArrayList<String> OpDer = new ArrayList<>();
        ArrayList<String> EtiResultado;
        int IndiceOperador = 0;
        String Operador;
        Boolean EsOPTemp = false;

        Cuartetos Cuarteto = new Cuartetos();
        ArrayList<String> ListaOperaciones = GenerarListaOperaciones(CadenaOP);
        Resultado = ListaOperaciones.get(0);
        // Limpia ID : = de la lista
        if (ListaOperaciones.get(0).equals("t")) {
            Resultado = ListaOperaciones.get(0) + ListaOperaciones.get(1);
            ListaOperaciones.subList(0, 4).clear();
            EsOPTemp = true;
        } else {
            Resultado = ListaOperaciones.get(0);
            ListaOperaciones.subList(0, 3).clear();
        }

        // verifica que no sea una igualacion
        if (ListaOperaciones.size() == 1) {
            Cuarteto.Operador = "Asignacion";
            Cuarteto.Argumento1 = ListaOperaciones.get(0);
            Cuarteto.Resultado = Resultado;
            System.out.println(Cuarteto.toString());
            return;
        }

        // Verficiar que no sea una operacion simple
        if (ListaOperaciones.size() == 3) {

            // vefica si es una operacion directa (3+5)
            if (ListaOperaciones.get(0).matches("\\d+") && ListaOperaciones.get(2).matches("\\d+")) {
                Cuarteto.Operador = "Asignacion";
                Cuarteto.Argumento1 = String.valueOf(
                        OperacionSimple(ListaOperaciones.get(0), ListaOperaciones.get(1), ListaOperaciones.get(2)));
                Cuarteto.Resultado = Resultado;
                System.out.println(Cuarteto.toString());
                return;
            }
            // es una operacion con una variable y un numero (j+5)
            else {
                // Genera la operacion en temporal
                if (Resultado.indexOf("t") != -1) {
                    Cuarteto.Argumento1 = ListaOperaciones.get(0);
                    Cuarteto.Operador = ListaOperaciones.get(1);
                    Cuarteto.Argumento2 = ListaOperaciones.get(2);
                    Cuarteto.Resultado = Resultado;
                    System.out.println(Cuarteto.toString());
                    return;
                } else {
                    // Genera la operacion con temporal
                    Cuarteto.Argumento1 = ListaOperaciones.get(0);
                    Cuarteto.Operador = ListaOperaciones.get(1);
                    Cuarteto.Argumento2 = ListaOperaciones.get(2);
                    Cuarteto.Resultado = "t" + ContadorEtiquetasTemp;

                    System.out.println(Cuarteto.toString());
                    Cuarteto.Operador = "Asignacion";
                    Cuarteto.Argumento1 = "t" + ContadorEtiquetasTemp;
                    Cuarteto.Resultado = Resultado;
                    System.out.println(Cuarteto.toString());
                    ContadorEtiquetasTemp++;
                    return;
                }

            }

        }

        // Busca indice de operador y asigna operador
        IndiceOperador = EcontrarIndiceOperador(ListaOperaciones);
        Operador = ListaOperaciones.get(IndiceOperador);

        ArrayList<String> ListaIzq = new ArrayList<>(ListaOperaciones.subList(0, IndiceOperador));
        ArrayList<String> ListaDer = new ArrayList<>(
                ListaOperaciones.subList(IndiceOperador + 1, ListaOperaciones.size()));

        // Analizamos hijo izquierdo

        if (ListaIzq.size() == 1) {
            // caso que sea una asignacion
            OpIzq = ListaIzq;
        } else {
            // es una operacion directa o con una variable
            if (ListaIzq.size() == 3) {
                // vefica si es una operacion directa (3+5)
                if (ListaIzq.get(0).matches("\\d+") && ListaIzq.get(2).matches("\\d+")) {
                    OpIzq.add(String.valueOf(OperacionSimple(ListaOperaciones.get(0), ListaOperaciones.get(1),
                            ListaOperaciones.get(2))));

                } else {
                    // es una operacion con una variable y un numero (j+5)
                    // Genera la operacion en temporal
                    String cadenaOP = "";

                    OpIzq.add("t");
                    OpIzq.add(Integer.toString(ContadorEtiquetasTemp));
                    OpIzq.add(":=");
                    OpIzq.addAll(ListaIzq);
                    for (String s : OpIzq)
                        cadenaOP += s;
                    ContadorEtiquetasTemp++;
                    Operaciones(cadenaOP);

                }
                ;

            } else {

                // Genera la operacion en temporal
                String cadenaOP = "";

                OpIzq.add("t");
                OpIzq.add(Integer.toString(ContadorEtiquetasTemp));
                OpIzq.add(":=");
                OpIzq.addAll(ListaIzq);
                for (String s : OpIzq)
                    cadenaOP += s;
                ContadorEtiquetasTemp++;
                Operaciones(cadenaOP);
            }

        }

        // Analizamos hijo derecho
        if (ListaDer.size() == 1) {
            // caso que sea una asignacion
            OpDer = ListaDer;
        } else {
            // es una operacion directa o con una variable
            if (ListaDer.size() == 3) {
                // vefica si es una operacion directa (3+5)
                if (ListaDer.get(0).matches("\\d+") && ListaDer.get(2).matches("\\d+")) {
                    OpDer.add(String.valueOf(OperacionSimple(ListaDer.get(0), ListaDer.get(1), ListaDer.get(2))));

                } else {
                    // es una operacion con una variable y un numero (j+5)
                    // Genera la operacion en temporal
                    String cadenaOP = "";
                    OpDer.add("t" + ContadorEtiquetasTemp + ":=");
                    OpDer.add(Integer.toString(ContadorEtiquetasTemp));
                    OpDer.add(":=");
                    OpDer.addAll(ListaDer);
                    for (String s : OpDer)
                        cadenaOP += s;
                    ContadorEtiquetasTemp++;
                    Operaciones(cadenaOP);

                }
                ;

            } else {

                // Genera la operacion en temporal
                String cadenaOP = "";

                OpDer.add("t");
                OpDer.add(Integer.toString(ContadorEtiquetasTemp));
                OpDer.add(":=");
                OpDer.addAll(ListaDer);
                for (String s : OpDer)
                    cadenaOP += s;
                ContadorEtiquetasTemp++;
                Operaciones(cadenaOP);
            }

        }

        // asigna valores a los argumentos
        Cuarteto.Operador = Operador;
        if (OpIzq.size()==1) {
            Cuarteto.Argumento1 = OpIzq.get(0);
        }
        else{
            Cuarteto.Argumento1 = OpIzq.get(0)+OpIzq.get(1);
        }
        if (OpDer.size()==1) {
            Cuarteto.Argumento1 = OpDer.get(0);
        }
        else{
            Cuarteto.Argumento1 = OpDer.get(0)+OpDer.get(1);
        }

        // Planteamos la operacionc con temporal
        if (Resultado.indexOf("t") != -1) {
            Cuarteto.Resultado = Resultado;
            System.out.println(Cuarteto.toString());

        } else {
            Cuarteto.Resultado = "t" + ContadorEtiquetasTemp;
            System.out.println(Cuarteto.toString());
            Cuarteto.Operador = "Asignacion";
            Cuarteto.Argumento1 = "t" + ContadorEtiquetasTemp;
            Cuarteto.Resultado = Resultado;
            System.out.println(Cuarteto.toString());
            ContadorEtiquetasTemp++;
        }

    }

    public static ArrayList<String> GenerarListaOperaciones(String operation) {
        ArrayList<String> elements = new ArrayList<>();

        // Expresión regular para encontrar números y operadores
        Pattern pattern = Pattern.compile("\\d+|\\D");
        Matcher matcher = pattern.matcher(operation);

        while (matcher.find()) {
            elements.add(matcher.group());
        }

        return elements;
    }

    public static int OperacionSimple(String num1, String operator, String num2) {
        // Convertir las cadenas de números a enteros
        int number1 = Integer.parseInt(num1);
        int number2 = Integer.parseInt(num2);

        // Realizar la operación basada en el operador
        int result = 0;
        switch (operator) {
            case "+":
                result = number1 + number2;
                break;
            case "-":
                result = number1 - number2;
                break;
            case "*":
                result = number1 * number2;
                break;
            case "/":
                if (number2 != 0) {
                    result = number1 / number2;
                } else {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }

        return result;
    }

    public static int EcontrarIndiceOperador(ArrayList<String> Lista) {
        int Indice = 0;

        for (int i = Lista.size() - 1; i >= 0; i--) {
            if (Lista.get(i).equals("-") || Lista.get(i).equals("+")) {
                Indice = i;
                return Indice;
            }
        }
        for (int i = Lista.size() - 1; i >= 0; i--) {
            if (Lista.get(i).equals("/") || Lista.get(i).equals("*")) {
                Indice = i;
                return Indice;
            }
        }
        return Indice;

    }

}
