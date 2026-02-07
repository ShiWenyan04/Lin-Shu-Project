package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.WildlifeSpeciesMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CameraStatisticsMapper;
import com.ruoyi.system.domain.CameraStatistics;
import com.ruoyi.system.service.ICameraStatisticsService;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * 红外相机数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-26
 */
@Service
public class CameraStatisticsServiceImpl implements ICameraStatisticsService 
{
    // 添加日志记录器 - 使用SLF4J
    private static final Logger log = LoggerFactory.getLogger(CameraStatisticsServiceImpl.class);

    @Autowired
    private CameraStatisticsMapper cameraStatisticsMapper;
    // 新增: 注入野生动物物种Mapper
    @Autowired
    private WildlifeSpeciesMapper wildlifeSpeciesMapper;

    /**
     * 查询红外相机数据
     * 
     * @param id 红外相机数据主键
     * @return 红外相机数据
     */
    @Override
    public CameraStatistics selectCameraStatisticsById(Long id)
    {
        return cameraStatisticsMapper.selectCameraStatisticsById(id);
    }

    /**
     * 查询红外相机数据列表
     * 
     * @param cameraStatistics 红外相机数据
     * @return 红外相机数据
     */
    @Override
    public List<CameraStatistics> selectCameraStatisticsList(CameraStatistics cameraStatistics)
    {
        return cameraStatisticsMapper.selectCameraStatisticsList(cameraStatistics);
    }

    /**
     * 新增红外相机数据
     * 
     * @param cameraStatistics 红外相机数据
     * @return 结果
     */
    @Override
    public int insertCameraStatistics(CameraStatistics cameraStatistics)
    {
        return cameraStatisticsMapper.insertCameraStatistics(cameraStatistics);
    }

    /**
     * 修改红外相机数据
     * 
     * @param cameraStatistics 红外相机数据
     * @return 结果
     */
    @Override
    public int updateCameraStatistics(CameraStatistics cameraStatistics)
    {
        return cameraStatisticsMapper.updateCameraStatistics(cameraStatistics);
    }

    /**
     * 批量删除红外相机数据
     * 
     * @param ids 需要删除的红外相机数据主键
     * @return 结果
     */
    @Override
    public int deleteCameraStatisticsByIds(Long[] ids)
    {
        return cameraStatisticsMapper.deleteCameraStatisticsByIds(ids);
    }

    /**
     * 删除红外相机数据信息
     * 
     * @param id 红外相机数据主键
     * @return 结果
     */
    @Override
    public int deleteCameraStatisticsById(Long id)
    {
        return cameraStatisticsMapper.deleteCameraStatisticsById(id);
    }

    @Override
    @Transactional
    public String importCameraStatistics(List<CameraStatistics> statisticsList, boolean updateSupport) {
        if (CollectionUtils.isEmpty(statisticsList)) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        int updateNum = 0;
        StringBuilder failureMsg = new StringBuilder();

        // 用于收集缺失的物种名称(去重)
        Set<String> missingSpecies = new HashSet<>();
        // 用于收集失败的文件编号
        List<String> failedFileNos = new ArrayList<>();

        for (CameraStatistics statistics : statisticsList) {
            try {
                // 物种校验
                if (statistics.getSpeciesName() != null && !statistics.getSpeciesName().isEmpty()) {
                    int speciesCount = wildlifeSpeciesMapper.countBySpeciesName(statistics.getSpeciesName());
                    if (speciesCount == 0) {
                        missingSpecies.add(statistics.getSpeciesName());
                    }
                }

                // 验证数据唯一性
                CameraStatistics exist = cameraStatisticsMapper.selectCameraStatisticsByFileNo(statistics.getFileNo());

                if (exist == null) {
                    // 插入新数据
                    statistics.setId(null);
                    cameraStatisticsMapper.insertCameraStatistics(statistics);
                    successNum++;
                } else {
                    if (updateSupport) {
                        // 更新数据
                        statistics.setId(exist.getId());
                        cameraStatisticsMapper.updateCameraStatistics(statistics);
                        updateNum++;
                    } else {
                        throw new ServiceException("文件编号已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                failedFileNos.add(statistics.getFileNo());
                failureMsg.append("<br/>")
                        .append(failureNum)
                        .append("、文件编号 ")
                        .append(statistics.getFileNo())
                        .append(" 导入失败：")
                        .append(e.getMessage());
            }
        }

        // 构建最终消息
        StringBuilder finalMessage = new StringBuilder();

        if (failureNum > 0) {
            finalMessage.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
            finalMessage.append(failureMsg);
        } else {
            finalMessage.append("恭喜您，数据导入成功！共导入 ")
                    .append(successNum + updateNum)
                    .append(" 条数据");

            if (updateNum > 0) {
                finalMessage.append("（其中新增 ").append(successNum).append(" 条，更新 ").append(updateNum).append(" 条）");
            }

            // 添加物种缺失提示
            if (!missingSpecies.isEmpty()) {
                finalMessage.append("<br/><br/><span style='color: #F56C6C; font-weight: bold;'>")
                        .append("注意：以下物种在野生动物信息库中不存在，请及时添加：")
                        .append("</span>")
                        .append("<ul style='margin-top: 5px; margin-bottom: 5px;'>");

                for (String species : missingSpecies) {
                    finalMessage.append("<li>").append(species).append("</li>");
                }

                finalMessage.append("</ul>");
            }
        }

        return finalMessage.toString();
    }


}
