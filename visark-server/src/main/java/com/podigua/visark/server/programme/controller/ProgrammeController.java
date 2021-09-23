package com.podigua.visark.server.programme.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.server.programme.entity.Programme;
import com.podigua.visark.server.programme.service.ProgrammeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-09-17 15:48
 **/
@RestController
@RequestMapping("/programme")
@WebResult
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProgrammeController {
    private final ProgrammeService programmeService;

    @PostMapping
    public Programme save(@RequestBody Programme programme) {
        return programmeService.save(programme);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        programmeService.deleteById(id);
    }

    /**
     * 获取列表
     *
     * @param cluster
     * @param topic
     * @return
     */
    @GetMapping("/list")
    public List<Programme> queryList(String cluster, String topic) {
        return programmeService.getList(cluster, topic);
    }
}
