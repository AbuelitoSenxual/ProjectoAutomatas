package compilador;




public class Cuartetos {
    public String Operador;
    public String Argumento1;
    public String Argumento2;
    public String Resultado;
    public Cuartetos SigCuarteto;


    @Override
    public String toString() {
        String resultado = String.format("(%s , %s , %s , %s)", Operador, Argumento1, Argumento2, Resultado);
        return resultado;
    }
    

}