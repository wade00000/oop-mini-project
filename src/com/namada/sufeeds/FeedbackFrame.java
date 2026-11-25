// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;   // explicit import: java.awt also has a List class

public class FeedbackFrame extends BaseFrame implements ActionListener {

    private static final int MAX_FEEDBACK = 2000;   // a UI limit, well inside the TEXT column

    private final ClassEntry cls;     // kept so Back can return to this class's topics
    private final Topic topic;
    private final Student current;

    private DefaultListModel<Feedback> model = new DefaultListModel<>();
    private JList<Feedback> lstFeedback = new JList<>(model);
    private JTextArea txtDetail = new JTextArea();
    private JTextArea txtNew = new JTextArea(3, 40);
    private JButton btnPost, btnEdit, btnDelete, btnBack;

    public FeedbackFrame(ClassEntry cls, Topic topic) {
        super("SU Feeds - Feedback");
        setSize(700, 580);
        setLocationRelativeTo(null);
        this.cls = cls;
        this.topic = topic;
        this.current = Session.getCurrentStudent();

        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.add(new JLabel("Feedback on: " + topic.getTitle(), SwingConstants.CENTER),
                BorderLayout.NORTH);

        // Middle: the list of feedback on top, the full text of the selected item underneath
        lstFeedback.setCellRenderer(new ItemRenderer());
        lstFeedback.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtDetail.setEditable(false);
        txtDetail.setLineWrap(true);
        txtDetail.setWrapStyleWord(true);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(lstFeedback), new JScrollPane(txtDetail));
        split.setResizeWeight(0.55);
        contentPanel.add(split, BorderLayout.CENTER);

        // ListSelectionListener has a single method, so a lambda can stand in for the class
        lstFeedback.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showDetail();
            }
        });

        // Bottom: box for a new post, then the buttons
        txtNew.setLineWrap(true);
        txtNew.setWrapStyleWord(true);
        JScrollPane newScroll = new JScrollPane(txtNew);
        newScroll.setBorder(BorderFactory.createTitledBorder("Write new feedback"));

        btnPost = new JButton("Post Feedback");
        btnEdit = new JButton("Edit My Feedback");
        btnDelete = new JButton("Delete My Feedback");
        btnBack = new JButton("Back");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttons.add(btnPost);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnBack);

        JPanel bottom = new JPanel(new BorderLayout(0, 5));
        bottom.add(newScroll, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);
        contentPanel.add(bottom, BorderLayout.SOUTH);

        btnPost.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        btnBack.addActionListener(this);

        loadFeedback();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPost) {
            postFeedback();
        } else if (e.getSource() == btnEdit) {
            editFeedback();
        } else if (e.getSource() == btnDelete) {
            deleteFeedback();
        } else if (e.getSource() == btnBack) {
            new TopicsFrame(cls).setVisible(true);
            dispose();
        }
    }

    private void loadFeedback() {
        try {
            List<Feedback> items = FeedbackDAO.findByTopic(topic.getId());
            model.clear();   // also clears the selection, which clears the detail box
            for (Feedback f : items) {
                model.addElement(f);
            }
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    // Shows the full text of whatever is selected
    private void showDetail() {
        Feedback f = lstFeedback.getSelectedValue();
        if (f == null) {
            txtDetail.setText("");
            return;
        }
        String who = f.getStudentId().equals(current.getId()) ? "You" : "Another student";
        txtDetail.setText(who + " wrote on " + ItemRenderer.formatDate(f.getCreatedAt())
                + ":\n\n" + f.getContent());
        txtDetail.setCaretPosition(0);   // scroll back to the top of long text
    }

    private void postFeedback() {
        String text = txtNew.getText().trim();
        if (text.isEmpty()) {
            Dialogs.warn(this, "Write something before posting.", "Empty Feedback");
            return;
        }
        if (text.length() > MAX_FEEDBACK) {
            Dialogs.warn(this, "Feedback can be at most " + MAX_FEEDBACK + " characters. Yours is "
                    + text.length() + ".", "Too Long");
            return;
        }

        try {
            Feedback f = new Feedback(topic.getId(), current.getId(), text);
            FeedbackDAO.insert(f);
            txtNew.setText("");
            loadFeedback();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void editFeedback() {
        Feedback selected = lstFeedback.getSelectedValue();
        if (!writtenByMe(selected, "edit")) {
            return;
        }

        String newText = Dialogs.promptParagraph(this, "Edit your feedback", selected.getContent());
        if (newText == null) {
            return;   // cancelled
        }
        if (newText.isEmpty() || newText.length() > MAX_FEEDBACK) {
            Dialogs.warn(this, "Feedback must be between 1 and " + MAX_FEEDBACK + " characters.",
                    "Invalid Feedback");
            return;
        }

        try {
            boolean done = FeedbackDAO.update(selected.getId(), newText, current.getId());
            if (!done) {
                Dialogs.warn(this, "Could not update: this feedback is not yours or no longer exists.",
                        "Not Updated");
            }
            loadFeedback();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    private void deleteFeedback() {
        Feedback selected = lstFeedback.getSelectedValue();
        if (!writtenByMe(selected, "delete")) {
            return;
        }
        if (!Dialogs.confirm(this, "Delete this feedback?", "Delete Feedback")) {
            return;
        }

        try {
            boolean done = FeedbackDAO.delete(selected.getId(), current.getId());
            if (!done) {
                Dialogs.warn(this, "Could not delete: this feedback is not yours or no longer exists.",
                        "Not Deleted");
            }
            loadFeedback();
        } catch (SQLException ex) {
            Dialogs.dbError(this, ex);
        }
    }

    // The GUI check gives a clear message; the WHERE student_id = ? in the DAO is the real enforcement.
    private boolean writtenByMe(Feedback feedback, String action) {
        if (feedback == null) {
            Dialogs.warn(this, "Select a piece of feedback first.", "Nothing Selected");
            return false;
        }
        // Integer objects must be compared with equals(), not ==
        if (!feedback.getStudentId().equals(current.getId())) {
            Dialogs.warn(this, "You can only " + action + " feedback you wrote.", "Not Your Feedback");
            return false;
        }
        return true;
    }
}