package cn.com.mooyeali.ticktact.common.utils

import cn.hutool.core.util.StrUtil
import org.springframework.aop.framework.AopContext
import org.springframework.beans.BeansException
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author mooyeali
 */
@Component
class SpringUtils : BeanFactoryPostProcessor, ApplicationContextAware {
    @Throws(BeansException::class)
    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        Companion.beanFactory = beanFactory
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        Companion.applicationContext = applicationContext
    }

    companion object {
        /**
         * Spring应用上下文环境
         */
        private var beanFactory: ConfigurableListableBeanFactory? = null

        private var applicationContext: ApplicationContext? = null

        /**
         * 获取对象
         *
         * @param name
         * @return Object 一个以所给名字注册的bean的实例
         * @throws BeansException
         */
        @Throws(BeansException::class)
        fun <T> getBean(name: String): T {
            return beanFactory!!.getBean(name) as T
        }

        /**
         * 获取类型为requiredType的对象
         *
         * @param clz bean类型
         * @return Object 一个bean的实例
         * @throws BeansException BeansException
         */
        @Throws(BeansException::class)
        fun <T> getBean(clz: Class<T>): T {
            val result: T = beanFactory!!.getBean(clz)
            return result
        }

        /**
         * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
         *
         * @param name bean名
         * @return boolean
         */
        fun containsBean(name: String): Boolean {
            return beanFactory!!.containsBean(name)
        }

        /**
         * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
         *
         * @param name
         * @return boolean
         * @throws NoSuchBeanDefinitionException
         */
        @Throws(NoSuchBeanDefinitionException::class)
        fun isSingleton(name: String): Boolean {
            return beanFactory!!.isSingleton(name)
        }

        /**
         * @param name
         * @return Class 注册对象的类型
         * @throws NoSuchBeanDefinitionException
         */
        @Throws(NoSuchBeanDefinitionException::class)
        fun getType(name: String): Class<*>? {
            return beanFactory!!.getType(name)
        }

        /**
         * 如果给定的bean名字在bean定义中有别名，则返回这些别名
         *
         * @param name
         * @return
         * @throws NoSuchBeanDefinitionException
         */
        @Throws(NoSuchBeanDefinitionException::class)
        fun getAliases(name: String): Array<String> {
            return beanFactory!!.getAliases(name)
        }

        /**
         * 获取aop代理对象
         *
         * @param invoker
         * @return
         */
        fun <T> getAopProxy(invoker: T): T {
            return AopContext.currentProxy() as T
        }

        val activeProfiles: Array<String>
            /**
             * 获取当前的环境配置，无配置返回null
             *
             * @return 当前的环境配置
             */
            get() = applicationContext!!.environment.activeProfiles

        val activeProfile: String?
            /**
             * 获取当前的环境配置，当有多个环境配置时，只获取第一个
             *
             * @return 当前的环境配置
             */
            get() {
                val activeProfiles = activeProfiles
                for (activeProfile in activeProfiles) {
                    if (StrUtil.isNotEmpty(activeProfile)) {
                        return activeProfile
                    }
                }
                return null
            }

        /**
         * 获取配置文件中的值
         *
         * @param key 配置文件的key
         * @return 当前的配置文件的值
         */
        fun getRequiredProperty(key: String): String {
            return applicationContext!!.environment.getRequiredProperty(key)
        }
    }
}
