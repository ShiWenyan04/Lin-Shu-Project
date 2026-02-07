package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BusiFieldDocMapper;
import com.ruoyi.system.domain.BusiFieldDoc;
import com.ruoyi.system.service.IBusiFieldDocService;

/**
 * 外业文档模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-22
 */
@Service
public class BusiFieldDocServiceImpl implements IBusiFieldDocService 
{
    @Autowired
    private BusiFieldDocMapper busiFieldDocMapper;

    /**
     * 查询外业文档模板
     * 
     * @param docId 外业文档模板主键
     * @return 外业文档模板
     */
    @Override
    public BusiFieldDoc selectBusiFieldDocByDocId(Long docId)
    {
        return busiFieldDocMapper.selectBusiFieldDocByDocId(docId);
    }

    /**
     * 查询外业文档模板列表
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 外业文档模板
     */
    @Override
    public List<BusiFieldDoc> selectBusiFieldDocList(BusiFieldDoc busiFieldDoc)
    {
        return busiFieldDocMapper.selectBusiFieldDocList(busiFieldDoc);
    }

    /**
     * 新增外业文档模板
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 结果
     */
    @Override
    public int insertBusiFieldDoc(BusiFieldDoc busiFieldDoc)
    {
        busiFieldDoc.setCreateTime(DateUtils.getNowDate());
        return busiFieldDocMapper.insertBusiFieldDoc(busiFieldDoc);
    }

    /**
     * 修改外业文档模板
     * 
     * @param busiFieldDoc 外业文档模板
     * @return 结果
     */
    @Override
    public int updateBusiFieldDoc(BusiFieldDoc busiFieldDoc)
    {
        busiFieldDoc.setUpdateTime(DateUtils.getNowDate());
        return busiFieldDocMapper.updateBusiFieldDoc(busiFieldDoc);
    }

    /**
     * 批量删除外业文档模板
     * 
     * @param docIds 需要删除的外业文档模板主键
     * @return 结果
     */
    @Override
    public int deleteBusiFieldDocByDocIds(Long[] docIds)
    {
        return busiFieldDocMapper.deleteBusiFieldDocByDocIds(docIds);
    }

    /**
     * 删除外业文档模板信息
     * 
     * @param docId 外业文档模板主键
     * @return 结果
     */
    @Override
    public int deleteBusiFieldDocByDocId(Long docId)
    {
        return busiFieldDocMapper.deleteBusiFieldDocByDocId(docId);
    }
}
