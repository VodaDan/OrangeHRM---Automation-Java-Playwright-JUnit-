package utils;

import base.BaseTest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;
import org.junit.jupiter.params.provider.Arguments;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
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

    // Data Transfer Object for json parsing
    public static class RegisterTestDTO extends User {
        private String expectedPage;
        public String getExpectedPage() { return expectedPage; }
    }

    public static Stream<Arguments> registerJsonProvider() throws Exception {
        Properties properties = new Properties();
        String path;

        try (InputStream config = BaseTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(config);
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        path = properties.getProperty("registerJson");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<RegisterTestDTO>>() {}.getType();

        List<RegisterTestDTO> data = gson.fromJson(new FileReader(Paths.get(path).toFile()), listType);

        return data.stream().map(Arguments::of);
    }
}
