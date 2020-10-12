package cviko07.uloha2;

import cviko07.uloha1.DirectoryForbiddenException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class SizeTask extends RecursiveTask<DirSize> {
    private File dir;

    public SizeTask(File dir) {
        this.dir = dir;
    }

    private long analyzeDir(File dir) throws DirectoryForbiddenException {
        long sumSize = 0;
        File[] files = dir.listFiles();
        if (files == null) throw new DirectoryForbiddenException(dir);
        List<SizeTask> tasks = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile())
                sumSize += files[i].length();
            if (files[i].isDirectory()) {
                SizeTask task = new SizeTask(files[i]);
                task.fork();
                tasks.add(task);
            }
        }
        for (SizeTask task : tasks) {
            sumSize += task.join().getSize();
        }
        return sumSize;
    }

    @Override
    public DirSize compute() {
        return new DirSize(dir, analyzeDir(dir));
    }
}
