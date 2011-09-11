package org.backgitup;

import java.nio.file.Path;

final class CreateEventResponse extends EventResponse {

	CreateEventResponse(Path target) {
		super(target);
	}

	@Override
	public void run() {
		System.out.println("WatchDir.CreateEventResponse.doRun("
				+ getTarget() + ")");
	}
}