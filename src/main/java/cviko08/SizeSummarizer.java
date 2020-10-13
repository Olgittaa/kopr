package cviko08;

import java.util.concurrent.*;

public class SizeSummarizer {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        SizeSummarizerTask task = new SizeSummarizerTask();
        long start = System.nanoTime();
        Future<?> future = executor.submit(task);
        try {
            future.get(1, TimeUnit.SECONDS);
            System.out.println("vsetko sme stihli prehliadat");
        } catch (TimeoutException e) {
            // úloha ukončená po timeoute
            System.out.println("skoncili sme predcasne");
        } catch (ExecutionException e) {
            // úloha vyhodila výnimku, vyhodíme ju tiež
            System.out.println("prehliadavanie disku skoncilo chybou");
        } catch (InterruptedException e) {
            System.out.println("niekto vypol cely program");
            e.printStackTrace();
        } finally {
            // nastaví interrupt, ak úloha ešte beží
            future.cancel(true);
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long duration = System.nanoTime() - start;
            System.out.println("celkovy cas " + duration / 1000000.0 + " ms");

        }
    }
}
