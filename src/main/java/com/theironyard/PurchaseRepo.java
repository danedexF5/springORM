package com.theironyard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PurchaseRepo extends PagingAndSortingRepository<Purchase, Integer> {
    Page<Purchase> findPurchaseByCategory(Pageable pageable, String category);


}
