package io.github.wyatt.muffer.domain.board.entity;

import io.github.wyatt.muffer.domain.board.code.ChargeType;
import io.github.wyatt.muffer.domain.board.code.MicType;
import io.github.wyatt.muffer.domain.board.code.ProductCategory;
import io.github.wyatt.muffer.domain.board.code.QualityGrade;
import io.github.wyatt.muffer.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//NOTE 나중에 컬럼이 카테고리에 따라 명확히 갈리면 JSON 타입으로 변경해 관리
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private QualityGrade qualityGrade;

    private Integer brandOptionId;
    private String customBrand;

    private String serialCode;
    private boolean isSecondedHand;
    private int usePeriod;
    private boolean isDamaged;
    private String damagedInfo;

    private String driver; // 드라이버 크기
    @Enumerated(EnumType.STRING)
    private ChargeType chargeType; // 충전 방식
    @Enumerated(EnumType.STRING)
    private MicType micType; // 마이크 타입
    private String connectType; // 연결방식
    private float batteryTime;
    private boolean canWire;
    private boolean canWireless;

    @Builder
    private Product(String name, ProductCategory category, QualityGrade qualityGrade,
                    Integer brandOptionId, String customBrand,
                    String serialCode, boolean isSecondedHand, int usePeriod, boolean isDamaged,
                    String damagedInfo, String driver, ChargeType chargeType, MicType micType,
                    String connectType, float batteryTime, boolean canWire, boolean canWireless) {
        this.name = name;
        this.category = category;
        this.qualityGrade = qualityGrade;
        this.brandOptionId = brandOptionId;
        this.customBrand = customBrand;
        this.serialCode = serialCode;
        this.isSecondedHand = isSecondedHand;
        this.usePeriod = usePeriod;
        this.isDamaged = isDamaged;
        this.damagedInfo = damagedInfo;
        this.driver = driver;
        this.chargeType = chargeType;
        this.micType = micType;
        this.connectType = connectType;
        this.batteryTime = batteryTime;
        this.canWire = canWire;
        this.canWireless = canWireless;
    }

    public static Product create(String name, ProductCategory category, QualityGrade qualityGrade,
                                 Integer brandOptionId, String customBrand,
                                 String serialCode, boolean isSecondedHand, int usePeriod, boolean isDamaged,
                                 String damagedInfo, String driver, ChargeType chargeType, MicType micType,
                                 String connectType, float batteryTime, boolean canWire, boolean canWireless) {
        return Product.builder()
                .name(name)
                .category(category)
                .qualityGrade(qualityGrade)
                .brandOptionId(brandOptionId)
                .customBrand(customBrand)
                .serialCode(serialCode)
                .isSecondedHand(isSecondedHand)
                .usePeriod(usePeriod)
                .isDamaged(isDamaged)
                .damagedInfo(damagedInfo)
                .driver(driver)
                .chargeType(chargeType)
                .micType(micType)
                .connectType(connectType)
                .batteryTime(batteryTime)
                .canWire(canWire)
                .canWireless(canWireless)
                .build();
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", qualityGrade=" + qualityGrade +
                ", brandOptionId=" + brandOptionId +
                ", customBrand='" + customBrand + '\'' +
                ", serialCode='" + serialCode + '\'' +
                ", isSecondedHand=" + isSecondedHand +
                ", usePeriod=" + usePeriod +
                ", isDamaged=" + isDamaged +
                ", damagedInfo='" + damagedInfo + '\'' +
                ", driver='" + driver + '\'' +
                ", chargeType=" + chargeType +
                ", micType=" + micType +
                ", connectType='" + connectType + '\'' +
                ", batteryTime=" + batteryTime +
                ", canWire=" + canWire +
                ", canWireless=" + canWireless +
                '}';
    }

}
