package cn.com.mooyeali.ticktact.common.factory

import org.quartz.Job
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class DynamicTaskFactory : ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun registerDynamicTask(beanName: String, taskClass: Class<out Job?>) {
        // 获取Spring容器的BeanFactory
        val beanFactory = applicationContext!!.autowireCapableBeanFactory as ConfigurableListableBeanFactory

        // 注册新的Bean
        beanFactory.registerSingleton(beanName, beanFactory.createBean(taskClass)!!)

        println("Registered dynamic task: $beanName")
    }
}
