// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;   // explicit import: java.awt also has a List class

public class TopicsFrame extends BaseFrame implements ActionListener {

    private static final int MAX_TITLE = 150;   // matches tbl_topics.title

    private final ClassEntry cls;
    private final Student current;

    private DefaultListModel<Topic> model = new DefaultListModel<>();
    private JList<Topic> lstTopics = new JList<>(model);
    private JButton btnAdd, btnEdit, btnDelete, btnFeedback, btnBack;

    public TopicsFrame(ClassEntry cls) {
        super("SU Feeds - Topics");
        setSize(650, 450);
        setLocationRelativeTo(null);
        this.cls = cls;
        this.current = Session.getCurrentStudent();

        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.add(new JLabel("Topics in " + cls.getCode() + " - " + cls.getTitle(),
                SwingConstants.CENTER), BorderLayout.NORTH);

        lstTopics.setCellRenderer(new ItemRenderer());
        lstTopics.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contentPanel.add(new JScrollPane(lstTopics), BorderLayout.CENTER);

        btnAdd = new JButton("Add Topic");
        btnEdit = new JButton("Edit My Topic");
        btnDelete = new JButton("Delete My Topic");
        btnFeedback = new JButton("View Feedback");
        btnBack = new JButton("Back");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnFeedback);
        buttons.add(btnBack);
        contentPanel.add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnFeedback.addActionListener(this);
        btnBack.addActionListener(this);

        loadTopics();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addTopic();
        } else if (e.getSource() == btnEdit) {
            editTopic();
        } else if (e.getSource() == btnDelete) {
            deleteTopic();
        } else if (e.getSource() == btnFeedback) {
            openFeedback();
        } else if (e.getSource() == btnBack) {
            new MyClassesFrame().setVisible(true);
            dispose();
        }
    }

    private void loadTopics() {
        try {
            List<Topic> topics = TopicDAO.findByClass(cls.getId());
            model.clear();
            for (Topic t : topics) {
                model.addElement(t);
            }
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void addTopic() {
        String title = Dialogs.promptText(this, "What topic did you learn?", "Add Topic", "");
        if (title == null) {
            return;   // user cancelled
        }
        if (title.isEmpty()) {
            Dialogs.warn(this, "A topic needs a title.", "Empty Title");
            return;
        }
        if (title.length() > MAX_TITLE) {
            Dialogs.warn(this, "Title can be at most " + MAX_TITLE + " characters.", "Too Long");
            return;
        }

        try {
            Topic t = new Topic(cls.getId(), current.getId(), title);
            TopicDAO.insert(t);
            loadTopics();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void editTopic() {
        Topic selected = lstTopics.getSelectedValue();
        if (!ownedByMe(selected, "edit")) {
            return;
        }

        String newTitle = Dialogs.promptText(this, "New title:", "Edit Topic", selected.getTitle());
        if (newTitle == null) {
            return;
        }
        if (newTitle.isEmpty() || newTitle.length() > MAX_TITLE) {
            Dialogs.warn(this, "A title must be between 1 and " + MAX_TITLE + " characters.", "Invalid Title");
            return;
        }

        try {
            selected.setTitle(newTitle);
            boolean done = TopicDAO.update(selected, current.getId());
            if (!done) {
                Dialogs.warn(this, "Could not update: the topic is not yours or no longer exists.",
                        "Not Updated");
            }
            loadTopics();   // always reload, so the list matches the database
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void deleteTopic() {
        Topic selected = lstTopics.getSelectedValue();
        if (!ownedByMe(selected, "delete")) {
            return;
        }
        if (!Dialogs.confirm(this, "Delete this topic and all the feedback on it?", "Delete Topic")) {
            return;
        }

        try {
            boolean done = TopicDAO.delete(selected.getId(), current.getId());
            if (!done) {
                Dialogs.warn(this, "Could not delete: the topic is not yours or no longer exists.",
                        "Not Deleted");
            }
            loadTopics();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void openFeedback() {
        Topic selected = lstTopics.getSelectedValue();
        if (selected == null) {
            Dialogs.warn(this, "Select a topic first.", "No Topic Selected");
            return;
        }
        new FeedbackFrame(cls, selected).setVisible(true);
        dispose();
    }

    // The GUI check gives a clear message; the WHERE student_id = ? in the DAO is the real enforcement.
    private boolean ownedByMe(Topic topic, String action) {
        if (topic == null) {
            Dialogs.warn(this, "Select a topic first.", "No Topic Selected");
            return false;
        }
        // Integer objects must be compared with equals(), not ==
        if (!topic.getStudentId().equals(current.getId())) {
            Dialogs.warn(this, "You can only " + action + " topics you added.", "Not Your Topic");
            return false;
        }
        return true;
    }
}