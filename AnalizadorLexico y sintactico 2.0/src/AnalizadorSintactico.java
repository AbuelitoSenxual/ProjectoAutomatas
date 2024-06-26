import java.util.Arrays;

public class AnalizadorSintactico {
    private static Nodo NodoIni;
    private static Nodo Auxiliar;
    private static boolean StatusError;

    public static void main(String[] args) {
        NodoIni = AnalizadorLexico.ObtenerNodoIniLexemas();
        AnalizadorLexico.RecorrerTokens();
        Auxiliar = NodoIni;
        // verificar si se empieza con program
        if (Auxiliar.Campo2 != "200") {
            System.out.println("\n" + " Error se esperar la palabra reservada Program");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }

        if (Auxiliar.Campo2 == "200") {
            Auxiliar = Auxiliar.NodoSig;
            // verifica que se iniciialize un identificador
            if (Auxiliar.Campo2 != "100") {
                System.out.println("\n" + " Error se esperar un identificador o lista de identificadores");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if (Auxiliar.Campo2 == "100") {
                Auxiliar = Auxiliar.NodoSig;
                // Verifica que empiece una lista de identificadores con ,=110 o se termine la
                // oración con ;=111
                if (Auxiliar == null || (!Auxiliar.Campo2.equals("110") && !Auxiliar.Campo2.equals("111"))) {
                    System.out.println("\n" + "Error: Se esperaba lista de identificadores o final de oración con ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                // verifica si es una lista de idetificadores al recibir ,
                if (Auxiliar.Campo2 == "110" && StatusError == false) {
                    ListaIdentificadores();

                }
                // Captura error de terminar de manera incorrecta a orracion
                if (Auxiliar == null || !"111".equals(Auxiliar.Campo2)) {
                    System.out.println("\n" + " Error se esperarba lista de identificadores o final de oracion con ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                // verifica si termina la oracio con ;
                if ("111".equals(Auxiliar.Campo2)) {
                    Auxiliar = Auxiliar.NodoSig;
                    // capturar error al no terminar d emanera exitosa el programa
                    // que no sea bloque(para que sea bloque inicia con la palabra reservada
                    // begin=206 o var=201)
                    // que no sea .=109 que sea auxiliar nulo
                    if (Auxiliar == null || !Auxiliar.Campo2.equals("201") && !Auxiliar.Campo2.equals("206")
                            && !Auxiliar.Campo2.equals("109")) {
                        System.out.println("\n" + "Error: No se terminó de manera correcta el código");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    // Verificar si le sigue un bloque
                    if (Auxiliar != null && (Auxiliar.Campo2.equals("201") || Auxiliar.Campo2.equals("206"))) {
                        Bloque();
                    }
                    // verificar si se termina de manera correcta el codigo
                    if (!Auxiliar.Campo2.equals("109") && StatusError == false) {
                        System.out.println("\n"
                                + "Error: No se terminó de manera correcta el código o hay un error en la contuinidad de la gramatica");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    if (Auxiliar.Campo2.equals("109") && StatusError == false) {
                        Nodo Auxiliar2 = Auxiliar.NodoSig;
                        if (Auxiliar2!= null) {
                            System.out.println("\n"
                                + "Error: No puede haber nada depues del .");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                        }
                        else{
                            System.out.println("\n" + "Ejecucion exitosa");
                        }
                        
                       
                    }

                }

            }
        }

    }

    private static void ListaIdentificadores() {
        do {
            if (Auxiliar == null && StatusError == false) {
                // Si Auxiliar es null, se rompe el bucle
                System.out.println("\n" + " Error se esperaba un identificador ");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
                break;
            }

            Auxiliar = Auxiliar.NodoSig;

            if (Auxiliar == null && StatusError == false) {
                // Si Auxiliar es null después de moverse al siguiente nodo
                System.out.println("\n" + " Error se esperaba un identificador despues de , ");
                StatusError = true;
                break;
            }

            // Captura el error donde después de la coma no hay identificador
            if (!"100".equals(Auxiliar.Campo2) && StatusError == false) {
                System.out.println("\n" + " Error se esperaba un identificador despues de ,");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
                break;
            }

            if ("100".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;

                if (Auxiliar == null || !"110".equals(Auxiliar.Campo2)) {
                    // Si Auxiliar es null después de moverse al siguiente nodo o no es "110"
                    break;
                }
            }
        } while (!StatusError);
    }

    private static void definicion() {
        // lee el siguiente lexema y verifica si es nesesario analizar una lista analiza
        // su lista
        Auxiliar = Auxiliar.NodoSig;
        if (Auxiliar == null || !"100".equals(Auxiliar.Campo2) && StatusError == false) {
            System.out.println("\n" + " Error se esperaba una lista de identificadores");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("100".equals(Auxiliar.Campo2)) {
            Auxiliar = Auxiliar.NodoSig;
            // verifica que el siguiente elemento sea , y asi inicilisar una lista
            if ("110".equals(Auxiliar.Campo2)) {
                ListaIdentificadores();
            }
            // verifica que el siguente elemento sea : si no captura el erro
            if (Auxiliar == null || !"119".equals(Auxiliar.Campo2) && StatusError == false) {
                System.out.println("\n" + " Error se esperaba :");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }

            if ("119".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;
                // verifica el siguiente elemento sea tipo de datos validos
                if (Auxiliar == null || !EstaTioDato(Auxiliar)) {
                    System.out.println("\n" + " Error se esperaba tipo valido");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }

                if (EstaTioDato(Auxiliar)) {
                    // en el caso que el tipo de dato sea integer verifica que se cierre de manera
                    // correcta con ;
                    Auxiliar = Auxiliar.NodoSig;
                    if (!"111".equals(Auxiliar.Campo2)) {
                        System.out.println("\n" + " Error se esperaba ;");
                        System.out.println("linea  " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                    if ("111".equals(Auxiliar.Campo2)) {
                        // en el caso que se cierre bien se verifica si es necesario volver a llamar al
                        // metodo
                        Auxiliar = Auxiliar.NodoSig;
                        if ("201".equals(Auxiliar.Campo2)) {
                            definicion();

                        }

                    }

                }
            }

        }

    }

    private static boolean EstaTioDato(Nodo Auxiliar) {
        // Regresa true si Auxiliar.Campo2 contiene string 202, 203, o 205
        return "202".equals(Auxiliar.Campo2) || "203".equals(Auxiliar.Campo2) || "204".equals(Auxiliar.Campo2)
                || "205".equals(Auxiliar.Campo2);
    }

    private static void Bloque() {
        Nodo Auxiliar2;
        // verifica si es Definicion Al inicia con la plaba reservada var =201
        if ("201".equals(Auxiliar.Campo2)) {
            definicion();

        }
        if ("206".equals(Auxiliar.Campo2)) {
            // bloque de enunciados Al inicia con la plaba reservada begin =
            // 206
            BloqueEnunciados();

        }

    }

    private static void BloqueEnunciados() {
        // ID= 100 (para operacion o asignacion)
        // if = 210 (para alternativa simple y doble) o while=213(para bucle)
        Auxiliar = Auxiliar.NodoSig;
        // captura que no se inicialice bien
        if (!"100".equals(Auxiliar.Campo2) && !"210".equals(Auxiliar.Campo2) && !"213".equals(Auxiliar.Campo2)
                && Auxiliar == null && StatusError == false) {
            System.out.println("\n" + " Error no se inicializo correctamente la lista de enunciados ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        } else {
            // se insilizo bien la lista de enunciados
            ListaEnunciado();
            Auxiliar = Auxiliar.NodoSig;
            // captura el error generado al no cerrar con end
            if ((!"207".equals(Auxiliar.Campo2) || Auxiliar == null) && StatusError == false) {
                System.out.println("\n" + " Error se esperaba un end");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if ("207".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;
            }
        }

    }
    private static void ListaEnunciado() {
        // captura operaciones
        if ("100".equals(Auxiliar.Campo2)) {
            operaciones();

        } else {
            // Captura alternativas simples y doble
            if ("210".equals(Auxiliar.Campo2)) {
                AlternativaSimple();
            } else {
                // Captura bucles
                if ("213".equals(Auxiliar.Campo2)) {
                    bucle();
                }

            }
        }


        // En el caso que si se termine bien verifica si llamar Lista de Enunciados o a
        // un bloque de enunciados id while if begin
        if ("111".equals(Auxiliar.Campo2)) {
            Nodo Auxiliar2 = Auxiliar.NodoSig;
            if ("206".equals(Auxiliar2.Campo2)) {
                Auxiliar = Auxiliar2;
                BloqueEnunciados();

            }
            if ("100".equals(Auxiliar2.Campo2) || "213".equals(Auxiliar2.Campo2) || "210".equals(Auxiliar2.Campo2)) {

                Auxiliar = Auxiliar2;
                ListaEnunciado();
            }

        }
    }
    // Se recive el metodo con un identificador 100
    private static void operaciones() {
        

        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un operador relacional (campo2 == "112"
        // | "113" | "114" | "115" | "116" | "117"|"119")
        if ((Auxiliar == null
                || !Arrays.asList("112", "113", "114", "115", "116", "117", "118","119").contains(Auxiliar.Campo2))&& StatusError == false) {
            System.out.println("\n" + "Error: Se esperaba un operador relacional.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un identificador (campo2 == "100") o un
        // dígito (campo2 == "101" o "102")
        if ((Auxiliar == null || (!"100".equals(Auxiliar.Campo2) && !"101".equals(Auxiliar.Campo2)
                && !"102".equals(Auxiliar.Campo2)))&& StatusError == false) {
            System.out.println("\n" + "Error: Se esperaba un identificador o un dígito.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }

        Nodo Auxiliar2 = Auxiliar.NodoSig;
        if ("111".equals(Auxiliar2.Campo2)) {
            Auxiliar = Auxiliar2;
            return;
        }


        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un operador aritmético (campo2 == "+"
        // 103| "-" 104| "*" 105| "/"106)
        if ((Auxiliar == null || !Arrays.asList("103", "104", "106", "106").contains(Auxiliar.Campo2))&& StatusError == false) {
            System.out.println("\n" + "Error: Se esperaba un operador aritmético.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el siguiente token sea un identificador (campo2 == "100") o un
        // dígito (campo2 == "101" o "102")
        if ((Auxiliar == null || (!"100".equals(Auxiliar.Campo2) && !"101".equals(Auxiliar.Campo2)
                && !"102".equals(Auxiliar.Campo2)))&& StatusError ==false) {
            System.out.println("\n" + "Error: Se esperaba un identificador o un dígito.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }
        Auxiliar = Auxiliar.NodoSig;

        // Verificar que el último token sea un punto y coma (campo2 == "111")
        if ((Auxiliar == null || !"111".equals(Auxiliar.Campo2))&& StatusError == false) {
            System.out.println("\n" + "Error: Se esperaba un punto y coma ';' al final de la operación.");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
            return;
        }

    }
    private static void AlternativaDoble() {
        Auxiliar = Auxiliar.NodoSig;
        // verificar que se inicio correctamente un bloque de enunciado con palabra
        // recervada begin 206
        if (!"206".equals(Auxiliar.Campo2) || Auxiliar == null&& StatusError == false) {
            System.out.println("\n" + " Error se esperaba palabra reservada begin");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("206".equals(Auxiliar.Campo2)) {
            BloqueEnunciados();
        }

    }
    private static void AlternativaSimple() {
        Auxiliar = Auxiliar.NodoSig;
        // verifica error no se inicializa correctamente una condicion (tiene que ser
        // digito(101 NE,102 ND) o identificador(100))
        if ((!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2)
                && Auxiliar == null) && StatusError == false) {
            System.out.println("\n" + " Error no se  inicializo correctamente la condicion ");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        }
        if ("101".equals(Auxiliar.Campo2) || "102".equals(Auxiliar.Campo2) || "100".equals(Auxiliar.Campo2)) {
            condicion();
            Auxiliar = Auxiliar.NodoSig;
            // verifica que le siga la palabra reservada then 211
            if (((!"211".equals(Auxiliar.Campo2) || Auxiliar == null))&& StatusError == false) {
                System.out.println("\n" + " Error se esperaba palabra reservada then");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            }
            if ("211".equals(Auxiliar.Campo2) || Auxiliar == null) {
                // verifica que se inisialie bien el bloque de enunciado con la palalbra
                // resevada begin = 206
                Auxiliar = Auxiliar.NodoSig;
                if ((!"206".equals(Auxiliar.Campo2) || Auxiliar == null) && StatusError == false) {
                    System.out.println("\n" + " Error se esperaba palabra reservada begin");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                else{
                    BloqueEnunciados();
                // verifica si es necesario llamar a alterantiva doble al recibir la palabra
                // reservada else =212
                Nodo Auxiliar2 = Auxiliar;
                Auxiliar2 = Auxiliar.NodoSig;
                if ("212".equals(Auxiliar.Campo2)) {
                    Auxiliar = Auxiliar2;
                    AlternativaDoble();

                }
                }
                

            }
        }
    }
    private static void condicion() {
        Auxiliar = Auxiliar.NodoSig;
        // Captura error al no resivir un operador relacional
        if ((Auxiliar == null ||
                (!"112".equals(Auxiliar.Campo2) &&
                        !"113".equals(Auxiliar.Campo2) &&
                        !"114".equals(Auxiliar.Campo2) &&
                        !"115".equals(Auxiliar.Campo2) &&
                        !"116".equals(Auxiliar.Campo2) &&
                        !"117".equals(Auxiliar.Campo2) &&
                        !"118".equals(Auxiliar.Campo2)) && StatusError == false)) {

            System.out.println("\n" + " Error se esperaba un Operador relacional");
            System.out.println("linea  " + Auxiliar.NumLinea);
            StatusError = true;
        } else {
            Auxiliar = Auxiliar.NodoSig;
            // verificar si le sigue un identificador o digito
            if ((!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2)
                    ||Auxiliar == null)&&StatusError==false) {
                System.out.println("\n" + " Error no se  esperaba un identificador o digito ");
                System.out.println("linea  " + Auxiliar.NumLinea);
                StatusError = true;
            } else {
                Auxiliar = Auxiliar.NodoSig;
                // verifica que se termine de manera correcta la coindicion con ;
                if ((!"111".equals(Auxiliar.Campo2) || Auxiliar == null)&& StatusError == false) {
                    System.out.println("\n" + " Error se termino de manera correcta la coindicion con ;");
                    System.out.println("linea  " + Auxiliar.NumLinea);
                    StatusError = true;
                }
            }
        }
    }



    private static void bucle() {
        Auxiliar = Auxiliar.NodoSig;
        
        // Verifica error: no se inicializa correctamente una condición
        if ((Auxiliar == null || (!"101".equals(Auxiliar.Campo2) && !"102".equals(Auxiliar.Campo2) && !"100".equals(Auxiliar.Campo2))) && !StatusError) {
            System.out.println("\n" + " Error no se inicializó correctamente la condición ");
            System.out.println("linea " + Auxiliar.NumLinea);
            StatusError = true;
        }
        
        if ("101".equals(Auxiliar.Campo2) || "102".equals(Auxiliar.Campo2) || "100".equals(Auxiliar.Campo2)) {
            condicion();
            
            Auxiliar = Auxiliar.NodoSig;
            if ((Auxiliar == null || !"214".equals(Auxiliar.Campo2)) && !StatusError) {
                System.out.println("\n" + " Error no se inicializó la palabra reservada do");
                System.out.println("linea " + Auxiliar.NumLinea);
                StatusError = true;
            }
            
            if ("214".equals(Auxiliar.Campo2)) {
                Auxiliar = Auxiliar.NodoSig;
                
                if ((Auxiliar == null || (!Auxiliar.Campo2.equals("201") && !Auxiliar.Campo2.equals("206"))) && !StatusError) {
                    System.out.println("\n" + " Error se esperaba la palabra reservada var o begin");
                    System.out.println("linea " + Auxiliar.NumLinea);
                    StatusError = true;
                }
                
                // Verificar si le sigue un bloque
                if (Auxiliar != null && (Auxiliar.Campo2.equals("201") || Auxiliar.Campo2.equals("206"))) {
                    Bloque();
                    Auxiliar = Auxiliar.NodoSig;
                    
                    // Verifica que se termine de manera correcta el bucle con ;
                    if ((Auxiliar == null || !"111".equals(Auxiliar.Campo2)) && !StatusError) {
                        System.out.println("\n" + " Error se esperaba un ;");
                        System.out.println("linea " + Auxiliar.NumLinea);
                        StatusError = true;
                    }
                }
            }
        }
    }
    

}
