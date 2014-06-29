package com.android.icon.generator;

import com.android.icon.generator.core.AndroidIconGenerator;
import com.android.icon.generator.core.AndroidTheme;
import com.android.icon.generator.actionbar.AndroidActionBarIconGenerator;
import com.android.icon.generator.core.DIP;
import com.android.icon.generator.fontawesome.FontAwesome;
import com.android.icon.generator.utils.SaveFileUtils;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Generator {

    public static void main(String[] args) throws ParseException, IOException {
        Options options = prepareOptions();
        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        if(cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("aig [options]", options);
            return;
        }

        if(cmd.hasOption("u")) {
            try {
                new FontAwesome().updateIcons();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        boolean isInputFontAwesome = cmd.hasOption("fa");
        if(!isInputFontAwesome && !cmd.hasOption("i")) {
            System.out.println("Input file/directory or font awesome icon is required. See --help for usage.");
            return;
        }

        String inputFilePath = cmd.getOptionValue("i");
        if(isInputFontAwesome) {
           inputFilePath = cmd.getOptionValue("fa");
        }

        File inputFile = new File(inputFilePath);
        if(!isInputFontAwesome && !inputFile.exists()) {
            System.out.println("Input file or directory doesn't exist.");
            return;
        } else if(isInputFontAwesome && !fontAwesomeMappingContains(inputFilePath)) {
            System.out.println("Unknown Font Awesome icon identifier '" + inputFilePath + "'. See --help for usage.");
            return;
        }

        IconType iconType = parseIconType(cmd);
        List<AndroidTheme> androidThemes = parseAndroidThemes(cmd);
        List<DIP> dipList = parseDensities(cmd);

        for(AndroidTheme androidTheme : androidThemes) {
            AndroidIconGenerator generator = null;
            if(iconType == IconType.ACTION_BAR) {
                generator = new AndroidActionBarIconGenerator(androidTheme);
            }

            if(!isInputFontAwesome) {
                if(inputFile.isDirectory()) {
                    generateIcons(cmd, androidTheme, generator);
                } else {
                    try {
                        generateIcon(cmd, inputFile, androidTheme, generator);
                    } catch (NonImageFileException e) {
                        System.out.println(inputFile + " is not an image. Aborting.");
                        return;
                    }
                }
            } else {
                String outputFilePath = cmd.hasOption("o") ? cmd.getOptionValue("o") : System.getProperty("user.home");
                outputFilePath = FilenameUtils.concat(outputFilePath, inputFilePath);

                try {
                    FontAwesome fontAwesome = new FontAwesome();
                    BufferedImage inputImage = fontAwesome.convertToImage(inputFilePath);
                    generateIcon(inputImage, generateOutputFile(outputFilePath, androidTheme), generator, dipList);
                } catch (FontFormatException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void generateIcon(CommandLine cmd, File file, AndroidTheme androidTheme, AndroidIconGenerator generator) throws IOException, NonImageFileException {
        String outputFilePath;
        if(cmd.hasOption("o")) {
            outputFilePath = cmd.getOptionValue("o");
            outputFilePath = FilenameUtils.concat(outputFilePath, FilenameUtils.getBaseName(file.getAbsolutePath()));
        } else {
            outputFilePath = file.getAbsolutePath();
        }

        List<DIP> dipList = parseDensities(cmd);
        generateIcon(file, generateOutputFile(outputFilePath, androidTheme), generator, dipList);
    }

    private static void generateIcons(CommandLine cmd, AndroidTheme androidTheme, AndroidIconGenerator generator) throws IOException {
        File inputFile = new File(cmd.getOptionValue("i"));
        for(File file : inputFile.listFiles()) {
            if(!file.isDirectory()) {
                try {
                    generateIcon(cmd, file, androidTheme, generator);
                } catch (NonImageFileException e) {
                    // Ignore because we are iterating through directory
                    e.printStackTrace();
                }
            }
        }
    }

    private static File generateOutputFile(String path, AndroidTheme androidTheme) {
        String outputFilePath = FilenameUtils.getFullPath(path);
        outputFilePath = FilenameUtils.concat(outputFilePath, androidTheme.toString().toLowerCase());

        String fileName = FilenameUtils.getBaseName(path);
        fileName = fileName.replace(" ", "_").replace("-", "_");
        fileName = fileName.replaceAll("[^0-9A-Za-z_]", "");
        outputFilePath = FilenameUtils.concat(outputFilePath, "ic_" + fileName);

        return new File(outputFilePath);
    }

    private static void generateIcon(File inputFile, File outputFile, AndroidIconGenerator generator, List<DIP> densities) throws IOException, NonImageFileException {
        BufferedImage inputImage = ImageIO.read(inputFile);
        if (inputImage == null) {
            throw new NonImageFileException();
        }

        System.out.println(outputFile.getAbsolutePath());
        generateIcon(inputImage, outputFile, generator, densities);
    }

    private static void generateIcon(BufferedImage inputImage, File outputFile, AndroidIconGenerator generator, List<DIP> densities) throws IOException {
        for(DIP dip: densities) {
            BufferedImage image = generator.generateIcon(inputImage, dip);
            SaveFileUtils.saveAsDrawable(image, outputFile, dip);
        }
    }

    private static boolean fontAwesomeMappingContains(String iconIdentifier) {
        try {
            return new FontAwesome().getIcons(false).containsKey(iconIdentifier);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static List<DIP> parseDensities(CommandLine cmd) {
        List<DIP> dipList = new ArrayList<DIP>();
        for(DIP dip : DIP.values()) {
            if(cmd.hasOption(dip.toString().toLowerCase())) {
                dipList.add(dip);
            }
        }

        if(dipList.size() == 0) {
            dipList.addAll(Arrays.asList(DIP.values()));
        }

        return dipList;
    }

    private static List<AndroidTheme> parseAndroidThemes(CommandLine cmd) {
        List<AndroidTheme> androidThemes = new ArrayList<AndroidTheme>();

        String[] themes = cmd.getOptionValues("t");
        if(themes != null && themes.length > 0) {
            for(String theme : themes) {
                if(theme.equals("hl") || theme.equals("holo-light")) {
                    androidThemes.add(AndroidTheme.HOLO_LIGHT);
                } else if(theme.equals("hd") || theme.equals("holo-dark")) {
                    androidThemes.add(AndroidTheme.HOLO_DARK);
                }
            }
        } else {
            androidThemes.addAll(Arrays.asList(AndroidTheme.values()));
        }

        return androidThemes;
    }

    private static IconType parseIconType(CommandLine cmd) {
        IconType iconType = IconType.ACTION_BAR;
        if(cmd.hasOption("ab")) {
            iconType = IconType.ACTION_BAR;
        }

        // TODO: More options will follow

        return iconType;
    }

    @SuppressWarnings("static-access")
    private static Options prepareOptions() {
        Options options = new Options();

        options.addOption("h", "help", false, "help");
        options.addOption("u", "update", false, "update FontAwesome icons");

        for(DIP dip : DIP.values()) {
            String dipString = dip.toString().toLowerCase();
            options.addOption(dipString, false, String.format("generate %s density", dipString));
        }

        options.addOption("ab", "action-bar", false, "Action Bar Icon");

        Option themeOption = OptionBuilder.withArgName("theme1,theme2...")
                .hasArgs()
                .withValueSeparator(',')
                .withDescription("use given Android theme(s)\n-hl,--holo-light\n-hd,--holo-dark\nIf none is provided, all themes are included.")
                .withLongOpt("theme")
                .create("t");
        options.addOption(themeOption);

        Option fontAwesomeInput = OptionBuilder.withArgName("icon")
                .hasArg()
                .withDescription("use given FontAwesome icon identifier as input (ex. fa-car)")
                .withLongOpt("font-awesome")
                .create("fa");
        options.addOption(fontAwesomeInput);

        Option inputFileOption = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("use given file or directory as image input")
                .withLongOpt("input")
                .create("i");
        options.addOption(inputFileOption);

        Option outputFileOption = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("use given directory as image output")
                .withLongOpt("output")
                .create("o");
        options.addOption(outputFileOption);

        return options;
    }

}
