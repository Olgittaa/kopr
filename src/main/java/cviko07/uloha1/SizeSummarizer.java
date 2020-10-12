package cviko07.uloha1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class SizeSummarizer {

    public static final String START_DIR = "/Users/olga_charna/Documents";
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private DirSize dirSize;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
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
            } catch (Exception e) {
                if (e.getCause() instanceof DirectoryForbiddenException) {
                    DirectoryForbiddenException ex = (DirectoryForbiddenException) e.getCause();
                    System.out.println("adresar " + ex.getDir().getAbsolutePath() + " nepusti");
                }
            }
        }
        forkJoinPool.shutdown();
    }

}
