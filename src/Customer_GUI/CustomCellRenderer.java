package Customer_GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    private int targetRow;
    private Color backgroundColor;

    public CustomCellRenderer(int targetRow, Color backgroundColor) {
        this.targetRow = targetRow;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check if the current cell is in the target row
        if (row == targetRow) {
            component.setBackground(backgroundColor);
            //component.setForeground(Color.WHITE);
        } else {
            // Reset the background color for other rows
            component.setBackground(table.getBackground());
        }

        return component;
    }
}
