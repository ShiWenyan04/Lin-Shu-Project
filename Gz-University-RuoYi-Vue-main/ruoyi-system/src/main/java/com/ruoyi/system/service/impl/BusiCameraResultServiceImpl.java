package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.BusiCameraResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BusiCameraResult;
import com.ruoyi.system.service.IBusiCameraResultService;

/**
 * 红外相机成果管理Service业务层处理
 */
@Service
public class BusiCameraResultServiceImpl implements IBusiCameraResultService
{
    @Autowired
    private BusiCameraResultMapper busiCameraResultMapper;

    @Override
    public BusiCameraResult selectBusiCameraResultById(Long id)
    {
        return busiCameraResultMapper.selectBusiCameraResultById(id);
    }

    @Override
    public List<BusiCameraResult> selectBusiCameraResultList(BusiCameraResult busiCameraResult)
    {
        return busiCameraResultMapper.selectBusiCameraResultList(busiCameraResult);
    }

    /**
     * 新增：自动设置创建人，默认待审核
     */
    @Override
    public int insertBusiCameraResult(BusiCameraResult busiCameraResult)
    {
        busiCameraResult.setCreateTime(DateUtils.getNowDate());
        // 1. 设置创建人为当前用户
        busiCameraResult.setCreateBy(SecurityUtils.getUsername());
        // 2. 默认状态 0=待审核
        busiCameraResult.setAuditStatus("0");
        return busiCameraResultMapper.insertBusiCameraResult(busiCameraResult);
    }

    /**
     * 修改：用户编辑后，重置为待审核
     */
    @Override
    public int updateBusiCameraResult(BusiCameraResult busiCameraResult)
    {
        busiCameraResult.setUpdateTime(DateUtils.getNowDate());

        // 核心逻辑：如果是用户普通修改（非审核操作），强制变为待审核
        // 通过判断 auditStatus 是否为 null 来区分（审核接口会传状态，前端修改接口通常只传业务数据）
        // 或者直接强制重置，专门的审核走 audit 方法
        if (StringUtils.isEmpty(busiCameraResult.getAuditStatus()) || "2".equals(busiCameraResult.getAuditStatus())) {
            busiCameraResult.setAuditStatus("0");
            busiCameraResult.setAuditReason(""); // 清空旧的驳回原因
        }

        return busiCameraResultMapper.updateBusiCameraResult(busiCameraResult);
    }

    /**
     * 专属审核方法
     */
    public int auditBusiCameraResult(BusiCameraResult busiCameraResult) {
        busiCameraResult.setUpdateTime(DateUtils.getNowDate());
        // 这里不做重置，直接更新状态和原因
        return busiCameraResultMapper.updateBusiCameraResult(busiCameraResult);
    }

    @Override
    public int deleteBusiCameraResultByIds(Long[] ids)
    {
        // 如果需要限制“只能删除草稿/被驳回”的数据，可以在这里加查询判断
        // 目前先保持标准删除
        return busiCameraResultMapper.deleteBusiCameraResultByIds(ids);
    }

    @Override
    public int deleteBusiCameraResultById(Long id)
    {
        return busiCameraResultMapper.deleteBusiCameraResultById(id);
    }
}