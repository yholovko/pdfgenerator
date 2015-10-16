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

    private float lastYPos;         //equals lastY

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
            contentStream.drawImage(logoImg, Constants.PAGE_LEFT_MARGIN, rect.getHeight() - Constants.LOGO_HEIGHT - Constants.PAGE_TOP_MARGIN, Constants.LOGO_WIDTH, Constants.LOGO_HEIGHT);
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

            float scale = coverImg.getWidth() / Constants.COVER_WIDTH;
            float height = coverImg.getHeight() / scale;
            contentStream.drawImage(coverImg, Constants.SECOND_COLUMN_LEFT_MARGIN, rect.getHeight() - height - Constants.PAGE_TOP_MARGIN, Constants.COVER_WIDTH, height);
        } catch (FileNotFoundException | NullPointerException | ImageReadException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        drawMultiLineText(documentTitle, Constants.ADVANCE_INFO_TITLE_LEFT_MARGIN, rect.getHeight() - Constants.ADVANCE_INFO_TITLE_TOP_MARGIN, Constants.ADVANCE_INFO_TITLE_ALLOWED_WIDTH, contentStream, robotoLight, Constants.ADVANCE_INFO_TITLE_FONT_SIZE, primaryColor, Constants.ADVANCE_INFO_TITLE_LINE_HEIGHT, 2, false, false);
        drawMultiLineText("Advance Information", Constants.ADVANCE_INFO_TITLE_LEFT_MARGIN, lastYPos - Constants.ADVANCE_INFO_TITLE_LINE_HEIGHT, Constants.ADVANCE_INFO_TITLE_ALLOWED_WIDTH, contentStream, robotoLight, Constants.ADVANCE_INFO_TITLE_FONT_SIZE, primaryColor, Constants.ADVANCE_INFO_TITLE_LINE_HEIGHT, 2, false, false);
        drawMultiLineText(info.getTitle(), Constants.PAGE_LEFT_MARGIN, rect.getHeight() - Constants.TITLE_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoLight, Constants.TITLE_FONT_SIZE, mainColor, Constants.TITLE_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText(info.getSubtitle(), Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SUBTITLE_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoLight, Constants.SUBTITLE_FONT_SIZE, primaryColor, Constants.SUBTITLE_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText(info.getAuthorName(), Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.AUTHOR_NAME_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoLight, Constants.AUTHOR_NAME_FONT_SIZE, mainColor, Constants.AUTHOR_NAME_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText(info.getPromotinalHeadline(), Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.PROMO_HEADLINE_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.PROMO_HEADLINE_FONT_SIZE, primaryColor, Constants.PROMO_HEADLINE_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText("SALES POINTS:", Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SALES_HEADLINE_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.SALES_HEADLINE_FONT_SIZE, primaryColor, Constants.SALES_HEADLINE_LINE_HEIGHT, 0f, false, false);

        for (String salesPoint : info.getSalesPoints()) {
            drawMultiLineText(salesPoint, Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SALES_POINTS_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoLight, Constants.SALES_POINTS_FONT_SIZE, secondaryColor, Constants.SALES_POINTS_LINE_HEIGHT, 0f, false, false);
        }

        drawMultiLineText("SYNOPSIS:", Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SYNOPSIS_HEADLINE_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.SYNOPSIS_HEADLINE_FONT_SIZE, primaryColor, Constants.SYNOPSIS_HEADLINE_LINE_HEIGHT, 0f, false, false);

        String[] synopsis = info.getSynopsis().split("\n");    //split paragraphs
        for (int i = 0; i < synopsis.length; i++) {
            if (i == 0) {       //1st line without paragraph
                drawMultiLineText(synopsis[i], Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SYNOPSIS_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH, contentStream, robotoLight, Constants.SYNOPSIS_FONT_SIZE, mainColor, Constants.SYNOPSIS_LINE_HEIGHT, 0f, false, false);
            } else {
                drawMultiLineText(synopsis[i], Constants.PAGE_LEFT_MARGIN, lastYPos - Constants.SYNOPSIS_TOP_MARGIN, Constants.FIRST_COLUMN_ALLOWED_WIDTH - (int)Constants.PARAGRAPH_SIZE, contentStream, robotoLight, Constants.SYNOPSIS_FONT_SIZE, mainColor, Constants.SYNOPSIS_LINE_HEIGHT, 0f, true, false);
            }
        }

        drawMultiLineText(String.format("© %s enquiries@watkinspublishing.com", documentTitle), Constants.COMPANY_INFO_LEFT_MARGIN, rect.getHeight() - Constants.COMPANY_INFO_BOTTOM_MARGIN, (int) rect.getWidth(), contentStream, robotoLight, Constants.COMPANY_INFO_FONT_SIZE, mainColor, Constants.COMPANY_INFO_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText("Angel Business Club 359 Goswell Rd London  EC1V 7JL Phone: 0207 323 2229 AI generated by Bibliocloud on 12 October 2015", Constants.COMPANY_INFO_LEFT_MARGIN, lastYPos - Constants.COMPANY_INFO_LINE_HEIGHT, (int) rect.getWidth(), contentStream, robotoLight, Constants.COMPANY_INFO_FONT_SIZE, mainColor, Constants.COMPANY_INFO_LINE_HEIGHT, 0f, false, false);
        drawMultiLineText("ISBN: " + info.getIsbn(), Constants.SECOND_COLUMN_LEFT_MARGIN, rect.getHeight() - Constants.ISBN_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.ISBN_FONT_SIZE, secondaryColor, Constants.ISBN_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("Published: " + simpleDateFormat.format(info.getPublicationDate()).toUpperCase(), Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.PUBLISHED_DATE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.PUBLISHED_DATE_FONT_SIZE, secondaryColor, Constants.PUBLISHED_DATE_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("Price: £" + info.getPrice(), Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.PRICE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.PRICE_FONT_SIZE, secondaryColor, Constants.PRICE_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("CATEGORY", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.CATEGORY_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.CATEGORY_HEADLINE_FONT_SIZE, primaryColor, Constants.CATEGORY_HEADLINE_LINE_HEIGHT, 0f, false, true);

        for (String catPart : info.getCategory().split("\n")) {
            drawMultiLineText(catPart, Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.CATEGORY_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.CATEGORY_FONT_SIZE, secondaryColor, Constants.CATEGORY_LINE_HEIGHT, 0f, false, true);
        }

        drawMultiLineText("BINDING", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.BINDING_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.BINDING_HEADLINE_FONT_SIZE, primaryColor, Constants.BINDING_HEADLINE_LINE_HEIGHT, 0f, false, true);

        for (String bindingPart : info.getBinding().split("\n")) {
            drawMultiLineText(bindingPart, Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.BINDING_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.BINDING__FONT_SIZE, secondaryColor, Constants.BINDING_LINE_HEIGHT, 0f, false, true);
        }

        drawMultiLineText("FORMAT", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.FORMAT_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.FORMAT_HEADLINE_FONT_SIZE, primaryColor, Constants.FORMAT_HEADLINE_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText(info.getFormat(), Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.FORMAT_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.FORMAT_FONT_SIZE, secondaryColor, Constants.FORMAT_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("EXTENT", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.EXTENT_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.EXTENT_HEADLINE_FONT_SIZE, primaryColor, Constants.EXTENT_HEADLINE_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText(info.getExtent(), Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.EXTENT_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.EXTENT_FONT_SIZE, secondaryColor, Constants.EXTENT_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("ILLUSTRATIONS", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.ILLUSTRATIONS_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.ILLUSTRATIONS_HEADLINE_FONT_SIZE, primaryColor, Constants.ILLUSTRATIONS_HEADLINE_LINE_HEIGHT, 0f, false, true);

        for (String illustrationsPart : info.getIllustrations().split("\n")) {
            drawMultiLineText(illustrationsPart, Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.ILLUSTRATIONS_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.ILLUSTRATIONS_FONT_SIZE, secondaryColor, Constants.ILLUSTRATIONS_LINE_HEIGHT, 0f, false, true);
        }

        drawMultiLineText("AUTHOR LOCATION", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.AUTHOR_LOCATION_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.AUTHOR_LOCATION_HEADLINE_FONT_SIZE, primaryColor, Constants.AUTHOR_LOCATION_HEADLINE_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText(info.getAuthorLocation(), Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.AUTHOR_LOCATION_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.AUTHOR_LOCATION_FONT_SIZE, secondaryColor, Constants.AUTHOR_LOCATION_LINE_HEIGHT, 0f, false, true);
        drawMultiLineText("About the author:", Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.ABOUT_AUTHOR_HEADLINE_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoBold, Constants.ABOUT_AUTHOR_HEADLINE_FONT_SIZE, secondaryColor, Constants.ABOUT_AUTHOR_HEADLINE_LINE_HEIGHT, 0f, false, true);

        for (String aboutAuthorPart : info.getBio().split("\n")) {
            drawMultiLineText(aboutAuthorPart, Constants.SECOND_COLUMN_LEFT_MARGIN, lastYPos - Constants.ABOUT_AUTHOR_TOP_MARGIN, Constants.SECOND_COLUMN_ALLOWED_WIDTH, contentStream, robotoRegular, Constants.ABOUT_AUTHOR_FONT_SIZE, secondaryColor, Constants.ABOUT_AUTHOR_LINE_HEIGHT, 0f, false, true);
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
                size = (int) ((fontSize * font.getStringWidth(myLine + word) / 1000) + Constants.PARAGRAPH_SIZE);
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
            contentStream.beginText();
            contentStream.appendRawCommands(String.valueOf(charSpacing) + " Tc\n");
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(fontColor);
            if (isFirstParagraph) {
                contentStream.newLineAtOffset(x + Constants.PARAGRAPH_SIZE, y);
                isFirstParagraph = false;
            } else if (isRightAlignment) {
                if (line.length() != 0 && line.charAt(line.length() - 1) == ' ') {   //delete last empty symbol (for better right alignment)
                    line = line.substring(0, line.length() - 1);
                }

                float lineWidth = (fontSize * font.getStringWidth(line) / 1000);
                contentStream.newLineAtOffset(x + (allowedWidth - lineWidth), y);
            } else {
                contentStream.newLineAtOffset(x, y);
            }

            contentStream.showText(line);
            contentStream.endText();
            lastYPos = y;
            y -= lineHeight;
        }

        return lines.size();
    }
}
