import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class WatkinsInfo {
    public static final Color WATKINS_MAIN_COLOR = new Color(0x000000);
    public static final Color WATKINS_PRIMARY_COLOR = new Color(0x009999);
    public static final Color WATKINS_SECONDARY_COLOR = new Color(0x6A5F55);

    private PDFont robotoLight;
    private PDFont robotoBold;
    private PDFont robotoRegular;
    private float nextYPos;         //equals lastY + lineHeight

    public void createPdf(String outputFileName) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(new PDRectangle(215.9f * Units.MM_TO_UNITS, 279.4f * Units.MM_TO_UNITS));
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
            PDImageXObject ximage = PDImageXObject.createFromFile(new File("src/pdf-resourses//temp/Watkins_Logo_original.jpg"), document);
            contentStream.drawImage(ximage, 59.52f, rect.getHeight() - 60.00f - 42.51f, 60.00f, 60.00f);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        try {
            PDImageXObject ximage = PDImageXObject.createFromFile(new File("src/pdf-resourses//temp/mudras.jpg"), document);
            float scale = ximage.getWidth() / 128.01f;
            float height = ximage.getHeight() / scale;

            contentStream.drawImage(ximage, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        drawMultiLineText("Watkins", 162.70f, rect.getHeight() - 56.97f, 248, page1, contentStream, robotoLight, 18, WATKINS_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText("Advance Information", 162.70f, rect.getHeight() - 56.97f - 1.2f * 18, 248, page1, contentStream, robotoLight, 18, WATKINS_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText("Mudras for Modern Life", 59.52f, rect.getHeight() - 139.18f, 351, page1, contentStream, robotoLight, 22, WATKINS_MAIN_COLOR, 1.2f * 22, 0f, false, false);
        drawMultiLineText("Boost your health, re-energize your life, enhance your yoga and deepen your meditation", 59.52f, nextYPos + ((1.2f * 22 - 1.2f * 13) / 2), 351, page1, contentStream, robotoLight, 13, WATKINS_PRIMARY_COLOR, 1.5f * 13, 0f, false, false);
        drawMultiLineText("By Swami Saradananda", 59.52f, nextYPos - 0.5f * 13, 351, page1, contentStream, robotoLight, 12, WATKINS_MAIN_COLOR, 1.2f * 12, 0f, false, false);

        drawMultiLineText("This new, definitive, fully illustrated guide to the ancient art of mudras provides a highly " +
                "practical and inspirational overview of how to use subtle yogic hand gestures to " +
                "revitalize every aspect of your life", 59.52f, nextYPos - 0.6f * 13, 351, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.6f * 9, 0f, false, false);

        drawMultiLineText("SALES POINTS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, WATKINS_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);

        ArrayList<String> list = new ArrayList<>();
        list.add(0, "• The first full-colour photographic book on the ancient, life-enhancing art of mudras");
        list.add(1, "• Experienced yoga and meditation teacher Swami Saradananda makes complex, spiritual concepts clear, accessible and relevant to modern life");
        list.add(2, "• Each chapter focuses on a key holistic benefit that will appeal to experienced MBS seekers and holistic health fans alike");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                drawMultiLineText(list.get(i), 59.52f, nextYPos - 3, 351, page1, contentStream, robotoLight, 9, WATKINS_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            } else {
                drawMultiLineText(list.get(i), 59.52f, nextYPos, 351, page1, contentStream, robotoLight, 9, WATKINS_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            }
        }

        drawMultiLineText("SYNOPSIS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, WATKINS_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);

        String description = "In this new, beautifully presented guide to the ancient art of mudras – an often overlooked Eastern practice that involves making established hand gestures to direct subtle energy to boost health and wellbeing – readers will discover how to integrate more than 60 mudras into their daily life and/or yoga and meditation practice for increased vitality and inner peace.\n" +
                "After introductory chapters laying the foundation of mudras, the six central chapters show why and how to do the mudras themselves. Each chapter is dedicated to a different part of the hand and its corresponding element – fire (thumb), air (index finger), ether (middle finger), earth (ring finger), water (little finger) and mind (palm) – focusing on each element’s holistic benefits, whether boosting inner strength, relieving stress, enhancing creativity or increasing concentration. In addition, each mudra entry is enhanced with an accompanying chant, meditation, pranayama, asana, visualization, or personal report about the mudra's benefits.\n" +
                "The book then ends with a series of highly useful mudra routines for a range of health issues, both physical and emotional – from anxiety and chronic fatigue to arthritis and headaches. There’s genuinely something for everyone in this beautiful new book on the health-enhancing art of mudras.\n" +
                "\n";

        String[] parts = description.split("\n");

        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                drawMultiLineText(parts[i], 59.52f, nextYPos - 3, 351 - 11, page1, contentStream, robotoLight, 9, WATKINS_MAIN_COLOR, 1.6f * 9, 0f, false, false); //1st line without paragraph
            } else {
                drawMultiLineText(parts[i], 59.52f, nextYPos, 351 - 11, page1, contentStream, robotoLight, 9, WATKINS_MAIN_COLOR, 1.6f * 9, 0f, true, false);
            }
        }

        drawMultiLineText("© Watkins enquiries@watkinspublishing.com", 85.03f, rect.getHeight() - 746.36f, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, WATKINS_MAIN_COLOR, 1.2f * 8, 0f, false, false);
        drawMultiLineText("Angel Business Club 359 Goswell Rd London  EC1V 7JL Phone: 0207 323 2229 AI generated by Bibliocloud on 12 October 2015", 85.03f, nextYPos, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, WATKINS_MAIN_COLOR, 1.2f * 8, 0f, false, false);

        drawMultiLineText("ISBN: 9781780289984", 418.39f, rect.getHeight() - 268.15f, 147, page1, contentStream, robotoRegular, 9, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Published: 15 OCTOBER 2015", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Price: £12.99", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("CATEGORY", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String category = "(BIC) VXA\n" +
                "(BISAC) BODY, MIND & SPIRIT /\n" +
                "Inspiration & Personal Growth\n";
        String[] catParts = category.split("\n");
        for (String catPart : catParts) {
            drawMultiLineText(catPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("BINDING", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String binding = "Paperback / softback\n" +
                "With flaps";
        String[] bindingParts = binding.split("\n");
        for (String bindingPart : bindingParts) {
            drawMultiLineText(bindingPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("FORMAT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("235mm x 162mm", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("EXTENT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("160pp", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("ILLUSTRATIONS", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);

        String illustrations = "A combination of specially\n" +
                "commissioned photography and\n" +
                "evocative artwork";
        String[] illustrationsParts = illustrations.split("\n");
        for (String illustrationsPart : illustrationsParts) {
            drawMultiLineText(illustrationsPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("AUTHOR LOCATION", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("London", 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("About the author:", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        String aboutAuthor = "Swami Saradananda is an internationally renowned yoga and meditation teacher and the author of a number of books, including Chakra Meditation, The Power of Breath and The Essential Guide to Chakras. Having worked for almost 30 years with the International Sivananda Yoga Vedanta Centres as a senior teacher in New York, London and Delhi, she is now based in London, teaches yoga and meditation worldwide, and leads pilgrimages to India.";
        String[] aboutAuthorParts = aboutAuthor.split("\n");
        for (String aboutAuthorPart : aboutAuthorParts) {
            drawMultiLineText(aboutAuthorPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, WATKINS_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
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
            if (line.charAt(line.length()-1) == ' '){   //delete last empty symbol (for better right alignment)
                line = line.substring(0, line.length()-1);
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
