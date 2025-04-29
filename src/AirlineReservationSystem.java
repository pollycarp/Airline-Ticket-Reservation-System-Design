import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

class Passenger implements Comparable<Passenger> {
    private String firstName;
    private String lastName;

    public Passenger(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int compareTo(Passenger other) {
        int lastNameCompare = this.lastName.compareToIgnoreCase(other.lastName);
        if (lastNameCompare != 0) {
            return lastNameCompare;
        } else {
            return this.firstName.compareToIgnoreCase(other.firstName);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Passenger other = (Passenger) obj;
        return firstName.equalsIgnoreCase(other.firstName) && lastName.equalsIgnoreCase(other.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }
}

class Flight {
    private String flightNumber;
    private LinkedList<Passenger> passengers;
    public static final int MAX_SEATS = 370;

    public Flight(String flightNumber) {
        this.flightNumber = flightNumber;
        this.passengers = new LinkedList<>();
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public boolean addPassenger(Passenger passenger) {
        if (passengers.size() >= MAX_SEATS) {
            return false;
        }
        int index = 0;
        for (Passenger p : passengers) {
            if (passenger.compareTo(p) < 0) {
                break;
            }
            index++;
        }
        passengers.add(index, passenger);
        return true;
    }

    public boolean removePassenger(Passenger passenger) {
        return passengers.remove(passenger);
    }

    public boolean containsPassenger(Passenger passenger) {
        return passengers.contains(passenger);
    }

    public void displayPassengers() {
        if (passengers.isEmpty()) {
            System.out.println("No passengers on this flight.");
            return;
        }
        for (Passenger p : passengers) {
            System.out.println(p);
        }
    }

    public int getNumberOfPassengers() {
        return passengers.size();
    }
}

public class AirlineReservationSystem {
    private static LinkedList<Flight> flights = new LinkedList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    reserveTicket();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    checkReservation();
                    break;
                case 4:
                    displayPassengers();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\nAirline Ticket Reservation System");
        System.out.println("1. Reserve a ticket");
        System.out.println("2. Cancel a reservation");
        System.out.println("3. Check reservation");
        System.out.println("4. Display passengers");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static Flight findOrCreateFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return flight;
            }
        }
        Flight newFlight = new Flight(flightNumber);
        flights.add(newFlight);
        return newFlight;
    }

    private static Flight findFlight(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equalsIgnoreCase(flightNumber)) {
                return flight;
            }
        }
        return null;
    }

//    private static void reserveTicket() {
//        System.out.print("Enter flight number: ");
//        String flightNumber = scanner.nextLine().trim();
//        Flight flight = findOrCreateFlight(flightNumber);
//
//        if (flight.getNumberOfPassengers() >= Flight.MAX_SEATS) {
//            System.out.println("Sorry, this flight is full. Cannot reserve more tickets.");
//            return;
//        }
//
//        System.out.print("Enter passenger's first name: ");
//        String firstName = scanner.nextLine().trim();
//        System.out.print("Enter passenger's last name: ");
//        String lastName = scanner.nextLine().trim();
//
//        Passenger passenger = new Passenger(firstName, lastName);
//        if (flight.addPassenger(passenger)) {
//            System.out.println("Reservation successful for " + passenger);
//        } else {
//            System.out.println("Reservation failed. Flight is full.");
//        }
//    }
    private static void reserveTicket() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = findOrCreateFlight(flightNumber);

        // Auto-populate 370 passengers if flight number is "FULL"
        if (flightNumber.equalsIgnoreCase("FULL")) {
            if (flight.getNumberOfPassengers() == 0) {
                // Add 370 dummy passengers
                for (int i = 1; i <= Flight.MAX_SEATS; i++) {
                    Passenger dummy = new Passenger("Passenger" + i, "Doe");
                    flight.addPassenger(dummy);
                }
                System.out.println("Auto-added 370 passengers to flight FULL.");
            }
            return; // Skip manual input for this test
        }

        if (flight.getNumberOfPassengers() >= Flight.MAX_SEATS) {
            System.out.println("Sorry, this flight is full. Cannot reserve more tickets.");
            return;
        }

        System.out.print("Enter passenger's first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter passenger's last name: ");
        String lastName = scanner.nextLine().trim();

        Passenger passenger = new Passenger(firstName, lastName);
        if (flight.addPassenger(passenger)) {
            System.out.println("Reservation successful for " + passenger);
        } else {
            System.out.println("Reservation failed. Flight is full.");
        }
    }
    private static void cancelReservation() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = findFlight(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.print("Enter passenger's first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter passenger's last name: ");
        String lastName = scanner.nextLine().trim();

        Passenger passenger = new Passenger(firstName, lastName);
        if (flight.removePassenger(passenger)) {
            System.out.println("Reservation canceled for " + passenger);
        } else {
            System.out.println("Passenger not found on this flight.");
        }
    }

    private static void checkReservation() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = findFlight(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.print("Enter passenger's first name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter passenger's last name: ");
        String lastName = scanner.nextLine().trim();

        Passenger passenger = new Passenger(firstName, lastName);
        if (flight.containsPassenger(passenger)) {
            System.out.println("Passenger has a reservation on this flight.");
        } else {
            System.out.println("Passenger does not have a reservation on this flight.");
        }
    }

    private static void displayPassengers() {
        System.out.print("Enter flight number: ");
        String flightNumber = scanner.nextLine().trim();
        Flight flight = findFlight(flightNumber);
        if (flight == null) {
            System.out.println("Flight not found.");
            return;
        }

        System.out.println("Passengers on flight " + flightNumber + ":");
        flight.displayPassengers();
    }

}