package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiGraduateTeachingMapper;
import com.ruoyi.system.domain.BusiGraduateTeaching;
import com.ruoyi.system.service.IBusiGraduateTeachingService;

@Service
public class BusiGraduateTeachingServiceImpl implements IBusiGraduateTeachingService
{
    @Autowired
    private BusiGraduateTeachingMapper busiGraduateTeachingMapper;

    @Override
    public BusiGraduateTeaching selectBusiGraduateTeachingById(Long id)
    {
        return busiGraduateTeachingMapper.selectBusiGraduateTeachingById(id);
    }

    @Override
    public List<BusiGraduateTeaching> selectBusiGraduateTeachingList(BusiGraduateTeaching busiGraduateTeaching)
    {
        return selectBusiGraduateTeachingList(busiGraduateTeaching, null, null);
    }

    // 🚀 核心：带权限查询
    @Override
    public List<BusiGraduateTeaching> selectBusiGraduateTeachingList(BusiGraduateTeaching busiGraduateTeaching, String tabType, Long userId)
    {
        if ("ALL".equals(tabType)) {
            busiGraduateTeaching.setAuditStatus("1");
        } else if ("AUDIT".equals(tabType)) {
            busiGraduateTeaching.setAuditStatus("0");
        } else if ("MINE".equals(tabType) && userId != null) {
            busiGraduateTeaching.getParams().put("dataScope", " AND create_by = '" + userId + "'");
        }
        return busiGraduateTeachingMapper.selectBusiGraduateTeachingList(busiGraduateTeaching);
    }

    @Override
    public int insertBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching)
    {
        // 默认待审核
        busiGraduateTeaching.setAuditStatus("0");
        busiGraduateTeaching.setCreateTime(DateUtils.getNowDate());
        return busiGraduateTeachingMapper.insertBusiGraduateTeaching(busiGraduateTeaching);
    }

    @Override
    public int updateBusiGraduateTeaching(BusiGraduateTeaching busiGraduateTeaching)
    {
        // 驳回重置逻辑
        BusiGraduateTeaching old = busiGraduateTeachingMapper.selectBusiGraduateTeachingById(busiGraduateTeaching.getId());
        if (old != null && "2".equals(old.getAuditStatus())) {
            busiGraduateTeaching.setAuditStatus("0");
            busiGraduateTeaching.setAuditReason("");
        }
        busiGraduateTeaching.setUpdateTime(DateUtils.getNowDate());
        return busiGraduateTeachingMapper.updateBusiGraduateTeaching(busiGraduateTeaching);
    }

    // 🚀 核心：审核逻辑
    @Override
    public int auditBusiGraduateTeaching(Long id, String status, String reason) {
        BusiGraduateTeaching update = new BusiGraduateTeaching();
        update.setId(id);
        update.setAuditStatus(status);
        update.setAuditReason(reason);
        update.setUpdateTime(DateUtils.getNowDate());
        return busiGraduateTeachingMapper.updateBusiGraduateTeaching(update);
    }

    @Override
    public int deleteBusiGraduateTeachingByIds(Long[] ids)
    {
        return busiGraduateTeachingMapper.deleteBusiGraduateTeachingByIds(ids);
    }

    @Override
    public int deleteBusiGraduateTeachingById(Long id)
    {
        return busiGraduateTeachingMapper.deleteBusiGraduateTeachingById(id);
    }
}