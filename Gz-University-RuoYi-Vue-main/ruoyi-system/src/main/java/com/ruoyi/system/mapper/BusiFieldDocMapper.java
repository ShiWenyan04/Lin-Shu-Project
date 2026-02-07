package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BusiFieldDoc;

/**
 * 外业文档模板Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
public interface BusiFieldDocMapper 
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
     * 删除外业文档模板
     * 
     * @param docId 外业文档模板主键
     * @return 结果
     */
    public int deleteBusiFieldDocByDocId(Long docId);

    /**
     * 批量删除外业文档模板
     * 
     * @param docIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBusiFieldDocByDocIds(Long[] docIds);
}
