package com.ruoyi.system.domain.dto;

public class SampleUsageDTO {
    private Long sampleId;      // 样本ID
    private Long usedQuantity; // 使用数量
    private String remarks;     // 备注

    // Getters and Setters
    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(Long usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "SampleUsageDTO{" +
                "sampleId=" + sampleId +
                ", usedQuantity=" + usedQuantity +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
