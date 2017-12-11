package com.oxchains.themis.arbitrate.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "order_arbitrate_upload_evidence")
@Data
public class OrderEvidence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private String buyerContent;
    private String buyerFiles;
    private String sellerContent;
    private String sellerFiles;
}
