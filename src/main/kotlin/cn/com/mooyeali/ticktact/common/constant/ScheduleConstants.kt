package cn.com.mooyeali.ticktact.common.constant

class ScheduleConstants {
    
    companion object {
        /**
         * 任务名称
         */
        const val TASK_CLASS_NAME: String = "TASK_CLASS_NAME"

        /**
         * 执行目标key
         */
        const val TASK_PROPERTIES: String = "TASK_PROPERTIES"

        /**
         * 默认
         */
        const val MISFIRE_DEFAULT: String = "0"

        /**
         * 立即触发执行
         */
        const val MISFIRE_IGNORE_MISFIRES: String = "1"

        /**
         * 触发一次执行
         */
        const val MISFIRE_FIRE_AND_PROCEED: String = "2"

        /**
         * 不触发立即执行
         */
        const val MISFIRE_DO_NOTHING: String = "3"

        enum class Status(val value: String) {
            /**
             * 正常
             */
            NORMAL("0"),

            /**
             * 暂停
             */
            PAUSE("1")
        }
    }
    
}
