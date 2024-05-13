package fly.sample.file;

import java.io.InputStream;
import java.util.Properties;

public class ClassPathFile {
    public static void main(String[] args) throws Exception {
        InputStream input = ClassPathFile.class.getResourceAsStream("/message.properties");
        Properties properties = new Properties();
        properties.load(input);
        System.out.println(properties.getProperty("hello"));
    }
}
