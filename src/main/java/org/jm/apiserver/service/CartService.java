package org.jm.apiserver.service;

import org.jm.apiserver.dto.CartItemDTO;
import org.jm.apiserver.dto.CartItemListDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CartService {

    List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO);

    List<CartItemListDTO> getCartItems(String email);

    List<CartItemListDTO> remove(Long cino);


}
