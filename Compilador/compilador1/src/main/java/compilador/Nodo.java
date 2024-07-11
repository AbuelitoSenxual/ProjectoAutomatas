package compilador;
public class Nodo {
    String Token;
    String Campo2;
    int NumLinea;
    Nodo NodoSig;
    
    public Nodo(String Token,String Campo2,int NumLinea){
        this.Token = Token;
        this.Campo2 = Campo2;
        this.NumLinea = NumLinea;
    }

}
