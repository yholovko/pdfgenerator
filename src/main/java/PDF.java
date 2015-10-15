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

public class PDF {
    private String documentTitle;
    private String outputFileName;
    private AditionalInformation info;
    private Color mainColor;
    private Color primaryColor;
    private Color secondaryColor;

    private float nextYPos;         //equals lastY + lineHeight

    /**
     * @param documentTitle  Nourish or Watkins
     * @param outputFileName A filename with the file extension ("****.pdf")
     * @param info           An object with all the fields filled in.
     */
    public PDF(String documentTitle, String outputFileName, AditionalInformation info, Color mainColor, Color primaryColor, Color secondaryColor) {
        this.documentTitle = documentTitle;
        this.outputFileName = outputFileName;
        this.info = info;
        this.mainColor = mainColor;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public void create() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        PDRectangle rect = page.getMediaBox();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        JPEGReader jpegReader = new JPEGReader();
        String coverPictureFilename = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMMM yyyy", Locale.US);

        PDFont robotoLight = PDType0Font.load(document, new FileInputStream(new File(Constants.PDF_RESOURCES + "/roboto-ttf/Roboto-Light.ttf")));
        PDFont robotoBold = PDType0Font.load(document, new FileInputStream(new File(Constants.PDF_RESOURCES + "/roboto-ttf/Roboto-Bold.ttf")));
        PDFont robotoRegular = PDType0Font.load(document, new FileInputStream(new File(Constants.PDF_RESOURCES + "/roboto-ttf/Roboto-Regular.ttf")));

        try {
            FileDownloader.saveFile(Constants.LOGO_FILE_NAME + FileDownloader.getFileExtension(info.getLogo()), new URL(info.getLogo()));

            PDImageXObject logoImg;
            if (FileDownloader.getFileExtension(info.getLogo()).equals(".jpg")) {
                BufferedImage sourceImg = jpegReader.readImage(new File(Constants.FILE_SAVING_DIR + Constants.LOGO_FILE_NAME + ".jpg"));
                logoImg = JPEGFactory.createFromImage(document, sourceImg);
            } else {
                logoImg = PDImageXObject.createFromFile(new File(Constants.FILE_SAVING_DIR + Constants.LOGO_FILE_NAME + FileDownloader.getFileExtension(info.getLogo())), document);
            }
            contentStream.drawImage(logoImg, 59.52f, rect.getHeight() - 60.00f - 42.51f, 60.00f, 60.00f);
        } catch (FileNotFoundException | NullPointerException | ImageReadException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            coverPictureFilename = info.getTitle().replace(" ", "") + new Date().getTime() + FileDownloader.getFileExtension(info.getCoverPicture());    //save unique cover fileName
            FileDownloader.saveFile(coverPictureFilename, new URL(info.getCoverPicture()));
            PDImageXObject coverImg;

            if (FileDownloader.getFileExtension(coverPictureFilename).equals(".jpg")) {
                BufferedImage sourceImg = jpegReader.readImage(new File(Constants.FILE_SAVING_DIR + coverPictureFilename));
                coverImg = JPEGFactory.createFromImage(document, sourceImg);
            } else {
                coverImg = PDImageXObject.createFromFile(new File(Constants.FILE_SAVING_DIR + coverPictureFilename), document);
            }

            float scale = coverImg.getWidth() / 128.01f;
            float height = coverImg.getHeight() / scale;
            contentStream.drawImage(coverImg, 418.39f, rect.getHeight() - height - 42.51f, 128.01f, height);
        } catch (FileNotFoundException | NullPointerException | ImageReadException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        drawMultiLineText(documentTitle, 162.70f, rect.getHeight() - 56.97f, 248, contentStream, robotoLight, 18, primaryColor, 1.2f * 18, 2, false, false);
        drawMultiLineText("Advance Information", 162.70f, rect.getHeight() - 56.97f - 1.2f * 18, 248, contentStream, robotoLight, 18, primaryColor, 1.2f * 18, 2, false, false);
        drawMultiLineText(info.getTitle(), 59.52f, rect.getHeight() - 139.18f, 351, contentStream, robotoLight, 22, mainColor, 1.2f * 22, 0f, false, false);
        drawMultiLineText(info.getSubtitle(), 59.52f, nextYPos + ((1.2f * 22 - 1.2f * 13) / 2), 351, contentStream, robotoLight, 13, primaryColor, 1.5f * 13, 0f, false, false);
        drawMultiLineText(info.getAuthorName(), 59.52f, nextYPos - 0.5f * 13, 351, contentStream, robotoLight, 12, mainColor, 1.2f * 12, 0f, false, false);
        drawMultiLineText(info.getPromotinalHeadline(), 59.52f, nextYPos - 0.6f * 13, 351, contentStream, robotoBold, 9, primaryColor, 1.6f * 9, 0f, false, false);
        drawMultiLineText("SALES POINTS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, contentStream, robotoBold, 11, primaryColor, 1.2f * 11, 0f, false, false);

        for (int i = 0; i < info.getSalesPoints().size(); i++) {
            if (i == 0) {
                drawMultiLineText(info.getSalesPoints().get(i), 59.52f, nextYPos - 3, 351, contentStream, robotoLight, 9, secondaryColor, 1.6f * 9, 0f, false, false);
            } else {
                drawMultiLineText(info.getSalesPoints().get(i), 59.52f, nextYPos, 351, contentStream, robotoLight, 9, secondaryColor, 1.6f * 9, 0f, false, false);
            }
        }

        drawMultiLineText("SYNOPSIS:", 59.52f, nextYPos + ((1.6f * 9) / 2) - 1.6f * 9 - 1, 351, contentStream, robotoBold, 11, primaryColor, 1.2f * 11, 0f, false, false);

        String[] synopsis = info.getSynopsis().split("\n");    //split paragraphs
        for (int i = 0; i < synopsis.length; i++) {
            if (i == 0) {
                drawMultiLineText(synopsis[i], 59.52f, nextYPos - 3, 351 - 11, contentStream, robotoLight, 9, mainColor, 1.6f * 9, 0f, false, false); //1st line without paragraph
            } else {
                drawMultiLineText(synopsis[i], 59.52f, nextYPos, 351 - 11, contentStream, robotoLight, 9, mainColor, 1.6f * 9, 0f, true, false);
            }
        }

        drawMultiLineText(String.format("© %s enquiries@watkinspublishing.com", documentTitle), 85.03f, rect.getHeight() - 792.47f, (int) rect.getWidth(), contentStream, robotoLight, 8, mainColor, 1.2f * 8, 0f, false, false);
        drawMultiLineText("Angel Business Club 359 Goswell Rd London  EC1V 7JL Phone: 0207 323 2229 AI generated by Bibliocloud on 12 October 2015", 85.03f, nextYPos, (int) rect.getWidth(), contentStream, robotoLight, 8, mainColor, 1.2f * 8, 0f, false, false);
        drawMultiLineText("ISBN: " + info.getIsbn(), 418.39f, rect.getHeight() - 268.15f, 147, contentStream, robotoRegular, 9, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Published: " + simpleDateFormat.format(info.getPublicationDate()).toUpperCase(), 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("Price: £" + info.getPrice(), 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("CATEGORY", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);

        for (String catPart : info.getCategory().split("\n")) {
            drawMultiLineText(catPart, 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("BINDING", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);

        for (String bindingPart : info.getBinding().split("\n")) {
            drawMultiLineText(bindingPart, 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("FORMAT", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getFormat(), 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("EXTENT", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getExtent(), 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("ILLUSTRATIONS", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);

        for (String illustrationsPart : info.getIllustrations().split("\n")) {
            drawMultiLineText(illustrationsPart, 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        }

        drawMultiLineText("AUTHOR LOCATION", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, primaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText(info.getAuthorLocation(), 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        drawMultiLineText("About the author:", 418.39f, nextYPos - 7.3f, 147, contentStream, robotoBold, 9, secondaryColor, 1.2f * 9, 0f, false, true);

        for (String aboutAuthorPart : info.getBio().split("\n")) {
            drawMultiLineText(aboutAuthorPart, 418.39f, nextYPos, 147, contentStream, robotoRegular, 8, secondaryColor, 1.2f * 9, 0f, false, true);
        }

        contentStream.close();

        document.save(outputFileName);
        document.close();

        FileDownloader.deleteFile(Constants.LOGO_FILE_NAME + FileDownloader.getFileExtension(info.getLogo()));
        FileDownloader.deleteFile(coverPictureFilename);
    }

    /**
     * @param text             The text to write on the page.
     * @param x                The position on the x-axis.
     * @param y                The position on the y-axis.
     * @param allowedWidth     The maximum allowed width(px) of the whole text (e.g. the width of the page - a defined margin).
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
    private int drawMultiLineText(String text, float x, float y, int allowedWidth, PDPageContentStream contentStream,
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
            if (line.charAt(line.length() - 1) == ' ') {   //delete last empty symbol (for better right alignment)
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
