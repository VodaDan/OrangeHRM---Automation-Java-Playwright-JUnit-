package utils;

import base.BaseTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class DataProvider {
    public static Stream<Arguments> loginInvalidCsvProvider () throws Exception {
        Properties properties = new Properties();
        String path;

        try (InputStream config = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(config);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        path = properties.getProperty("loginInvalidCsv");
        List<String[]> rows = Files.lines(Paths.get(path))
                .skip(1)
                .map(line -> line.split(",",-1))
                .toList();

//        System.out.println("CONFIG PATH = " + path);
//        System.out.println("ABS PATH   = " + Paths.get(path).toAbsolutePath());
//        System.out.println("exists?    = " + Files.exists(Paths.get(path)));

        return rows.stream().map(r -> Arguments.of(r[0],r[1]));
    }
}
