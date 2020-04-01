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

import java.util.List;
import java.util.Collection;

/**
 * An {@link Executor} that provides methods to manage termination and methods
 * that can produce a {@link Future} for tracking progress of one or more
 * asynchronous tasks.
 *
 * <p>
 * An {@code ExecutorService} can be shut down, which will cause it to reject
 * new tasks. Two different methods are provided for shutting down an
 * {@code ExecutorService}. The {@link #shutdown} method will allow previously
 * submitted tasks to execute before terminating, while the {@link #shutdownNow}
 * method prevents waiting tasks from starting and attempts to stop currently
 * executing tasks. Upon termination, an executor has no tasks actively
 * executing, no tasks awaiting execution, and no new tasks can be submitted. An
 * unused {@code ExecutorService} should be shut down to allow reclamation of
 * its resources.
 *
 * <p>
 * Method {@code submit} extends base method {@link Executor#execute(Runnable)}
 * by creating and returning a {@link Future} that can be used to cancel
 * execution and/or wait for completion. Methods {@code invokeAny} and
 * {@code invokeAll} perform the most commonly useful forms of bulk execution,
 * executing a collection of tasks and then waiting for at least one, or all, to
 * complete. (Class {@link ExecutorCompletionService} can be used to write
 * customized variants of these methods.)
 *
 * <p>
 * The {@link Executors} class provides factory methods for the executor
 * services provided in this package.
 *
 * <h3>Usage Examples</h3>
 *
 * Here is a sketch of a network service in which threads in a thread pool
 * service incoming requests. It uses the preconfigured
 * {@link Executors#newFixedThreadPool} factory method:
 *
 * <pre>
 * {
 * 	&#64;code
 * 	class NetworkService implements Runnable {
 * 		private final ServerSocket serverSocket;
 * 		private final ExecutorService pool;
 *
 * 		public NetworkService(int port, int poolSize) throws IOException {
 * 			serverSocket = new ServerSocket(port);
 * 			pool = Executors.newFixedThreadPool(poolSize);
 * 		}
 *
 * 		public void run() { // run the service
 * 			try {
 * 				for (;;) {
 * 					pool.execute(new Handler(serverSocket.accept()));
 * 				}
 * 			} catch (IOException ex) {
 * 				pool.shutdown();
 * 			}
 * 		}
 * 	}
 *
 * 	class Handler implements Runnable {
 * 		private final Socket socket;
 * 
 * 		Handler(Socket socket) {
 * 			this.socket = socket;
 * 		}
 * 
 * 		public void run() {
 * 			// read and service request on socket
 * 		}
 * 	}
 * }
 * </pre>
 *
 * The following method shuts down an {@code ExecutorService} in two phases,
 * first by calling {@code shutdown} to reject incoming tasks, and then calling
 * {@code shutdownNow}, if necessary, to cancel any lingering tasks:
 *
 * <pre>
 *  {@code
 * void shutdownAndAwaitTermination(ExecutorService pool) {
 *   pool.shutdown(); // Disable new tasks from being submitted
 *   try {
 *     // Wait a while for existing tasks to terminate
 *     if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
 *       pool.shutdownNow(); // Cancel currently executing tasks
 *       // Wait a while for tasks to respond to being cancelled
 *       if (!pool.awaitTermination(60, TimeUnit.SECONDS))
 *           System.err.println("Pool did not terminate");
 *     }
 *   } catch (InterruptedException ie) {
 *     // (Re-)Cancel if current thread also interrupted
 *     pool.shutdownNow();
 *     // Preserve interrupt status
 *     Thread.currentThread().interrupt();
 *   }
 * }}
 * </pre>
 *
 * <p>
 * Memory consistency effects: Actions in a thread prior to the submission of a
 * {@code Runnable} or {@code Callable} task to an {@code ExecutorService}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a> any
 * actions taken by that task, which in turn <i>happen-before</i> the result is
 * retrieved via {@code Future.get()}.
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface ExecutorService extends Executor {

	/**
	 * Initiates an orderly shutdown in which previously submitted tasks are
	 * executed, but no new tasks will be accepted. Invocation has no additional
	 * effect if already shut down.
	 *
	 *
	 * <p>
	 * This method does not wait for previously submitted tasks to complete
	 * execution. Use {@link #awaitTermination awaitTermination} to do that.
	 *
	 * @throws SecurityException if a security manager exists and shutting down this
	 *                           ExecutorService may manipulate threads that the
	 *                           caller is not permitted to modify because it does
	 *                           not hold
	 *                           {@link java.lang.RuntimePermission}{@code ("modifyThread")},
	 *                           or the security manager's {@code checkAccess}
	 *                           method denies access.
	 * 
	 *                           平滑地关闭线程池，已经提交到线程池中的任务会继续执行完。
	 */
	void shutdown();

	/**
	 * Attempts to stop all actively executing tasks, halts the processing of
	 * waiting tasks, and returns a list of the tasks that were awaiting execution.
	 *
	 * <p>
	 * This method does not wait for actively executing tasks to terminate. Use
	 * {@link #awaitTermination awaitTermination} to do that.
	 *
	 * <p>
	 * There are no guarantees beyond best-effort attempts to stop processing
	 * actively executing tasks. For example, typical implementations will cancel
	 * via {@link Thread#interrupt}, so any task that fails to respond to interrupts
	 * may never terminate.
	 *
	 * @return list of tasks that never commenced execution
	 * @throws SecurityException if a security manager exists and shutting down this
	 *                           ExecutorService may manipulate threads that the
	 *                           caller is not permitted to modify because it does
	 *                           not hold
	 *                           {@link java.lang.RuntimePermission}{@code ("modifyThread")},
	 *                           or the security manager's {@code checkAccess}
	 *                           method denies access.
	 * 
	 *                           立即关闭线程池，返回还没有开始执行的任务列表。 会尝试中断正在执行的任务（每个线程调用
	 *                           interruput方法），但这个行为不一定会成功。
	 */
	List<Runnable> shutdownNow();

	/**
	 * Returns {@code true} if this executor has been shut down.
	 *
	 * @return {@code true} if this executor has been shut down
	 * 
	 *         判断线程池是否已经关闭
	 */
	boolean isShutdown();

	/**
	 * Returns {@code true} if all tasks have completed following shut down. Note
	 * that {@code isTerminated} is never {@code true} unless either
	 * {@code shutdown} or {@code shutdownNow} was called first.
	 *
	 * @return {@code true} if all tasks have completed following shut down
	 * 
	 *         判断线程池的任务是否已经执行完毕。
	 *         注意此方法调用之前需要先调用shutdown()方法或者shutdownNow()方法，否则总是会返回false
	 */
	boolean isTerminated();

	/**
	 * Blocks until all tasks have completed execution after a shutdown request, or
	 * the timeout occurs, or the current thread is interrupted, whichever happens
	 * first.
	 *
	 * @param timeout the maximum time to wait
	 * @param unit    the time unit of the timeout argument
	 * @return {@code true} if this executor terminated and {@code false} if the
	 *         timeout elapsed before termination
	 * @throws InterruptedException if interrupted while waiting
	 * 
	 *                              判断线程池的任务是否都执行完。
	 *                              如果没有任务没有执行完毕则阻塞，直至任务完成或者达到了指定的timeout时间就会返回
	 */
	boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Submits a value-returning task for execution and returns a Future
	 * representing the pending results of the task. The Future's {@code get} method
	 * will return the task's result upon successful completion.
	 *
	 * <p>
	 * If you would like to immediately block waiting for a task, you can use
	 * constructions of the form {@code result = exec.submit(aCallable).get();}
	 *
	 * <p>
	 * Note: The {@link Executors} class includes a set of methods that can convert
	 * some other common closure-like objects, for example,
	 * {@link java.security.PrivilegedAction} to {@link Callable} form so they can
	 * be submitted.
	 *
	 * @param task the task to submit
	 * @param      <T> the type of the task's result
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException if the task cannot be scheduled for
	 *                                    execution
	 * @throws NullPointerException       if the task is null
	 * 
	 *                                    提交带有一个返回值的任务到线程池中去执行（回调），返回的 Future
	 *                                    表示任务的待定结果。 当任务成功完成后，通过 Future 实例的 get()
	 *                                    方法可以获取该任务的结果。 Future 的 get() 方法是会阻塞的。
	 */
	<T> Future<T> submit(Callable<T> task);

	/**
	 * Submits a Runnable task for execution and returns a Future representing that
	 * task. The Future's {@code get} method will return the given result upon
	 * successful completion.
	 *
	 * @param task   the task to submit
	 * @param result the result to return
	 * @param        <T> the type of the result
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException if the task cannot be scheduled for
	 *                                    execution
	 * @throws NullPointerException       if the task is null
	 * 
	 *                                    提交一个Runnable的任务，当任务完成后，可以通过Future.get()获取的是提交时传递的参数T
	 *                                    result
	 */
	<T> Future<T> submit(Runnable task, T result);

	/**
	 * Submits a Runnable task for execution and returns a Future representing that
	 * task. The Future's {@code get} method will return {@code null} upon
	 * <em>successful</em> completion.
	 * 
	 * 提交一个Runnable的人无语，它的Future.get()得不到任何内容，它返回值总是Null。
	 * 
	 * 为什么有这个方法？为什么不直接设计成void submit(Runnable task)这种方式？
	 * 这是因为Future除了get这种获取任务信息外，还可以控制任务， 具体体现在 Future的这个方法上：boolean cancel(boolean
	 * mayInterruptIfRunning) 这个方法能够去取消提交的Rannable任务。
	 *
	 * @param task the task to submit
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException if the task cannot be scheduled for
	 *                                    execution
	 * @throws NullPointerException       if the task is null
	 */
	Future<?> submit(Runnable task);

	/**
	 * Executes the given tasks, returning a list of Futures holding their status
	 * and results when all complete. {@link Future#isDone} is {@code true} for each
	 * element of the returned list. Note that a <em>completed</em> task could have
	 * terminated either normally or by throwing an exception. The results of this
	 * method are undefined if the given collection is modified while this operation
	 * is in progress.
	 * 
	 * 执行一组给定的Callable任务，返回对应的Future列表。列表中每一个Future都将持有该任务的结果和状态。
	 * 当所有任务执行完毕后，方法返回，此时并且每一个Future的isDone()方法都是true。 完成的任务可能是正常结束，也可以是异常结束
	 * 如果当任务执行过程中，tasks集合被修改了，那么方法的返回结果将是不确定的， 即不能确定执行的是修改前的任务，还是修改后的任务
	 *
	 * @param tasks the collection of tasks
	 * @param       <T> the type of the values returned from the tasks
	 * @return a list of Futures representing the tasks, in the same sequential
	 *         order as produced by the iterator for the given task list, each of
	 *         which has completed
	 * @throws InterruptedException       if interrupted while waiting, in which
	 *                                    case unfinished tasks are cancelled
	 * @throws NullPointerException       if tasks or any of its elements are
	 *                                    {@code null}
	 * @throws RejectedExecutionException if any task cannot be scheduled for
	 *                                    execution
	 */
	<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;

	/**
	 * Executes the given tasks, returning a list of Futures holding their status
	 * and results when all complete or the timeout expires, whichever happens
	 * first. {@link Future#isDone} is {@code true} for each element of the returned
	 * list. Upon return, tasks that have not completed are cancelled. Note that a
	 * <em>completed</em> task could have terminated either normally or by throwing
	 * an exception. The results of this method are undefined if the given
	 * collection is modified while this operation is in progress.
	 * 
	 * 执行一组给定的Callable任务，返回对应的Future列表。列表中每一个Future都将持有该任务的结果和状态。
	 * 当所有任务执行完毕后或者超时后，方法将返回，此时并且每一个Future的isDone()方法都是true。
	 * 一旦方法返回，未执行完成的任务被取消，而完成的任务可能正常结束或者异常结束， 完成的任务可以是正常结束，也可以是异常结束
	 * 如果当任务执行过程中，tasks集合被修改了，那么方法的返回结果将是不确定的
	 *
	 * @param tasks   the collection of tasks
	 * @param timeout the maximum time to wait
	 * @param unit    the time unit of the timeout argument
	 * @param         <T> the type of the values returned from the tasks
	 * @return a list of Futures representing the tasks, in the same sequential
	 *         order as produced by the iterator for the given task list. If the
	 *         operation did not time out, each task will have completed. If it did
	 *         time out, some of these tasks will not have completed.
	 * @throws InterruptedException       if interrupted while waiting, in which
	 *                                    case unfinished tasks are cancelled
	 * @throws NullPointerException       if tasks, any of its elements, or unit are
	 *                                    {@code null}
	 * @throws RejectedExecutionException if any task cannot be scheduled for
	 *                                    execution
	 */
	<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Executes the given tasks, returning the result of one that has completed
	 * successfully (i.e., without throwing an exception), if any do. Upon normal or
	 * exceptional return, tasks that have not completed are cancelled. The results
	 * of this method are undefined if the given collection is modified while this
	 * operation is in progress.
	 * 
	 * 执行一组给定的Callable任务，当成功执行完（没抛异常）一个任务后此方法便返回，返回的是该任务的结果
	 * 一旦此正常返回或者异常结束，未执行的任务都会被取消。 如果当任务执行过程中，tasks集合被修改了，那么方法的返回结果将是不确定的
	 *
	 * @param tasks the collection of tasks
	 * @param       <T> the type of the values returned from the tasks
	 * @return the result returned by one of the tasks
	 * @throws InterruptedException       if interrupted while waiting
	 * @throws NullPointerException       if tasks or any element task subject to
	 *                                    execution is {@code null}
	 * @throws IllegalArgumentException   if tasks is empty
	 * @throws ExecutionException         if no task successfully completes
	 * @throws RejectedExecutionException if tasks cannot be scheduled for execution
	 */
	<T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException;

	/**
	 * Executes the given tasks, returning the result of one that has completed
	 * successfully (i.e., without throwing an exception), if any do before the
	 * given timeout elapses. Upon normal or exceptional return, tasks that have not
	 * completed are cancelled. The results of this method are undefined if the
	 * given collection is modified while this operation is in progress.
	 * 
	 * 执行一组给定的Callable任务，当在timeout（超时）之前成功执行完（没抛异常）一个任务后此方法便返回，返回的是该任务的结果
	 * 一旦此正常返回或者异常结束，未执行的任务都会被取消。 如果当任务执行过程中，tasks集合被修改了，那么方法的返回结果将是不确定的
	 *
	 * @param tasks   the collection of tasks
	 * @param timeout the maximum time to wait
	 * @param unit    the time unit of the timeout argument
	 * @param         <T> the type of the values returned from the tasks
	 * @return the result returned by one of the tasks
	 * @throws InterruptedException       if interrupted while waiting
	 * @throws NullPointerException       if tasks, or unit, or any element task
	 *                                    subject to execution is {@code null}
	 * @throws TimeoutException           if the given timeout elapses before any
	 *                                    task successfully completes
	 * @throws ExecutionException         if no task successfully completes
	 * @throws RejectedExecutionException if tasks cannot be scheduled for execution
	 */
	<T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
