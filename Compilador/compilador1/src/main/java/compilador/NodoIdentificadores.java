package compilador;
public class NodoIdentificadores {
    String Nombre;//x,y,z
    String TipoDato;// integer , real , boolean
    Object Valor;// "hola" , true , 4 , 45.2
    NodoIdentificadores SigIdentificador;
    
    
    public NodoIdentificadores (String Nombre,String TipoDato,Object Valor){
        this.Nombre = Nombre;
        this.TipoDato = TipoDato;
        this.Valor = Valor;
    
    }
}


