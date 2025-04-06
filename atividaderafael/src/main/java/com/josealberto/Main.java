package com.josealberto;

import com.josealberto.entities.User;
import com.josealberto.repository.UserRepository;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:usuarios.db")) {
            runInitScript(connection);

            UserRepository userRepository = new UserRepository(connection);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1 - Cadastrar usuário");
                System.out.println("2 - Listar usuários");
                System.out.println("3 - Sair");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    System.out.print("Nome: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Senha: ");
                    String password = scanner.nextLine();
                    UUID uuid = UUID.randomUUID();
                    User user = new User(uuid, name, email, password);
                    userRepository.save(user);
                    System.out.println("Usuário cadastrado com sucesso!\n");
                } else if (opcao == 2) {
                    List<User> users = userRepository.findAll();
                    for (User u : users) {
                        System.out.println("UUID: " + u.getUuid());
                        System.out.println("Nome: " + u.getName());
                        System.out.println("Email: " + u.getEmail());
                        System.out.println("---------------");
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runInitScript(Connection connection) {
        try {
            InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("scripts/init.sql");
            if (inputStream == null) {
                throw new RuntimeException("Arquivo init.sql não encontrado!");
            }


            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            String script = scanner.hasNext() ? scanner.next() : "";

            Statement stmt = connection.createStatement();
            stmt.execute(script);
            stmt.close();
            System.out.println(" Script init.sql executado com sucesso!");
        } catch (Exception e) {
            System.out.println(" Erro ao executar init.sql: " + e.getMessage());
        }
    }
}
