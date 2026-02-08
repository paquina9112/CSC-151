import java.util.Scanner;

public class RavensTeamSelector {

    static class Player {
        String name;
        String position;

        Player(String name, String position) {
            this.name = name;
            this.position = position;
        }

        @Override
        public String toString() {
            return name + " (" + position + ")";
        }
    }

    public static void main(String[] args) {
        List<Player> roster = new ArrayList<>();
        roster.add(new Player("Lamar Jackson", "QB"));
        roster.add(new Player("Mark Andrews", "TE"));
        roster.add(new Player("Tyler Linderbaum", "C"));
        roster.add(new Player("Marlon Humphrey", "CB"));
        roster.add(new Player("Odafe Oweh", "LB"));
        roster.add(new Player("Zay Flowers", "WR"));
        roster.add(new Player("Kyle Hamilton", "S"));
        roster.add(new Player("Rashod Bateman", "WR"));
        roster.add(new Player("Roquan Smith", "LB"));
        roster.add(new Player("Justin Tucker", "K"));

        List<Player> selectedTeam = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Baltimore Ravens 2026 Team Selector!");
        System.out.println("Here is the roster:");

        for (int i = 0; i < roster.size(); i++) {
            System.out.println((i + 1) + ". " + roster.get(i));
        }

        while (true) {
            System.out.println("\nEnter the number of the player to add to your team (or 0 to finish):");
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (choice == 0) {
                break;
            }

            if (choice < 1 || choice > roster.size()) {
                System.out.println("Invalid choice. Please select a number from the list.");
                continue;
            }

            Player selected = roster.get(choice - 1);
            if (selectedTeam.contains(selected)) {
                System.out.println(selected.name + " is already in your team.");
            } else {
                selectedTeam.add(selected);
                System.out.println(selected.name + " added to your team.");
            }
        }

        System.out.println("\nYour selected team:");
        if (selectedTeam.isEmpty()) {
            System.out.println("No players selected.");
        } else {
            for (Player p : selectedTeam) {
                System.out.println("- " + p);
            }
        }

        scanner.close();
    }
}