package cviko08;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SizeSummarizerTask implements Runnable {

    public static final String START_DIR = "/Users/olga_charna/Documents";
    public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private DirSize dirSize;

    @Override
    public void run() {
        File rootDir = new File(START_DIR);
        System.out.println("Pocet vlakien " + THREAD_COUNT);
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        long start = System.nanoTime();
        File[] files = rootDir.listFiles();
        List<Future<DirSize>> futures = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                SizeTask sizeTask = new SizeTask(files[i]);
                Future<DirSize> future = executor.submit(sizeTask);
                futures.add(future);
            }
        }
        int i = 0;
        try {
            for (; i < futures.size(); i++) {
                Future<DirSize> future = futures.get(i);
                DirSize dirSize = null;
                try {
                    dirSize = future.get();
                    System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        } catch (InterruptedException e) {
            System.out.println("sizesummarizer prerusil ulohu sizesummarizertask");
            List<Runnable> notStartedtasks = executor.shutdownNow();
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                e.printStackTrace();
            }
            for (; i < futures.size(); i++) {
                Future<DirSize> future = futures.get(i);
                try {
                    if (future.isDone()) {
                        DirSize dirSize = future.get();
                        System.out.println("Čas: " + (System.nanoTime() - start) / 1000000 + " ms   " + dirSize);
                    }
                } catch (InterruptedException interruptedException) {
                    //nenastane
                    interruptedException.printStackTrace();
                } catch (ExecutionException executionException) {
                    if (executionException.getCause() instanceof DirectoryForbiddenException) {
                        DirectoryForbiddenException exception = (DirectoryForbiddenException) executionException.getCause();
                        System.out.println("adresar " + exception.getDir().getAbsolutePath() + " nas nepusti");
                    }
                    if (executionException.getCause() instanceof SizeTaskInterraptedException) {
                        SizeTaskInterraptedException exception = (SizeTaskInterraptedException) executionException.getCause();
                        System.out.println("ciastocny adresar " + exception.getDirSize());
                    }
                }
            }
            Thread.interrupted();
        }

        executor.shutdown();

    }

}
