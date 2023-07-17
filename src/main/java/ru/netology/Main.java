package ru.netology;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static int counterA = 0;
    private static int counterB = 0;
    private static int counterC = 0;
    private static final char A = 'a';
    private static final char B = 'b';
    private static final char C = 'c';
    private static String lineWithMaxA = null;
    private static String lineWithMaxB = null;
    private static String lineWithMaxC = null;
    private static BlockingQueue<String> queueASymbol = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueBSymbol = new ArrayBlockingQueue<>(100);
    private static BlockingQueue<String> queueCSymbol = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        int length = 10_000;
        Thread generatorString = new Thread(() -> {
            for (int i = 0; i < length; i++) {
                String text = RandomTextArrayGenerator.generateText("abc", 100_000);
                try {
                    queueASymbol.put(text);
                    queueBSymbol.put(text);
                    queueCSymbol.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread counterASymbol = new Thread(() -> {
            for (int i = 0; i < length; i++) {
                try {
                    String currentLine = queueASymbol.take();
                    int resentNumberOfA = countNumberOfSymbol(currentLine, A);
                    if (resentNumberOfA > counterA) {
                        counterA = resentNumberOfA;
                        lineWithMaxA = currentLine;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread counterBSymbol = new Thread(() -> {
            for (int i = 0; i < length; i++) {
                try {
                    String currentLine = queueBSymbol.take();
                    int resentNumberOfB = countNumberOfSymbol(currentLine, B);
                    if (resentNumberOfB > counterB) {
                        counterB = resentNumberOfB;
                        lineWithMaxB = currentLine;
                    }

                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread counterCSymbol = new Thread(() -> {
            for (int i = 0; i < length; i++) {
                try {
                    String currentLine = queueCSymbol.take();
                    int resentNumberOfC = countNumberOfSymbol(currentLine, C);
                    if (resentNumberOfC > counterC) {
                        counterC = resentNumberOfC;
                        lineWithMaxC = currentLine;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        generatorString.start();
        counterASymbol.start();
        counterBSymbol.start();
        counterCSymbol.start();

        generatorString.join();
        counterASymbol.join();
        counterBSymbol.join();
        counterCSymbol.join();

        System.out.println("Line with max A symbols was found - " + !lineWithMaxA.isBlank());
        System.out.println("Line with max B symbols was found - " + !lineWithMaxB.isBlank());
        System.out.println("Line with max C symbols was found - " + !lineWithMaxC.isBlank());
    }

    private static int countNumberOfSymbol(String line, char symbol) {
        int counter = 0;
        for (char item : line.toCharArray()) {
            if (item == symbol) counter++;
        }
        return counter;
    }
}