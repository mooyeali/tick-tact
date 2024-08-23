package cn.com.mooyeali.ticktact.common.manager

import cn.com.mooyeali.ticktact.common.abstracts.AbstractQuartzJob
import cn.com.mooyeali.ticktact.common.annotations.Slf4k
import cn.com.mooyeali.ticktact.common.annotations.Slf4k.Companion.log
import cn.com.mooyeali.ticktact.common.constant.ScheduleConstants
import cn.com.mooyeali.ticktact.common.items.Task
import cn.com.mooyeali.ticktact.common.loader.TaskConfigLoader.Companion.loadTaskConfig
import org.quartz.*
import org.springframework.beans.factory.BeanFactory
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Service
@Slf4k
class TaskManager {
    @Resource
    lateinit var scheduler: Scheduler

    @Resource
    lateinit var beanFactory: BeanFactory

    @PostConstruct
    @Throws(SchedulerException::class)
    fun initTasks() {
        val taskConfig = loadTaskConfig()
        val tasks = taskConfig.taskConfig

        for (task in tasks) {
            createOrUpdateTask(task)
        }
        scheduler.start()
    }

    private fun createOrUpdateTask(task: Task) {
        log.info("开始初始化定时任务,任务信息:{}", task)
        val beanName = task.name
        // 检查 bean 是否存在于 Spring 上下文中
        if (beanFactory.containsBean(beanName)) {
            log.info("通过beanFactory获取的 bean:{}", beanFactory.getBean(beanName))
        } else {
            log.warn("Spring 上下文中不存在名称为 {} 的 bean。", beanName)
        }
        if (!task.isStarted) {
            log.info("定时任务:{} 的启动状态为 false 因此不创建!", task.name)
            return
        }
        try {
            val timestamp = System.currentTimeMillis()
            val clazz: Class<out Job?> = AbstractQuartzJob::class.java
            val jobDetail = JobBuilder.newJob(clazz)
                .withIdentity(beanName, "group_${task.name}_${timestamp}") // 使用更稳定的名称
                .build()

            val trigger = TriggerBuilder.newTrigger()
                .withIdentity(beanName, "group_${task.name}_${timestamp}") // 使用更稳定的名称
                .withSchedule(CronScheduleBuilder.cronSchedule(task.cron))
                .build()
            log.info("任务管理器中传递的 task:{}", task)
            jobDetail.jobDataMap[ScheduleConstants.TASK_PROPERTIES] = task
            log.info("任务管理器中传递的 JobDataMap:{}", jobDetail.jobDataMap)
            scheduler.scheduleJob(jobDetail, trigger)
        } catch (e: SchedulerException) {
            throw IllegalArgumentException("创建或更新任务时出错: $beanName", e)
        }
        log.info("定时任务 {} 初始化完成", task.name)
    }

    fun updateTasks() {
        val taskConfig = loadTaskConfig()
        val tasks = taskConfig.taskConfig

        for (task in tasks) {
            try {
                // 确保触发器键正确
                scheduler.unscheduleJob(TriggerKey(task.name, "group"))
                createOrUpdateTask(task)
            } catch (e: SchedulerException) {
                throw IllegalArgumentException("更新任务时出错: " + task.name, e)
            }
        }
    }
}
