package utils;

import java.util.Random;
import java.util.UUID;

public class TestDataFactory {

    public static String generateRandomUsername () {
        return "user" + UUID.randomUUID().toString().substring(0, 5);
    }

    public static String generateRandomName() {
        String[] names = {"Dan","Marian","Mary","Julia"};
        return names[new Random().nextInt(names.length)];
    }

    public static String generateRandomLastName() {
        String[] names = {"TheGreat","TheGreatest","PrettyGreat","Great"};
        return names[new Random().nextInt(names.length)];
    }

    public static String generateRandomPassword () {
        return UUID.randomUUID().toString().substring(0,7)+"!A";
    }

    public static String generateEmployeeId() {
        return UUID.randomUUID().toString().substring(0,8);
    }

}
