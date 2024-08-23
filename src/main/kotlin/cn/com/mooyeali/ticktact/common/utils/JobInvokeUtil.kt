package cn.com.mooyeali.ticktact.common.utils

import cn.com.mooyeali.ticktact.common.items.Task
import cn.hutool.core.util.StrUtil
import java.lang.reflect.InvocationTargetException
import java.util.*



object JobInvokeUtil {
    /**
     * 执行方法(根据 bean 名称)
     *
     * @param task 任务
     */
    @Throws(Exception::class)
    fun invokeMethod(task: Task) {
        val beanName: String = task.name
        val methodName: String = task.methodName
        val methodParams = getMethodParams(task.params)
        val bean = SpringUtils.getBean<Any>(beanName)
        invokeMethod(bean, methodName, methodParams)
    }

    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    @Throws(
        NoSuchMethodException::class,
        SecurityException::class,
        IllegalAccessException::class,
        IllegalArgumentException::class,
        InvocationTargetException::class
    )
    private fun invokeMethod(bean: Any, methodName: String, methodParams: List<Array<Any>>?) {
        if (!methodParams.isNullOrEmpty()) {
            val method = bean.javaClass.getMethod(methodName, *getMethodParamsType(methodParams))
            method.invoke(bean, *getMethodParamsValue(methodParams))
        } else {
            val method = bean.javaClass.getMethod(methodName)
            method.invoke(bean)
        }
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param params 目标字符串
     * @return method方法相关参数列表
     */
    private fun getMethodParams(params: String): List<Array<Any>>? {
        if (StrUtil.isEmpty(params)) {
            return null
        }
        val methodParams = params.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val clazzs: MutableList<Array<Any>> = LinkedList()
        for (methodParam in methodParams) {
            val str = StrUtil.trimToEmpty(methodParam)
            if ("true".equals(str, ignoreCase = true) || "false".equals(str, ignoreCase = true)) {
                clazzs.add(arrayOf(str.toBoolean(), Boolean::class.java))
            } else {
                clazzs.add(arrayOf(str, String::class.java))
            }
        }
        return clazzs
    }


    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    private fun getMethodParamsType(methodParams: List<Array<Any>>): Array<Class<*>?> {
        val classs: Array<Class<*>?> = arrayOfNulls(methodParams.size)
        for ((index, os) in methodParams.withIndex()) {
            classs[index] = os[1] as Class<*>
            println(os[0].javaClass)
        }
        return classs
    }


    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    private fun getMethodParamsValue(methodParams: List<Array<Any>>): Array<Any?> {
        val classes = arrayOfNulls<Any>(methodParams.size)
        for ((index, os) in methodParams.withIndex()) {
            classes[index] = os[0]
        }
        return classes
    }
}
