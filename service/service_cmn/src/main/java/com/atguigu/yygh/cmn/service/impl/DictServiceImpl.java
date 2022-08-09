package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 组织架构表 服务实现类
 * </p>
 *
 * @author Liu-Liu
 * @since 2022-08-08
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private DictListener dictListener;

    // 根据数据id查询子数据列表
    @Cacheable(value = "dict", key = "'selectIndexList'+#id")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(queryWrapper);
        // 由于 dict 中有 hasChildren 属性，所以需要设置其属性。
        // 判断当前 dict 是否有子节点，如果有子节点，就将 hasChildren 属性设置为 true，否则设置为 false
        for (Dict dict : dictList) {
            Long dictId = dict.getId();
            boolean isChild = this.hasChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    @Override
    public void exportData(HttpServletResponse response) {

        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和 easyExcel 没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 获取所有的 dict 列表
            List<Dict> dictList = baseMapper.selectList(null);
            List<DictEeVo> dictEeVoList = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictEeVo);
                dictEeVoList.add(dictEeVo);
            }
            // 将 dictEeVoList 写入 excel 中
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("sheet").doWrite(dictEeVoList);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void importDictData(MultipartFile multipartFile) {
        try {
            EasyExcel.read(multipartFile.getInputStream(), DictEeVo.class, dictListener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断id下面是否有子节点
    public boolean hasChildren(Long id) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        // 例如：SELECT count(1) from dict where parent_id = 86;
        Integer count = baseMapper.selectCount(queryWrapper);
        // 当查询结果大于 0 时，说明其有子节点
        return count > 0;
    }
}
