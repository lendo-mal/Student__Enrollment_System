package myPackage;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.*;

public class STUDENT_ENROLLMENT_SYSTEM extends JFrame {

    public static final String FILE_NAME = "Student_Enrolled.txt";
    public static final String DELIMITER = "#";

    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;

    private JTextField fullName, email, finalGrade;
    private ArrayList<String> lines;

    public static void main(String[] args) {
        new STUDENT_ENROLLMENT_SYSTEM();
    }

    STUDENT_ENROLLMENT_SYSTEM() {

        // LABELS
        add(new JLabel("Student")).setBounds(20, 0, 50, 50);
        add(new JLabel("ENROLLMENT SYSTEM")).setBounds(20, 20, 200, 50);

        add(new JLabel("Full name")).setBounds(30, 56, 150, 50);
        add(new JLabel("Email")).setBounds(30, 106, 100, 50);
        add(new JLabel("Final Grade")).setBounds(30, 156, 150, 50);

        // TEXTFIELDS
        fullName = new JTextField();
        fullName.setBounds(30, 90, 370, 28);
        add(fullName);

        email = new JTextField();
        email.setBounds(30, 140, 370, 28);
        add(email);

        finalGrade = new JTextField();
        finalGrade.setBounds(30, 190, 150, 28);
        add(finalGrade);

        // BUTTONS
        JButton btnRegister = new JButton("Register");
        btnRegister.setBackground(new Color(0x0FF00));
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setBounds(30, 230, 85, 30);
        add(btnRegister);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(0xFFE856));
        btnUpdate.setForeground(Color.BLACK);
        btnUpdate.setBounds(130, 230, 85, 30);
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(255, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setBounds(230, 230, 85, 30);
        add(btnDelete);

        // TABLE
        String[] columns = {"Full Name", "Email", "Final Grade"};
        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 275, 380, 330);
        add(scrollPane);

        refreshTable();

        btnRegister.addActionListener(e -> registerStudent());

        btnUpdate.addActionListener(e -> updateStudent());

        btnDelete.addActionListener(e -> deleteStudent());

        // TABLE CLICK EVENT
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    fullName.setText(model.getValueAt(row, 0).toString());
                    email.setText(model.getValueAt(row, 1).toString());
                    finalGrade.setText(model.getValueAt(row, 2).toString());
                }
            }
        });

        setLayout(null);
        setTitle("ENROLLMENT SYSTEM");
        setVisible(true);
        setResizable(false);
        setSize(450, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    // REGISTER STUDENT
    private void registerStudent() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
          
            String name = fullName.getText();
            String mail = email.getText();
            String grade = finalGrade.getText();
          
            bw.write(name + DELIMITER + mail + DELIMITER + grade);
            bw.newLine();
            JOptionPane.showMessageDialog(this, "Student Enrolled Successfully");
            clearFields();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving: " + e.getMessage());
        }
        refreshTable();
    }
    // UPDATE STUDENT
    private void updateStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to update.");
            return;
        }
        lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                if (rowIndex == selectedRow) {
                    String updatedRecord =
                            fullName.getText() + DELIMITER +
                            email.getText() + DELIMITER +
                            finalGrade.getText();
                    lines.add(updatedRecord);
                } else {
                    lines.add(line);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String record : lines) {
                bw.write(record);
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Updated Successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating file: " + e.getMessage());
        }
        clearFields();
        refreshTable();
    }
    // DELETE STUDENT
    private void deleteStudent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;
        lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                if (rowIndex != selectedRow)
                    lines.add(line);
                rowIndex++;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String record : lines) {
                bw.write(record);
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
        JOptionPane.showMessageDialog(this, "Record Deleted!");
        clearFields();
        refreshTable();
    }
    // REFRESH TABLE
    private void refreshTable() {

        model.setRowCount(0);

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] row = line.split(DELIMITER);

                model.addRow(row);

            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, e);

        }
    }
    // CLEAR TEXTFIELDS
    private void clearFields() {

        fullName.setText("");
        email.setText("");
        finalGrade.setText("");

    }
}