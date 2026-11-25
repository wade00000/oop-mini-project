// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;   // explicit import: java.awt also has a List class

public class MyClassesFrame extends BaseFrame implements ActionListener {

    // These match the column sizes in tbl_classes
    private static final int MAX_CODE = 20;
    private static final int MAX_TITLE = 100;

    private final Student current;

    private DefaultListModel<ClassEntry> model = new DefaultListModel<>();
    private JList<ClassEntry> lstClasses = new JList<>(model);
    private JButton btnOpen, btnJoin, btnCreate, btnDrop, btnBack;

    public MyClassesFrame() {
        super("SU Feeds - My Classes");
        setSize(650, 450);
        setLocationRelativeTo(null);
        current = Session.getCurrentStudent();

        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.add(new JLabel("My classes this semester", SwingConstants.CENTER), BorderLayout.NORTH);

        lstClasses.setCellRenderer(new ItemRenderer());
        lstClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentPanel.add(new JScrollPane(lstClasses), BorderLayout.CENTER);

        btnOpen = new JButton("Open Topics");
        btnJoin = new JButton("Join Existing Class");
        btnCreate = new JButton("Create New Class");
        btnDrop = new JButton("Drop Class");
        btnBack = new JButton("Back");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.add(btnOpen);
        buttons.add(btnJoin);
        buttons.add(btnCreate);
        buttons.add(btnDrop);
        buttons.add(btnBack);
        contentPanel.add(buttons, BorderLayout.SOUTH);

        btnOpen.addActionListener(this);
        btnJoin.addActionListener(this);
        btnCreate.addActionListener(this);
        btnDrop.addActionListener(this);
        btnBack.addActionListener(this);

        loadClasses();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnOpen) {
            openTopics();
        } else if (e.getSource() == btnJoin) {
            joinExisting();
        } else if (e.getSource() == btnCreate) {
            createNew();
        } else if (e.getSource() == btnDrop) {
            dropSelected();
        } else if (e.getSource() == btnBack) {
            new DashboardFrame().setVisible(true);
            dispose();
        }
    }

    // Reloads the list from the database
    private void loadClasses() {
        try {
            List<ClassEntry> mine = EnrollmentDAO.findClassesByStudent(current.getId());
            model.clear();
            for (ClassEntry c : mine) {
                model.addElement(c);
            }
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void openTopics() {
        ClassEntry selected = lstClasses.getSelectedValue();   // null if nothing is selected
        if (selected == null) {
            Dialogs.warn(this, "Select a class first.", "No Class Selected");
            return;
        }
        new TopicsFrame(selected).setVisible(true);
        dispose();
    }

    // Join a class somebody already created
    private void joinExisting() {
        try {
            List<ClassEntry> mine = EnrollmentDAO.findClassesByStudent(current.getId());
            List<ClassEntry> available = new ArrayList<>();
            for (ClassEntry c : ClassDAO.findAll()) {
                if (!containsId(mine, c)) {
                    available.add(c);
                }
            }

            if (available.isEmpty()) {
                Dialogs.info(this, "There are no other classes to join. Use 'Create New Class' to add one.",
                        "Nothing To Join");
                return;
            }

            JComboBox<ClassEntry> combo = new JComboBox<>(available.toArray(new ClassEntry[0]));
            combo.setRenderer(new ItemRenderer());
            int choice = JOptionPane.showConfirmDialog(this, combo, "Join which class?",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice != JOptionPane.OK_OPTION) {
                return;
            }

            ClassEntry picked = (ClassEntry) combo.getSelectedItem();
            EnrollmentDAO.enroll(current.getId(), picked.getId());
            loadClasses();

        } catch (SQLIntegrityConstraintViolationException ex) {
            // Subclass first, or this block could never run
            Dialogs.warn(this, "You are already in that class.", "Already Enrolled");
            loadClasses();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    // Create a brand-new class and enroll in it straight away
    private void createNew() {
        JTextField txtCode = new JTextField(12);
        JTextField txtTitle = new JTextField(24);
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Class code:"));
        form.add(txtCode);
        form.add(new JLabel("Class title:"));
        form.add(txtTitle);

        int choice = JOptionPane.showConfirmDialog(this, form, "Create a new class",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String code = txtCode.getText().trim().toUpperCase();   // consistent codes: "bbit 1.2" -> "BBIT 1.2"
        String title = txtTitle.getText().trim();

        if (code.isEmpty() || title.isEmpty()) {
            Dialogs.warn(this, "Please fill in both the code and the title.", "Empty Fields");
            return;
        }
        if (code.length() > MAX_CODE || title.length() > MAX_TITLE) {
            Dialogs.warn(this, "Code can be at most " + MAX_CODE + " characters and title at most "
                    + MAX_TITLE + ".", "Too Long");
            return;
        }

        try {
            if (ClassDAO.findByCode(code) != null) {
                Dialogs.warn(this, "That class already exists. Use 'Join Existing Class' instead.",
                        "Class Exists");
                return;
            }

            ClassEntry created = new ClassEntry(code, title);
            ClassDAO.insert(created);                      // fills in created's id
            EnrollmentDAO.enroll(current.getId(), created.getId());
            loadClasses();

        } catch (SQLIntegrityConstraintViolationException ex) {
            Dialogs.warn(this, "That class code already exists.", "Class Exists");
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void dropSelected() {
        ClassEntry selected = lstClasses.getSelectedValue();
        if (selected == null) {
            Dialogs.warn(this, "Select a class first.", "No Class Selected");
            return;
        }
        if (!Dialogs.confirm(this, "Drop " + selected.getCode() + "? Its topics stay available to the other students.",
                "Drop Class")) {
            return;
        }

        try {
            EnrollmentDAO.drop(current.getId(), selected.getId());
            loadClasses();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    // Integer objects must be compared with equals(), not ==
    private boolean containsId(List<ClassEntry> list, ClassEntry target) {
        for (ClassEntry c : list) {
            if (c.getId().equals(target.getId())) {
                return true;
            }
        }
        return false;
    }
}