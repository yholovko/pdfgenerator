
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        NourishInfo nourishInfo = new NourishInfo();

        try {
            nourishInfo.createPdf("nourish2.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
