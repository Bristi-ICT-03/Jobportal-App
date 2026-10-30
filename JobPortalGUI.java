import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JobPortalGUI extends JFrame {

    private JTextField nameField;
    private JComboBox<String> roleCombo, userTypeCombo;
    private JTextArea resultArea;
    private JPanel roadmapPanel;
    private JLabel matchLabel, statusLabel;

    // Roles, skill graph and roadmap logic live in SkillCatalog (GUI only shows results)
    private final SkillCatalog catalog = new SkillCatalog();
    private final Map<String, JCheckBox> skillBoxes = new HashMap<>();
    // All submitted applications (Recruiter reads from this list)
    private final List<Application> applications = new ArrayList<>();

    // VS Code Dark Theme Colors
    private final Color VS_BG = new Color(30, 30, 30);
    private final Color VS_PANEL_BG = new Color(37, 37, 38);
    private final Color VS_INPUT_BG = new Color(60, 60, 60);
    private final Color VS_BORDER = new Color(65, 65, 65);
    private final Color VS_BLUE = new Color(0, 122, 204);
    private final Color VS_BLUE_HOVER = new Color(28, 142, 222);
    private final Color VS_TEXT = new Color(212, 212, 212);
    private final Color VS_ACCENT = new Color(255, 175, 0);

    public JobPortalGUI() {
        createGUI();
    }

    private void createGUI() {
        setTitle("Job Portal");
        setSize(1150, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel mainPanel = new JPanel(new BorderLayout(12, 12));
        mainPanel.setBackground(VS_BG);
        mainPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel title = new JLabel("JOB PORTAL", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(VS_ACCENT);
        mainPanel.add(title, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(VS_BG);
        center.add(createFormPanel(), BorderLayout.NORTH);
        JPanel body = new JPanel(new GridLayout(1, 2, 12, 0));
        body.setBackground(VS_BG);
        body.add(createSkillPanel());
        body.add(createResultPanel());
        center.add(body, BorderLayout.CENTER);
        center.add(createBottomPanel(), BorderLayout.SOUTH);
        mainPanel.add(center, BorderLayout.CENTER);
        add(new JScrollPane(mainPanel));
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(VS_PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VS_BORDER, 1), new EmptyBorder(10, 15, 10, 15)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField();
        styleTextField(nameField);
        roleCombo = new JComboBox<>(catalog.getRoles().toArray(new String[0]));
        styleComboBox(roleCombo);
        userTypeCombo = new JComboBox<>(new String[]{"Job Seeker", "Recruiter"});
        styleComboBox(userTypeCombo);
        JButton applyButton = createButton("Apply Job");
        applyButton.addActionListener(e -> applyForJob());
        JButton viewButton = createButton("View Applicants");
        viewButton.addActionListener(e -> viewApplicants());
        JPanel buttons = new JPanel(new GridLayout(1, 2, 6, 0));
        buttons.setBackground(VS_PANEL_BG);
        buttons.add(applyButton);
        buttons.add(viewButton);
        addComponent(panel, createLabel("Name:"), gbc, 0, 0, 0.1);
        addComponent(panel, nameField, gbc, 1, 0, 0.4);
        addComponent(panel, createLabel("I am a:"), gbc, 2, 0, 0.1);
        addComponent(panel, userTypeCombo, gbc, 3, 0, 0.3);
        addComponent(panel, createLabel("Target Role:"), gbc, 0, 1, 0.1);
        addComponent(panel, roleCombo, gbc, 1, 1, 0.4);
        addComponent(panel, new JLabel(""), gbc, 2, 1, 0.1);
        addComponent(panel, buttons, gbc, 3, 1, 0.3);
        return panel;
    }

    private JPanel createSkillPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(VS_BG);
        panel.add(createTitleLabel("Select Your Skills"), BorderLayout.NORTH);
        JPanel skillsGrid = new JPanel(new GridLayout(7, 2, 6, 6));
        skillsGrid.setBackground(VS_PANEL_BG);
        skillsGrid.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VS_BORDER, 1), new EmptyBorder(8, 8, 8, 8)));
        for (String skill : catalog.getAllSkills()) {
            JCheckBox box = new JCheckBox(skill);
            box.setForeground(VS_TEXT);
            box.setBackground(VS_PANEL_BG);
            box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            box.setFocusPainted(false);
            skillBoxes.put(skill, box);
            skillsGrid.add(box);
        }
        panel.add(new JScrollPane(skillsGrid), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(VS_BG);
        panel.add(createTitleLabel("Results"), BorderLayout.NORTH);
        JPanel header = new JPanel(new GridLayout(1, 2));
        header.setBackground(VS_PANEL_BG);
        header.setBorder(BorderFactory.createLineBorder(VS_BORDER, 1));
        matchLabel = createHeaderLabel("Match: 0%", VS_ACCENT);
        statusLabel = createHeaderLabel("Status: Not Applied", VS_TEXT);
        header.add(matchLabel);
        header.add(statusLabel);
        resultArea = new JTextArea("Enter details and click Apply Job.");
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setForeground(VS_TEXT);
        resultArea.setBackground(VS_PANEL_BG);
        resultArea.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(BorderFactory.createLineBorder(VS_BORDER, 1));
        JPanel content = new JPanel(new BorderLayout(0, 5));
        content.setBackground(VS_BG);
        content.add(header, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(VS_BG);
        JButton roadmapButton = createButton("Generate Skill Roadmap");
        roadmapButton.addActionListener(e -> generateRoadmap());
        JPanel pathContainer = new JPanel(new BorderLayout());
        pathContainer.setBackground(VS_PANEL_BG);
        pathContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(VS_ACCENT), "Learning Path",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12), VS_ACCENT));
        roadmapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        roadmapPanel.setBackground(VS_PANEL_BG);
        JLabel placeholder = createLabel("  Click 'Generate Skill Roadmap' to view path.");
        placeholder.setForeground(Color.GRAY);
        roadmapPanel.add(placeholder);
        pathContainer.add(new JScrollPane(roadmapPanel), BorderLayout.CENTER);
        panel.add(roadmapButton, BorderLayout.NORTH);
        panel.add(pathContainer, BorderLayout.CENTER);
        return panel;
    }

    // Inheritance + Polymorphism: userType decides which BaseUser subclass is created
    private BaseUser createUser(String name, String userType) {
        return userType.equals("Recruiter") ? new Recruiter(name) : new JobSeeker(name);
    }

    private void applyForJob() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = (String) roleCombo.getSelectedItem();
        BaseUser user = createUser(name, (String) userTypeCombo.getSelectedItem());

        // Polymorphism: each subclass answers canApply() in its own way
        if (!user.canApply()) {
            JOptionPane.showMessageDialog(this, "Recruiters cannot apply for jobs. Use 'View Applicants' instead.",
                    "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Interface + Polymorphism: IMatcher reference, SmartJobMatcher object
        IMatcher matcher = new SmartJobMatcher();
        List<String> selected = getSelectedSkills();
        List<String> required = catalog.getRequiredSkills(role);
        int matchPercent = matcher.calculateMatch(selected, required);
        List<String> missing = matcher.getMissingSkills(selected, required);

        Application application = new Application(user.getName(), role, matchPercent);
        // Same person + same job applying again replaces the old application
        applications.removeIf(a -> a.getUserName().equalsIgnoreCase(name) && a.getJobTitle().equals(role));
        applications.add(application);

        matchLabel.setText("Match: " + matchPercent + "%");
        statusLabel.setText("Status: Applied");

        StringBuilder sb = new StringBuilder();
        sb.append(application).append("\n\n");
        sb.append("Role Applied As: ").append(user.getRole()).append("\n\n");
        sb.append("Missing Skills:\n");
        if (missing.isEmpty()) {
            sb.append("  \u2022 None (All skills matched!)\n");
        }
        for (String m : missing) {
            sb.append("  \u2022 ").append(m).append("\n");
        }
        resultArea.setText(sb.toString());
    }

    private void viewApplicants() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your name.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String role = (String) roleCombo.getSelectedItem();
        BaseUser user = createUser(name, (String) userTypeCombo.getSelectedItem());

        if (!user.canViewApplicants()) {
            JOptionPane.showMessageDialog(this, "Only a Recruiter can view applicants.",
                    "Not Allowed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Safe: only a Recruiter object can reach this line
        List<Application> list = ((Recruiter) user).getApplicantsFor(applications, role);

        StringBuilder sb = new StringBuilder("Applicants for " + role + " (best match first)\n\n");
        if (list.isEmpty()) {
            sb.append("  No applications yet.\n");
        }
        int rank = 1;
        for (Application a : list) {
            sb.append("  ").append(rank++).append(". ").append(a).append("\n");
        }
        resultArea.setText(sb.toString());
        matchLabel.setText("Applicants: " + list.size());
        statusLabel.setText("Viewing as Recruiter");
    }

    private void generateRoadmap() {
        String role = (String) roleCombo.getSelectedItem();
        Set<Skill> known = new LinkedHashSet<>();
        for (String s : getSelectedSkills()) {
            known.add(new Skill(s));
        }

        // Interface + Polymorphism: RoadmapStrategy reference, BFS object
        RoadmapStrategy strategy = new BFSRoadmapStrategy();
        Set<Skill> roadmap = catalog.buildRoadmap(role, known, strategy);

        roadmapPanel.removeAll();
        Skill prev = null;
        int step = 1;

        for (Skill skill : roadmap) {
            boolean isKnown = known.contains(skill);
            Color color = isKnown ? new Color(40, 110, 40) : new Color(180, 100, 0);
            String label = isKnown ? "\u2713 " + skill.getName() : step + ": " + skill.getName();

            if (prev != null) {
                // arrow = direct next step, bar = new branch
                roadmapPanel.add(createLabel(catalog.isDirectStep(prev, skill) ? "\u2192" : "|"));
            }
            roadmapPanel.add(createNodeLabel(label, color));

            if (!isKnown) {
                step++;
            }
            prev = skill;
        }

        if (roadmap.isEmpty()) {
            roadmapPanel.add(createLabel("  You already know all required skills!"));
        }

        roadmapPanel.revalidate();
        roadmapPanel.repaint();
    }

    private List<String> getSelectedSkills() {
        List<String> selected = new ArrayList<>();
        for (Map.Entry<String, JCheckBox> entry : skillBoxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    private JLabel createLabel(String text) {
        return createLabel(text, VS_TEXT, Font.PLAIN, 12);
    }

    private JLabel createTitleLabel(String text) {
        return createLabel(text, VS_ACCENT, Font.BOLD, 13);
    }

    private JLabel createHeaderLabel(String text, Color color) {
        JLabel lbl = createLabel(text, color, Font.BOLD, 13);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        return lbl;
    }

    private JLabel createLabel(String text, Color color, int style, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(color);
        lbl.setFont(new Font("Segoe UI", style, size));
        return lbl;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(VS_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(VS_BLUE_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(VS_BLUE); }
        });
        return btn;
    }

    private JLabel createNodeLabel(String text, Color bg) {
        JLabel lbl = new JLabel(" " + text + " ");
        lbl.setOpaque(true);
        lbl.setBackground(bg);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return lbl;
    }

    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBackground(VS_INPUT_BG);
        tf.setForeground(VS_TEXT);
        tf.setCaretColor(VS_TEXT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VS_BORDER, 1), BorderFactory.createEmptyBorder(3, 5, 3, 5)));
    }

    private void styleComboBox(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setBackground(VS_INPUT_BG);
        cb.setForeground(VS_TEXT);
        cb.setBorder(BorderFactory.createLineBorder(VS_BORDER, 1));
    }

    private void addComponent(JPanel panel, Component comp, GridBagConstraints gbc, int x, int y, double weight) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weight;
        panel.add(comp, gbc);
    }
}