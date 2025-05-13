package fnote.infrastructure.impl.file;

import fly4j.common.file.FileUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class RedirectPathServiceImpl extends PathServiceImpl {
    @Override
    public Path getUserDir(String pin) {
        File redirectFile = super.getUserDir(pin).resolve("linkToNewAddress.txt").toFile();
        if (redirectFile.exists()) {
            try {
                String directDest = FileUtils.readFileToString(redirectFile, StandardCharsets.UTF_8);
                return Path.of(directDest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.getUserDir(pin);
    }
}
