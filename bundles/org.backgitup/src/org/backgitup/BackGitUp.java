package org.backgitup;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.Git;

public class BackGitUp {

	static void usage() {
		System.err.println("usage: java BackGitUp dir");
		System.exit(-1);
	}

	public static void main(String[] args) throws IOException {
		// parse arguments
		if (args.length != 1)
			usage();

		// register directory and process its events
		Path dir = Paths.get(args[0]);
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		Git git = GitUtils.initOrOpenGit(dir);
		GitUtils.addAll(git);
		GitUtils.pull(git);
		
		FolderWatcher folderWatcher = new FolderWatcher(dir, new EventResponseFactory(), true);
		executor.execute(folderWatcher);
		
		ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		final GitPush command = new GitPush();
		ScheduledFuture<?> future = scheduledExecutor.schedule(command, 50, TimeUnit.SECONDS);
		future.cancel(false);
	}
	
	static class GitPush implements Runnable {

		private final Git git = null;
		private final ScheduledExecutorService scheduledExecutor = null;
		
		@Override
		public void run() {
			GitUtils.push(git);
			scheduledExecutor.schedule(this, 50, TimeUnit.SECONDS);
		}
		
	}
}
