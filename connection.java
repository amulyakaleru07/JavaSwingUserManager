package sample2;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import sample2.SQLiteConnect;

public class connection extends JFrame {
    private static final long serialVersionUID = 1L;
    private SQLiteConnect db;

    private DefaultTableModel model;
    private JTextField txt_name, txt_password, txt_username, txt_role;
    private JTable table;

    public static void main(String args[]) {
        new connection();
    }

    public connection() {
        getContentPane().setFont(new Font("Arial", Font.BOLD, 13));
        setTitle("Backend Connections");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 450);

        db = new SQLiteConnect();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        getContentPane().add(mainPanel);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lbl_name = new JLabel("Name:");
        lbl_name.setFont(new Font("Arial", Font.BOLD, 15));
        txt_name = new JTextField(15);

        JLabel lbl_password = new JLabel("Password:");
        lbl_password.setFont(new Font("Arial", Font.BOLD, 15));
        txt_password = new JTextField(15);

        JLabel lbl_username = new JLabel("User Name:");
        lbl_username.setFont(new Font("Arial", Font.BOLD, 15));
        txt_username = new JTextField(15);

        JLabel lbl_role = new JLabel("Role:");
        lbl_role.setFont(new Font("Arial", Font.BOLD, 15));
        txt_role = new JTextField(15);

        panel.add(lbl_name); panel.add(txt_name);
        panel.add(lbl_password); panel.add(txt_password);
        panel.add(lbl_username); panel.add(txt_username);
        panel.add(lbl_role); panel.add(txt_role);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton saveBtn = new JButton("Save");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        saveBtn.setPreferredSize(updateBtn.getPreferredSize());
        deleteBtn.setPreferredSize(updateBtn.getPreferredSize());
        clearBtn.setPreferredSize(updateBtn.getPreferredSize());

        buttonPanel.add(saveBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);

        // Table Setup
        String[] columnNames = {"Name", "Password", "Username", "Role"};
        model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 150));

        // Add panels
        mainPanel.add(panel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Save (Insert)
        saveBtn.addActionListener(e -> {
            String name = txt_name.getText();
            String password = txt_password.getText();
            String username = txt_username.getText();
            String role = txt_role.getText();

            if (!name.isEmpty() && !password.isEmpty() && !username.isEmpty() && !role.isEmpty()) {
                db.insertUser(name, password, username, role);
                loadData();
                clearFields();
                JOptionPane.showMessageDialog(null, "User inserted.");
            } else {
                JOptionPane.showMessageDialog(null, "Please fill all fields.");
            }
        });

        // Update
        updateBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String oldUsername = model.getValueAt(selectedRow, 2).toString();

                String name = txt_name.getText();
                String password = txt_password.getText();
                String username = txt_username.getText();
                String role = txt_role.getText();

                db.updateUser(oldUsername, name, password, username, role);
                loadData();
                clearFields();
                JOptionPane.showMessageDialog(null, "User updated.");
            } else {
                JOptionPane.showMessageDialog(null, "Select a row to update.");
            }
        });

        // Delete
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String username = model.getValueAt(selectedRow, 2).toString();
                db.deleteUser(username);
                loadData();
                clearFields();
                JOptionPane.showMessageDialog(null, "User deleted.");
            } else {
                JOptionPane.showMessageDialog(null, "Select a row to delete.");
            }
        });

        // Clear fields
        clearBtn.addActionListener(e -> clearFields());

        // On row selection, populate text fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                txt_name.setText(model.getValueAt(selectedRow, 0).toString());
                txt_password.setText(model.getValueAt(selectedRow, 1).toString());
                txt_username.setText(model.getValueAt(selectedRow, 2).toString());
                txt_role.setText(model.getValueAt(selectedRow, 3).toString());
            }
        });

        loadData(); // Initial data load
        setVisible(true);
    }

    private void clearFields() {
        txt_name.setText("");
        txt_password.setText("");
        txt_username.setText("");
        txt_role.setText("");
    }

    public void loadData() {
        model.setRowCount(0); // Clear existing rows
        ResultSet rs = db.getAllUsers();
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String password = rs.getString("password");
                String username = rs.getString("username");
                String role = rs.getString("role");
                model.addRow(new Object[]{name, password, username, role});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
