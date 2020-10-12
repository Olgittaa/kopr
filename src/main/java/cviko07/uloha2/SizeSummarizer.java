package cviko07.uloha2;

import cviko07.uloha1.DirectoryForbiddenException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class SizeSummarizer {

    public static final String START_DIR = "/Users/olga_charna/Documents";
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private DirSize dirSize;

    public static void main(String[] args) {
        File rootDir = new File(START_DIR);
        System.out.println("Pocet vlakien " + THREAD_COUNT);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        long start = System.nanoTime();
        File[] files = rootDir.listFiles();
        List<SizeTask> tasks = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                SizeTask sizeTask = new SizeTask(files[i]);
                forkJoinPool.submit(sizeTask);
                tasks.add(sizeTask);
            }
        }
        for (SizeTask task : tasks) {
            DirSize dirSize = null;
            try {
                dirSize = task.join();
                System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
            } catch (DirectoryForbiddenException e) {
                System.out.println("adresar " + e.getDir().getAbsolutePath() + " nepusti");
            }
        }
        forkJoinPool.shutdown();
    }

}
