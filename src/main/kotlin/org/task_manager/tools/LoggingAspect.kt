package org.task_manager.tools

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Aspect
@Component
class LoggingAspect {
    @Around("within(@LogMethods *)")
    fun logMethod(joinPoint: ProceedingJoinPoint): Any {
        val targetClass: Class<*> = joinPoint.target.javaClass
        val logger = LoggerFactory.getLogger(targetClass)

        val signature = joinPoint.signature as MethodSignature
        val methodName = signature.method.name
        val args = joinPoint.args
        val parameters = args.contentToString()

        logger.info("Called: $methodName with params: $parameters")

        val result = joinPoint.proceed()

        logger.info("Result: $result")
        return result
    }
}
