

import java.util.concurrent.atomic.AtomicLong;

public class IDGenerator {
	private final AtomicLong id = new AtomicLong(System.currentTimeMillis()-1525258511000l);
	
	private final static IDGenerator instance = new IDGenerator();
	
	private IDGenerator() {}
	
	public long getID() {
		return id.incrementAndGet();
	}

	public static IDGenerator getInstance() {
		return instance;
	}
}
