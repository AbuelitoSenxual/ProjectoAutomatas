package compilador;
public class Nodo {
    String Token;  // if
    String Campo2; // 210
    int NumLinea; // 4
    Nodo NodoSig;
    
    public Nodo(String Token,String Campo2,int NumLinea){
        this.Token = Token;
        this.Campo2 = Campo2;
        this.NumLinea = NumLinea;
    }

}
