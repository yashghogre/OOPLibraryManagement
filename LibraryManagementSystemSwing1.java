import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LibraryManagementSystemSwing1 {
    private JFrame frame;
    private JTextField studentTextField, bookTextField, isbnTextField;
    private JTextArea displayArea;
    private ArrayList<String> issuedBooks;
    private ArrayList<String> returnedBooks;

    Connection con;
    Statement stmt;
    ResultSet res;

    public LibraryManagementSystemSwing1() {

        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement", "root", "root");
            stmt = con.createStatement();
        }catch(Exception e){
            e.printStackTrace();
        }

        frame = new JFrame("Library Management System");
        frame.getContentPane().setBackground(new Color(240, 240, 240));

        JLabel studentLabel = new JLabel("Name of Student");
        studentLabel.setForeground(new Color(50, 50, 50));
        studentTextField = new JTextField(20);

        JLabel bookLabel = new JLabel("Name of Book");
        bookLabel.setForeground(new Color(50, 50, 50));
        bookTextField = new JTextField(20);

        JLabel isbnLabel = new JLabel("Enter ISBN number");
        isbnLabel.setForeground(new Color(50, 50, 50));
        isbnTextField = new JTextField(20);

        JButton issueButton = new JButton("Issue");
        issueButton.setBackground(new Color(100, 200, 100));
        issueButton.setForeground(Color.white);
        issueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String student = studentTextField.getText();
                    String book = bookTextField.getText();
                    String isbn = isbnTextField.getText();
                    String message = "Book issued successfully - Student: " + student + ", Book: " + book + ", ISBN: " + isbn;
                    displayMessage(message);
                    issuedBooks.add(student);
                    stmt.executeUpdate("insert into libtable values('" + student + "', '" + book + "', '" + isbn + "')");
                    clearFields();
                }catch (Exception err){
                    err.printStackTrace();
                }
            }
        });

        JButton returnButton = new JButton("Return");
        returnButton.setBackground(new Color(200, 100, 100));
        returnButton.setForeground(Color.white);
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String student = studentTextField.getText();
                    String book = bookTextField.getText();
                    String isbn = isbnTextField.getText();
                    String message = "Book returned successfully - Student: " + student + ", Book: " + book + ", ISBN: " + isbn;
                    displayMessage(message);
                    returnedBooks.add(student);

                    stmt.executeUpdate("DELETE FROM libtable WHERE isbn = '"+isbn+"'");
                    clearFields();
                }catch(Exception err){
                    err.printStackTrace();
                }
            }
        });

        JButton displayButton = new JButton("Display");
        displayButton.setBackground(new Color(150, 150, 200));
        displayButton.setForeground(Color.white);
        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    displayIssuedReturnedStudents();

                    stmt.executeQuery("SELECT * FROM libtable");
                }catch (Exception err){
                    err.printStackTrace();
                }
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(200, 200, 100));
        clearButton.setForeground(Color.white);
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        displayArea = new JTextArea(10, 40);
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(230, 230, 255));
        JScrollPane scrollPane = new JScrollPane(displayArea);

        issuedBooks = new ArrayList<>();
        returnedBooks = new ArrayList<>();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(studentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(studentTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(bookLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(bookTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(isbnTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(issueButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(returnButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(displayButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(clearButton, gbc);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void displayIssuedReturnedStudents() {
        displayArea.setText("Issued Books:\n");
        for (String student : issuedBooks) {
            displayArea.append("- " + student + "\n");
        }

        displayArea.append("\nReturned Books:\n");
        for (String student : returnedBooks) {
            displayArea.append("- " + student + "\n");
        }
    }

    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    private void clearFields() {
        studentTextField.setText("");
        bookTextField.setText("");
        isbnTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryManagementSystemSwing1());
    }
}