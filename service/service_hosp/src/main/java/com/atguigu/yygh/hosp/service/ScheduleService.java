package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosScheduleId);

    //根据医院编号 和 科室编号 ，查询排班规则数据
    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    /**
     * 获取医院科室预约规则信息。含有分页功能
     *
     * @param page    当前页
     * @param limit   每页显示条数
     * @param hoscode 医院编号
     * @param depcode 科室编号
     * @return 返回 map 对象是因为 String 对应某一天的日期，Object 对应这该天能够预约的医生排班信息。
     */
    Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hoscode, String depcode);
}
