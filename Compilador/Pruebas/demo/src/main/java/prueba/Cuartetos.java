package prueba;




public class Cuartetos {
    public String Operador;
    public String Argumento1;
    public String Argumento2;
    public String Resultado;


    @Override
    public String toString() {
        String resultado = String.format("(%s , %s , %s , %s)", Operador, Argumento1, Argumento2, Resultado);
        limpiar(); // Limpia el cuarteto después de imprimirlo
        return resultado;
    }
    
    private void limpiar() {
        Operador = null;
        Argumento1 = null;
        Argumento2 = null;
        Resultado = null;
    }
}