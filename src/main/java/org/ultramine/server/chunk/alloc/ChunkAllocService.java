package org.ultramine.server.chunk.alloc;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.ultramine.core.service.Service;

@ThreadSafe
@Service(singleProvider = true)
public interface ChunkAllocService {
	@Nonnull
	MemSlot allocateSlot();

	long getOffHeapTotalMemory();

	long getOffHeapUsedMemory();
}
