import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaCode_GroupProject_01_HindsChristopher extends JFrame {

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

    private DefaultListModel<Player> rosterModel;
    private DefaultListModel<Player> teamModel;
    private JList<Player> rosterList;
    private JList<Player> teamList;
    private JLabel teamCountLabel;

    public JavaCode_GroupProject_01_HindsChristopher() {
        setTitle("Baltimore Ravens 2026 Team Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Initialize models
        rosterModel = new DefaultListModel<>();
        teamModel = new DefaultListModel<>();

        // Add players to roster
        initializeRoster();

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(36, 28, 79)); // Ravens purple

        // Header
        JLabel headerLabel = new JLabel("Baltimore Ravens 2026 Team Selector", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(198, 159, 60)); // Ravens gold
        headerLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Center panel with two lists
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);

        // Roster panel (left)
        JPanel rosterPanel = createListPanel("Available Players", rosterModel, true);
        centerPanel.add(rosterPanel);

        // Team panel (right)
        JPanel teamPanel = createListPanel("Your Team", teamModel, false);
        centerPanel.add(teamPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false);

        JButton addButton = createStyledButton("Add to Team >>");
        JButton removeButton = createStyledButton("<< Remove");
        JButton clearButton = createStyledButton("Clear Team");
        JButton saveButton = createStyledButton("Save Team");

        addButton.addActionListener(e -> addSelectedPlayer());
        removeButton.addActionListener(e -> removeSelectedPlayer());
        clearButton.addActionListener(e -> clearTeam());
        saveButton.addActionListener(e -> writeTeamToCSV());

        bottomPanel.add(removeButton);
        bottomPanel.add(addButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(saveButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Double-click to add/remove
        rosterList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addSelectedPlayer();
                }
            }
        });

        teamList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    removeSelectedPlayer();
                }
            }
        });
    }

    private void initializeRoster() {
        readPlayersFromCSV();
    }

    // Written by: Christopher Hinds – This method reads player data from the CSV file.
    private void readPlayersFromCSV() {
        String csvFile = "ravens_roster.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String position = parts[1].trim();
                    rosterModel.addElement(new Player(name, position));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Could not read roster file: " + e.getMessage()
                + "\nPlease make sure ravens_roster.csv is in the program directory.",
                "File Read Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Written by: Christopher Hinds – This method writes the selected team to a CSV file.
    private void writeTeamToCSV() {
        if (teamModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your team is empty. Add players before saving.",
                "Empty Team",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String csvFile = "my_team.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("Name,Position");
            bw.newLine();
            for (int i = 0; i < teamModel.size(); i++) {
                Player player = teamModel.get(i);
                bw.write(player.name + "," + player.position);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this,
                "Team saved to " + csvFile + " successfully!",
                "Team Saved",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Could not save team: " + e.getMessage(),
                "File Write Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createListPanel(String title, DefaultListModel<Player> model, boolean isRoster) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);

        // Title label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        // List
        JList<Player> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer(new PlayerCellRenderer());
        list.setBackground(new Color(255, 255, 255));

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(198, 159, 60), 2));
        panel.add(scrollPane, BorderLayout.CENTER);

        if (isRoster) {
            rosterList = list;
        } else {
            teamList = list;
            // Add count label for team
            teamCountLabel = new JLabel("Players: 0", SwingConstants.CENTER);
            teamCountLabel.setForeground(new Color(198, 159, 60));
            teamCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
            panel.add(teamCountLabel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(198, 159, 60)); // Ravens gold
        button.setForeground(new Color(36, 28, 79)); // Ravens purple
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 120, 40), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addSelectedPlayer() {
        Player selected = rosterList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a player from the roster.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if already in team
        for (int i = 0; i < teamModel.size(); i++) {
            if (teamModel.get(i).name.equals(selected.name)) {
                JOptionPane.showMessageDialog(this,
                    selected.name + " is already on your team!",
                    "Duplicate Player",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        teamModel.addElement(selected);
        updateTeamCount();
    }

    private void removeSelectedPlayer() {
        Player selected = teamList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a player from your team to remove.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        teamModel.removeElement(selected);
        updateTeamCount();
    }

    private void clearTeam() {
        if (teamModel.isEmpty()) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear your team?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            teamModel.clear();
            updateTeamCount();
        }
    }

    private void updateTeamCount() {
        teamCountLabel.setText("Players: " + teamModel.size());
    }

    // Custom cell renderer for player list
    private class PlayerCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Player) {
                Player player = (Player) value;
                setText(player.toString());

                // Color code by position
                if (!isSelected) {
                    switch (player.position) {
                        case "QB":
                            setBackground(new Color(255, 230, 230)); // Light red
                            break;
                        case "RB":
                            setBackground(new Color(230, 255, 230)); // Light green
                            break;
                        case "WR":
                            setBackground(new Color(230, 230, 255)); // Light blue
                            break;
                        case "TE":
                            setBackground(new Color(255, 255, 230)); // Light yellow
                            break;
                        default:
                            setBackground(Color.WHITE);
                    }
                }
            }

            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            return this;
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        SwingUtilities.invokeLater(() -> {
            JavaCode_GroupProject_01_HindsChristopher gui = new JavaCode_GroupProject_01_HindsChristopher();
            gui.setVisible(true);
        });
    }
}
