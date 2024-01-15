package org.jm.apiserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.jm.apiserver.domain.Cart;
import org.jm.apiserver.domain.CartItem;
import org.jm.apiserver.domain.Member;
import org.jm.apiserver.domain.Product;
import org.jm.apiserver.dto.CartItemDTO;
import org.jm.apiserver.dto.CartItemListDTO;
import org.jm.apiserver.repository.CartItemRepository;
import org.jm.apiserver.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;


    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();

        int qty = cartItemDTO.getQty();

        Long cino = cartItemDTO.getCino();

        // 기존에 담겨 있는 상품 -> 수량만 변경
        if (cino != null) {
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);

            CartItem cartItem = cartItemResult.orElseThrow();

            cartItem.changeQty(qty);

            cartItemRepository.save(cartItem);

            return getCartItems(email);

        }

        Cart cart = getCart(email);

        CartItem cartItem = null;

        cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem == null) {

            Product product = Product.builder().pno(pno).build();

            cartItem = CartItem.builder()
                    .product(product)
                    .cart(cart)
                    .qty(qty)
                    .build();
        }else{

            cartItem.changeQty(qty);
        }

        cartItemRepository.save(cartItem);

        return getCartItems(email);
    }

    /**
     * 사용자의 카트가 없을 경우 카트 생성해주는 메서드
     * @param email
     * @return
     */
    private Cart getCart(String email) {

        // 해당 email의 장바구니(Cart)가 있는지 확인 -> 있으면 반환

        // 없으면 Cart 객체 생성하고 추가 반환

        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if (result.isEmpty()) {
            log.info("Cart of the member is not exist");

            Member member = Member.builder().email(email).build();

            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);


        } else {

            cart = result.get();
        }

        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {

        return  cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {

        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cno);

        return cartItemRepository.getItemsOfCartDTOByCart(cno);
    }
}
