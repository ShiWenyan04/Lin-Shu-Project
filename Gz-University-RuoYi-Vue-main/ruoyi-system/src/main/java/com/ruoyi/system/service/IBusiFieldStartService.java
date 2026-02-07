package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldStart;

/**
 * 外业启动申请Service接口
 */
public interface IBusiFieldStartService
{
    /**
     * 查询外业启动申请
     */
    public BusiFieldStart selectBusiFieldStartByStartId(Long startId);

    /**
     * 1. 【旧方法保留】查询列表 (兼容导出功能等)
     */
    public List<BusiFieldStart> selectBusiFieldStartList(BusiFieldStart busiFieldStart);

    /**
     * 2. 【新方法新增】查询列表 (支持 Tab 过滤)
     */
    public List<BusiFieldStart> selectBusiFieldStartList(BusiFieldStart busiFieldStart, String tabType, Long userId);

    /**
     * 新增外业启动申请
     */
    public int insertBusiFieldStart(BusiFieldStart busiFieldStart);

    /**
     * 修改外业启动申请
     */
    public int updateBusiFieldStart(BusiFieldStart busiFieldStart);

    /**
     * 批量删除
     */
    public int deleteBusiFieldStartByStartIds(Long[] startIds);

    /**
     * 删除
     */
    public int deleteBusiFieldStartByStartId(Long startId);

    /**
     * 审核
     */
    public int auditBusiFieldStart(Long startId, String status, String reason);
}