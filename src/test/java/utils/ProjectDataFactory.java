package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class ProjectDataFactory {

    public static String generateProjectName() {
        String[] names = {"Nike","Adidas","BT","OrangeHRM","Bacanie"};
        String[] works = {"Maintenance","Automation","App","Mobile","Web","Infrastructure"};
        return names[new Random().nextInt(names.length)] + works[new Random().nextInt(works.length)] + UUID.randomUUID().toString().substring(0,4);
    }

    public static String generateProjectDescription(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        return name + " project created at " + sdf.format(new Date());
    }
}
