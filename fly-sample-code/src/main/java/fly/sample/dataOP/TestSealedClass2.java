package fly.sample.dataOP;

import java.nio.file.Path;

public class TestSealedClass2 {
    public static void main(String[] args) {

    }
}
sealed interface Option {
    record InputFile(Path path) implements Option { }
    record OutputFile(Path path) implements Option { }
    record MaxLines(int maxLines) implements Option { }
    record PrintLineNumbers() implements Option { }
}