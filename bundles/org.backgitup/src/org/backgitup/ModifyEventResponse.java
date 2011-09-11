package org.backgitup;

import java.nio.file.Path;

final class ModifyEventResponse extends EventResponse {

	ModifyEventResponse(Path target) {
		super(target);
	}

	@Override
	public void run() {
		System.out.println("WatchDir.ModifyEventResponse.doRun("
				+ getTarget() + ")");
	}
}