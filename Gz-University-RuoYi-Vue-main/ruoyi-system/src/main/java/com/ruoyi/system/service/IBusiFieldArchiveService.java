package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldArchive;

/**
 * 外业数据归档Service接口
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public interface IBusiFieldArchiveService 
{
    /**
     * 查询外业数据归档
     * 
     * @param archiveId 外业数据归档主键
     * @return 外业数据归档
     */
    public BusiFieldArchive selectBusiFieldArchiveByArchiveId(Long archiveId);

    /**
     * 查询外业数据归档列表
     * 
     * @param busiFieldArchive 外业数据归档
     * @return 外业数据归档集合
     */
    public List<BusiFieldArchive> selectBusiFieldArchiveList(BusiFieldArchive busiFieldArchive);

    /**
     * 新增外业数据归档
     * 
     * @param busiFieldArchive 外业数据归档
     * @return 结果
     */
    public int insertBusiFieldArchive(BusiFieldArchive busiFieldArchive);

    /**
     * 修改外业数据归档
     * 
     * @param busiFieldArchive 外业数据归档
     * @return 结果
     */
    public int updateBusiFieldArchive(BusiFieldArchive busiFieldArchive);

    /**
     * 批量删除外业数据归档
     * 
     * @param archiveIds 需要删除的外业数据归档主键集合
     * @return 结果
     */
    public int deleteBusiFieldArchiveByArchiveIds(Long[] archiveIds);

    /**
     * 删除外业数据归档信息
     * 
     * @param archiveId 外业数据归档主键
     * @return 结果
     */
    public int deleteBusiFieldArchiveByArchiveId(Long archiveId);

    // 🚀 新增：带 Tab 过滤的查询
    public List<BusiFieldArchive> selectBusiFieldArchiveList(BusiFieldArchive busiFieldArchive, String tabType, Long userId);

    // 🚀 新增：审核
    public int auditBusiFieldArchive(Long archiveId, String status, String reason);

    // 🚀 新增：查询可归档的任务 (下拉框)
    public List<Object> selectMyFinishedStarts(Long userId);
}
