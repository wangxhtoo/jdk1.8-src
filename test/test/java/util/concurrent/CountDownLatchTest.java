package test.java.util.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(5);

		List<Thread> threads = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			CDThread thread = new CDThread(latch);
			threads.add(thread);
		}

		for (Thread t : threads) {
			t.start();
		}

		latch.await();

		System.out.println("结束：" + latch.getCount());
	}

}

class CDThread extends Thread {

	private CountDownLatch latch;

	public CDThread(CountDownLatch latch) {
		super();
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			System.out.println(getName() + " --- start");
			TimeUnit.SECONDS.sleep(new Random().nextInt(10));
			latch.countDown();
			System.out.println(getName() + " 剩余线程：" + latch.getCount());
			System.out.println(getName() + " --- end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}