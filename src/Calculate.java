import java.util.Stack;
import java.util.regex.Pattern;

public class Calculate {
    private static String regex = "^\\d+((\\+|\\*)\\d+)*$";
    private enum Operator{ADD, MULTIPLY, BLANK}

    //The method parse the string and gives the final result.
    String parser(String expression) {
        //removing spaces
        expression = expression.replaceAll("\\s+","");
        Pattern p = Pattern.compile(regex);
        if (p.matcher(expression.trim()).matches()) {
            int temp = doCalculate(expression);
            if (temp == Integer.MIN_VALUE) {
                return "Overflow occurred";

            }
            return Integer.toString(doCalculate(expression));
        } else {
            return "You entered an invalid expression";
        }
    }

    //The method plunges the number into the stack.
    private Integer pullNum(int index, String symbols) {
        StringBuilder num = new StringBuilder();

        while (index < symbols.length() && Character.isDigit(symbols.charAt(index))) {
            num.append(symbols.charAt(index));
            index++;
        }
        return Integer.parseInt(num.toString());
    }

    //The method defines the operator.
    private Operator defineOperator(int index, String symbols) {
        if(index < symbols.length()){
            char op = symbols.charAt(index);
            switch (op){
                case '+': return Operator.ADD;
                case '*': return Operator.MULTIPLY;
            }
        }
        return Operator.BLANK;
    }

    //
    private Integer doCalculate(String expression) {
        Stack<Integer> numbers = new Stack<>();
        Stack<Operator> operations = new Stack<>();
        //parse the string into pieces
        for (int i = 0; i < expression.length(); i++) {
            try {
                Integer number = pullNum(i, expression);
                i += Integer.toString(number).length();
                numbers.push(number);

                if (i >= expression.length()) {
                    break;
                }
                Operator operator = defineOperator(i, expression);
                compute(numbers, operations, operator);
                operations.push(operator);
            } catch (NumberFormatException ex) {
                return Integer.MIN_VALUE;
            }
        }
        compute(numbers, operations, Operator.BLANK);

        if(numbers.size() == 1 && operations.size() == 0){
            return numbers.pop();
        }
        return 0;
    }

    //Processing numbers before calculation and outputting a result between two numbers.
    private void compute(Stack<Integer> numbers, Stack<Operator> operators, Operator operator) {
        while (numbers.size() >= 2 && operators.size() >= 1) {
            if(priorityOper(operator) <= priorityOper(operators.peek())){
                Operator op = operators.pop();
                Integer second = numbers.pop();
                Integer first = numbers.pop();

                Integer result = applyOper(first, op, second);
                numbers.push(result);
            } else{
                break;
            }
        }
    }

    //The method distributes the calculation order.
    private Integer priorityOper(Operator operator) {
        switch (operator){
            case ADD: return 1;
            case MULTIPLY: return 2;
            case BLANK: return 0;
        }
        return 0;
    }

    //The method does the calculation of two numbers.
    private Integer applyOper(Integer first, Operator operator, Integer second) {
        switch (operator) {
            case ADD: return first + second;
            case MULTIPLY: return first * second;
            default: return second;
        }
    }
}
