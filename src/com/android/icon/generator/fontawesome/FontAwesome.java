package com.android.icon.generator.fontawesome;

import com.android.icon.generator.core.AndroidIconGenerator;
import com.android.icon.generator.core.DIP;
import com.android.icon.generator.utils.FileUtils;
import com.android.icon.generator.utils.FontUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FontAwesome {

    public static final String MAPPING_PATH = "font-awesome/font-awesome-mapping.properties";
    public static final String FONT_PATH = "font-awesome/fontawesome-webfont.ttf";

    /**
     * Generates all font awesome icons into output directory. Every icon is provided in 6 different
     * densities (ldpi, mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi).
     *
     * @param iconGenerator
     * @param outputDirectory
     * @param forceNew  If <code>true</code>, local cached files (if any) will be deleted and all mappings
     *                  and font files will be freshly downloaded.
     * @throws IOException
     * @throws FontFormatException
     */
    public void generateAllIcons(AndroidIconGenerator iconGenerator, File outputDirectory, boolean forceNew) throws IOException, FontFormatException {
        if(forceNew) {
            clearCache();
        }

        System.out.println("Generating FontAwesome icons...");

        Map<String, Character> icons = getIcons();
        for(Map.Entry<String, Character> entry : icons.entrySet()) {
            BufferedImage originalImage = convertToImage(entry.getKey());
            String outputPath = FilenameUtils.concat(outputDirectory.getAbsolutePath(), "ic_" + entry.getKey().replace("-", "_"));

            for(DIP dip : DIP.values()) {
                BufferedImage image = iconGenerator.generateIcon(originalImage, dip);
                FileUtils.saveAsDrawable(image, new File(outputPath), dip);
            }
        }

        System.out.println(icons.size() + " icons generated.");
    }

    private void clearCache() {
        File mappingFile = new File(MAPPING_PATH);
        if(mappingFile.exists()) {
            mappingFile.delete();
        }

        File fontPath = new File(FONT_PATH);
        if(fontPath.exists()) {
            fontPath.delete();
        }
    }

    /**
     * Converts font awesome icon into image (size depends on the icon but it's in ~500x500px range).
     *
     * @param iconId    Format is fa-{iconName} so for example fa-car, fa-automobile etc.
     * @return
     * @throws IOException
     * @throws FontFormatException
     */
    public BufferedImage convertToImage(String iconId) throws IOException, FontFormatException {
        return FontUtils.convertStringToImage(getFontFile(), getIcons().get(iconId), 512f);
    }

    /**
     * Retrieves mapping file from the local file or downloads (generates) it if it doesn't exist.
     * Mapping file contains key-value pairs where key is icon identifier (ex. <code>fa-car</code>)
     * and value is it's unicode character (ex. <code>\uF1B9</code>).
     *
     * @return
     * @throws IOException
     */
    private Map<String, Character> getIcons() throws IOException {
        Properties properties = new Properties();

        if(!new File(MAPPING_PATH).exists()) {
            System.out.println("Font Awesome mapping file doesn't exist. Creating new one...");

            new File(FilenameUtils.getFullPath(MAPPING_PATH)).mkdirs();

            Document document = Jsoup.connect("http://fortawesome.github.io/Font-Awesome/icons/").get();
            Elements links = document.getElementsByTag("a");

            for(int i = 0; i < links.size(); i++) {
                Element link = links.get(i);
                String href = link.attr("abs:href");
                if (href.startsWith("http://fortawesome.github.io/Font-Awesome/icon/")) {
                    String idOrAlias = link.text().replace(" (alias)", "");

                    String key = idOrAlias;
                    String value = Character.toString(getUnicode(href));
                    properties.put(key, value);
                }

                int percentage = (int) ((double) i/links.size()*100);
                System.out.print("\rIn progress " + percentage + "%" + "...");
            }

            properties.store(new FileOutputStream(MAPPING_PATH), null);
            System.out.println("\nFont Awesome mapping created.");
        }

        properties.load(new InputStreamReader(new FileInputStream(MAPPING_PATH), "UTF-8"));

        Map<String, Character> icons = new HashMap<String, Character>();
        for(Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            icons.put(key, value.charAt(0));
        }

        return icons;
    }

    /**
     * Get font file from local file or download if it doesn't exist.
     *
     * @return
     * @throws IOException
     */
    private File getFontFile() throws IOException {
        File fontFile = new File(FONT_PATH);
        if(!fontFile.exists()) {
            System.out.println("Font Awesome TTF font file not found. Downloading...");

            new File(FilenameUtils.getFullPath(FONT_PATH)).mkdirs();

            // Download newest .ttf file
            String ttfUrl = "https://github.com/FortAwesome/Font-Awesome/raw/master/fonts/fontawesome-webfont.ttf";
            Request request = new Request.Builder().url(ttfUrl).build();
            Response response = new OkHttpClient().newCall(request).execute();
            IOUtils.copy(response.body().byteStream(), new FileOutputStream(fontFile));

            System.out.println("Font Awesome TTF font file downloaded.");
        }

        return fontFile;
    }

    /**
     * Retrieves icon unicode string from the webpage.
     *
     * @param iconUrl   <code>http://fortawesome.github.io/Font-Awesome/icon/{iconName}</code>
     * @return
     * @throws IOException
     */
    private char getUnicode(String iconUrl) throws IOException {
        Document document = Jsoup.connect(iconUrl).get();
        String unicodeNumber = document.getElementsByClass("upper").first().text();
        int unicode = Integer.parseInt(unicodeNumber, 16);

        return Character.toChars(unicode)[0];
    }

}
