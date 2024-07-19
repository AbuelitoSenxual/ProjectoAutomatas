package prueba;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// a:=j+35;
// j:=4;
// j:=7+8/8-2;
public class Main {
    public static int CountEtiq = 1;

    public static void main(String[] args) {

        String operacion = "j:=445";
   
        
        Operaciones(operacion);

    }

    public static void Operaciones(String CadenaOP) {
        String Resultado;
        String OpIzq;
        String OpDer;
        String EtiResultado;
        int IndiceOperador = 0;
        char Operador;

        // convierte la operacion en un array list de caracteres
        ArrayList<String> Operacion = GenerarListaOperaciones(CadenaOP);

        // optiene el Primera valor de el array (EL identificador resultado)
        Resultado = String.valueOf(Operacion.get(0));
        // elimina el ID := de la lista
        Operacion.subList(0, 3).clear();
        // Verifica que no sea una asignacion (si la operacion contiene un solo valor)

        if (Operacion.size() == 1) {
            System.out.println(String.format("(assign , %s , - , %s)", Operacion.get(0), Resultado));
        }
        // Ubicar idice del elemento pivote que dara origen a la operacion Izquierda y
        // derecha
        char[] operadores = { '+', '-', '*', '/' };
        for (char operador : operadores) {
            int indice = Operacion.indexOf(operador);
            if (indice != -1) {
                IndiceOperador = indice;
                Operador = operador;
                break;
            }
        }

        // Separa la operacion en Izquiero
        if (Operacion.subList(0, IndiceOperador).size() == 1) {
            // caso donde solo sea un elemento osea una igualcion
            OpIzq = Operacion.subList(0, IndiceOperador).toString();
        } else {
            // dado caso que sea mas grande que 1 elemento osea una operacion
            OpIzq = "T" + CountEtiq + ":=";
            CountEtiq++;
            OpIzq += Operacion.subList(0, IndiceOperador).toString();

        }
        ;

        // Separa la operacion derecha
        if (Operacion.subList(IndiceOperador + 1, Operacion.size()).size() == 1) {
            // caso donde solo sea un elemento osea una igualcion
            OpIzq = Operacion.subList(IndiceOperador + 1, Operacion.size()).toString();
        } else {
            // dado caso que sea mas grande que 1 elemento osea una operacion
            OpIzq = "T" + CountEtiq + ":=";
            CountEtiq++;
            OpIzq += Operacion.subList(0, IndiceOperador).toString();

        }
        ;

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




}