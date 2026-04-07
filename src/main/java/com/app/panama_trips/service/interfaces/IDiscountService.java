package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.DiscountRequest;
import com.app.panama_trips.presentation.dto.DiscountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service contract for managing discounts.
 */
public interface IDiscountService {

  /**
   * Gets all discounts with pagination.
   * 
   * @param pageable pagination configuration
   * @return page of discounts
   */
  Page<DiscountResponse> getAllDiscounts(Pageable pageable);

  /**
   * Gets a discount by its ID.
   * 
   * @param id discount identifier
   * @return the found discount
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  DiscountResponse getDiscountById(Long id);

  /**
   * Gets all discounts for a specific reservation.
   * 
   * @param reservationId identifier of the reservation
   * @return list of discounts for the reservation
   */
  List<DiscountResponse> getDiscountsByReservationId(Integer reservationId);

  /**
   * Creates a new discount.
   * 
   * @param request discount data to create
   * @return the created discount
   */
  DiscountResponse saveDiscount(DiscountRequest request);

  /**
   * Updates an existing discount.
   * 
   * @param id      identifier of the discount to update
   * @param request new discount data
   * @return the updated discount
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  DiscountResponse updateDiscount(Long id, DiscountRequest request);

  /**
   * Deletes a discount by its ID.
   * 
   * @param id identifier of the discount to delete
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  void deleteDiscount(Long id);
}
