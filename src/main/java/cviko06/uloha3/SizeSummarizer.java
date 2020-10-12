package cviko06.uloha3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SizeSummarizer {

    public static final String START_DIR = "/Users/olga_charna/Documents";
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        File rootDir = new File(START_DIR);
        System.out.println("Pocet vlakien " + THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CompletionService<DirSize> completionService = new ExecutorCompletionService<>(executor);

        long start = System.nanoTime();
        File[] files = rootDir.listFiles();
        int dirCount = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                SizeTask sizeTask = new SizeTask(files[i]);
                completionService.submit(sizeTask);
                dirCount++;
            }
        }
        for (int i = 0; i < dirCount; i++) {
            DirSize dirSize = completionService.take().get();
            System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
        }
        executor.shutdown();
    }

}
