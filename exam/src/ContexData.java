import java.util.ArrayList;

public class ContexData {
    public static final ArrayList<Items> items = new ArrayList<>();

    static {
        items.add(new Items(10, 0.012, "55"));
        items.add(new Items(100, 0.184, "5050"));
        items.add(new Items(1000, 2.731, "500500"));
    }
}