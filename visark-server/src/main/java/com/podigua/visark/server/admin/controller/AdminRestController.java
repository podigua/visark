package com.podigua.visark.server.admin.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.entity.NewTopicEntity;
import com.podigua.visark.server.admin.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-01 20:55
 **/
@RestController
@RequestMapping("/admin")
@WebResult
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminRestController {

    private final AdminService adminService;

    /**
     * 获取左侧树
     *
     * @return
     */
    @GetMapping("/{id}/trees")
    public List<TreeNode> getTrees(@PathVariable String id) {
        return adminService.getTrees(id);
    }

    /**
     * 获取Topics
     *
     * @return
     */
    @GetMapping("/{id}/topics")
    public List<TreeNode> getTopics(@PathVariable String id) {
        return adminService.getTopics(id);
    }

    /**
     * 创建topic
     *
     * @param id
     * @param topic
     */
    @PostMapping("/{id}/topic/create")
    public void createTopic(@PathVariable String id, @RequestBody NewTopicEntity topic) {
        adminService.createTopic(id, topic);
    }

    /**
     * 删除topic
     *
     * @param id
     * @param topic
     */
    @DeleteMapping("/{id}/{topic}")
    public void deleteTopic(@PathVariable String id, @PathVariable String topic) {
        adminService.deleteTopic(id,topic);
    }
}
