package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Coupon;
import com.app.panama_trips.persistence.entity.Discount;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.repository.CouponRepository;
import com.app.panama_trips.persistence.repository.DiscountRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.DiscountRequest;
import com.app.panama_trips.presentation.dto.DiscountResponse;
import com.app.panama_trips.service.interfaces.IDiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService implements IDiscountService {

  private final DiscountRepository discountRepository;
  private final ReservationRepository reservationRepository;
  private final CouponRepository couponRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<DiscountResponse> getAllDiscounts(Pageable pageable) {
    return this.discountRepository.findAll(pageable)
        .map(DiscountResponse::new);
  }

  @Override
  @Transactional(readOnly = true)
  public DiscountResponse getDiscountById(Long id) {
    return this.discountRepository.findById(id)
        .map(DiscountResponse::new)
        .orElseThrow(() -> new ResourceNotFoundException("Discount with id " + id + " not found"));
  }

  @Override
  @Transactional(readOnly = true)
  public List<DiscountResponse> getDiscountsByReservationId(Integer reservationId) {
    return this.discountRepository.findByReservationId_Id(reservationId)
        .stream()
        .map(DiscountResponse::new)
        .toList();
  }

  @Override
  @Transactional
  public DiscountResponse saveDiscount(DiscountRequest request) {
    Discount discount = new Discount();
    updateDiscountFields(discount, request);
    return new DiscountResponse(this.discountRepository.save(discount));
  }

  @Override
  @Transactional
  public DiscountResponse updateDiscount(Long id, DiscountRequest request) {
    Discount discount = this.discountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Discount with id " + id + " not found"));

    updateDiscountFields(discount, request);
    return new DiscountResponse(this.discountRepository.save(discount));
  }

  @Override
  @Transactional
  public void deleteDiscount(Long id) {
    if (!this.discountRepository.existsById(id)) {
      throw new ResourceNotFoundException("Discount with id " + id + " not found");
    }
    this.discountRepository.deleteById(id);
  }

  private void updateDiscountFields(Discount discount, DiscountRequest request) {
    Reservation reservation = this.reservationRepository.findById(request.reservationId())
        .orElseThrow(
            () -> new ResourceNotFoundException("Reservation with id " + request.reservationId() + " not found"));

    discount.setReservationId(reservation);

    if (request.couponId() != null) {
      Coupon coupon = this.couponRepository.findById(request.couponId())
          .orElseThrow(() -> new ResourceNotFoundException("Coupon with id " + request.couponId() + " not found"));
      discount.setCouponId(coupon);
    } else {
      discount.setCouponId(null);
    }

    discount.setDiscountAmount(request.discountAmount());
  }
}
