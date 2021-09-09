package com.podigua.visark.client.index.service;

import com.podigua.visark.server.admin.TreeNode;
import com.podigua.visark.server.admin.service.AdminService;
import javafx.concurrent.Task;

import java.util.List;

import static com.podigua.visark.client.index.controller.IndexController.ERROR;

/**
 * 异步任务
 *
 * @author: podigua
 * @create: 2021-09-09 10:49
 **/
public class ConnectKafkaService extends javafx.concurrent.Service<List<TreeNode>> {
    private final String id;
    private final AdminService adminService;

    public ConnectKafkaService(String id, AdminService adminService) {
        this.id = id;
        this.adminService = adminService;
    }

    @Override
    protected Task<List<TreeNode>> createTask() {
        return new Task<List<TreeNode>>() {
            @Override
            protected List<TreeNode> call() throws Exception {
                try {
                    List<TreeNode> trees = adminService.getTrees(id);
                    return trees;
                } catch (Exception e) {
                    updateMessage(ERROR);
                    return null;
                }
            }
        };
    }
}
