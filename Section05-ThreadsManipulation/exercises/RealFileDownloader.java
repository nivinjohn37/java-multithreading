package exercises;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class RealFileDownloader {

    public static void main(String[] args) throws InterruptedException {
        // 3 example files (you can change these to real URLs)
        List<String> urls = Arrays.asList(
                "https://speed.hetzner.de/100MB.bin",
                "https://speed.hetzner.de/50MB.bin",
                "https://speed.hetzner.de/10MB.bin"
        );

        Thread[] threads = new Thread[urls.size()];
        AtomicLong totalBytesDownloaded = new AtomicLong(0);
        long[] fileSizes = new long[urls.size()];

        // Pre-fetch file sizes (HEAD requests)
        for (int i = 0; i < urls.size(); i++) {
            try {
                URL url = new URL(urls.get(i));
                fileSizes[i] = url.openConnection().getContentLengthLong();
            } catch (IOException e) {
                System.err.println("Couldn't get size for " + urls.get(i));
                fileSizes[i] = 0;
            }
        }

        long totalSize = Arrays.stream(fileSizes).sum();

        // 🧩 Progress Monitor Daemon
        Thread monitor = new Thread(() -> {
            while (true) {
                long downloaded = totalBytesDownloaded.get();
                int percent = (int) ((downloaded * 100.0) / totalSize);
                System.out.printf("[Monitor] Total progress: %d%% (%d/%d MB)%n",
                        percent, downloaded / (1024 * 1024), totalSize / (1024 * 1024));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }, "ProgressMonitor");

        monitor.setDaemon(true);
        monitor.start();

        // 🧱 Create downloader threads
        for (int i = 0; i < urls.size(); i++) {
            final int fileIndex = i;
            String urlStr = urls.get(i);
            threads[i] = new Thread(() -> {
                String outputName = "download_" + (fileIndex + 1) + ".bin";
                try (InputStream in = new BufferedInputStream(new URL(urlStr).openStream());
                     FileOutputStream out = new FileOutputStream(outputName)) {

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        totalBytesDownloaded.addAndGet(bytesRead);
                    }
                    System.out.println(Thread.currentThread().getName() + " finished downloading " + outputName);

                } catch (IOException e) {
                    System.err.println(Thread.currentThread().getName() + " failed: " + e.getMessage());
                }
            }, "Downloader-" + (fileIndex + 1));
        }

        // Start threads
        Arrays.stream(threads).forEach(Thread::start);

        // Wait for all
        for (Thread t : threads) t.join();

        System.out.println("✅ All downloads completed!");
    }
}
