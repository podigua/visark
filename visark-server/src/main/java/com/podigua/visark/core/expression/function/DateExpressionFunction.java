package com.podigua.visark.core.expression.function;

import com.podigua.visark.core.expression.ExpressionFunction;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * 日期
 *
 * @author: podigua
 * @create: 2021-06-10 19:36
 **/
public class DateExpressionFunction implements ExpressionFunction {
    /**
     * 当前时间戳
     *
     * @return
     */
    public Long timestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 当前时间
     *
     * @return
     */
    public Date current() {
        return new Date();
    }

    /**
     * 添加天数,可以为负数
     *
     * @param current  开始时间
     * @param amount 天数
     * @return Date
     */
    public Date addDays(Date current, Integer amount) {
        return DateUtils.addDays(current, amount);
    }

    /**
     * 根据当前时间添加天数
     *
     * @param amount 开始时间
     * @return Date
     */
    public Date addDays(Integer amount) {
        return DateUtils.addDays(new Date(), amount);
    }

    /**
     * 添加分钟
     *
     * @param current 开始时间
     * @param amount  分钟数
     * @return Date
     */
    public Date addMinutes(Date current, Integer amount) {
        return DateUtils.addMinutes(current, amount);
    }

    /**
     * 根据当前时间添加分钟
     *
     * @param amount 分钟数
     * @return Date
     */
    public Date addMinutes(Integer amount) {
        return DateUtils.addMinutes(new Date(), amount);
    }

    /**
     * 添加秒
     *
     * @param current 开始时间
     * @param amount 秒数
     * @return Date
     */
    public Date addSeconds(Date current, Integer amount) {
        return DateUtils.addSeconds(current, amount);
    }

    /**
     * 根据当前时间添加秒
     *
     * @param amount 秒数
     * @return Date
     */
    public Date addSeconds(Integer amount) {
        return DateUtils.addSeconds(new Date(), amount);
    }

    @Override
    public String name() {
        return "date";
    }
}
