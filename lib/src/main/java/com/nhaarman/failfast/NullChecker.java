package com.nhaarman.failfast;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * An Aspect which checks for nullity of annotated methods and parameters.<br/>
 * Parameters and methods annotated with a {@code NotNull} annotation will have their values checked,
 * and a {@link java.lang.NullPointerException} is thrown if the contract is violated.
 * <p/>
 * Supported annotations:
 * <ul>
 * <li>org.jetbrains.annotations.NotNull</li>
 * <li>android.support.annotation.NonNull</li>
 * </ul>
 */
@Aspect
@SuppressWarnings({"UnusedDeclaration", "MethodMayBeStatic", "MethodWithTooManyParameters", "StandardVariableNames"})
public class NullChecker {

    private static final String PRECONDITION_VIOLATED_MESSAGE = "Parameter \"%s\" of method \"%s\" was specified as @NotNull, but null was given.";

    private static final String POSTCONDITION_VIOLATED_MESSAGE = "Result of method \"%s\" was specified as @NotNull, but null was returned.";

    @Before(
            "(" +
                "execution(* *(@org.jetbrains.annotations.NotNull (*),..))" +
                "|| execution(* *(*, @android.support.annotation.NonNull (*),..))" +
            ") " +
            "&& args(o)"
    )
    public void checkFirstParameterNotNull(final JoinPoint joinPoint, final Object o) {
        checkPreCondition(joinPoint, o, 0);
    }

    @Before(
            "(" +
                "execution(* *(*, @org.jetbrains.annotations.NotNull (*),..)) " +
                "|| execution(* *(*, @android.support.annotation.NonNull (*),..))" +
            ") " +
            "&& args(o, p)"
    )
    public void checkSecondParameterNotNull(final JoinPoint joinPoint, final Object o, final Object p) {
        checkPreCondition(joinPoint, p, 1);
    }

    @Before(
            "(" +
                "execution(* *(*, *, @org.jetbrains.annotations.NotNull (*),..)) " +
                "|| execution(* *(*, *, @android.support.annotation.NonNull (*),..))" +
            ") " +
            "&& args(o, p, q)"
    )
    public void checkThirdParameterNotNull(final JoinPoint joinPoint, final Object o, final Object p, final Object q) {
        checkPreCondition(joinPoint, q, 2);
    }

    @Before(
            "(" +
                "execution(* *(*, *, @org.jetbrains.annotations.NotNull (*),..)) " +
                "|| execution(* *(*, *, @android.support.annotation.NonNull (*),..))" +
            ") " +
            "&& args(o, p, q, r)"
    )
    public void checkFourthParameterNotNull(final JoinPoint joinPoint, final Object o, final Object p, final Object q, final Object r) {
        checkPreCondition(joinPoint, r, 3);
    }

    @Before(
            "(" +
                "execution(* *(*, *, @org.jetbrains.annotations.NotNull (*),..)) " +
                "|| execution(* *(*, *, @android.support.annotation.NonNull (*),..))" +
            ") " +
            "&& args(o, p, q, r, s)"
    )
    public void checkFifthParameterNotNull(final JoinPoint joinPoint, final Object o, final Object p, final Object q, final Object r, final Object s) {
        checkPreCondition(joinPoint, s, 4);
    }

    @AfterReturning(
            pointcut = "@annotation(org.jetbrains.annotations.NotNull) " +
                    "|| @annotation(android.support.annotation.NonNull)",
            returning = "r"
    )
    public void checkResultNotNull(final JoinPoint joinPoint, final Object r) {
        checkPostCondition(joinPoint, r);
    }

    /**
     * Checks if parameter {@code o} is {@code null}, and if so, throws a {@link java.lang.NullPointerException}.
     *
     * @param joinPoint the {@link org.aspectj.lang.JoinPoint}.
     * @param o the {@code Object} to check for nullity.
     * @param paramNumber the number of the parameter in the method signature.
     */
    private static void checkPreCondition(final JoinPoint joinPoint, final Object o, final int paramNumber) {
        if (o == null) {
            CodeSignature signature = (CodeSignature) joinPoint.getSignature();
            String argName = signature.getParameterNames()[paramNumber];
            String methodName = signature.getName();

            String message = String.format(PRECONDITION_VIOLATED_MESSAGE, argName, methodName);
            throwNullPointerException(message, 4);
        }
    }

    /**
     * Checks if {@code r} is {@code null}, and if so, throws a {@link java.lang.NullPointerException}.
     *
     * @param joinPoint the {@link org.aspectj.lang.JoinPoint}.
     * @param r the {@code Object} to check for nullity.
     */
    private static void checkPostCondition(final JoinPoint joinPoint, final Object r) {
        if (r == null) {
            Signature signature = joinPoint.getSignature();
            String methodName = signature.getName();

            String message = String.format(POSTCONDITION_VIOLATED_MESSAGE, methodName);
            throwNullPointerException(message, 3);
        }
    }

    /**
     * Throws a {@link java.lang.NullPointerException}, and strips {@code stripHeadSize} lines from the top of the {@code StackTrace}.
     *
     * @param message the message to supply to the {@code NullPointerException}.
     * @param stripHeadSize the number of elements to remove from the top of the {@code StackTrace}.
     */
    private static void throwNullPointerException(final String message, final int stripHeadSize) {
        NullPointerException nullPointerException = new NullPointerException(message);

        /* Strip stack trace of last four elements */
        StackTraceElement[] stackTrace = nullPointerException.getStackTrace();
        StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length - stripHeadSize];
        System.arraycopy(stackTrace, stripHeadSize, newStackTrace, 0, newStackTrace.length);
        nullPointerException.setStackTrace(newStackTrace);

        throw nullPointerException;
    }
}