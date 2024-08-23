package cn.com.mooyeali.ticktact.common.loader

import cn.com.mooyeali.ticktact.common.annotations.Slf4k
import cn.com.mooyeali.ticktact.common.annotations.Slf4k.Companion.log
import cn.com.mooyeali.ticktact.common.items.Task
import cn.com.mooyeali.ticktact.common.items.TaskConfig
import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import org.springframework.core.io.ClassPathResource
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.Consumer


@Slf4k
class TaskConfigLoader {
    
    companion object {
        @JvmStatic
        fun loadTaskConfig(): TaskConfig {
            val innerConfigPath = "config/task-config.yml"
            val outerConfigPath = System.getProperty("user.dir") + File.separator + innerConfigPath
            log.info("外部配置文件地址:{}", outerConfigPath)
            var inputStream: InputStream? = null
            try {
                // 默认加载内部配置文件
                inputStream = ClassPathResource(innerConfigPath).inputStream
                if (File(outerConfigPath).exists()) {
                    TaskConfigLoader.log.info("外部配置文件存在,加载外部配置文件!")
                    inputStream = Files.newInputStream(Paths.get(outerConfigPath))
                }
                val yaml = Yaml()
                val obj = yaml.load<Map<String, Any>>(inputStream)
                TaskConfigLoader.log.info(obj.toString())
                val list: MutableList<Task> = ArrayList()
                obj.forEach { (_: String?, value: Any?) ->
                    val array = JSONArray.parseArray(JSON.toJSONString(value))
                    array.forEach(Consumer { item: Any? ->
                        list.add(
                            JSONObject.parseObject(
                                JSON.toJSONString(item),
                                Task::class.java
                            )
                        )
                    })
                }
                return TaskConfig(list)
            } catch (e: Exception) {
                TaskConfigLoader.log.error("读取task-config.yml出现异常,异常消息为: {}", e.message, e)
                throw RuntimeException("Error loading task configuration", e)
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    TaskConfigLoader.log.error("关闭inputStream出现异常,异常消息为: {}", e.message, e)
                }
            }
        }
    }
}
