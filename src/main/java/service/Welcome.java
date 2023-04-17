package service;


import java.util.Scanner;

public class Welcome {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите Ваше имя:");
        String name = scanner.nextLine();
        System.out.println("Здравствуйте " + name);
    }

}
