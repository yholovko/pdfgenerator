
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        WatkinsInfo watkinsInfo = new WatkinsInfo();

        try {
            watkinsInfo.createPdf("watkins.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
