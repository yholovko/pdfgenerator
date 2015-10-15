import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException, IOException {
        new PDF(Constants.WATKINS ,"watkins.pdf", getAdditionalInformation(), new Color(0x000000), new Color(0x009999), new Color(0x6A5F55)).create();
        new PDF(Constants.NOURISH ,"nourish.pdf", getAdditionalInformation(), new Color(0x000000), new Color(0xCD1F20), new Color(0x6A5F55)).create();
    }

    public static AditionalInformation getAdditionalInformation() throws ParseException {
        AditionalInformation additionalInformation = new AditionalInformation();
        List<String> salesPoints = new ArrayList<>();

        additionalInformation.setLogo("https://s3-eu-west-1.amazonaws.com/publihubimages/imprints/2/Watkins_Logo_original.jpg");
        additionalInformation.setTitle("Gratitude");
        additionalInformation.setSubtitle("Effortless Inspiration for a Happier Life");
        additionalInformation.setAuthorName("By Dani Dipirro");
        additionalInformation.setPromotinalHeadline("The first in a new series of vibrantly illustrated, feel-good gift books, this title features a wide range of inspirational quotes, insightful reflections, thought-provoking activities and empowering affirmations to provide readers with the motivation to live with more gratitude in their everyday lives for increased joy and fulfilment.");

        salesPoints.add(0, "• A series of fresh, contemporary gift books, especially designed to tap into the current, fast-growing interest in self-development topics, such as gratitude and mindfulness");
        salesPoints.add(1, "• The conscious practice of gratitude, which has been proven to boost the immune system and enhance");
        salesPoints.add(2, "• Other titles in the series include Living in the Moment, Compassion and Forgiveness – all qualities that will help readers lead more positive and happy livesoverall quality of life, is a hot topic and has been receiving wide media coverage");
        salesPoints.add(3, "• All titles in the series are written and illustrated by popular blogger, designer and positivity aficionado Dani DiPirro, whose upbeat tone and simple, modern designs make the chosen topics a joy to explore");

        additionalInformation.setSalesPoints(salesPoints);
        additionalInformation.setSynopsis("Cultivating gratitude doesn't cost any money and doesn't take much time, but the benefits can be enormous, helping you focus on what you have rather than what you don't: friends and family, positive personal qualities, your surroundings, a healthy body, a vibrant mind, and the list goes on.\n Perfect as either a gift or self-purchase, this lovely little book features 18 inspirational quotes on the theme of gratitude, each one followed by an insightful explanation, a thought-provoking activity or question, and a memorable affirmation – all intended to inspire readers to think about gratitude in a fresh way as well as encouraging them to incorporate it into their everyday lives.");
        additionalInformation.setCoverPicture("https://publihubimages.s3-eu-west-1.amazonaws.com/1/1361//_original.jpg");
        additionalInformation.setIsbn("9781780289311");
        additionalInformation.setPublicationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2016-05-19"));
        additionalInformation.setPrice(5.99f);
        additionalInformation.setCategory("(BIC) WZG (BISAC) SELF-HELP / Affirmations");
        additionalInformation.setBinding("Hardback");
        additionalInformation.setFormat("140mm x 140mm");
        additionalInformation.setExtent("128pp");
        additionalInformation.setIllustrations("1-colour graphic illustrations throughout");
        additionalInformation.setAuthorLocation("Maryland, USA");
        additionalInformation.setBio("Dani DiPirro, both author and illustrator of this lovely series of gift books, is founder of PositivelyPresent.com, an inspiring website created in 2009 to help others live more happily in each and every moment. Dani is also the author of The Positively Present Guide to Life and of the Every Day Matters 2015 and 2016 Diaries. She has a loyal following, her work has been featured on sites such as The Happiness Project, Psychology Today and Forbes, and she was featured as Elle magazine's coach of the month in March 2015. For more information, visit: www.danidipirro.com");

        return additionalInformation;
    }
}
