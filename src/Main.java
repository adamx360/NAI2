import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public final static File trainingFile = new File("iris_training.txt");
    public final static File testFile = new File("iris_test.txt");
    public final static String name = "Iris-setosa";
    public static int a;
    public static ArrayList<Flower> training = new ArrayList<>();
    public static ArrayList<Flower> test = new ArrayList<>();

    public static void main(String[] args) {
        training = loadFile(trainingFile);
        a = training.get(0).attrs.size();
        test = loadFile(testFile);
        Scanner scanner = new Scanner(System.in);
        Perceptron perceptron = new Perceptron(name, a, 0.00001, 0.00001);
        double wr = 0;
        while (wr != 1) {
            for (Flower i : training) {
                perceptron.train(i);
            }
            int w = 0;
            for (Flower i : training) {
                w += (perceptron.guess(i.attrs) == name.equals(i.name)) ? 1 : 0;
            }
            wr = (double) w / training.size();
            System.out.print("weights: ");
            for (double d :
                    perceptron.weights) {
                System.out.print(d + "\t");
            }
            System.out.println("\nbias: " + perceptron.activation);
            System.out.println();
            System.out.println(w + "/" + training.size());
            System.out.println(wr * 100 + "%");
        }
        System.out.println("===============================");
        int w = 0;
        for (Flower i :
                test) {
            w += perceptron.guess(i.attrs) == name.equals(i.name) ? 1 : 0;
        }
        wr = (double) w / test.size();
        System.out.println(w + "/" + test.size());
        System.out.println(wr * 100 + "%");
        boolean b = true;
        while (b) {
            System.out.println("czy chcesz podac atrybuty?\n1 - tak\n2 - nie");
            int tmp = scanner.nextInt();
            switch (tmp) {
                case 1 -> {
                    ArrayList<Double> attrs = new ArrayList<>();
                    for (int i = 0; i < a; i++) {
                        System.out.print("atrybut " + (i + 1) + ": ");
                        attrs.add(scanner.nextDouble());
                    }
                    System.out.println((perceptron.guess(attrs) ? "zidentyfikowano " : "nie zidentyfikowano ") + name);
                }
                case 2 -> b = false;
                default -> {
                }
            }
        }
        scanner.close();
    }

    public static ArrayList<Flower> loadFile(File file) {
        ArrayList<Flower> list = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] s = scanner.nextLine().split("\t");
                list.add(new Flower(s));
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static class Perceptron {
        public String name;
        public double alpha;
        public double beta;
        public int a;
        public double activation;
        public double[] weights;
        public Perceptron(String name, int a, double alpha, double beta) {
            this.name = name;
            this.alpha = alpha;
            this.beta = beta;
            this.a = a;
            activation = 1;
            weights = new double[a];
            Random rand = new Random();
            for (int i = 0; i < a; i++) {
                weights[i] = rand.nextDouble() + 0.01;
            }
        }
        public void train(Flower flower) {
            if(guess(flower.attrs)!=name.equals(flower.name)) {
                int d = name.equals(flower.name) ? 1 : 0;
                int y = guess(flower.attrs) ? 1 : 0;
                double delta = (d - y);
                for (int i = 0; i < a; i++) {
                    weights[i] += delta * alpha * flower.attrs.get(i);
                }
                activation -= delta * beta;
            }
        }
        public boolean guess(ArrayList<Double> attrs) {
            double sum = 0;
            for (int i = 0; i < a; i++) {
                sum += attrs.get(i) * weights[i];
            }
            return sum >= activation;
        }
    }

    public static class Flower {
        public ArrayList<Double> attrs;
        public String name;
        public Flower(String[] s){
            attrs = new ArrayList<>();
            for (int i = 0; i < s.length - 1; i++) {
                attrs.add(Double.parseDouble(s[i].replace(',', '.').strip()));
            }
            name = s[s.length - 1].strip();
        }
    }
}