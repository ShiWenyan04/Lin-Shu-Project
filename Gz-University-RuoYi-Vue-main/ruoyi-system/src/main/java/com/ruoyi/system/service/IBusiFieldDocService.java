package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldDoc;

/**
 * 外业文档模板Service接口
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public interface IBusiFieldDocService 
{
    /**
     * 查询外业文档模板
     * 
     * @param docId 外业文档模板主键
     * @return 外业文档模板
     */
    public BusiFieldDoc selectBusiFieldDocByDocId(Long docId);

    /**
     * 查询外业文档模板列表
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 外业文档模板集合
     */
    public List<BusiFieldDoc> selectBusiFieldDocList(BusiFieldDoc busiFieldDoc);

    /**
     * 新增外业文档模板
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 结果
     */
    public int insertBusiFieldDoc(BusiFieldDoc busiFieldDoc);

    /**
     * 修改外业文档模板
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 结果
     */
    public int updateBusiFieldDoc(BusiFieldDoc busiFieldDoc);

    /**
     * 批量删除外业文档模板
     * 
     * @param docIds 需要删除的外业文档模板主键集合
     * @return 结果
     */
    public int deleteBusiFieldDocByDocIds(Long[] docIds);

    /**
     * 删除外业文档模板信息
     * 
     * @param docId 外业文档模板主键
     * @return 结果
     */
    public int deleteBusiFieldDocByDocId(Long docId);
}
