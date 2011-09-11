package org.backgitup;

import java.nio.file.Path;

final class DeleteEventResponse extends EventResponse {

	DeleteEventResponse(Path target) {
		super(target);
	}

	@Override
	public void run() {
		System.out.println("WatchDir.DeleteEventResponse.doRun("
				+ getTarget() + ")");
	}
}