package test.java.util.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 问：你知道 Java 的 Exchanger 吗？简单说说其特点及应用场景？
 * 
 * 答：Exchanger 是 JDK 1.5 开始提供的一个用于两个工作线程之间交换数据的封装工具类，</br>
 * 简单说就是一个线程在完成一定的事务后想与另一个线程交换数据，则第一个先拿出数据的线程会一直等待第二个线程，</br>
 * 直到第二个线程拿着数据到来时才能彼此交换对应数据。其定义为 Exchanger<V> 泛型类型，其中 V 表示可交换的数据类型，</br>
 * 对外提供的接口很简单，具体如下：
 * 
 * Exchanger()：无参构造方法。</br>
 * V exchange(V v)：等待另一个线程到达此交换点（除非当前线程被中断），然后将给定的对象传送给该线程，并接收该线程的对象。</br>
 * V exchange(V v, long timeout, TimeUnit unit)：等待另一个线程到达此交换点（除非当前线程被中断或超出了指定的等待时间），</br>
 * 然后将给定的对象传送给该线程，并接收该线程的对象。
 * 
 * 可以看出，当一个线程到达 exchange 调用点时，如果其他线程此前已经调用了此方法，则其他线程会被调度唤醒并与之进行对象交换，</br>
 * 然后各自返回；如果其他线程还没到达交换点，则当前线程会被挂起，直至其他线程到达才会完成交换并正常返回，或者当前线程被中断或超时返回。
 * 
 * 
 * @author wangxh
 *
 */

public class ExchangerTest {

	public static void main(String[] args) throws InterruptedException {

		Exchanger<Integer> exchanger = new Exchanger<>();
		new Producer("", exchanger).start();
		new Consumer("", exchanger).start();
		TimeUnit.SECONDS.sleep(10);
		System.exit(-1);

	}

}

class Producer extends Thread {

	private Exchanger<Integer> exchanger;

	private int data = 0;

	public Producer(String name, Exchanger<Integer> exchanger) {
		super("Producer-" + name);
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			try {
//				TimeUnit.SECONDS.sleep(1);
				data = 100 * (i + 1);
				System.out.println(getName() + "交换前：" + data);
				data = exchanger.exchange(data);
				System.out.println(getName() + "交换后：" + data);
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Consumer extends Thread {

	private Exchanger<Integer> exchanger;

	private int data = 0;

	public Consumer(String name, Exchanger<Integer> exchanger) {
		super("Consumer-" + name);
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		while (true) {
			try {
				data = 0;
				System.out.println(getName() + "交换前：" + data);
//				TimeUnit.SECONDS.sleep(5);
				data = exchanger.exchange(data);
//				TimeUnit.SECONDS.sleep(5);
				System.out.println(getName() + "交换后：" + data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}