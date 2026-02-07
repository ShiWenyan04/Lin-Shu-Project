-- 1. 修改 学术论文写作表
ALTER TABLE academic_paper MODIFY create_by VARCHAR(64) DEFAULT '' COMMENT '创建者';
ALTER TABLE academic_paper MODIFY update_by VARCHAR(64) DEFAULT '' COMMENT '更新者';

-- 2. 修改 学术论文投稿表
ALTER TABLE academic_paper_submission MODIFY create_by VARCHAR(64) DEFAULT '' COMMENT '创建者';
ALTER TABLE academic_paper_submission MODIFY update_by VARCHAR(64) DEFAULT '' COMMENT '更新者';