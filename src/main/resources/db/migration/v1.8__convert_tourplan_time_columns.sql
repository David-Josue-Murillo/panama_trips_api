-- Convert startTime/endTime from VARCHAR(8) to TIME
ALTER TABLE tour_plans
    ALTER COLUMN start_time TYPE TIME USING start_time::TIME;

ALTER TABLE tour_plans
    ALTER COLUMN end_time TYPE TIME USING end_time::TIME;
