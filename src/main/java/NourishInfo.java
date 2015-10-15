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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NourishInfo {
    public static final Color NOURISH_MAIN_COLOR = new Color(0x000000);
    public static final Color NOURISH_PRIMARY_COLOR = new Color(0xCD1F20);
    public static final Color NOURISH_SECONDARY_COLOR = new Color(0x6A5F55);

    private PDFont robotoLight;
    private PDFont robotoBold;
    private PDFont robotoRegular;
    private float nextYPos;         //equals lastY + lineHeight

    /**
     * @param outputFileName A filename with the file extension ("file1.pdf")
     * @param info           An object with all the fields filled in.
     * @throws IOException
     */
    public void createPdf(String outputFileName, AditionalInformation info) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        PDPageContentStream contentStream = new PDPageContentStream(document, page1);
        JPEGReader jpegReader = new JPEGReader();
        FileDownloader fileDownloader = new FileDownloader();
        String coverPictureFilename;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMM yyyy", Locale.US);

        try {
            robotoLight = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Light.ttf")));
            robotoBold = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Bold.ttf")));
            robotoRegular = PDType0Font.load(document, new FileInputStream(new File("src/pdf-resourses/roboto-ttf/Roboto-Regular.ttf")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileDownloader.saveFile(Constants.NOURISH_LOGO_FILE_NAME + FileDownloader.getFileExtension(Constants.NOURISH_LOGO_URL), new URL(Constants.NOURISH_LOGO_URL));
            PDImageXObject logoImg;

            if (FileDownloader.getFileExtension(Constants.NOURISH_LOGO_URL).equals(".jpg")) {           //draw logo
                BufferedImage sourceImg;
                sourceImg = jpegReader.readImage(new File(Constants.FILE_SAVING_DIR + Constants.NOURISH_LOGO_FILE_NAME + ".jpg"));
                logoImg = JPEGFactory.createFromImage(document, sourceImg);
            } else {
                logoImg = PDImageXObject.createFromFile(new File(Constants.FILE_SAVING_DIR + Constants.NOURISH_LOGO_FILE_NAME + FileDownloader.getFileExtension(Constants.NOURISH_LOGO_URL)), document);
            }
            contentStream.drawImage(logoImg, 59.52f, rect.getHeight() - 60.00f - 42.51f, 60.00f, 60.00f);
        } catch (FileNotFoundException | NullPointerException | ImageReadException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            coverPictureFilename = info.getTitle().replace(" ", "") + new Date().getTime() + FileDownloader.getFileExtension(info.getCoverPicture());    //save unique cover fileName
            fileDownloader.saveFile(coverPictureFilename, new URL(info.getCoverPicture()));
            PDImageXObject coverImg;

            if (FileDownloader.getFileExtension(coverPictureFilename).equals(".jpg")) {                 //draw cover
                BufferedImage sourceImg;
                sourceImg = jpegReader.readImage(new File(Constants.FILE_SAVING_DIR + coverPictureFilename));
                coverImg = JPEGFactory.createFromImage(document, sourceImg);
            } else {
                coverImg = PDImageXObject.createFromFile(new File(Constants.FILE_SAVING_DIR + coverPictureFilename), document);
            }
            float scale = coverImg.getWidth() / 128.01f;
            float height = coverImg.getHeight() / scale;
            contentStream.drawImage(coverImg, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);

        } catch (FileNotFoundException | NullPointerException | ImageReadException | IllegalArgumentException e) {
            e.printStackTrace();

            coverPictureFilename = Constants.NO_IMAGE_FILENAME;                        //load default cover
            PDImageXObject coverImg;

            BufferedImage sourceImg = null;
            try {
                sourceImg = jpegReader.readImage(new File(Constants.FILE_SAVING_DIR + coverPictureFilename));
            } catch (ImageReadException e1) {
                e1.printStackTrace();
            }
            coverImg = JPEGFactory.createFromImage(document, sourceImg);

            float scale = coverImg.getWidth() / 128.01f;
            float height = coverImg.getHeight() / scale;
            contentStream.drawImage(coverImg, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);
        }

        drawMultiLineText("Nourish", 162.70f, rect.getHeight() - 56.97f, 248, page1, contentStream, robotoLight, 18, NOURISH_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText("Advance Information", 162.70f, rect.getHeight() - 56.97f - 1.2f * 18, 248, page1, contentStream, robotoLight, 18, NOURISH_PRIMARY_COLOR, 1.2f * 18, 2, false, false);
        drawMultiLineText(info.getTitle(), 59.52f, rect.getHeight() - 139.18f, 351, page1, contentStream, robotoLight, 22, NOURISH_MAIN_COLOR, 1.2f * 22, 0f, false, false);
        drawMultiLineText(info.getSubtitle(), 59.52f, nextYPos + ((1.2f * 22 - 1.2f * 13) / 2), 351, page1, contentStream, robotoLight, 13, NOURISH_PRIMARY_COLOR, 1.5f * 13, 0f, false, false);
        drawMultiLineText(info.getAuthorName(), 59.52f, nextYPos - 0.5f * 13, 351, page1, contentStream, robotoLight, 12, NOURISH_MAIN_COLOR, 1.2f * 12, 0f, false, false);
        drawMultiLineText(info.getPromotinalHeadline(), 59.52f, nextYPos - 0.6f * 13, 351, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.6f * 9, 0f, false, false);

        drawMultiLineText("SALES POINTS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, NOURISH_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);
        for (int i = 0; i < info.getSalesPoints().size(); i++) {
            if (i == 0) {
                drawMultiLineText(info.getSalesPoints().get(i), 59.52f, nextYPos - 3, 351, page1, contentStream, robotoLight, 9, NOURISH_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            } else {
                drawMultiLineText(info.getSalesPoints().get(i), 59.52f, nextYPos, 351, page1, contentStream, robotoLight, 9, NOURISH_SECONDARY_COLOR, 1.6f * 9, 0f, false, false);
            }
        }

        drawMultiLineText("SYNOPSIS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, page1, contentStream, robotoBold, 11, NOURISH_PRIMARY_COLOR, 1.2f * 11, 0f, false, false);
        String[] synopsis = info.getSynopsis().split("\n");    //split paragraphs
        for (int i = 0; i < synopsis.length; i++) {
            if (i == 0) {
                drawMultiLineText(synopsis[i], 59.52f, nextYPos - 3, 351 - 11, page1, contentStream, robotoLight, 9, NOURISH_MAIN_COLOR, 1.6f * 9, 0f, false, false); //1st line without paragraph
            } else {
                drawMultiLineText(synopsis[i], 59.52f, nextYPos, 351 - 11, page1, contentStream, robotoLight, 9, NOURISH_MAIN_COLOR, 1.6f * 9, 0f, true, false);
            }
        }

        drawMultiLineText("© Nourish enquiries@watkinspublishing.com ", 85.03f, rect.getHeight() - 792.47f, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, NOURISH_MAIN_COLOR, 1.2f * 8, 0f, false, false);
        drawMultiLineText("Angel Business Club 359 Goswell Rd London  EC1V 7JL Phone: 0207 323 2229 AI generated by Bibliocloud on 12 October 2015", 85.03f, nextYPos, (int) rect.getWidth(), page1, contentStream, robotoLight, 8, NOURISH_MAIN_COLOR, 1.2f * 8, 0f, false, false);

        drawMultiLineText("ISBN: " + info.getIsbn(), 418.39f, rect.getHeight() - 268.15f, 147, page1, contentStream, robotoRegular, 9, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Published: " + simpleDateFormat.format(info.getPublicationDate()).toUpperCase(), 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Price: £" + info.getPrice(), 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("CATEGORY", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        String[] catParts = info.getCategory().split("\n");
        for (String catPart : catParts) {
            drawMultiLineText(catPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("BINDING", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        String[] bindingParts = info.getBinding().split("\n");
        for (String bindingPart : bindingParts) {
            drawMultiLineText(bindingPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("FORMAT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getFormat(), 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("EXTENT", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getExtent(), 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("ILLUSTRATIONS", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        String[] illustrationsParts = info.getIllustrations().split("\n");
        for (String illustrationsPart : illustrationsParts) {
            drawMultiLineText(illustrationsPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("AUTHOR LOCATION", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_PRIMARY_COLOR, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getAuthorLocation(), 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);

        drawMultiLineText("About the author:", 418.39f, nextYPos - 7.3f, 147, page1, contentStream, robotoBold, 9, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        String[] aboutAuthorParts = info.getBio().split("\n");
        for (String aboutAuthorPart : aboutAuthorParts) {
            drawMultiLineText(aboutAuthorPart, 418.39f, nextYPos, 147, page1, contentStream, robotoRegular, 8, NOURISH_SECONDARY_COLOR, 1.2f * 9, 0f, false, true);
        }

        contentStream.close();
        document.save(outputFileName);
        document.close();

        fileDownloader.deleteFile(Constants.NOURISH_LOGO_FILE_NAME + FileDownloader.getFileExtension(Constants.NOURISH_LOGO_URL));      //delete downloaded files
        if (!coverPictureFilename.equals(Constants.NO_IMAGE_FILENAME)) {
            fileDownloader.deleteFile(coverPictureFilename);
        }
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
            if (line.length() != 0 && line.charAt(line.length() - 1) == ' ') {   //delete last empty symbol (for better right alignment)
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
