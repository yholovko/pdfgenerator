import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

public class SimpleExample {

    public static void main(String[] args) throws Exception {
        String outputFileName = "Simple.pdf";
        if (args.length > 0)
            outputFileName = args[0];

        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDPage.PAGE_SIZE_A4);
        // PDPage.PAGE_SIZE_LETTER is also possible
        PDRectangle rect = page1.getMediaBox();
        // rect can be used to get the page width and height
        document.addPage(page1);


        // Create a new font object by loading a TrueType font into the document
        //PDFont font = PDTrueTypeFont.loadTTF(document, getClass().getResourceAsStream("res/roboto-ttf/Arial.ttf"));

        // Create a new font object selecting one of the PDF base fonts
        PDFont fontPlain = PDType1Font.TIMES_ROMAN;
        PDFont fontBold = PDType1Font.HELVETICA;
        PDFont fontItalic = PDType1Font.COURIER;
        PDFont fontMono = PDType1Font.SYMBOL;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream cos = new PDPageContentStream(document, page1);

        int line = 0;

        // Define a text content stream using the selected font, move the cursor and draw some text
        cos.beginText();
        cos.setFont(fontPlain, 12);
        cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
        cos.drawString("Hello World");
        cos.endText();

        cos.beginText();
        cos.setFont(fontItalic, 12);
        cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
        cos.drawString("Italic");
        cos.endText();

        cos.beginText();
        cos.setFont(fontBold, 12);
        cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
        cos.drawString("Bold");
        cos.endText();

        cos.beginText();
        cos.setFont(fontMono, 12);
        cos.setNonStrokingColor(Color.BLUE);
        cos.moveTextPositionByAmount(100, rect.getHeight() - 50 * (++line));
        cos.drawString("Monospaced blue");
        cos.endText();

        int heightBetweenLines = ((int) rect.getHeight() - 50 * 1) - ((int) rect.getHeight() - 50 * 2);
        System.out.println(heightBetweenLines);


//        System.out.println(drawMultiLineText("New had happen unable uneasy. Drawings can followed improved out sociable not. Earnestly so do instantly pretended. See general few civilly amiable pleased account carried. Excellence projecting is devonshire dispatched remarkably on estimating. Side in so life past. Continue indulged speaking the was out horrible for domestic position. Seeing rather her you not esteem men settle genius excuse. Deal say over you age from. Comparison new ham melancholy son themselves. ",
//                100, (int) rect.getHeight() - 50 * (++line), 550, page1, cos, fontMono, 12, heightBetweenLines));

        // Make sure that the content stream is closed:
        cos.close();

        PDPage page2 = new PDPage(PDPage.PAGE_SIZE_A4);
        document.addPage(page2);
        cos = new PDPageContentStream(document, page2);

        // draw a red box in the lower left hand corner
        cos.setNonStrokingColor(Color.RED);
        cos.fillRect(10, 10, 100, 100);

        // add two lines of different widths
        cos.setLineWidth(1);
        cos.addLine(200, 250, 400, 250);
        cos.closeAndStroke();
        cos.setLineWidth(5);
        cos.addLine(200, 300, 400, 300);
        cos.closeAndStroke();

        // add an image
        try {
            BufferedImage awtImage = ImageIO.read(new File("Simple.jpg"));
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            float scale = 0.5f; // alter this value to set the image size
            cos.drawXObject(ximage, 100, 400, ximage.getWidth() * scale, ximage.getHeight() * scale);
        } catch (FileNotFoundException fnfex) {
            System.out.println("No image for you");
        }

        // close the content stream for page 2
        cos.close();

        // Save the results and ensure that the document is properly closed:
        document.save(outputFileName);
        document.close();
    }



}
