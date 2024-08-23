package cn.com.mooyeali.ticktact.common.abstracts

import cn.com.mooyeali.ticktact.common.annotations.Slf4k
import cn.com.mooyeali.ticktact.common.annotations.Slf4k.Companion.log
import cn.com.mooyeali.ticktact.common.constant.ScheduleConstants
import cn.com.mooyeali.ticktact.common.items.TaskConfig
import cn.com.mooyeali.ticktact.common.utils.JobInvokeUtil
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.BeanUtils
import java.util.*


/**
 * 定时任务实现类(用于构建和执行任务)
 *
 * @author mooyeali
 */
@Slf4k
open class AbstractQuartzJob : Job {
    override fun execute(context: JobExecutionContext) {
        threadLocal.set(Date())
        try {
            val task = TaskConfig.Task()
            BeanUtils.copyProperties(context.mergedJobDataMap[ScheduleConstants.TASK_PROPERTIES]!!, task)
            log.info(
                "任务实体:{},任务管理器中传递过来的context:{}", task,
                context.mergedJobDataMap[ScheduleConstants.TASK_PROPERTIES]
            )
            doExecute(task)
        } catch (e: Exception) {
            AbstractQuartzJob.log.error("执行定时任务失败,失败原因:{}", e.message, e)
        }
    }

    @Throws(Exception::class)
    protected fun doExecute(task: TaskConfig.Task) {
        JobInvokeUtil.invokeMethod(task)
    }

    companion object {
        private val threadLocal = ThreadLocal<Date>()
    }
}
