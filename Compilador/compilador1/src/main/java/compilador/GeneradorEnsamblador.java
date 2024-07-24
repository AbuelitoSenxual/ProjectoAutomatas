package compilador;

import org.checkerframework.checker.units.qual.s;

public class GeneradorEnsamblador {
    static Cuartetos cuartetoActual;
    public static void main(String[] args) {

        
      
      cuartetoActual = CodigoIntermedio.ObtenerCuarteto();
        String codigo = generarCodigo(cuartetoActual);
        System.out.println(codigo);

    }

    public static String generarCodigo(Cuartetos cuartetoInicial) {
        StringBuilder codigo = new StringBuilder();

        codigo.append("section .bss\n");
        Cuartetos actual = cuartetoInicial;
        while (actual != null) {
            if (actual.Operador.equals("Declare")) {
                codigo.append(String.format("    %s resd 1\n", actual.Argumento2));
            }
            actual = actual.SigCuarteto;
        }

        codigo.append("\nsection .text\n");
        codigo.append("global _start\n\n");
        codigo.append("_start:\n");

        actual = cuartetoInicial;
        while (actual != null) {
            switch (actual.Operador) {
                case "Asignacion":
                    codigo.append(String.format("    mov dword [%s], %s\n", actual.Resultado, actual.Argumento1));
                    break;
                case "+":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append(String.format("    add eax, %s\n", actual.Argumento2));
                    codigo.append(String.format("    mov dword [%s], eax\n", actual.Resultado));
                    break;
                case "-":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append(String.format("    sub eax, %s\n", actual.Argumento2));
                    codigo.append(String.format("    mov dword [%s], eax\n", actual.Resultado));
                    break;
                case "*":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append(String.format("    imul eax, %s\n", actual.Argumento2));
                    codigo.append(String.format("    mov dword [%s], eax\n", actual.Resultado));
                    break;
                case "/":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append("    xor edx, edx\n");
                    codigo.append(String.format("    div dword [%s]\n", actual.Argumento2));
                    codigo.append(String.format("    mov dword [%s], eax\n", actual.Resultado));
                    break;
                case "<":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append(String.format("    cmp eax, %s\n", actual.Argumento2));
                    codigo.append(String.format("    jl %s\n", actual.Resultado));
                    break;
                case ">":
                    codigo.append(String.format("    mov eax, %s\n", actual.Argumento1));
                    codigo.append(String.format("    cmp eax, %s\n", actual.Argumento2));
                    codigo.append(String.format("    jg %s\n", actual.Resultado));
                    break;
                case "if_False":
                    codigo.append(String.format("    cmp dword [%s], 0\n", actual.Argumento1));
                    codigo.append(String.format("    je %s\n", actual.Resultado));
                    break;
                case "label":
                    codigo.append(String.format("%s:\n", actual.Resultado));
                    break;
                case "goto":
                    codigo.append(String.format("    jmp %s\n", actual.Resultado));
                    break;
                default:
                    break;
            }
            actual = actual.SigCuarteto;
        }

        codigo.append("\n    ; Salida del programa\n");
        codigo.append("    mov eax, 1      ; sys_exit\n");
        codigo.append("    xor ebx, ebx    ; status 0\n");
        codigo.append("    int 0x80        ; llamada al kernel para salir\n");

        return codigo.toString();
    }
}
