package com.tr_dat.orderservice.service;

import com.tr_dat.orderservice.dto.InventoryResponse;
import com.tr_dat.orderservice.dto.OrderLineItemsDto;
import com.tr_dat.orderservice.dto.OrderRequest;
import com.tr_dat.orderservice.model.Order;
import com.tr_dat.orderservice.model.OrderLineItems;
import com.tr_dat.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;

  public String placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
      .stream()
      .map(this::mapToDto)
      .toList();

    order.setOrderLineItemsList(orderLineItems);

    List<String> skuCodes = order.getOrderLineItemsList().stream()
      .map(OrderLineItems::getSkuCode)
      .toList();

    InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
      .uri("http://inventory-service/api/inventory",
        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        .retrieve()
          .bodyToMono(InventoryResponse[].class)
            .block();

    assert inventoryResponseArray != null;
    Boolean allProductsInStock = Arrays.stream(inventoryResponseArray).
      allMatch(InventoryResponse::getInStock);

    if (Boolean.TRUE.equals(allProductsInStock)) {
      orderRepository.save(order);
      return "Order Placed Successfully";
    } else {
      throw new IllegalArgumentException("Product is not in stock. Please try again later.");
    }
  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();

    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setOrderNumber(orderLineItems.getOrderNumber());
    orderLineItems.setQuantity(orderLineItems.getQuantity());
    orderLineItems.setSkuCode(orderLineItems.getSkuCode());
    return orderLineItems;
  }
}
