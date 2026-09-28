import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

public final class UITheme {
    public static final Color BG = new Color(248, 250, 252);
    public static final Color SIDEBAR = new Color(15, 23, 42);
    public static final Color PANEL = new Color(255, 255, 255);
    public static final Color PRIMARY = new Color(37, 99, 235);
    public static final Color PRIMARY_DARK = new Color(29, 78, 216);
    public static final Color SECONDARY = new Color(22, 163, 74);
    public static final Color ACCENT = new Color(99, 102, 241);
    public static final Color WARNING = new Color(245, 158, 11);
    public static final Color DANGER = new Color(220, 38, 38);
    public static final Color TEXT = new Color(15, 23, 42);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color SELECTED = new Color(239, 246, 255);

    private static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font UI_BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);

        private static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            new RoundedBorder(8, BORDER),
            BorderFactory.createEmptyBorder(5, 10, 5, 10));

    private UITheme() {
    }

    public static void install() {
        UIManager.put("Panel.background", BG);
        UIManager.put("Label.font", UI_FONT);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.font", UI_BOLD_FONT);
        UIManager.put("TextField.background", PANEL);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.font", UI_FONT);
        UIManager.put("TextField.border", FIELD_BORDER);
        UIManager.put("ComboBox.background", PANEL);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("ComboBox.font", UI_FONT);
        UIManager.put("Button.background", PANEL);
        UIManager.put("Button.foreground", TEXT);
        UIManager.put("Button.border", new RoundedBorder(10, BORDER));
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("OptionPane.messageForeground", TEXT);
        UIManager.put("OptionPane.messageFont", UI_FONT);
    }

    public static void stylePanel(JPanel panel) {
        panel.setBackground(BG);
    }

    public static void styleCard(JPanel panel) {
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(14, BORDER),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
    }

    public static JLabel createTitle(String text, int x, int y, int width) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(new Color(31, 41, 55));
        label.setBounds(x, y, width, 28);
        return label;
    }

    public static JLabel createLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(UI_BOLD_FONT);
        label.setForeground(TEXT);
        label.setBounds(x, y, width, height);
        return label;
    }

    public static void styleButton(JButton button) {
        styleButton(button, PRIMARY, PRIMARY_DARK);
    }

    public static void styleButton(JButton button, Color baseColor, Color hoverColor) {
        button.setFont(UI_BOLD_FONT);
        button.setUI(new BasicButtonUI());
        button.setBackground(baseColor);
        button.setForeground(buttonTextColor(baseColor));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, hoverColor.darker()),
            BorderFactory.createEmptyBorder(7, 11, 7, 11)));
        button.setBorderPainted(true);
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }

    private static Color buttonTextColor(Color background) {
        int brightness = (299 * background.getRed()
                + 587 * background.getGreen()
                + 114 * background.getBlue()) / 1000;
        return brightness >= 130 ? TEXT : Color.WHITE;
    }

    public static void styleTextField(JTextComponent textComponent) {
        textComponent.setBackground(PANEL);
        textComponent.setForeground(TEXT);
        textComponent.setBorder(FIELD_BORDER);
        textComponent.setFont(UI_FONT);
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setBackground(PANEL);
        textArea.setForeground(TEXT);
        textArea.setBorder(FIELD_BORDER);
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(PANEL);
        comboBox.setForeground(TEXT);
        comboBox.setBorder(FIELD_BORDER);
        comboBox.setFont(UI_FONT);
    }

    public static void styleTable(JTable table) {
        table.setFont(UI_FONT);
        table.setForeground(TEXT);
        table.setBackground(PANEL);
        table.setSelectionBackground(SELECTED);
        table.setSelectionForeground(TEXT);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(UI_BOLD_FONT);
        header.setForeground(MUTED);
        header.setBackground(BG);
        header.setReorderingAllowed(false);
    }

    private static final class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        private RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public Insets getBorderInsets(Component component) {
            return new Insets(1, 1, 1, 1);
        }

        @Override
        public void paintBorder(Component component, Graphics graphics, int x, int y,
                                 int width, int height) {
            graphics.setColor(color);
            graphics.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    public static void styleSearchField(JTextField field) {
        styleTextField(field);
        field.setFont(UI_FONT);
    }
}
