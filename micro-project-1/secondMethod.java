package com.company;

import java.util.concurrent.locks.ReentrantLock;

public class secondMethod {

    //  Blokada wejścia
    //  Zamek odpowiadający mechanizmowi synchronized
    static ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        double start = 1;
        double end = 40;
        double precision = 0.00001;

        printThread(format(secondExercise(start, end, 4, precision)));

    }

    public static double secondExercise(
            double partitionStart,
            double partitionEnd,
            int threadCount,
            double precision) throws InterruptedException {
        {
            Thread[] threads = new Thread[threadCount];
            double dx = (partitionEnd - partitionStart) / threadCount;

            Wrapper<Double> sum = new Wrapper<>(.0);

            for (int i = 0; i < threadCount; i++) {
                //  Początek obszaru pojedynczego wątku:
                double start = partitionStart + i * dx;
                //  Do końca swojego obszaru:
                double end = start + dx;

                threads[i] = new Thread(() -> {
                    double value = calcIntegralRectangle(start, end, precision);
                    //  Gdy zamek jest "zatrzaśnięty" - inne wątki nie mają dostępu do zmiennej
                    LOCK.lock();
                    //  Kod sekcji krytycznej:
                    sum.value += value;
                    //  Odblokowujemy zamek do dalszych sumowań:
                    LOCK.unlock();
                });
            }

            //  Uruchamiam wątki:
            for (Thread thread : threads) {
                thread.start();
            }

            //  Czekam na skończenie wątków:
            for (Thread thread : threads) {
                thread.join();
            }

            return sum.value;
        }
    }

    private static void printThread(String s) {
        System.out.println(Thread.currentThread().getName() + "\t|\t" + s);
    }

    // Formatuje pokazywanie 4 cyfr po przecinku:
    private static String format(double d) {
        return String.format("%.4f", d);
    }

    public static double calcIntegralRectangle(
            double partitionStart,
            double partitionEnd,
            double precision
    ) {
        double sum = 0;

        // Pokazuje w jakich przedziałach działa dany wątek:
        printThread("[" + format(partitionStart) + ", " + format(partitionEnd) + "]");

        double pos = partitionStart;

        do {
            pos += precision;
            sum += precision * functionIntegral(pos);
            printThread("[" + format(pos) + " => " + format(sum) + "]");
        } while (pos <= partitionEnd);

        return sum;
    }

    // f(x) = 3*x^3 + cos(7*x) - ln(2*x)
    static double functionIntegral(double x) {
        return 3 * Math.pow(x, 3.0) + Math.cos(7 * x) - Math.log(2 * x);
    }

    //  Klasa generyczna
    //  Klasa Double zawija w obiekcie wartość typu pierwotnego double.
    //  Obiekt typu Double zawiera pojedyncze pole o typie double.
    public static class Wrapper<Double> {
        public Double value;

        public Wrapper(Double value) {
            this.value = value;
        }
    }
}

