package cn.com.mooyeali.ticktact.common.timer

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.springframework.stereotype.Service

@Service("testDynamic1Job")
class TestDynamic1Job : Job {
    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext) {
        println("TestDynamic1Job")
    }
}
