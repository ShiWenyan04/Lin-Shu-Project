package com.ruoyi.system.mapper.system;



import java.util.List;
import com.ruoyi.system.domain.RyThesis;

/**
 * 学位论文管理Mapper接口
 *
 * @author ruoyi
 * @date 2025-12-13
 */
public interface RyThesisMapper
{
    /**
     * 查询学位论文管理
     *
     * @param id 学位论文管理主键
     * @return 学位论文管理
     */
    public RyThesis selectRyThesisById(Long id);

    /**
     * 查询学位论文管理列表
     *
     * @param ryThesis 学位论文管理
     * @return 学位论文管理集合
     */
    public List<RyThesis> selectRyThesisList(RyThesis ryThesis);

    /**
     * 新增学位论文管理
     *
     * @param ryThesis 学位论文管理
     * @return 结果
     */
    public int insertRyThesis(RyThesis ryThesis);

    /**
     * 修改学位论文管理
     *
     * @param ryThesis 学位论文管理
     * @return 结果
     */
    public int updateRyThesis(RyThesis ryThesis);

    /**
     * 删除学位论文管理
     *
     * @param id 学位论文管理主键
     * @return 结果
     */
    public int deleteRyThesisById(Long id);

    /**
     * 批量删除学位论文管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRyThesisByIds(Long[] ids);
}
