package com.company;

import java.util.ArrayList;
import java.util.List;

public class fourthMethod {

    public static void main(String[] args) throws InterruptedException {

        double start = 1;
        double end = 40;
        double precision = 0.00001;

        //  Lista punktów:
        List<Double> list = new ArrayList<>((int) ((end - start) / precision));

        //  Do każdego elementu wkładam kolejną wartość punktu:
        double pos = start;
        do {
            pos += precision;
            list.add(pos);
        } while (pos <= end);

        double sum = list
                //  Tworze stream z kolekcji List:
                .stream()
                //  Generuje strumień równoległy:
                .parallel()
                //  Zwracam strumień w Double z przekształconymi danymi za pomocą funkcji obliczającej:
                //  W funkcji przekazuje parametr zawarty w List:
                .mapToDouble(x -> precision * functionIntegral(x))
                //  Możemy również uzyć wbudowanej funkcji, np .sum();
                //  Funkcja sum() jest szczególnym przypadkiem redukcji:
                .reduce((left, right) -> left + right)
                .getAsDouble();

        printThread(format(sum));
    }

    private static void printThread(String s) {
        System.out.println(Thread.currentThread().getName() + "\t|\t" + s);
    }

    // Formatuje pokazywanie 4 cyfr po przecinku:
    private static String format(double d) {
        return String.format("%.4f", d);
    }

    // f(x) = 3*x^3 + cos(7*x) - ln(2*x)
    static double functionIntegral(double x) {
        return 3 * Math.pow(x, 3.0) + Math.cos(7 * x) - Math.log(2 * x);
    }
}

