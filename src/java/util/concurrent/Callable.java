/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package java.util.concurrent;

/**
 * A task that returns a result and may throw an exception. Implementors define
 * a single method with no arguments called {@code call}.
 *
 * <p>
 * The {@code Callable} interface is similar to {@link java.lang.Runnable}, in
 * that both are designed for classes whose instances are potentially executed
 * by another thread. A {@code Runnable}, however, does not return a result and
 * cannot throw a checked exception.
 *
 * <p>
 * The {@link Executors} class contains utility methods to convert from other
 * common forms to {@code Callable} classes.
 * 
 * 
 * Callable与Runnalbe的区别:</br>
 * 1、Callable接口的call()方法可以有返回值，而Runnable接口的run()方法没有返回值</br>
 * 2、Callable接口的call()方法可以声明抛出异常，而Runnable接口的run()方法不能声明抛出异常
 * 
 *
 * @see Executor
 * @since 1.5
 * @author Doug Lea
 * @param <V> the result type of method {@code call}
 */
@FunctionalInterface
public interface Callable<V> {
	/**
	 * Computes a result, or throws an exception if unable to do so.
	 *
	 * @return computed result
	 * @throws Exception if unable to compute a result
	 */
	V call() throws Exception;
}
