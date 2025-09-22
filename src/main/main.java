package main;

import config.config;
import java.util.Scanner;

public class main {

    public static void viewuser() {
        String votersQuery = "SELECT * FROM user_table";
        String[] votersHeaders = {"ID", "Name", "Email", "CONTACT"};
        String[] votersColumns = {"user_id", "user_n", "user_email", "user_contactnum"};
        config c = new config();
        c.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        config c = new config();
        c.connectDB();

        String name, email, pn;
        int choice = 0;
        String input;

        do {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. Enter and Insert User Details");
            System.out.println("2. View User Details");
            System.out.println("3. Update User Details");
            System.out.println("4. Delete User");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1 to 5): ");
            input = s.nextLine();

            if (input.matches("[0-9]+")) {
                choice = Integer.parseInt(input);
            } else {
                System.out.println("❌ Invalid input. Please enter a number (1 to 5).");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    name = s.nextLine();

                    System.out.print("Enter Email: ");
                    email = s.nextLine();

                    System.out.print("Enter Phone Number: ");
                    pn = s.nextLine();

                    String sql = "INSERT INTO user_table (user_n, user_email, user_contactnum) VALUES (?, ?, ?)";
                    c.addRecord(sql, name, email, pn);

                    System.out.println("✅ User inserted successfully!");
                    break;

                case 2:
                    viewuser();
                    break;
                case 3:
                    viewuser();
                    System.out.print("Enter user ID: ");
                    int user_id = s.nextInt();

                    System.out.print("\nEnter new name: ");
                    String new_name = s.next();
                    System.out.print("Enter new email: ");
                    String new_email = s.next();
                    System.out.print("Enter new contact: ");
                    String new_contact = s.next();
                    sql = "UPDATE user_table SET user_n = ?, user_email = ?, user_contactnum = ? WHERE user_id = ?";
                    c.updateRecord(sql, new_name, new_email, new_contact, user_id);
                    break;
                case 4:
                    viewuser();
                    System.out.print("Enter user ID: ");
                    user_id = s.nextInt();
                    
                    sql = "DELETE from user_table WHERE user_id = ?";
                    c.deleteRecord(sql, user_id);                   
                    break;
                    
                case 5: 
                    System.out.println("Goodbye and thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 to 4.");
                    break;
            }
        } while (choice != 5);

        s.close();
    }
}
