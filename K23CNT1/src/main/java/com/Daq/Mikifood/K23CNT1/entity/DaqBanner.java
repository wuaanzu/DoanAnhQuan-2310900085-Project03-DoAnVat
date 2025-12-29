package com.Daq.Mikifood.K23CNT1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaqBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Integer bannerId;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "link_url", length = 255)
    private String linkUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private DaqCategory category;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
