package com.tr_dat.inventoryservice.service;

import com.tr_dat.inventoryservice.dto.InventoryResponse;
import com.tr_dat.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
//        log.info("Wait started!");
//        Thread.sleep(10000);
//        log.info("Wait Ended!");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
          .map(inventory ->
              InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .inStock(inventory.getQuantity() > 0)
                .build()
          ).toList();
    }
}
