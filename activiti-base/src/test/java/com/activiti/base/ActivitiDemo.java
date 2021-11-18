package com.activiti.base;

import com.activiti.base.bo.EvectionRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhi.xu
 * @date 2021/10/13
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
class ActivitiDemo {
    private static final String KEY = "evection";
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deploy() {
        // 查询部署流程
        log.info("-----查询部署流程------");
        final List<Deployment> list = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : list) {
            log.info(deployment.getName());
        }
        final List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : definitionList) {
            logProcessDefinition(processDefinition);
        }

        // 新建实例
        log.info("-----新建实例------");
        final EvectionRequest request = EvectionRequest.builder()
                .bizSeq("123")
                .applicant("chenzhi.xu")
                .manager("feng.li")
                .generalManager("haibin.lin")
                .finance("zhangsan")
                .build();
        final ProcessInstance instance = runtimeService.startProcessInstanceByKey(KEY, request.getBizSeq(), object2Map(request));
        logProcessInstance(instance);

        // 查询处理中任务
        List<Task> tasks = queryAndLogTask();

        taskService.setAssignee(tasks.get(0).getId(), "xuchenzhi");
        queryAndLogTask();
        // 完成任务
        taskService.complete(tasks.get(0).getId());

        // 查询处理中任务
        queryAndLogTask();
    }

    private Map<String, Object> object2Map(Object obj) {
        return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
    }

    private List<Task> queryAndLogTask() {
        // 查询处理中任务
        log.info("-----查询处理中任务------");
        final List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(KEY).list();
        for (Task task : taskList) {
            logTask(task);
        }
        return taskList;
    }

    private void logProcessInstance(ProcessInstance instance) {
        log.info("流程定义ID:" + instance.getProcessDefinitionId());
        log.info("流程实例ID:" + instance.getId());
        log.info("当前活动的ID:" + instance.getActivityId());
    }

    private void logProcessDefinition(ProcessDefinition processDefinition) {
        log.info("流程定义ID:" + processDefinition.getId());
        log.info("流程名:" + processDefinition.getName());
        log.info("流程KEY:" + processDefinition.getKey());
        log.info("流程版本:" + processDefinition.getVersion());
        log.info("流程资源文件:" + processDefinition.getResourceName());
        log.info("流程图:" + processDefinition.getDiagramResourceName());
        log.info("部署ID:" + processDefinition.getDeploymentId());
    }

    private void logTask(Task task) {
        log.info("流程实例ID:" + task.getProcessInstanceId());
        log.info("任务ID:" + task.getId());
        log.info("任务负责人:" + task.getAssignee());
        log.info("任务名称:" + task.getName());
    }
}
