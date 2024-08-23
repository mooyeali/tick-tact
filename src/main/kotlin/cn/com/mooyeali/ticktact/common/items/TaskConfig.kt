package cn.com.mooyeali.ticktact.common.items

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
@Data
data class TaskConfig(
    @JsonProperty("task_config")
    var taskConfig: List<Task>
)
