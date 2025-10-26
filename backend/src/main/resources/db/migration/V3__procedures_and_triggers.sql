-- Stored procedures & triggers

DELIMITER $$


CREATE PROCEDURE sp_calculate_refund(IN p_booking_id BIGINT, IN p_cancel_ts DATETIME, OUT p_refund DECIMAL(10,2))
BEGIN
  DECLARE v_trip_id BIGINT;
  DECLARE v_departure DATETIME;
  DECLARE v_total DECIMAL(10,2);
  DECLARE v_hours INT;

SELECT b.trip_id, t.departure_time, b.total_amount
INTO v_trip_id, v_departure, v_total
FROM bookings b
         JOIN trips t ON t.id = b.trip_id
WHERE b.id = p_booking_id;

SET v_hours = TIMESTAMPDIFF(HOUR, p_cancel_ts, v_departure);

  IF v_hours >= 24 THEN
    SET p_refund = v_total;
  ELSEIF v_hours >= 12 THEN
    SET p_refund = ROUND(v_total * 0.50, 2);
  ELSEIF v_hours >= 2 THEN
    SET p_refund = ROUND(v_total * 0.20, 2);
ELSE
    SET p_refund = 0.00;
END IF;
END$$


CREATE TRIGGER trg_booking_seat_after_ins
    AFTER INSERT ON booking_seats
    FOR EACH ROW
BEGIN
    UPDATE trips
    SET reserved_seats = (
        SELECT COUNT(*) FROM booking_seats bs
                                 JOIN bookings b ON b.id = bs.booking_id AND b.status = 'CONFIRMED'
        WHERE bs.trip_id = NEW.trip_id
    )
    WHERE id = NEW.trip_id;
    END$$

    CREATE TRIGGER trg_booking_seat_after_del
        AFTER DELETE ON booking_seats
        FOR EACH ROW
    BEGIN
        UPDATE trips
        SET reserved_seats = (
            SELECT COUNT(*) FROM booking_seats bs
                                     JOIN bookings b ON b.id = bs.booking_id AND b.status = 'CONFIRMED'
            WHERE bs.trip_id = OLD.trip_id
        )
        WHERE id = OLD.trip_id;
        END$$

        -- 3) Audit trip assignments
        CREATE TRIGGER trg_trip_assignment_audit
            AFTER UPDATE ON trips
            FOR EACH ROW
        BEGIN
            IF (OLD.bus_id <> NEW.bus_id) OR (OLD.driver_id <> NEW.driver_id) THEN
    INSERT INTO trip_assignments (trip_id,bus_id,driver_id,assigned_by_user_id,assigned_at)
    VALUES (NEW.id, COALESCE(NEW.bus_id, OLD.bus_id), COALESCE(NEW.driver_id, OLD.driver_id), 1, CURRENT_TIMESTAMP);
        END IF;
        END$$


        CREATE TRIGGER trg_trip_prevent_maint
            BEFORE UPDATE ON trips
            FOR EACH ROW
        BEGIN
            IF NEW.bus_id IS NOT NULL THEN
    IF EXISTS (
      SELECT 1
        FROM bus_maintenance m
       WHERE m.bus_id = NEW.bus_id
         AND m.status IN ('SCHEDULED','IN_PROGRESS')
         AND ( (m.start_time <= NEW.departure_time AND (m.end_time IS NULL OR m.end_time >= NEW.departure_time))
            OR (m.start_time <= NEW.arrival_time   AND (m.end_time IS NULL OR m.end_time >= NEW.arrival_time)) )
    ) THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bus is under maintenance during the trip window';
        END IF;
    END IF;
    END$$

    DELIMITER ;
