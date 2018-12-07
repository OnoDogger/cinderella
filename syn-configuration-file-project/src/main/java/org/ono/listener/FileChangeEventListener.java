package org.ono.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;

/**
 * Created by ono on 2018/11/21.
 */
public class FileChangeEventListener implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileChangeEventListener.class);

    private WatchService watcher;

    private Path watchedFile;

    public FileChangeEventListener(WatchService watcher, Path watchedFile) {
        this.watcher = watcher;
        this.watchedFile = watchedFile;
    }

    @Override
    public void run() {
        while (true){
            WatchKey key;
            try{
                key = watcher.take();
            }catch (InterruptedException e){
                return;
            }
            for (WatchEvent<?> event: key.pollEvents()){
                WatchEvent.Kind<?> kind = event.kind();
                if (kind != StandardWatchEventKinds.ENTRY_MODIFY){
                    continue;
                }
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();
                LOGGER.info("file {} change.",fileName);
            }
            boolean bool = key.reset();
            if (!bool){
                break;
            }
        }
    }
}
