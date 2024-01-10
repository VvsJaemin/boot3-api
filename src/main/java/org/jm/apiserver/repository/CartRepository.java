package org.jm.apiserver.repository;

import org.jm.apiserver.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select cart from Cart cart where cart.owner.email = :email") // 계정당 장바구니 조회
    Optional<Cart> getCartOfMember(@Param("email") String email);
}
