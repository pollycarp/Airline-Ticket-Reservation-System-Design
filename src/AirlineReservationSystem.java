import java.util.*;

// Class representing a Passenger
class Passenger {
    String name;
    String id;
    Passenger next; // Linked list node

    public Passenger(String name, String id) {
        this.name = name;
        this.id = id;
        this.next = null;
    }
}

// Class representing a Flight
class Flight {
    String flightNumber;
    Passenger headPassenger; // Head of the passenger linked list
    int reservedSeats;
    final int MAX_SEATS = 3;  // Changed to 3 for fast test instead of booking 371 times
    Flight next; // For linking multiple flights

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
        this.headPassenger = null;
        this.reservedSeats = 0;
        this.next = null;
    }

    // Method to reserve a ticket
    public boolean reserveTicket(String name, String id) {
        if (reservedSeats >= MAX_SEATS) {
            System.out.println("[!] Tickets sold out for flight: " + flightNumber);
            return false;
        }
        Passenger newPassenger = new Passenger(name, id);

        // Insert alphabetically
        if (headPassenger == null || name.compareToIgnoreCase(headPassenger.name) < 0) {
            newPassenger.next = headPassenger;
            headPassenger = newPassenger;
        } else {
            Passenger current = headPassenger;
            while (current.next != null && name.compareToIgnoreCase(current.next.name) > 0) {
                current = current.next;
            }
            newPassenger.next = current.next;
            current.next = newPassenger;
        }
        reservedSeats++;
        System.out.println("[+] Reservation successful for " + name);
        return true;
    }

    // Method to cancel a reservation
    public boolean cancelReservation(String name) {
        if (headPassenger == null) return false;

        if (headPassenger.name.equalsIgnoreCase(name)) {
            headPassenger = headPassenger.next;
            reservedSeats--;
            return true;
        }

        Passenger current = headPassenger;
        while (current.next != null && !current.next.name.equalsIgnoreCase(name)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            reservedSeats--;
            return true;
        }
        return false;
    }

    // Check if reservation exists
    public boolean isReserved(String name) {
        Passenger current = headPassenger;
        while (current != null) {
            if (current.name.equalsIgnoreCase(name)) return true;
            current = current.next;
        }
        return false;
    }

    // Display passengers
    public void displayPassengers() {
        Passenger current = headPassenger;
        System.out.println("Passengers on flight " + flightNumber + ":");
        while (current != null) {
            System.out.println("- " + current.name + " (ID: " + current.id + ")");
            current = current.next;
        }
    }
}

// Main class to handle user interaction
public class AirlineReservationSystem {
    static Map<String, Flight> flights = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Airline Reservation System\n");
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Reserve a ticket");
            System.out.println("2. Cancel a reservation");
            System.out.println("3. Check reservation");
            System.out.println("4. Display passengers");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> reserve();
                case 2 -> cancel();
                case 3 -> check();
                case 4 -> display();
                case 5 -> {
                    System.out.println("Exiting program.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    static void reserve() {
        System.out.print("Enter flight number: ");
        String flightNo = scanner.nextLine();
        System.out.print("Enter passenger name: ");
        String name = scanner.nextLine();
        System.out.print("Enter passenger ID: ");
        String id = scanner.nextLine();

        Flight flight = flights.getOrDefault(flightNo, new Flight(flightNo));
        flights.putIfAbsent(flightNo, flight);

        flight.reserveTicket(name, id);
    }

    static void cancel() {
        System.out.print("Enter flight number: ");
        String flightNo = scanner.nextLine();
        System.out.print("Enter passenger name to cancel: ");
        String name = scanner.nextLine();

        Flight flight = flights.get(flightNo);
        if (flight == null) {
            System.out.println("[!] Flight not found.");
            return;
        }
        if (flight.cancelReservation(name)) {
            System.out.println("[-] Reservation cancelled.");
        } else {
            System.out.println("[!] No reservation found under that name.");
        }
    }

    static void check() {
        System.out.print("Enter flight number: ");
        String flightNo = scanner.nextLine();
        System.out.print("Enter passenger name to check: ");
        String name = scanner.nextLine();

        Flight flight = flights.get(flightNo);
        if (flight != null && flight.isReserved(name)) {
            System.out.println("[✓] Reservation exists for " + name);
        } else {
            System.out.println("[✗] No reservation found.");
        }
    }

    static void display() {
        System.out.print("Enter flight number to display passengers: ");
        String flightNo = scanner.nextLine();
        Flight flight = flights.get(flightNo);
        if (flight == null) {
            System.out.println("[!] Flight not found.");
        } else {
            flight.displayPassengers();
        }
    }
}
