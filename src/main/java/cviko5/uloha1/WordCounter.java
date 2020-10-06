package cviko5.uloha1;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class WordCounter {

    public static final String START_DIR = "/Users/olga_charna/Documents/Учеба/I.Л./Paz1b";


    public static final int ANALIZERS_COUNT = 4;
    public static final int SEARCHERS_COUNT = 4;

    public static void main(String[] args) throws InterruptedException {
        File dir = new File(START_DIR);
        BlockingQueue<File> filesToAnalyze = new LinkedBlockingQueue<>();
        ConcurrentMap<String, Integer> words = new ConcurrentHashMap<>();
        long start = System.nanoTime();

        BlockingDeque<File>[] deques = new BlockingDeque[SEARCHERS_COUNT];
        for (int i = 0; i < SEARCHERS_COUNT; i++) {
            deques[i] = new LinkedBlockingDeque<>();
        }
        deques[0].offerFirst(dir);


        for (int i = 0; i < SEARCHERS_COUNT; i++) {
            Searcher searcher = new Searcher(filesToAnalyze, deques, i);
            Thread searcherWorker = new Thread(searcher);
            searcherWorker.start();
        }

        CountDownLatch gate = new CountDownLatch(ANALIZERS_COUNT);

        FileAnalyzer a = new FileAnalyzer(filesToAnalyze, words, gate);
        for (int i = 0; i < ANALIZERS_COUNT; i++) {
            Thread analizeWorker = new Thread(a);
            analizeWorker.start();
        }
        gate.await();
        System.out.println("Running time: " + (System.nanoTime() - start) / 1000000.0 + " ms");
        printTop20Words(words);
    }

    private static void printTop20Words(Map<String, Integer> words) {
        PriorityQueue<Entry<String, Integer>> sortedWords =
                new PriorityQueue<Entry<String, Integer>>(
                        words.size(), new Comparator<Entry<String, Integer>>() {

                    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });

        for (Entry<String, Integer> entry : words.entrySet()) {
            sortedWords.add(entry);
        }
        int min = Math.min(20, sortedWords.size());

        for (int i = 0; i < min; i++) {
            System.out.print(i + ": " + sortedWords.poll() + ", ");
        }
        System.out.println();
    }


}
