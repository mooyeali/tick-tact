package cn.com.mooyeali.ticktact.common.items

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Data

@Data
data class Task(
    @JsonProperty("name")
    var name: String,

    @JsonProperty("package_name")
    var packageName: String,

    @JsonProperty("cron")
    var cron: String,

    @JsonProperty("start")
    var isStarted: Boolean = false,

    @JsonProperty("method_name")
    var methodName: String,

    @JsonProperty("params")
    var params: String
)
