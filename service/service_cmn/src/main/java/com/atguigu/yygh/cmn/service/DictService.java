package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-08
 */
public interface DictService extends IService<Dict> {

    List<Dict> findChildData(Long id);

    void exportData(HttpServletResponse response);
}