package com.podigua.visark.core.expression.controller;

import com.podigua.visark.core.annotation.WebResult;
import com.podigua.visark.core.expression.ExpressionExecutor;
import com.podigua.visark.core.expression.ExpressionRequest;
import com.podigua.visark.core.expression.Function;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: podigua
 * @create: 2021-06-23 19:14
 **/
@RestController
@RequestMapping("/expression")
@ConditionalOnProperty(value = "square.itsm.expression.enabled", matchIfMissing = true)
@WebResult
public class ExpressionRestController {
    private final ExpressionExecutor expressionExecutor;

    public ExpressionRestController(ExpressionExecutor expressionExecutor) {
        this.expressionExecutor = expressionExecutor;
    }

    /**
     * 获取所有函数
     *
     * @return
     */
    @GetMapping("/functions")
    public List<Function> getFunction() {
        return expressionExecutor.getFunctions();
    }


    /**
     * 执行
     *
     * @return
     */
    @PostMapping("/execute")
    public Object execute(@RequestBody ExpressionRequest request) {
        return expressionExecutor.getValue(request.getExpression(), request.getRoot(), request.getVariables());
    }

    /**
     * 执行模板
     *
     * @return
     */
    @PostMapping("/template/execute")
    public Object executeTemplate(@RequestBody ExpressionRequest request) {
        return expressionExecutor.getValueByTemplate(request.getExpression(), request.getRoot(), request.getVariables());
    }
}
