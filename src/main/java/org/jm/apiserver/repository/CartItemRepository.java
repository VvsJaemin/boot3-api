package org.jm.apiserver.repository;

import org.jm.apiserver.domain.CartItem;
import org.jm.apiserver.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정한 사용자의 모든 장바구니 아이템들을 가져올 경우 input -> email out-> cartItemListDTO

    @Query("select " +
            " new org.jm.apiserver.dto.CartItemListDTO(ci.cino, ci.qty, p.pname, p.price, pi.fileName) " +
            " from " +
            " CartItem ci inner join Cart mc on ci.cart = mc " +
            " left join Product p on ci.product = p " +
            " left join p.imageList pi " +
            " where " +
            " mc.owner.email = :email and pi.ord = 0 order by ci.cino desc ")
    List<Object[]> getItemsOfCartDTOByEmail(@Param("email") String email);

    // 사용자의 이메일, 상품 번호로 해당 상품이 장비구니 아이템으로 존재하는지 확인

    // 장바구니 아이템 번호로 장바구니를 얻어오려고 하는 경우

    // 장바구니 번호로 모든 장바구니 아이템들 조회

}