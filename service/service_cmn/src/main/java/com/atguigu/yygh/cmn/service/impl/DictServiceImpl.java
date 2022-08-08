package com.atguigu.yygh.cmn.service.impl;

import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
