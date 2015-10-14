import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.encoding.EncodingManager;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private float nextYPos;         //equals lastY + lineHeight

    public void createPdf(String outputFileName) throws IOException, COSVisitorException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(new PDRectangle(215.9f * Units.MM_TO_UNITS, 279.4f * Units.MM_TO_UNITS));
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        PDPageContentStream contentStream = new PDPageContentStream(document, page1);

        try {
            robotoLight = PDTrueTypeFont.loadTTF(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Light.ttf")));
            robotoBold = PDTrueTypeFont.loadTTF(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Bold.ttf")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage awtImage = ImageIO.read(new FileInputStream(new File("src/pdf-resourses//temp/Watkins_Logo_original.jpg")));
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            contentStream.drawXObject(ximage, 59.52f, rect.getHeight() - 60.00f - 42.51f, 60.00f, 60.00f);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        try {
            BufferedImage awtImage = ImageIO.read(new FileInputStream(new File("src/pdf-resourses//temp/mudras.jpg")));
            PDXObjectImage ximage = new PDPixelMap(document, awtImage);
            float scale = awtImage.getWidth() / 128.01f;
            float height = awtImage.getHeight() / scale;

            contentStream.drawXObject(ximage, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }

        drawMultiLineText("Watkins", 162.70f, rect.getHeight() - 56.97f, 248, page1, contentStream, robotoLight, 18, WATKINS_PRIMARY_COLOR, 1.2f * 18, 2, false);
        drawMultiLineText("Advance Information", 162.70f, rect.getHeight() - 56.97f - 1.2f * 18, 248, page1, contentStream, robotoLight, 18, WATKINS_PRIMARY_COLOR, 1.2f * 18, 2, false);
        drawMultiLineText("Mudras for Modern Life", 59.52f, rect.getHeight() - 139.18f, 351, page1, contentStream, robotoLight, 22, WATKINS_MAIN_COLOR, 1.2f * 22, 0f, false);
        drawMultiLineText("Boost your health, re-energize your life, enhance your yoga and deepen your meditation", 59.52f, nextYPos + ((1.2f * 22 - 1.2f * 13) / 2), 351, page1, contentStream, robotoLight, 13, WATKINS_PRIMARY_COLOR, 1.5f * 13, 0f, false); // y = rect.getHeight() - 139.18f - (line * 1.2f * 22) + 3 * 1.2f
        drawMultiLineText("By Swami Saradananda", 59.52f, nextYPos - 0.5f * 13, 351, page1, contentStream, robotoLight, 12, WATKINS_MAIN_COLOR, 1.2f * 12, 0f, false);

        drawMultiLineText("This new, definitive, fully illustrated guide to the ancient art of mudras provides a highly " +
                "practical and inspirational overview of how to use subtle yogic hand gestures to " +
                "revitalize every aspect of your life", 59.52f, nextYPos - 0.6f * 13, 351, page1, contentStream, robotoBold, 9, WATKINS_PRIMARY_COLOR, 1.6f * 9, 0f, false);

        drawMultiLineText("SALES POINTS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, WATKINS_PRIMARY_COLOR, 1.2f * 11, 0f, false);

        ArrayList<String> list = new ArrayList<>();
        list.add(0, String.valueOf(Character.toChars(EncodingManager.INSTANCE.getEncoding(COSName.WIN_ANSI_ENCODING).getCode("bullet"))) + " The first full-colour photographic book on the ancient, life-enhancing art of mudras");
        list.add(1, String.valueOf(Character.toChars(EncodingManager.INSTANCE.getEncoding(COSName.WIN_ANSI_ENCODING).getCode("bullet"))) + " Experienced yoga and meditation teacher Swami Saradananda makes complex, spiritual concepts clear, accessible and relevant to modern life");
        list.add(2, String.valueOf(Character.toChars(EncodingManager.INSTANCE.getEncoding(COSName.WIN_ANSI_ENCODING).getCode("bullet"))) + " Each chapter focuses on a key holistic benefit that will appeal to experienced MBS seekers and holistic health fans alike");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                drawMultiLineText(list.get(i), 59.52f, nextYPos - 3, 351, page1, contentStream, robotoLight, 9, WATKINS_SECONDARY_COLOR, 1.6f * 9, 0f, false);
            } else {
                drawMultiLineText(list.get(i), 59.52f, nextYPos, 351, page1, contentStream, robotoLight, 9, WATKINS_SECONDARY_COLOR, 1.6f * 9, 0f, false);
            }
        }

        drawMultiLineText("SYNOPSIS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, WATKINS_PRIMARY_COLOR, 1.2f * 11, 0f, false);

        String description = "\"Humorous and wise, gritty and real, Brett Moran is a spiritual gangsta who knows the score about transformation. In Wake the F**k Up he shares the tools and techniques he’s learnt on his journey so you can do the same. Whether you’re looking to overhaul your health and energy, achieve your goals, or overcome negative behaviours and patterns, Wake the F**k Up will show you how to:\n" +
                "Tap into the natural highs of life by using meditation and mindfulness to help you overcome negative thoughts and feelings before creating a vision for what you want to achieve.\n" +
                "Move from lost to alive by learning how to smash negative habits and re-engineering your energy through healthy lifestyle habits and a by creating a positive mind-set.\n" +
                "Be successful and happy no matter what life throws at you through simple gratitude practices and living more authentically.\n" +
                "Real-life stories throughout will inspire you to think big and achieve even bigger while tough questions will help you overcome negative conditioning and start living the life you want, every day becomes an epic adventure.\n" +
                "\"\"I'm a big fan of Brett's work. He speaks with an authenticity that inspires you to truly be yourself\"\"\n" +
                "Dr David Hamilton, Bestselling Author When you wake the f*ck up\"\n";
        String[] parts = description.split("\n");

        for (int i = 0; i < parts.length; i++) {
            if (i == 0) {
                drawMultiLineText(parts[i], 59.52f, nextYPos - 3, 351, page1, contentStream, robotoLight, 9, WATKINS_MAIN_COLOR, 1.6f * 9, 0f, true);
            } else {
                drawMultiLineText(parts[i], 59.52f, nextYPos, 351, page1, contentStream, robotoLight, 9, WATKINS_MAIN_COLOR, 1.6f * 9, 0f, true);
            }
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
     * @return Count of printed lines.
     * @throws java.io.IOException
     */
    private int drawMultiLineText(String text, float x, float y, int allowedWidth, PDPage page, PDPageContentStream contentStream,
                                  PDFont font, int fontSize, Color fontColor, float lineHeight, float charSpacing, boolean isFirstParagraph) throws IOException {

        java.util.List<String> lines = new ArrayList<>();

        String myLine = "";

        // get all words from the text
        // keep in mind that words are separated by spaces -> "Lorem ipsum!!!!:)" -> words are "Lorem" and "ipsum!!!!:)"
        String[] words = text.split(" ");
        for (String word : words) {

            if (!myLine.isEmpty()) {
                myLine += " ";
            }

            // test the width of the current line + the current word
            int size = (int) (fontSize * font.getStringWidth(myLine + word) / 1000);
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

        for (String line : lines) {
            contentStream.beginText();
            contentStream.appendRawCommands(String.valueOf(charSpacing) + " Tc\n");
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(fontColor);
            if (isFirstParagraph) {
                contentStream.moveTextPositionByAmount(x + 11.34f, y);
                isFirstParagraph = false;
            } else {
                contentStream.moveTextPositionByAmount(x, y);
            }
            contentStream.drawString(line);
            contentStream.endText();
            y -= lineHeight;
            nextYPos = y;
        }

        return lines.size();
    }
}
