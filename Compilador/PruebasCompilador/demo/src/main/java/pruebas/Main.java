package pruebas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    public static List<String[]> generateCuartetos(List<String> rpn) {
        List<String[]> cuartetos = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        int tempVarCount = 1;

        for (String token : rpn) {
            if (Character.isLetterOrDigit(token.charAt(0))) {
                stack.push(token);
            } else {
                String arg2 = stack.pop();
                String arg1 = stack.pop();
                String tempVar = token.equals(":=") ? arg1 : "t" + tempVarCount++;
                cuartetos.add(new String[]{token, arg1, arg2, tempVar});
                if (!token.equals(":=")) {
                    stack.push(tempVar);
                }
            }
        }

        return cuartetos;
    }
    public static List<String> infixToRPN(String expression) {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put(":=", 1);
        precedence.put("+", 2);
        precedence.put("-", 2);
        precedence.put("*", 3);
        precedence.put("/", 3);

        Stack<String> operators = new Stack<>();
        List<String> output = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-*/:= ", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            if (Character.isLetterOrDigit(token.charAt(0))) {
                output.add(token);
            } else {
                while (!operators.isEmpty() && precedence.get(token) <= precedence.get(operators.peek())) {
                    output.add(operators.pop());
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            output.add(operators.pop());
        }

        return output;
    }
    public static void main(String[] args) {
       String expression = "a := b + c * d - e";
        List<String> rpn = infixToRPN(expression);
        List<String[]> cuartetos = generateCuartetos(rpn);
        
        System.out.println("RPN: " + rpn);
        System.out.println("Cuartetos:");
        for (String[] cuarteto : cuartetos) {
            System.out.println(Arrays.toString(cuarteto));
        }
    }
}