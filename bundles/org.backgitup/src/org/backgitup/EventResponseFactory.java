package org.backgitup;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.Path;
import java.nio.file.WatchEvent;

class EventResponseFactory {

	public EventResponse create(WatchEvent.Kind<?> eventKind, Path target) {
		EventResponse ret = null;
		if (eventKind == ENTRY_CREATE) {
			ret = new CreateEventResponse(target);
		} else if (eventKind == ENTRY_DELETE) {
			ret = new DeleteEventResponse(target);
		} else if (eventKind == ENTRY_MODIFY) {
			ret = new ModifyEventResponse(target);
		}
		return ret;
	}
	
}
