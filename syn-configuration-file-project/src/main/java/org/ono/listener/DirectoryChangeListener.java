package org.ono.listener;

import com.alibaba.fastjson.JSON;
import org.ono.exception.*;
import org.ono.services.IContextType;
import org.ono.services.impl.Reporter;
import org.ono.services.impl.Storage;
import org.ono.support.spring.AppContext;
import org.ono.utils.Constants;
import org.ono.utils.Extensions;
import org.ono.utils.FileUtils;
import org.ono.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ono on 2018/11/23.
 */
public class DirectoryChangeListener implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryChangeListener.class);

    private WatchService watchService;
    private List<Path> filePaths;
    private List<String> excludeFiles;
    private String type;

    public DirectoryChangeListener(List<Path> filePaths, List<String> excludeFiles, String type) {

        this.type = type;
        this.filePaths = new ArrayList<>(filePaths.size());
        this.filePaths.addAll(filePaths);

        if (!CollectionUtils.isEmpty(excludeFiles)) {
            this.excludeFiles = new ArrayList<>(excludeFiles.size());
            this.excludeFiles.addAll(excludeFiles);
        } else {
            this.excludeFiles = new ArrayList<>();
        }

    }

    private void updateData(final Path path, final String fileName) throws Exception {

        IContextType contextType = null;
        String type = FileUtils.findFileType(fileName);
        if (Constants.unmodifiableTypesList.contains(type)) {
            contextType = Extensions.getFactory(IContextType.class, type);
            contextType.updateConfig(path, excludeFiles, type);
            contextType.storageConfigMap();
        }
        Reporter reporter = AppContext.getBean("reporter");
        final Map<String, String> m = new HashMap<String, String>() {{
            put("hostName", ((Storage) AppContext.getBean("storage")).getHostname());
            if (StringUtils.isBlank(fileName)) {
                put("path", path.toString());
            }else {
                put("path", path.resolve(fileName).toString());
                put("fileName", fileName);
                put("fileType", FileUtils.findFileType(fileName).toLowerCase());
                put("context", new String(Files.readAllBytes(path.resolve(fileName)), Constants.ENCODING));
            }

        }};

        reporter.trigger(JSON.toJSONString(new HashMap<String, Object>() {{
            put("params", m);
        }}));

    }

    public void makeWatch() {
        try {
            for (Path targetPath : filePaths) {
                watchService = targetPath.getFileSystem().newWatchService();
                Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {
            LOG.error("makeWatch error: " + e.getMessage());
        }

    }

    @Override
    public void run() {
        makeWatch();
        WatchKey watchKey = null;
        String fileKey = "";
        long time = 0l;
        while (true) {
            try {
                watchKey = watchService.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (final WatchEvent<?> event : watchEvents) {
                    WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                    WatchEvent.Kind<Path> kind = watchEvent.kind();
                    Path watchable = ((Path) watchKey.watchable()).resolve(watchEvent.context());
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                        if (Files.isDirectory(watchable)) {
                            watchable.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                            updateData(watchable, null);
                        }
                    } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        if (Files.isRegularFile(watchable)) {
                            Path p = (Path) watchKey.watchable();
                            LOG.info("file {} changed", watchEvent.context());
                            String filePath = watchable.toString();
                            long t = System.currentTimeMillis();
                            if (!fileKey.equals(filePath) || time > (t + Constants.TIME_INTERVAL)) {
                                updateData(p, watchEvent.context().getFileName().toString());
                                fileKey = filePath;
                                time = t;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Directory Change Listener error :" + e.getMessage());
                continue;
            } finally {
                if (watchKey != null) {
                    boolean flag = watchKey.reset();
                    if (!flag) {
                        LOG.info("FileChangeEventThread quit!");
                        break;
                    }
                }
            }
        }
    }
}
