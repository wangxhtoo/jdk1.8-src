package test.java.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 典型应用场景是：使迭代算法优化，在测试代码中模拟高并发
 * 
 * @author wangxh
 *
 */

public class CyclicBarrierTest {

	public static void main(String[] args) throws InterruptedException {
		CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
			@Override
			public void run() {
				System.out.println("================");
			}
		});

		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			CBThread t = new CBThread(barrier);
			threads.add(t);
		}
		TimeUnit.SECONDS.sleep(1);
		for (Thread t : threads) {
			t.start();
		}
		System.out.println("main end!");
	}

}

class CBThread extends Thread {

	private CyclicBarrier barrier;

	public CBThread(CyclicBarrier barrier) {
		super();
		this.barrier = barrier;
	}

	@Override
	public void run() {
		try {
			System.out.println(getName() + " --- start");
			TimeUnit.SECONDS.sleep(new Random().nextInt(10));
			System.out.println(getName() + " 等待线程数" + barrier.getNumberWaiting());
			barrier.await();
			System.out.println(getName() + " --- end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

	}

}