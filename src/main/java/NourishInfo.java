import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.sanselan.ImageReadException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class NourishInfo {
    public static final Color NOURISH_MAIN_COLOR = new Color(0x000000);
    public static final Color NOURISH_PRIMARY_COLOR = new Color(0xCD1F20);
    public static final Color NOURISH_SECONDARY_COLOR = new Color(0x6A5F55);

    private PDFont robotoLight;
    private PDFont robotoBold;
    private PDFont robotoRegular;
    private float nextYPos;         //equals lastY + lineHeight

    public void createPdf(String outputFileName) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        PDPageContentStream contentStream = new PDPageContentStream(document, page1);

        try {
            robotoLight = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Light.ttf")));
            robotoBold = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Bold.ttf")));
            robotoRegular = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Regular.ttf")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PDImageXObject ximage = PDImageXObject.createFromFile(new File("src/pdf-resourses/temp/Bowl_and_spoon_original.jpg"), document);
            contentStream.drawImage(ximage, 59.52f, rect.getHeight() - 60.00f - 42.51f, 60.00f, 60.00f);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        try {
            JPEGReader reader = new JPEGReader();
            BufferedImage sourceImg;

            sourceImg = reader.readImage(new File("src/pdf-resourses/temp/the_right_bite.jpg"));

            PDImageXObject ximage = JPEGFactory.createFromImage(document, sourceImg);
            float scale = ximage.getWidth() / 128.01f;
            float height = ximage.getHeight() / scale;

            contentStream.drawImage(ximage, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);
        } catch (FileNotFoundException | NullPointerException | ImageReadException e) {
            e.printStackTrace();
        }

        drawMultiLineText("Nourish", 162.70f, rect.getHeight() - 56.97f, 248, page1, contentStream, robotoLight, 18, NOURISH_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText("Advance Information", 162.70f, rect.getHeight() - 56.97f - 1.2f * 18, 248, page1, contentStream, robotoLight, 18, NOURISH_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText("The Right Bite", 59.52f, rect.getHeight() - 139.18f, 351, page1, contentStream, robotoLight, 22, NOURISH_MAIN_COLOR, 1.2f * 22, 0f, false, false);
        drawMultiLineText("Smart Food Choices for Eating On The Go", 59.52f, nextYPos + ((1.2f * 22 - 1.2f * 13) / 2), 351, page1, contentStream, robotoLight, 13, NOURISH_PRIMARY_COLOR, 1.5f * 13, 0f, false, false);
        drawMultiLineText("By Jackie Lynch", 59.52f, nextYPos - 0.5f * 13, 351, page1, contentStream, robotoLight, 12, NOURISH_MAIN_COLOR, 1.2f * 12, 0f, false, false);

        drawMultiLineText("A practical guide to help people navigate the minefields of everyday eating and make healthy choices when nutritious food is not easily available – such as in coffee shops, office lunches or the cinema", 59.52f, nextYPos - 0.6f * 13, 351, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.6f * 9, 0f, false, false);

        drawMultiLineText("SALES POINTS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, NOURISH_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);

        ArrayList<String> list = new ArrayList<>();
        list.add(0, "• A unique book that provides a hugely useful resource – in a handy, accessible and inexpensive format");
        list.add(1, "• Jackie Lynch is a hugely experienced nutritional therapist who is the Chairman of the Institute of Optimum Nutrition, runs the WellWellWell clinics in Notting Hill and South Kensington, and runs clinics in the City for Nuffield Health");
        list.add(2, "• Broad-based lifestyle chapters provide extensive possibilities for publicity and marketing material, tying into public health campaigns, interest groups and media outlets");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                drawMultiLineText(list.get(i), 59.52f, nextYPos - 3, 351, page1, contentStream, robotoLight, 9, NOURISH_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            } else {
                drawMultiLineText(list.get(i), 59.52f, nextYPos, 351, page1, contentStream, robotoLight, 9, NOURISH_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            }
        }

        drawMultiLineText("SYNOPSIS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, NOURISH_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);

        String description = "It’s easy to follow a healthy diet when you’re in control of your shopping list and the contents of your fridge. But as soon as you step outside the front door, it can get a lot more complicated. Walk into a coffee shop, a bar or the cinema, and making the right decision can be a lot more challenging and confusing. The Right Bite is here to help – with accessible, practical advice for all those everyday occasions, you can make the smart choice even when healthy options are limited.\n" +
                "\n" +
                "Each chapter focuses on a different eating environment – from Breakfast on the Go to Working Lunches, Takeaway Food, Pubs, Picnics, Barbeques and the Cinema. For each situation The Right Bite then explores the type of foods likely to be available and compares them, explaining the main health pitfalls and highlighting top picks. A ham and cheese croissant is a better option in a coffee shop than a skinny muffin for example! The Right Bite explains why, providing useful insights with a down-to-earth approach. Packed with design features and small enough to slip in your handbag, this is the one-stop guide for anyone wanting to eat healthily in the real world.";

        String[] parts = description.split("\n");

        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                drawMultiLineText(parts[i], 59.52f, nextYPos - 3, 351 - 11, page1, contentStream, robotoLight, 9, NOURISH_MAIN_COLOR, 1.6f * 9, 0f, false, false); //1st line without paragraph
            } else {
                drawMultiLineText(parts[i], 59.52f, nextYPos, 351 - 11, page1, contentStream, robotoLight, 9, NOURISH_MAIN_COLOR, 1.6f * 9, 0f, true, false);
            }
        }

        drawMultiLineText("© Nourish enquiries@watkinspublishing.com ", 85.03f, rect.getHeight() - 792.47f, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, NOURISH_MAIN_COLOR, 1.2f * 8, 0f, false, false);
        drawMultiLineText("Angel Business Club 359 Goswell Rd London  EC1V 7JL Phone: 0207 323 2229 AI generated by Bibliocloud on 12 October 2015", 85.03f, nextYPos, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, NOURISH_MAIN_COLOR, 1.2f * 8, 0f, false, false);

        drawMultiLineText("ISBN: 9781848997301", 418.39f, rect.getHeight() - 268.15f, 147, page1, contentStream, robotoRegular, 9, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Published: 17 MARCH 2016", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Price: £6.99", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("CATEGORY", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String category = "(BIC) VFMD\n" +
                "(BISAC) HEALTH & FITNESS / Nutrition\n";
        String[] catParts = category.split("\n");
        for (String catPart : catParts) {
            drawMultiLineText(catPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("BINDING", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String binding = "Paperback / softback\n" +
                "Mass market (rack) paperback";
        String[] bindingParts = binding.split("\n");
        for (String bindingPart : bindingParts) {
            drawMultiLineText(bindingPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("FORMAT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("170mm x 140mm", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("EXTENT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("144pp", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("ILLUSTRATIONS", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String illustrations = "None";
        String[] illustrationsParts = illustrations.split("\n");
        for (String illustrationsPart : illustrationsParts) {
            drawMultiLineText(illustrationsPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("AUTHOR LOCATION", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("London", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("About the author:", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        String aboutAuthor = "Jackie Lynch is a qualified nutritionist and runs the WellWellWell clinics in Notting Hill and South Kensington. She develops corporate wellness programmes for companies such as JP Morgan, Expedia.com and Harper Collins, and provides food consultancy for brands including Tetleys. Jackie's advice features regularly in national newspapers and magazines such as the Mail on Sunday, Sunday Mirror, Marie Claire and Vogue. She is proud to be the Chairman of the Board of Trustees at the Centre for Optimum Nutrition.";
        String[] aboutAuthorParts = aboutAuthor.split("\n");
        for (String aboutAuthorPart : aboutAuthorParts) {
            drawMultiLineText(aboutAuthorPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        contentStream.close();
        document.save(outputFileName);
        document.close();
    }

    /**
     * @param text             The text to write on the page.
     * @param x                The position on the x-axis.
     * @param y                The position on the y-axis.
     * @param allowedWidth     The maximum allowed width(px) of the whole text (e.g. the width of the page - a defined margin).
     * @param page             The page for the text.
     * @param contentStream    The content stream to set the text properties and write the text.
     * @param font             The font used to write the text.
     * @param fontSize         The font size used to write the text.
     * @param fontColor        The font color used to write the text.
     * @param lineHeight       The line height of the font (typically 1.2 * fontSize or 1.5 * fontSize).
     * @param charSpacing      The value of character spacing (0 - default value).
     * @param isFirstParagraph Put the paragraph for the first line.
     * @param isRightAlignment Draw line using right alignment.
     * @return Count of printed lines.
     * @throws java.io.IOException
     */
    private int drawMultiLineText(String text, float x, float y, int allowedWidth, PDPage page, PDPageContentStream contentStream,
                                  PDFont font, int fontSize, Color fontColor, float lineHeight, float charSpacing, boolean isFirstParagraph, boolean isRightAlignment) throws IOException {

        java.util.List<String> lines = new ArrayList<>();

        String myLine = "";
        boolean isFirstParagraphTemp = isFirstParagraph;

        // get all words from the text
        // keep in mind that words are separated by spaces -> "Lorem ipsum!!!!:)" -> words are "Lorem" and "ipsum!!!!:)"
        String[] words = text.split(" ");
        for (String word : words) {

            if (!myLine.isEmpty()) {
                myLine += " ";
            }

            // test the width of the current line + the current word
            int size;
            if (isFirstParagraph) {
                size = (int) ((fontSize * font.getStringWidth(myLine + word) / 1000) + 11.33f);
                isFirstParagraph = false;
            } else {
                size = (int) (fontSize * font.getStringWidth(myLine + word) / 1000);
            }
            if (size > allowedWidth) {
                // if the line would be too long with the current word, add the line without the current word
                lines.add(myLine);

                // and start a new line with the current word
                myLine = word;
            } else {
                // if the current line + the current word would fit, add the current word to the line
                myLine += word;
            }
        }
        // add the rest to lines
        lines.add(myLine);

        isFirstParagraph = isFirstParagraphTemp;

        for (String line : lines) {
            if (line.length() !=0 && line.charAt(line.length() - 1) == ' ') {   //delete last empty symbol (for better right alignment)
                line = line.substring(0, line.length() - 1);
            }
            contentStream.beginText();
            contentStream.appendRawCommands(String.valueOf(charSpacing) + " Tc\n");
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(fontColor);
            if (isFirstParagraph) {
                contentStream.newLineAtOffset(x + 11.33f, y);
                isFirstParagraph = false;
            } else if (isRightAlignment) {
                float lineWidth = (fontSize * font.getStringWidth(line) / 1000);
                contentStream.newLineAtOffset(x + (allowedWidth - lineWidth), y);
            } else {
                contentStream.newLineAtOffset(x, y);
            }

            contentStream.showText(line);
            contentStream.endText();
            y -= lineHeight;
            nextYPos = y;
        }

        return lines.size();
    }
}
