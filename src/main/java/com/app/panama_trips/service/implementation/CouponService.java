package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.BusinessRuleException;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Coupon;
import com.app.panama_trips.persistence.repository.CouponRepository;
import com.app.panama_trips.presentation.dto.CouponRequest;
import com.app.panama_trips.presentation.dto.CouponResponse;
import com.app.panama_trips.service.interfaces.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CouponService implements ICouponService {

  private final CouponRepository couponRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<CouponResponse> getAllCoupons(Pageable pageable) {
    return this.couponRepository.findAll(pageable)
        .map(CouponResponse::new);
  }

  @Override
  @Transactional(readOnly = true)
  public CouponResponse getCouponById(Long id) {
    return this.couponRepository.findById(id)
        .map(CouponResponse::new)
        .orElseThrow(() -> new ResourceNotFoundException("Coupon with id " + id + " not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public CouponResponse getCouponByCode(String code) {
    return this.couponRepository.findByCode(code)
        .map(CouponResponse::new)
        .orElseThrow(() -> new ResourceNotFoundException("Coupon with code " + code + " not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public CouponResponse validateAndGetCoupon(String code) {
    Coupon coupon = this.couponRepository.findByCode(code)
        .orElseThrow(() -> new ResourceNotFoundException("Coupon with code " + code + " not found"));

    if (coupon.getExpirationDate().isBefore(LocalDate.now())) {
      throw new BusinessRuleException("Coupon with code " + code + " has expired");
    }

    return new CouponResponse(coupon);
  }

  @Override
  @Transactional
  public CouponResponse createCoupon(CouponRequest request) {
    if (this.couponRepository.findByCode(request.code()).isPresent()) {
      throw new BusinessRuleException("Coupon with code " + request.code() + " already exists");
    }

    Coupon coupon = new Coupon();
    coupon.setCode(request.code());
    coupon.setDiscountPercentage(request.discountPercentage());
    coupon.setExpirationDate(request.expirationDate());

    return new CouponResponse(this.couponRepository.save(coupon));
  }

  @Override
  @Transactional
  public CouponResponse updateCoupon(Long id, CouponRequest request) {
    Coupon coupon = this.couponRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Coupon with id " + id + " not found"));

    if (!coupon.getCode().equals(request.code()) &&
        this.couponRepository.findByCode(request.code()).isPresent()) {
      throw new BusinessRuleException("Coupon with code " + request.code() + " already exists");
    }

    coupon.setCode(request.code());
    coupon.setDiscountPercentage(request.discountPercentage());
    coupon.setExpirationDate(request.expirationDate());

    return new CouponResponse(this.couponRepository.save(coupon));
  }

  @Override
  @Transactional
  public void deleteCoupon(Long id) {
    if (!this.couponRepository.existsById(id)) {
      throw new ResourceNotFoundException("Coupon with id " + id + " not found");
    }
    this.couponRepository.deleteById(id);
  }
}
