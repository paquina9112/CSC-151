import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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

    public DefaultListModel<Player> rosterModel = new DefaultListModel<>();
    public DefaultListModel<Player> teamModel = new DefaultListModel<>();

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Baltimore Ravens Team Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);

        JList<Player> rosterList = new JList<>(rosterModel);
        JList<Player> teamList = new JList<>(teamModel);
        rosterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel center = new JPanel(new GridLayout(1, 3, 10, 10));

        center.add(new JScrollPane(rosterList));

        JPanel buttons = new JPanel(new GridLayout(6, 1, 5, 5));
        JButton addBtn = new JButton("Add →");
        JButton removeBtn = new JButton("← Remove");
        JButton shuffleBtn = new JButton("Shuffle Team");
        JButton clearBtn = new JButton("Clear Team");
        JButton selectAllBtn = new JButton("Select All Roster");
        JButton exitBtn = new JButton("Exit");

        buttons.add(addBtn);
        buttons.add(removeBtn);
        buttons.add(shuffleBtn);
        buttons.add(clearBtn);
        buttons.add(selectAllBtn);
        buttons.add(exitBtn);

        center.add(buttons);
        center.add(new JScrollPane(teamList));

        frame.getContentPane().add(center, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton showBtn = new JButton("Show Team Order");
        south.add(showBtn);
        frame.getContentPane().add(south, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            Player p = rosterList.getSelectedValue();
            if (p != null && !teamContains(p)) {
                teamModel.addElement(p);
            }
        });

        removeBtn.addActionListener(e -> {
            Player p = teamList.getSelectedValue();
            if (p != null) teamModel.removeElement(p);
        });

        clearBtn.addActionListener(e -> teamModel.clear());

        selectAllBtn.addActionListener(e -> rosterList.setSelectionInterval(0, rosterModel.getSize() - 1));

        shuffleBtn.addActionListener(e -> shuffleTeam());

        showBtn.addActionListener(e -> showTeamOrder());

        exitBtn.addActionListener(e -> System.exit(0));

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private boolean teamContains(Player p) {
        for (int i = 0; i < teamModel.size(); i++) {
            if (teamModel.get(i).name.equals(p.name)) return true;
        }
        return false;
    }

    private void shuffleTeam() {
        if (teamModel.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Team is empty — add players first.");
            return;
        }
        List<Player> temp = new ArrayList<>();
        for (int i = 0; i < teamModel.size(); i++) temp.add(teamModel.get(i));
        Collections.shuffle(temp);
        teamModel.clear();
        for (Player p : temp) teamModel.addElement(p);
        JOptionPane.showMessageDialog(null, "Team shuffled.");
    }

    private void showTeamOrder() {
        if (teamModel.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No players in team.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < teamModel.size(); i++) {
            sb.append((i + 1)).append(". ").append(teamModel.get(i)).append("\n");
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(350, 200));
        JOptionPane.showMessageDialog(null, sp, "Selected Team", JOptionPane.INFORMATION_MESSAGE);
    }

    public void loadRoster() {
        rosterModel.addElement(new Player("Lamar Jackson", "QB"));
        rosterModel.addElement(new Player("Tyler Huntley", "QB"));
        rosterModel.addElement(new Player("Mark Andrews", "TE"));
        rosterModel.addElement(new Player("Isaish Likely", "TE"));
        rosterModel.addElement(new Player("Derrick Henry", "RB"));
        rosterModel.addElement(new Player("Tyler Linderbaum", "C"));
        rosterModel.addElement(new Player("Marlon Humphrey", "CB"));
        rosterModel.addElement(new Player("DeAndre Hopkins", "WR"));
        rosterModel.addElement(new Player("Zay Flowers", "WR"));
        rosterModel.addElement(new Player("Kyle Hamilton", "S"));
        rosterModel.addElement(new Player("Roger Rosengarten", "T"));
        rosterModel.addElement(new Player("Kyle Van Noy", "OLB"));
        rosterModel.addElement(new Player("Rashod Bateman", "WR"));
        rosterModel.addElement(new Player("Roquan Smith", "LB"));
        rosterModel.addElement(new Player("Tyler Loop", "K"));
    }

    private void headlessMode() {
        List<Player> roster = new ArrayList<>();
        roster.add(new Player("Lamar Jackson", "QB"));
        roster.add(new Player("Tyler Huntley", "QB"));
        roster.add(new Player("Mark Andrews", "TE"));
        roster.add(new Player("Isaish Likely", "TE"));
        roster.add(new Player("Derrick Henry", "RB"));
        roster.add(new Player("Tyler Linderbaum", "C"));
        roster.add(new Player("Marlon Humphrey", "CB"));
        roster.add(new Player("DeAndre Hopkins", "WR"));
        roster.add(new Player("Zay Flowers", "WR"));
        roster.add(new Player("Kyle Hamilton", "S"));
        roster.add(new Player("Roger Rosengarten", "T"));
        roster.add(new Player("Kyle Van Noy", "OLB"));
        roster.add(new Player("Rashod Bateman", "WR"));
        roster.add(new Player("Roquan Smith", "LB"));
        roster.add(new Player("Tyler Loop", "K"));

        List<Player> team = new ArrayList<>();
        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("Headless console mode — Baltimore Ravens Team Selector");
            System.out.println("Roster:");
            for (int i = 0; i < roster.size(); i++) {
                System.out.println((i + 1) + ". " + roster.get(i));
            }

            while (true) {
                System.out.println("\nEnter the number of the player to add to your team (or 0 to finish):");
                String line = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    continue;
                }
                if (choice == 0) break;
                if (choice < 1 || choice > roster.size()) {
                    System.out.println("Invalid choice.");
                    continue;
                }
                Player p = roster.get(choice - 1);
                if (team.contains(p)) {
                    System.out.println(p.name + " is already in your team.");
                } else {
                    team.add(p);
                    System.out.println(p.name + " added.");
                }
            }

            if (team.isEmpty()) {
                System.out.println("No players selected.");
                return;
            }

            System.out.println("\nYour selected team:");
            for (int i = 0; i < team.size(); i++) System.out.println((i + 1) + ". " + team.get(i));

            System.out.println("\nShuffle team? (y/n)");
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.startsWith("y")) {
                Collections.shuffle(team);
                System.out.println("\nShuffled team:");
                for (int i = 0; i < team.size(); i++) System.out.println((i + 1) + ". " + team.get(i));
            }
        }
    }

    public static void main(String[] args) {
        // Check environment first
        String display = System.getenv("DISPLAY");
        
        if (display == null || display.isEmpty()) {
            // No display available, use console mode
            System.setProperty("java.awt.headless", "true");
            RavensTeamSelector app = new RavensTeamSelector();
            app.headlessMode();
        } else {
            // Display available, try GUI
            RavensTeamSelector app = new RavensTeamSelector();
            app.loadRoster();
            SwingUtilities.invokeLater(() -> app.createAndShowGUI());
        }
    }
}