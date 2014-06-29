package com.android.icon.generator.fontawesome;

import com.android.icon.generator.core.AndroidIconGenerator;
import com.android.icon.generator.core.DIP;
import com.android.icon.generator.utils.SaveFileUtils;
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
     * @throws IOException
     * @throws FontFormatException
     */
    public void generateAllIcons(AndroidIconGenerator iconGenerator, File outputDirectory) throws IOException, FontFormatException {
        System.out.println("Generating FontAwesome icons...");

        Map<String, Character> icons = getIcons(false);
        for(Map.Entry<String, Character> entry : icons.entrySet()) {
            BufferedImage originalImage = convertToImage(entry.getKey());
            String outputPath = FilenameUtils.concat(outputDirectory.getAbsolutePath(), "ic_" + entry.getKey().replace("-", "_"));

            for(DIP dip : DIP.values()) {
                BufferedImage image = iconGenerator.generateIcon(originalImage, dip);
                SaveFileUtils.saveAsDrawable(image, new File(outputPath), dip);
            }
        }

        System.out.println(icons.size() + " icons generated.");
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
        return FontUtils.convertStringToImage(getFontFile(false), getIcons(false).get(iconId), 512f);
    }

    public Map<String, Character> getIcons(boolean update) throws IOException {
        Properties properties = getMappingFile(update);

        Map<String, Character> icons = new HashMap<String, Character>();
        for(Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            icons.put(key, value.charAt(0));
        }

        return icons;
    }

    public void updateIcons() throws IOException {
        getMappingFile(true);
        getFontFile(true);
    }

    /**
     * Retrieves mapping file from the local file or downloads (generates) it if it doesn't exist.
     * Mapping file contains key-value pairs where key is icon identifier (ex. <code>fa-car</code>)
     * and value is it's unicode character (ex. <code>\uF1B9</code>).
     *
     * @param update
     * @return
     * @throws IOException
     */
    private Properties getMappingFile(boolean update) throws IOException {
        Properties properties = new Properties();

        boolean download = !new File(MAPPING_PATH).exists();
        if(download) {
            System.out.println("Font Awesome mapping file doesn't exist. Creating new one...");
            new File(FilenameUtils.getFullPath(MAPPING_PATH)).mkdirs();
        } else if(update) {
            System.out.println("Updating Font Awesome mapping file...");
            properties.load(new InputStreamReader(new FileInputStream(MAPPING_PATH), "UTF-8"));
        } else {
            properties.load(new InputStreamReader(new FileInputStream(MAPPING_PATH), "UTF-8"));
            return properties;
        }

        Document document = Jsoup.connect("http://fortawesome.github.io/Font-Awesome/icons/").get();
        Elements links = document.getElementsByTag("a");

        for(int i = 0; i < links.size(); i++) {
            Element link = links.get(i);
            String href = link.attr("abs:href");
            String iconid = link.text().replace(" (alias)", "");

            if (!properties.containsKey(iconid) && href.startsWith("http://fortawesome.github.io/Font-Awesome/icon/")) {
                String key = iconid;
                String value = Character.toString(getUnicode(href));
                properties.put(key, value);
            }

            int percentage = (int) ((double) i/links.size()*100);
            System.out.print("\rIn progress " + percentage + "%" + "...");
        }

        properties.store(new FileOutputStream(MAPPING_PATH), null);

        if(download) {
            System.out.println("\nFont Awesome mapping created.");
        } else {
            System.out.println("\nFont Awesome mapping updated.");
        }

        return properties;
    }

    /**
     * Get font file from local file or download if it doesn't exist.
     *
     * @return
     * @throws IOException
     */
    private File getFontFile(boolean forceDownload) throws IOException {
        File fontFile = new File(FONT_PATH);
        if(forceDownload || !fontFile.exists()) {
            if(!fontFile.exists()) {
                System.out.println("Font Awesome TTF font file not found. Downloading...");
            } else {
                System.out.println("Font Awesome TTF font is being updated. Downloading...");
            }

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
