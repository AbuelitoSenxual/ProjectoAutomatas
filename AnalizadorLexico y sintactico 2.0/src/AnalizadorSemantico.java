import java.util.ArrayList;
public class AnalizadorSemantico {

    static Nodo NodoIni;
    static String NombrePrograma;
    static Nodo Aux;
    static boolean StatusError;
    static NodoIdentificadores CabezaIdentificadores;

    public static void main(String[] args) {
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        
        /// retorna un true si la ejecuccion fue exitosa
        Aux = NodoIni.NodoSig;
        NombrePrograma = Aux.Token;

        // ID 100 , palabra reservada var 201
        do {
            Aux = Aux.NodoSig;
            //Captura el caso donde se incialice una lista de variables
            if ("201".equals(Aux.Campo2)&& StatusError!=true) {
                definicion();
            }
            
        } while (Aux!=null && StatusError!=true) ;
    }

    public static void definicion(){
        ArrayList<String> ListaIdentificadores = new ArrayList<String>();
        String TipoDato="";

        do {
            Aux= Aux.NodoSig;
            //Es identificador
            if ("100".equals(Aux.Campo2)) {
                //verifica que no se igual al nombre del programa
                if (NombrePrograma.equals(Aux.Token)) {
                    StatusError = true;
                    System.out.println("Error Semantico no se puede usar el  nombre del programa como identificador");
                    break;
                }
                //Verifica si ya se inicializo 
                if (ExisteID(Aux.Token)||ListaIdentificadores.contains(Aux.Token)) {
                    StatusError = true;
                    System.out.println("Error el identificadore :"+ Aux.Token+" ya se habia inicializado");
                    break;
                }
                ListaIdentificadores.add(Aux.Token);

            }
            // ES : (LE sigue el tipo de datos)
            if ("119".equals(Aux.Campo2)) {
                Aux= Aux.NodoSig;
                TipoDato = Aux.Token;
                break;
            }
        } while (StatusError!=true);
        //Rellena lista de identificadores
        for(String identificador :ListaIdentificadores){
            añadirNodoIdenticador(identificador,TipoDato,null);
        }

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
    

}
