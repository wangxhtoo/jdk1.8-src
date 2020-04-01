/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

import java.lang.annotation.*;

/**
 * 
 * 一个接口只有一个方法定义，则成为函数式接口
 * 
 * 
 * An informative annotation type used to indicate that an interface type
 * declaration is intended to be a <i>functional interface</i> as defined by the
 * Java Language Specification.
 *
 * Conceptually, a functional interface has exactly one abstract method. Since
 * {@linkplain java.lang.reflect.Method#isDefault() default methods} have an
 * implementation, they are not abstract. If an interface declares an abstract
 * method overriding one of the public methods of {@code java.lang.Object}, that
 * also does <em>not</em> count toward the interface's abstract method count
 * since any implementation of the interface will have an implementation from
 * {@code java.lang.Object} or elsewhere.
 *
 * <p>
 * Note that instances of functional interfaces can be created with lambda
 * expressions, method references, or constructor references.
 * 注意：函数式接口可以通过lambda表达式、方法引用或构造方法引用来创建实例
 *
 * <p>
 * If a type is annotated with this annotation type, compilers are required to
 * generate an error message unless: </br>
 * 如果一个被注解了该注解的类型，编译的时候不满足一下信息会被报告一个错误信息
 *
 * <ul>
 * <li>The type is an interface type and not an annotation type, enum, or class.
 * <li>
 * <li>The annotated type satisfies the requirements of a functional interface.
 * </ul>
 *
 * <p>
 * However, the compiler will treat any interface meeting the definition of a
 * functional interface as a functional interface regardless of whether or not a
 * {@code FunctionalInterface} annotation is present on the interface
 * declaration.
 * 
 * 总结： </br>
 * 如果一个接口只有一个抽象方法，那么该接口就是一个函数式接口
 * 如果我们在某个接口上声明了FunctionalInterface注解，那么编译器就会按照函数式接口的定义来要求该接口
 * 如果某个接口只有一个抽象方法，但我们并没有给该接口声明FunctionalInterface注解，那么编译器依旧会将该接口看作是函数式接口
 *
 * @jls 4.3.2. The Class Object
 * @jls 9.8 Functional Interfaces
 * @jls 9.4.3 Interface Method Body
 * @since 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FunctionalInterface {
}
