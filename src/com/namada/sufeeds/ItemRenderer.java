// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Controls how objects are DISPLAYED in a JList or JComboBox, so the models' toString()
// methods can stay as debugging output.
public class ItemRenderer extends DefaultListCellRenderer {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
    private static final int PREVIEW_LENGTH = 70;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        String text = "";

        if (value instanceof ClassEntry) {
            ClassEntry c = (ClassEntry) value;
            text = c.getCode() + " - " + c.getTitle();
        } else if (value instanceof Topic) {
            Topic t = (Topic) value;
            text = t.getTitle() + "   (" + formatDate(t.getCreatedAt()) + ")";
        } else if (value instanceof Feedback) {
            Feedback f = (Feedback) value;
            text = formatDate(f.getCreatedAt()) + ":  " + preview(f.getContent());
        } else if (value != null) {
            text = value.toString();
        }

        // Let the parent class do the drawing (selection colours etc.) with our text
        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
    }

    public static String formatDate(LocalDateTime when) {
        return when == null ? "" : when.format(FORMAT);
    }

    // First line-ish of a long text, so one list row stays one line
    private static String preview(String content) {
        if (content == null) {
            return "";
        }
        String oneLine = content.replace('\n', ' ').replace('\r', ' ').trim();
        if (oneLine.length() > PREVIEW_LENGTH) {
            return oneLine.substring(0, PREVIEW_LENGTH) + "...";
        }
        return oneLine;
    }
}