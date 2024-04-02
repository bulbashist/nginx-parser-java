import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class Main {
    static Storage storage = new Storage();
    static ZonedDateTime fromTime = null;
    static ZonedDateTime toTime = null;
    public static void main(String[] args) {
            try (Logger logger = new Logger()) {

                try {
                    String inputFilepath = args.length >= 1 ? args[0] : "access.log";
                    String outputFilepath = args.length >= 2 ? args[1] : "result.json";
                    String fromTimeArg = args.length >= 3 ? args[2] : null;
                    String toTimeArg = args.length >= 4 ? args[3] : null;

                    DateTimeFormatter argsTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH.mm.ss").withZone(ZoneOffset.ofHours(+3));
                    if (fromTimeArg != null) {
                        try {
                            fromTime = ZonedDateTime.parse(fromTimeArg, argsTimeFormatter);
                        } catch (DateTimeParseException e) {
                            logger.logException(e, "Incorrect start date format, this argument will be ignored");
                        }
                    }
                    if (toTimeArg != null) {
                        try {
                            toTime = ZonedDateTime.parse(toTimeArg, argsTimeFormatter);
                        } catch (DateTimeParseException e) {
                            logger.logException(e, "Incorrect end date format, this argument will be ignored");
                        }
                    }

                    File outputFile;
                    try {
                        outputFile = new File(outputFilepath);
                    } catch (NullPointerException e) {
                        logger.logException(e, "Set proper output filename");
                        return;
                    }

                    try {
                        outputFile.createNewFile();
                    } catch (IOException e) {
                        logger.logException(e, "I/O error occurred");
                        return;
                    } catch (SecurityException e) {
                        logger.logException(e, "Access to file denied");
                        return;
                    }

                    FileWriter myWriter;
                    try {
                        myWriter = new FileWriter(outputFile, StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        logger.logException(e);
                        return;
                    }

                    List<String> lines;
                    try {
                        lines = Files.readAllLines(Path.of(inputFilepath), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        logger.logException(e, "Cannot read from the file");
                        return;
                    }
                    logger.logMessage("File has been read");

                    NginxParser parser = new NginxParser(fromTime, toTime, logger);

                    CopyOnWriteArrayList<String> lines2 = new CopyOnWriteArrayList<>(lines);
                    lines2.stream().parallel().forEach(line -> {
                        try {
                            var hr = parser.parse(line);
                            if (hr != null) {
                                storage.add(hr);
                            }
                        } catch (Exception e) {
                            logger.logException(e);
                        }
                    });


                    logger.logMessage("Data has been transformed");

                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                    String res = gson.toJson(storage.getAll());
                    try {
                        myWriter.write(res);
                        myWriter.close();
                        logger.logMessage("Completed");
                    } catch (IOException e) {
                        logger.logException(e);
                    }
                } catch (Exception e) {
                    logger.logException(e);
                }
            }
    }
}

