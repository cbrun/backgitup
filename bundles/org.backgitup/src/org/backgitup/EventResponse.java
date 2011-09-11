package org.backgitup;

import java.nio.file.Path;

abstract class EventResponse implements Runnable {

	private Path fTarget;

	EventResponse(Path target) {
		fTarget = target;
	}

	public Path getTarget() {
		return fTarget;
	}
}