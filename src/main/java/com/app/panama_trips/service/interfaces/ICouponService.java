package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.CouponRequest;
import com.app.panama_trips.presentation.dto.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Service contract for managing coupons.
 */
public interface ICouponService {

  /**
   * Gets all coupons with pagination.
   * 
   * @param pageable pagination configuration
   * @return page of coupons
   */
  Page<CouponResponse> getAllCoupons(Pageable pageable);

  /**
   * Gets a coupon by its ID.
   * 
   * @param id coupon identifier
   * @return the found coupon
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  CouponResponse getCouponById(Long id);

  /**
   * Gets a coupon by its code.
   * 
   * @param code coupon code
   * @return the found coupon
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  CouponResponse getCouponByCode(String code);

  /**
   * Validates a coupon by its code and returns its details if valid.
   * 
   * @param code coupon code to validate
   * @return coupon details if valid
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   * @throws com.app.panama_trips.exception.BusinessRuleException     if expired
   */
  CouponResponse validateAndGetCoupon(String code);

  /**
   * Creates a new coupon.
   * 
   * @param request coupon data to create
   * @return the created coupon
   * @throws com.app.panama_trips.exception.BusinessRuleException if code already
   *                                                              exists
   */
  CouponResponse createCoupon(CouponRequest request);

  /**
   * Updates an existing coupon.
   * 
   * @param id      identifier of the coupon to update
   * @param request new coupon data
   * @return the updated coupon
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  CouponResponse updateCoupon(Long id, CouponRequest request);

  /**
   * Deletes a coupon by its ID.
   * 
   * @param id identifier of the coupon to delete
   * @throws com.app.panama_trips.exception.ResourceNotFoundException if not found
   */
  void deleteCoupon(Long id);
}
