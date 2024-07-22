package prueba;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.prueba.Cuartetos;

// a:=j+35;
// j:=4;
// j:=7+8/8-2;
public class Main {
    static int ContadorEtiquetasTemp = 1;

    public static void main(String[] args) {

        String operacion = "a:=455+h*6/g-2*3/h+5-h";

        Operaciones(operacion);

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
        Cuarteto.Argumento1 = OpIzq.get(0);
        Cuarteto.Argumento2 = OpDer.get(0);
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