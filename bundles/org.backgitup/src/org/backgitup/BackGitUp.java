package org.backgitup;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		new FolderWatcher(dir, true).run();
	}
}
