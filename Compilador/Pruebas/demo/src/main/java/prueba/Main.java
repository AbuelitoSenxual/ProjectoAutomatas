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
    static int ContadorEtiquetasTemp=1;

    public static void main(String[] args) {

        String operacion = "j:=445+b/343-b+2*2";
   
        
        Operaciones(operacion);

    }

    public static void Operaciones(String CadenaOP) {
        String Resultado;
        String OpIzq;
        String OpDer;
        String EtiResultado;
        int IndiceOperador = 0;
        String Operador;

        Cuartetos Cuarteto = new Cuartetos();
        ArrayList<String> ListaOperaciones = GenerarListaOperaciones(CadenaOP);
        Resultado = ListaOperaciones.get(0);
        
        //Limpia ID : = de la lista 
        ListaOperaciones.subList(0, 3).clear();

        //verifica que no sea una igualacion
        if (ListaOperaciones.size()==1) {
            Cuarteto.Operador = "Asignacion";
            Cuarteto.Argumento1 = ListaOperaciones.get(0);
            Cuarteto.Resultado = Resultado;
            System.out.println(Cuarteto.toString());
            return;
        }

        //Verficiar que no sea una operacion simple 
        if (ListaOperaciones.size() == 3) {
            
            //vefica si es una operacion directa (3+5)
            if(ListaOperaciones.get(0).matches("\\d+")&&ListaOperaciones.get(2).matches("\\d+")){
                Cuarteto.Operador = "Asignacion";
                Cuarteto.Argumento1 = String.valueOf(OperacionSimple(ListaOperaciones.get(0), ListaOperaciones.get(1), ListaOperaciones.get(2)));
                Cuarteto.Resultado = Resultado;
                System.out.println(Cuarteto.toString());
                return;
            }
            //es una operacion con una variable y un numero (j+5)
            else{
                //Genera la operacion en temporal 
                Cuarteto.Argumento1 = ListaOperaciones.get(0);
                Cuarteto.Operador = ListaOperaciones.get(1);
                Cuarteto.Argumento2 = ListaOperaciones.get(2);
                Cuarteto.Resultado = "t"+ContadorEtiquetasTemp;

                System.out.println(Cuarteto.toString());
                Cuarteto.Operador = "Asignacion";
                Cuarteto.Argumento1 = "t"+ContadorEtiquetasTemp;
                Cuarteto.Resultado = Resultado;
                System.out.println(Cuarteto.toString());
                ContadorEtiquetasTemp++;
                return;
            }
            
        }

        //Busca indice de operador y asigna operador
        IndiceOperador = EcontrarIndiceOperador(ListaOperaciones);
        Operador = ListaOperaciones.get(IndiceOperador);
        

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
        int Indice=0;
        
        for (int i = Lista.size() - 1; i >= 0; i--) {
            if (Lista.get(i).equals("-")||Lista.get(i).equals("+")) {
                Indice = i;
                return Indice;
            }
        }
        for (int i = Lista.size() - 1; i >= 0; i--) {
            if (Lista.get(i).equals("/")||Lista.get(i).equals("*")) {
                Indice = i;
                return Indice;
            }
        }
        return Indice;
        
    }


}