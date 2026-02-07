package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 引入事务
import com.ruoyi.common.exception.ServiceException; // 引入异常
import com.ruoyi.system.mapper.CameraReturnMapper;
import com.ruoyi.system.domain.CameraReturn;
import com.ruoyi.system.service.ICameraReturnService;

// === 1. 引入库存(设备)相关类 ===
import com.ruoyi.system.mapper.FieldSurveyMapper;
import com.ruoyi.system.domain.FieldSurvey;

/**
 * 红外相机归还与运维Service业务层处理
 * * @author ruoyi
 * @date 2025-12-18
 */
@Service
public class CameraReturnServiceImpl implements ICameraReturnService
{
    @Autowired
    private CameraReturnMapper cameraReturnMapper;

    // === 2. 注入设备表 Mapper ===
    @Autowired
    private FieldSurveyMapper fieldSurveyMapper;

    /**
     * 查询红外相机归还与运维
     */
    @Override
    public CameraReturn selectCameraReturnById(Long id)
    {
        return cameraReturnMapper.selectCameraReturnById(id);
    }

    /**
     * 查询红外相机归还与运维列表
     */
    @Override
    public List<CameraReturn> selectCameraReturnList(CameraReturn cameraReturn)
    {
        return cameraReturnMapper.selectCameraReturnList(cameraReturn);
    }

    /**
     * 新增红外相机归还与运维
     */
    @Override
    public int insertCameraReturn(CameraReturn cameraReturn)
    {
        cameraReturn.setCreateTime(DateUtils.getNowDate());
        return cameraReturnMapper.insertCameraReturn(cameraReturn);
    }

    /**
     * 修改红外相机归还与运维
     */
    @Override
    public int updateCameraReturn(CameraReturn cameraReturn)
    {
        cameraReturn.setUpdateTime(DateUtils.getNowDate());
        return cameraReturnMapper.updateCameraReturn(cameraReturn);
    }

    /**
     * 审核逻辑 (最终修复版：适配 Long 类型)
     */
    @Override
    @Transactional
    public int auditCameraReturn(CameraReturn cameraReturn) {
        // 1. 如果审核结果是 "1" (通过)，则执行库存变更
        if ("1".equals(cameraReturn.getAuditStatus())) {
            // 获取本次归还的详细数据
            CameraReturn returnInfo = cameraReturnMapper.selectCameraReturnById(cameraReturn.getId());

            if (returnInfo == null) {
                throw new ServiceException("未找到该归还单据");
            }

            // 查询对应的设备库存
            FieldSurvey asset = fieldSurveyMapper.selectFieldSurveyById(returnInfo.getEquipmentId());
            if (asset == null) {
                throw new ServiceException("关联的设备不存在！");
            }

            // === 1. 获取变动数值 (转为 long 类型，防止类型不匹配) ===
            // 这里利用 Java 的自动拆箱和宽化转换，把 Integer 转为 long
            long returnQty = returnInfo.getReturnQty() != null ? returnInfo.getReturnQty() : 0L;
            long recoveredQty = returnInfo.getRecoveredQty() != null ? returnInfo.getRecoveredQty() : 0L;
            long scrappedQty = returnInfo.getScrappedQty() != null ? returnInfo.getScrappedQty() : 0L;
            long stolenQty = returnInfo.getStolenQty() != null ? returnInfo.getStolenQty() : 0L;
            long installAddQty = returnInfo.getInstallAddQty() != null ? returnInfo.getInstallAddQty() : 0L;

            // === 2. 获取当前库存 (转为 long 类型) ===
            long currentAvailable = asset.getAvailableQty() != null ? asset.getAvailableQty() : 0L;
            long currentInstalled = asset.getInstalledQty() != null ? asset.getInstalledQty() : 0L;
            long currentBorrowed = asset.getBorrowedQty() != null ? asset.getBorrowedQty() : 0L;
            long currentScrapped = asset.getScrappedQty() != null ? asset.getScrappedQty() : 0L;

            // === 3. 核心公式运算 (全部使用 long 计算) ===

            // A. 可用数量 = 原可用数量 + 归还数量 + 本次回收数量 - 报废数量
            long newAvailable = currentAvailable + returnQty + recoveredQty - scrappedQty;
            if (newAvailable < 0) newAvailable = 0L;
            asset.setAvailableQty(newAvailable); // 这里传入 long，Java会自动装箱为 Long

            // B. 安装数量 = 原安装数量 - 本次被盗数量 - 本次回收数量 - 报废数量 + 本次安装数量
            long newInstalled = currentInstalled - stolenQty - recoveredQty - scrappedQty + installAddQty;
            if (newInstalled < 0) newInstalled = 0L;
            asset.setInstalledQty(newInstalled);

            // C. 外借数量逻辑 (自动推导：东西还回来了或安装了，外借自然减少)
            long newBorrowed = currentBorrowed - returnQty - installAddQty;
            if (newBorrowed < 0) newBorrowed = 0L;
            asset.setBorrowedQty(newBorrowed);

            // D. 总报废数量逻辑 (自动推导：加上本次报废和被盗)
            long newScrappedTotal = currentScrapped + scrappedQty + stolenQty;
            asset.setScrappedQty(newScrappedTotal);

            // 更新设备表
            fieldSurveyMapper.updateFieldSurvey(asset);
        }

        // 2. 更新归还单状态
        cameraReturn.setUpdateTime(DateUtils.getNowDate());
        return cameraReturnMapper.updateCameraReturn(cameraReturn);
    }

    /**
     * 批量删除红外相机归还与运维
     */
    @Override
    public int deleteCameraReturnByIds(Long[] ids)
    {
        return cameraReturnMapper.deleteCameraReturnByIds(ids);
    }

    /**
     * 删除红外相机归还与运维信息
     */
    @Override
    public int deleteCameraReturnById(Long id)
    {
        return cameraReturnMapper.deleteCameraReturnById(id);
    }
}