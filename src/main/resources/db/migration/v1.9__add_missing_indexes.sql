-- =============================================
-- v1.9: Add missing indexes for query optimization
-- =============================================

-- audit_logs (high-traffic table for security/compliance)
CREATE INDEX idx_audit_logs_entity_type_id ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_timestamp ON audit_logs(action_timestamp);
CREATE INDEX idx_audit_logs_ip_address ON audit_logs(ip_address);

-- reservations (missing tour_plan_id and status indexes)
CREATE INDEX idx_reservations_tour_plan_id ON reservations(tour_plan_id);
CREATE INDEX idx_reservations_status ON reservations(reservation_status);

-- payment_installments
CREATE INDEX idx_payment_installments_reservation_id ON payment_installments(reservation_id);
CREATE INDEX idx_payment_installments_payment_id ON payment_installments(payment_id);
CREATE INDEX idx_payment_installments_status ON payment_installments(status);
CREATE INDEX idx_payment_installments_due_date ON payment_installments(due_date);

-- tour_price_history
CREATE INDEX idx_tour_price_history_tour_plan_id ON tour_price_history(tour_plan_id);
CREATE INDEX idx_tour_price_history_changed_at ON tour_price_history(changed_at);
CREATE INDEX idx_tour_price_history_changed_by ON tour_price_history(changed_by);

-- notification_history
CREATE INDEX idx_notification_history_user_id ON notification_history(user_id);
CREATE INDEX idx_notification_history_status ON notification_history(delivery_status);

-- reviews
CREATE INDEX idx_reviews_tour_plan_id ON reviews(tour_plan_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);

-- guides
CREATE INDEX idx_guides_provider_id ON guides(provider_id);

-- tour_faqs
CREATE INDEX idx_tour_faqs_tour_plan_id ON tour_faqs(tour_plan_id);

-- tour_assignments
CREATE INDEX idx_tour_assignments_tour_plan_id ON tour_assignments(tour_plan_id);
CREATE INDEX idx_tour_assignments_guide_id ON tour_assignments(guide_id);

-- marketing_campaigns
CREATE INDEX idx_marketing_campaigns_status ON marketing_campaigns(status);
CREATE INDEX idx_marketing_campaigns_created_by ON marketing_campaigns(created_by);

-- notification_templates
CREATE INDEX idx_notification_templates_type ON notification_templates(type);
