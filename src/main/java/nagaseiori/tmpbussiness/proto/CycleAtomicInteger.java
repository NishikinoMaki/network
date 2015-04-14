package nagaseiori.tmpbussiness.proto;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 循环计算器
 * 
 * @author cancheung
 * 
 */
public class CycleAtomicInteger {
	private final static long PARK_TIME = 1000L * 1000;
	private AtomicInteger counter = new AtomicInteger(0);
	private final int range;
	private final int base;

	/**
	 * 
	 * @param base
	 *            基数
	 * @param range
	 *            上下浮动值
	 */
	public CycleAtomicInteger(int base, int range) {
		if (range < 2)
			throw new IllegalArgumentException();
		this.range = range;
		this.base = base;
	}

	/**
	 * 获取下个原子值
	 * 
	 * @return
	 */
	public int next() {
		for (;;) {
			int c = counter.get();
			int next = (c + 1) % range;
			if (counter.compareAndSet(c, next)) {
				return base+c;
			} else {
				LockSupport.parkNanos(PARK_TIME);
			}
		}
	}
}