package org.codenova.craft.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "part")
public class Part {

    @Id
    @Column(name = "SITE_ID")
    private String siteId;

    @Id
    @Column(name = "PART_ID")
    private String partId;

    @Id
    @Column(name = "PART_TYPE")
    private String partType;

    @Column(name = "SCENARIO_ID")
    private String scenarioId;

    @Column(name = "ROUTING_ID")
    private String routingId;

    @Column(name = "PART_NAME")
    private String partName;

    @Column(name = "MIN_BATCH_SIZE")
    private Integer minBatchSize;

    @Column(name = "MAX_BATCH_SIZE")
    private Integer maxBatchSize;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "MATERIAL_GROUP")
    private String materialGroup;

    @Column(name = "MATERIAL_GROUP_DESCRIPTION")
    private String materialGroupDescription;

    @Column(name = "ABC_CODE")
    private String abcCode;

    @Column(name = "SAFETY_STOCK_QTY")
    private String safetyStockQty;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PRODUCTFAMILY_VALUE")
    private String productFamilyValue;

    @Column(name = "PROCUREMENT_TYPE")
    private String procurementType;

    @Column(name = "SPECIAL_PROCUREMENT_TYPE")
    private String specialProcurementType;

    @Column(name = "LABOR")
    private String labor;

    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createDatetime;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "UPDATE_DATETIME")
    private LocalDateTime updateDatetime;

    @Column(name = "UPDATE_BY")
    private String updateBy;

    @Column(name = "INTERFACE_DATE")
    private String interfaceDate;

    @Column(name = "INTERFACE_TIME")
    private String interfaceTime;

    @Column(name = "INTERFACE_TYPE")
    private String interfaceType;

    @Column(name = "INTERFACE_TEXT")
    private String interfaceText;

    @Column(name = "OPERATION_ID")
    private String operationId;

    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
}
