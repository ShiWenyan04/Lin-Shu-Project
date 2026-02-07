package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldStart;

/**
 * 外业启动申请Mapper接口
 * * @author ruoyi
 * @date 2025-12-17
 */
public interface BusiFieldStartMapper
{
    /**
     * 查询外业启动申请
     * * @param startId 外业启动申请主键
     * @return 外业启动申请
     */
    public BusiFieldStart selectBusiFieldStartByStartId(Long startId);

    /**
     * 查询外业启动申请列表
     * * @param busiFieldStart 外业启动申请
     * @return 外业启动申请集合
     */
    public List<BusiFieldStart> selectBusiFieldStartList(BusiFieldStart busiFieldStart);

    /**
     * 新增外业启动申请
     * * @param busiFieldStart 外业启动申请
     * @return 结果
     */
    public int insertBusiFieldStart(BusiFieldStart busiFieldStart);

    /**
     * 修改外业启动申请
     * * @param busiFieldStart 外业启动申请
     * @return 结果
     */
    public int updateBusiFieldStart(BusiFieldStart busiFieldStart);

    /**
     * 删除外业启动申请
     * * @param startId 外业启动申请主键
     * @return 结果
     */
    public int deleteBusiFieldStartByStartId(Long startId);

    /**
     * 批量删除外业启动申请
     * * @param startIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiFieldStartByStartIds(Long[] startIds);
}