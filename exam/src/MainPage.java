import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JDialog {
    private JTable readBookTable;
    private JButton buttonCancel;
    private JPanel panel;

    public MainPage(Frame owner, MainWindow mainWindow) {
        super(owner, "Таблица", true); // true = модальное окно

        panel = new JPanel(new BorderLayout());

        readBookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(readBookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        buttonCancel = new JButton("Закрыть");
        panel.add(buttonCancel, BorderLayout.SOUTH);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        add(panel);


        setSize(500, 300);
        setLocationRelativeTo(owner);

        refresh();
    }

    public void refresh() {
        try {
            String[] columnNames = {"Число N", "Время(сек)", "Получившееся число"};
            Object[][] data = new Object[СompletedGame.completedGame.size()][3];

            for (int i = 0; i < СompletedGame.completedGame.size(); i++) {
                Items u = СompletedGame.completedGame.get(i);
                data[i] = new Object[]{
                        u.getN(),
                        u.getTimeSeconds(),
                        u.getResultNumber()
                };
            }

            readBookTable.setModel(new DefaultTableModel(data, columnNames));
        } catch (Exception e) {
            e.printStackTrace(); // лучше не оставлять пустым
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
