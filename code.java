import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalConditionPredictor extends JFrame implements ActionListener {
    private Connection connection;
    private List<JCheckBox> symptomCheckboxes;

    public MedicalConditionPredictor() {
        super("Medical Condition Predictor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize symptom checkboxes
        symptomCheckboxes = new ArrayList<>();

        // Connect to MySQL database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medical_conditions", "yourusername", "yourpassword");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create symptom selection panel
        JPanel symptomPanel = new JPanel(new GridLayout(0, 1));
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT symptom FROM symptoms");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String symptom = resultSet.getString("symptom");
                JCheckBox checkBox = new JCheckBox(symptom);
                symptomPanel.add(checkBox);
                symptomCheckboxes.add(checkBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create predict button
        JButton predictButton = new JButton("Predict Condition");
        predictButton.addActionListener(this);

        // Create close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(symptomPanel, BorderLayout.CENTER);
        getContentPane().add(predictButton, BorderLayout.NORTH);
        getContentPane().add(closeButton, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if ("Predict Condition".equals(e.getActionCommand())) {
            List<String> selectedSymptoms = new ArrayList<>();
            for (JCheckBox checkBox : symptomCheckboxes) {
                if (checkBox.isSelected()) {
                    selectedSymptoms.add(checkBox.getText());
                }
            }
            if (selectedSymptoms.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one symptom.");
                return;
            }

            try {
                StringBuilder queryBuilder = new StringBuilder("SELECT med_condition FROM conditions WHERE ");
                for (int i = 0; i < selectedSymptoms.size(); i++) {
                    if (i > 0) {
                        queryBuilder.append(" AND ");
                    }
                    queryBuilder.append("symptom = ?");
                }

                PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
                for (int i = 0; i < selectedSymptoms.size(); i++) {
                    statement.setString(i + 1, selectedSymptoms.get(i));
                }

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    StringBuilder conditionBuilder = new StringBuilder("The predicted medical condition(s) based on selected symptoms:\n");
                    do {
                        conditionBuilder.append(resultSet.getString("med_condition")).append("\n");
                    } while (resultSet.next());
                    JOptionPane.showMessageDialog(this, conditionBuilder.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "No matching medical conditions found for selected symptoms.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if ("Close".equals(e.getActionCommand())) {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MedicalConditionPredictor predictor = new MedicalConditionPredictor();
            predictor.setVisible(true);
        });
    }
}
