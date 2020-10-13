package cviko08;

import cviko07.uloha1.DirectoryForbiddenException;

import java.io.File;
import java.util.concurrent.Callable;

public class SizeTask implements Callable<DirSize> {
    private File dir;

    public SizeTask(File dir) {
        this.dir = dir;
    }

    private long analyzeDir(File dir) throws DirectoryForbiddenException, SizeTaskInterraptedException {
        long sumSize = 0;
        File[] files = dir.listFiles();
        if (files == null) throw new DirectoryForbiddenException(dir);
        if (Thread.currentThread().isInterrupted()) throw new SizeTaskInterraptedException(new DirSize(dir, 0));
        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile())
                    sumSize += files[i].length();
                if (files[i].isDirectory())
                    sumSize += analyzeDir(files[i]);
            }
        } catch (SizeTaskInterraptedException e) {
            throw new SizeTaskInterraptedException(e, new DirSize(dir, sumSize + e.getDirSize().getSize()));
        }
        return sumSize;
    }

    @Override
    public DirSize call() throws Exception {
        return new DirSize(dir, analyzeDir(dir));
    }
}
