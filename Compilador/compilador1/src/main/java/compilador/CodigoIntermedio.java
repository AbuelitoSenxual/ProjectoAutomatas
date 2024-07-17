package compilador;

import java.util.ArrayList;

public class CodigoIntermedio {

    static Nodo NodoIni;
    static Nodo Aux;
    static int ContadorEtiquetas=1;
    static int ContadorSalidaCodigo=1;
    static NodoIdentificadores IdentificadorInicial;

    public static void main(String[] args) {

        AnalizadorSemantico.AnalisisSemantico();
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
                System.out.println("Operacion");
            }
            //Captura el caso donde encuentre una condicional 210 if
            if ("210".equals(Aux.Campo2)) {
                //imprime todos la condiiconal al encontrar un ; 
                do {
                    System.out.print(Aux.Token + " ");
                    Aux = Aux.NodoSig;
                } while (!"111".equals(Aux.Campo2));
                System.out.println();
                System.out.println("L"+ContadorSalidaCodigo+":");
                ContadorSalidaCodigo++;
                GenerarCodigoIntermedio();
                System.out.println("L"+ContadorSalidaCodigo+":");
            }                                                       

            // Cptura el caso donde se termine el bloque de enunciados END 207
            if ("207".equals(Aux.Campo2)) {
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
                TipoDato = Aux.Campo2;
                break;
            }
        } while (!"119".equals(Aux.Campo2));
        //Rellena lista de identificadores
        for(String identificador :ListaIdentificadores){
            AnalizadorSemantico.añadirNodoIdenticador( identificador, TipoDato, null);
        }

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
    
}
