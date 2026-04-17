import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private final ListItems listItems;

    public MainWindow() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        listItems = new ListItems();
        mainPanel.add(listItems.getJPanel(), "listItems");

        cardLayout.show(mainPanel, "listItems");

    }

    public JPanel getMainPanel(){
        return mainPanel;
    }

    public void showPage(String page){
        cardLayout.show(mainPanel, page);
    }
}
