import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
    public static void saveFile(String fileName, URL download) {
        new File(Constants.FILE_SAVING_DIR).mkdirs();

        try (FileOutputStream fos = new FileOutputStream(Constants.FILE_SAVING_DIR + fileName)) {
            URLConnection conn = download.openConnection();
            conn.setConnectTimeout(10 * 60 * 1000);
            conn.setReadTimeout(10 * 60 * 1000); //10 min

            ReadableByteChannel rbc = Channels.newChannel(conn.getInputStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
            new File(Constants.FILE_SAVING_DIR + fileName).delete();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void deleteFile(String fileName){
        new File(Constants.FILE_SAVING_DIR + fileName).delete();
    }

    public static String getFileExtension(String url){
        return url.substring(url.lastIndexOf("."), url.length());
    }
}