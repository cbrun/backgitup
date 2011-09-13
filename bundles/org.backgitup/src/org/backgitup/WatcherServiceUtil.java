package org.backgitup;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;

final class WatcherServiceUtil {

	private WatcherServiceUtil() {
		// do nothing
	}
	
	/**
	 * Register the given directory with the WatchService
	 */
	static WatchKey register(WatchService watcher, Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		return key;
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	static WatchKey registerAll(WatchService watcher, final Path start) throws IOException {
		// register directory and sub-directories
		final FileTreeRegistrar visitor = new FileTreeRegistrar(watcher, start);
		Files.walkFileTree(start, visitor);
		return visitor.getRootKey();
	}
	
	static final class FileTreeRegistrar extends SimpleFileVisitor<Path> {
		private final Path fStart;
		private final WatchService fWatcher;
		
		private WatchKey fStartKey;

		FileTreeRegistrar(WatchService watcher, Path start) {
			fWatcher = watcher;
			fStart = start;
		}

		WatchKey getRootKey() {
			return fStartKey;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			final Path name = dir.getName(dir.getNameCount()-1);
			if (!name.toString().startsWith(".")) {
				WatchKey registeredKey = register(fWatcher, dir);
				if (dir.compareTo(fStart) == 0) {
					fStartKey = registeredKey;
				}
				
				return FileVisitResult.CONTINUE;
			}
			return FileVisitResult.SKIP_SUBTREE;
		}
	}
}
