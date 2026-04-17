import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ListItems {
    private final JPanel panel;
    private final JTable tableItems;
    private final DefaultTableModel model;

    private final JTextField fieldN;
    private final JTextField fieldTime;
    private final JTextField fieldResult;

    private final JButton btnAdd;
    private final JButton btnDelete;
    private final JButton btnClear;

    public ListItems() {
        panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Результаты производительности");
        title.setFont(title.getFont().deriveFont(Font.BOLD, title.getFont().getSize() + 4f));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Число N", "Время (сек)", "Получившееся число"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };

        tableItems = new JTable(model);
        tableItems.setFillsViewportHeight(true);
        tableItems.setRowHeight(Math.max(22, tableItems.getRowHeight()));
        tableItems.getTableHeader().setReorderingAllowed(false);

        // Валидация времени при редактировании в таблице
        JTextField timeEditorField = new JTextField();
        installNonNegativeNumberFilter(timeEditorField, true);
        DefaultCellEditor timeEditor = new DefaultCellEditor(timeEditorField) {
            @Override
            public boolean stopCellEditing() {
                String text = timeEditorField.getText().trim();
                if (!isValidNonNegativeTime(text)) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(
                            panel,
                            "Поле «Время (сек)» должно быть непустым и неотрицательным числом.",
                            "Некорректное время",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return false;
                }
                return super.stopCellEditing();
            }
        };
        tableItems.getColumnModel().getColumn(1).setCellEditor(timeEditor);

        JScrollPane scrollPane = new JScrollPane(tableItems);
        panel.add(scrollPane, BorderLayout.CENTER);

        fieldN = new JTextField(10);
        fieldTime = new JTextField(10);
        fieldResult = new JTextField(18);

        installNonNegativeNumberFilter(fieldN, false);
        installNonNegativeNumberFilter(fieldTime, true);

        btnAdd = new JButton("Добавить");
        btnDelete = new JButton("Удалить");
        btnClear = new JButton("Очистить");

        JPanel bottom = new JPanel(new BorderLayout(12, 12));
        bottom.add(buildInputPanel(), BorderLayout.CENTER);
        bottom.add(buildButtonsPanel(), BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(this::onAdd);
        btnDelete.addActionListener(this::onDelete);
        btnClear.addActionListener(this::onClear);

        installKeyBindings();

        refreshFromContext();
    }

    private JPanel buildInputPanel() {
        JPanel inputs = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 6, 10);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        inputs.add(new JLabel("Число N"), c);
        c.gridx = 1;
        inputs.add(fieldN, c);

        c.gridx = 0; c.gridy = 1;
        inputs.add(new JLabel("Время (сек)"), c);
        c.gridx = 1;
        inputs.add(fieldTime, c);

        c.gridx = 0; c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 10);
        inputs.add(new JLabel("Получившееся число"), c);
        c.gridx = 1;
        inputs.add(fieldResult, c);

        return inputs;
    }

    private JPanel buildButtonsPanel() {
        JPanel buttons = new JPanel(new GridLayout(3, 1, 8, 8));
        buttons.add(btnAdd);
        buttons.add(btnDelete);
        buttons.add(btnClear);
        return buttons;
    }

    private void onAdd(ActionEvent e) {
        String nText = fieldN.getText().trim();
        String timeText = fieldTime.getText().trim();
        String resultText = fieldResult.getText().trim();

        if (!isValidNonNegativeTime(timeText)) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(
                    panel,
                    "Поле «Время (сек)» должно быть непустым и неотрицательным числом.",
                    "Некорректное время",
                    JOptionPane.WARNING_MESSAGE
            );
            fieldTime.requestFocusInWindow();
            return;
        }

        Integer n = null;
        if (!nText.isEmpty()) {
            try {
                n = Integer.parseInt(nText);
            } catch (NumberFormatException ex) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(
                        panel,
                        "Поле «Число N» должно быть целым числом (или пустым).",
                        "Некорректное N",
                        JOptionPane.WARNING_MESSAGE
                );
                fieldN.requestFocusInWindow();
                return;
            }
        }

        double time = Double.parseDouble(timeText.replace(',', '.'));

        Items item = new Items(n, time, resultText.isEmpty() ? null : resultText);
        ContexData.items.add(item);
        model.addRow(new Object[]{item.getN(), item.getTimeSeconds(), item.getResultNumber()});

        fieldN.setText("");
        fieldTime.setText("");
        fieldResult.setText("");
        fieldN.requestFocusInWindow();
    }

    private void onDelete(ActionEvent e) {
        int row = tableItems.getSelectedRow();
        if (row < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(
                    panel,
                    "Выберите строку для удаления.",
                    "Нет выбора",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int modelRow = tableItems.convertRowIndexToModel(row);
        ContexData.items.remove(modelRow);
        model.removeRow(modelRow);
    }

    private void onClear(ActionEvent e) {
        int row = tableItems.getSelectedRow();
        if (row < 0) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(
                    panel,
                    "Выберите строку, которую нужно очистить.",
                    "Нет выбора",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int modelRow = tableItems.convertRowIndexToModel(row);
        model.setValueAt(null, modelRow, 0);
        model.setValueAt(null, modelRow, 1);
        model.setValueAt(null, modelRow, 2);

        Items item = ContexData.items.get(modelRow);
        item.setN(null);
        item.setTimeSeconds(null);
        item.setResultNumber(null);
    }

    private void installKeyBindings() {
        InputMap im = panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = panel.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "addRow");
        am.put("addRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAdd.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        am.put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnDelete.doClick();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "clearRow");
        am.put("clearRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnClear.doClick();
            }
        });
    }

    private void refreshFromContext() {
        model.setRowCount(0);
        for (Items item : ContexData.items) {
            model.addRow(new Object[]{item.getN(), item.getTimeSeconds(), item.getResultNumber()});
        }
    }

    private static boolean isValidNonNegativeTime(String text) {
        if (text == null) return false;
        String trimmed = text.trim();
        if (trimmed.isEmpty()) return false;
        try {
            double v = Double.parseDouble(trimmed.replace(',', '.'));
            return v >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void installNonNegativeNumberFilter(JTextField field, boolean allowDecimal) {
        AbstractDocument doc = (AbstractDocument) field.getDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }

                String current = fb.getDocument().getText(0, fb.getDocument().getLength());
                String next = new StringBuilder(current).replace(offset, offset + length, text).toString();

                if (next.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }

                if (allowDecimal) {
                    if (next.matches("\\d*([\\.,]\\d*)?")) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    if (next.matches("\\d*")) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
    }

    public JPanel getJPanel(){
        return panel;
    }
}
