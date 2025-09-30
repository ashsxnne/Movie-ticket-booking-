package main;

import config.config;
import java.util.*;

public class main {

    static config con = new config();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("=== MOVIE TICKET BOOKING SYSTEM ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice (1-3): ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        } while (true);
    }
   
    // ✅ Register
    public static void register() {
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Password: ");
        String pass = sc.next();

        System.out.println("Choose Role:");
        System.out.println("1. Customer");
        System.out.println("2. Admin");
        System.out.print("Enter choice (1-2): ");
        int roleChoice = sc.nextInt();

        String role;
        if (roleChoice == 1) {
            role = "Customer";
        } else if (roleChoice == 2) {
            role = "Admin";
        } else {
            System.out.println("Invalid choice! Defaulting to Customer.");
            role = "Customer";
        }

        String sql = "INSERT INTO user_table(u_name, u_email, u_pass, u_role, u_status) VALUES(?, ?, ?, ?, ?)";
        con.addRecord(sql, name, email, pass, role, "Pending");

        System.out.println("✅ Registration successful! Waiting for approval.");
    }

    // ✅ Login
    public static void login() {
        System.out.print("Enter Email: ");
        String email = sc.next();
        System.out.print("Enter Password: ");
        String pass = sc.next();

        String sql = "SELECT * FROM user_table WHERE u_email = ? AND u_pass = ?";
        List<Map<String, Object>> users = con.fetchRecords(sql, email, pass);

        if (users.isEmpty()) {
            System.out.println("❌ Invalid credentials!");
            return;
        }

        Map<String, Object> user = users.get(0);
        String role = user.get("u_role").toString();
        String status = user.get("u_status").toString();

        if (status.equals("Pending")) {
            System.out.println("⚠️ Account still pending approval!");
            return;
        }

        System.out.println("✅ Login successful! Welcome " + user.get("u_name") + " (Role: " + role + ")");

        if (role.equals("Admin")) {
            adminMenu();
        } else {
            customerMenu();
        }
    }

    // ✅ Admin Menu
    public static void adminMenu() {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View All Users");
            System.out.println("2. Approve a Customer Account");
            System.out.println("3. Logout");
            System.out.print("Enter choice (1-3): ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    String sql = "SELECT * FROM user_table";
                    String[] headers = {"ID", "Name", "Email", "Role", "Status"};
                    String[] cols = {"u_id", "u_name", "u_email", "u_role", "u_status"};
                    con.viewRecords(sql, headers, cols);
                    break;
                case 2:
                    System.out.print("Enter User ID to approve: ");
                    int id = sc.nextInt();
                    String upd = "UPDATE user_table SET u_status = ? WHERE u_id = ?";
                    con.updateRecord(upd, "Approved", id);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ✅ Customer Menu
    public static void customerMenu() {
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. Book Ticket");
            System.out.println("2. View My Bookings");
            System.out.println("3. Logout");
            System.out.print("Enter choice (1-3): ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Movie Name: ");
                    String movie = sc.next();
                    System.out.print("Enter Showtime: ");
                    String showtime = sc.next();
                    System.out.print("Enter Seat Number: ");
                    String seat = sc.next();

                    String sql = "INSERT INTO tbl_booking(movie_name, showtime, seat_no) VALUES (?, ?, ?)";
                    con.addRecord(sql, movie, showtime, seat);
                    break;
                case 2:
                    String view = "SELECT * FROM tbl_booking";
                    String[] headers = {"Booking ID", "Movie", "Showtime", "Seat"};
                    String[] cols = {"b_id", "movie_name", "showtime", "seat_no"};
                    con.viewRecords(view, headers, cols);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
