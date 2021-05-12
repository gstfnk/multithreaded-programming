package com.company;

public class firstMethod {

    public static void main(String[] args) throws InterruptedException {

        double start = 1;
        double end = 40;
        double precision = 0.00001;

        printThread(format(firstExercise(start, end, 4, precision)));

    }

    public static double firstExercise(
            double partitionStart,
            double partitionEnd,
            int threadCount,
            double precision) throws InterruptedException {
        {
            //  Zbieram wszystkie wątki do tablicy
            Thread[] threads = new Thread[threadCount];
            double dx = (partitionEnd - partitionStart) / threadCount;

            var ref = new Object() {
                double sum = 0;
            };

            //  Definiuje co mają robić poszczególne wątki:
            for (int i = 0; i < threadCount; i++) {
                //  Początek obszaru pojedynczego wątku:
                double start = partitionStart + i * dx;
                //  Do końca swojego obszaru:
                double end = start + dx;

                threads[i] = new Thread(() -> {
                    ref.sum += calcIntegralRectangle(start, end, precision);
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

            return ref.sum;
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

        } while (pos < partitionEnd);

        return sum;
    }

    public static class Wrapper<Double> {
        public Double value;

        public Wrapper(Double value) {
            this.value = value;
        }
    }

    // f(x) = 3*x^3 + cos(7*x) - ln(2*x)
    static double functionIntegral(double x) {
        return 3 * Math.pow(x, 3.0) + Math.cos(7 * x) - Math.log(2 * x);
    }
}

