package cviko07.uloha1;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class DirectoryForbiddenException extends RuntimeException {
private File dir;

    public DirectoryForbiddenException(File dir) {
        this.dir = dir;
    }

    public File getDir() {
        return dir;
    }
}
