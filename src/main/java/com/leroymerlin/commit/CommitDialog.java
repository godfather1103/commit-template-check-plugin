package com.leroymerlin.commit;

import com.godfather1103.util.StringUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Damien Arrachequesne
 */
public class CommitDialog extends DialogWrapper {

    private final CommitPanel panel;

    CommitDialog(@Nullable Project project) {
        super(project);
        panel = new CommitPanel(project);
        setTitle("Commit");
        setOKButtonText("OK");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel.getMainPanel();
    }

    String getCommitMessage() {
        return String.format("%s(%s): %s%n%n%s%s%s",
                panel.getChangeType(),
                panel.getChangeScope(),
                panel.getShortDescription(),
                getLongDescription(),
                getBreakingChanges(),
                getClosedIssues());
    }

    private String getLongDescription() {
        return breakLines(panel.getLongDescription(), 100);
    }

    private String getBreakingChanges() {
        if (StringUtils.isEmpty(panel.getBreakingChanges())) {
            return "";
        }
        return String.format("%n%n%s", "BREAKING CHANGE: " + panel.getBreakingChanges());
    }

    private String getClosedIssues() {
        if (StringUtils.isEmpty(panel.getClosedIssues())) {
            return "";
        }
        return String.format("%n%n%s", "Closes " + panel.getClosedIssues());
    }

    private static String breakLines(String input, int maxLineLength) {
        String[] tokens = input.split(" ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLength = 0;
        for (int i = 0; i < tokens.length; i++) {
            String word = tokens[i];

            boolean shouldAddNewLine = lineLength + (" " + word).length() > maxLineLength;
            if (shouldAddNewLine) {
                if (i > 0) {
                    output.append(System.lineSeparator());
                }
                lineLength = 0;
            }
            boolean shouldAddSpace = i < tokens.length - 1 &&
                    (lineLength + (word + " ").length() + tokens[i + 1].length() <= maxLineLength);
            if (shouldAddSpace) {
                word += " ";
            }
            output.append(word);
            lineLength += word.length();
        }
        return output.toString();
    }
}
