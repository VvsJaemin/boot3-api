package org.jm.apiserver.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    private String fileName;

    private int ord; // 대표이미지는 ord 0번만

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
