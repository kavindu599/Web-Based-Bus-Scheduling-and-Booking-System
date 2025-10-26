-- Meaningful SQL queries demonstrating JOIN, WHERE, GROUP BY, HAVING, ORDER BY,
-- aggregates, and subqueries. Adjust dates as needed for your demo.

-- 1) Trips with route, bus, driver names between dates (JOIN + WHERE + ORDER)
SELECT t.id AS trip_id,
       r.name AS route_name,
       t.departure_time, t.arrival_time,
       b.plate_number AS bus,
       CONCAT(d.first_name,' ',d.last_name) AS driver,
       t.status, t.reserved_seats
FROM trips t
         JOIN routes r ON r.id = t.route_id
         LEFT JOIN buses b ON b.id = t.bus_id
         LEFT JOIN drivers d ON d.id = t.driver_id
WHERE t.departure_time BETWEEN '2025-10-06 00:00:00' AND '2025-10-06 23:59:59'
ORDER BY r.name, t.departure_time;

-- 2) Seat utilization ratio per trip (GROUP BY + HAVING)
SELECT t.id AS trip_id,
       r.name AS route_name,
       b.plate_number,
       t.reserved_seats,
       b.capacity,
       ROUND(100 * t.reserved_seats / b.capacity, 1) AS utilization_pct
FROM trips t
         JOIN routes r ON r.id = t.route_id
         JOIN buses b ON b.id = t.bus_id
GROUP BY t.id, r.name, b.plate_number, t.reserved_seats, b.capacity
HAVING utilization_pct >= 10
ORDER BY utilization_pct DESC;

-- 3) Revenue per route (JOIN + SUM + GROUP BY)
SELECT r.name AS route_name,
       SUM(p.amount) AS total_revenue,
       COUNT(DISTINCT b.id) AS bookings_count
FROM payments p
         JOIN bookings b ON b.id = p.booking_id AND p.status = 'PAID'
         JOIN trips t ON t.id = b.trip_id
         JOIN routes r ON r.id = t.route_id
GROUP BY r.name
ORDER BY total_revenue DESC;

-- 4) Top drivers by number of trips in a date range (GROUP + ORDER)
SELECT CONCAT(d.first_name,' ',d.last_name) AS driver,
       COUNT(*) AS trips_count
FROM trips t
         JOIN drivers d ON d.id = t.driver_id
WHERE t.departure_time BETWEEN '2025-10-01' AND '2025-10-31'
GROUP BY d.id
ORDER BY trips_count DESC, driver;

-- 5) Passengers with more than one booking (HAVING)
SELECT p.full_name, p.phone, COUNT(b.id) AS bookings
FROM passengers p
         JOIN bookings b ON b.passenger_id = p.id
GROUP BY p.id
HAVING COUNT(b.id) > 1
ORDER BY bookings DESC;

-- 6) Find overlapping assignments for the same driver (self-join + time overlap)
SELECT t1.id AS trip1, t2.id AS trip2, d.id AS driver_id,
       CONCAT(d.first_name,' ',d.last_name) AS driver_name
FROM trips t1
         JOIN trips t2 ON t2.id > t1.id
         JOIN drivers d ON d.id = t1.driver_id AND d.id = t2.driver_id
WHERE t1.status = 'SCHEDULED' AND t2.status = 'SCHEDULED'
  AND t1.departure_time < t2.arrival_time
  AND t2.departure_time < t1.arrival_time;

-- 7) Buses available at a given time window (NOT EXISTS with subqueries)
-- Example: check availability for 2025-10-06 09:00–10:00
SELECT b.id, b.plate_number
FROM buses b
WHERE b.status = 'ACTIVE'
  AND NOT EXISTS (
    SELECT 1 FROM trips t
    WHERE t.bus_id = b.id
      AND t.status = 'SCHEDULED'
      AND t.departure_time < '2025-10-06 10:00:00'
      AND t.arrival_time   > '2025-10-06 09:00:00'
)
  AND NOT EXISTS (
    SELECT 1 FROM bus_maintenance m
    WHERE m.bus_id = b.id
      AND m.status IN ('SCHEDULED','IN_PROGRESS')
      AND ( (m.start_time <= '2025-10-06 09:00:00' AND (m.end_time IS NULL OR m.end_time >= '2025-10-06 09:00:00'))
        OR (m.start_time <= '2025-10-06 10:00:00' AND (m.end_time IS NULL OR m.end_time >= '2025-10-06 10:00:00')) )
);

-- 8) Refunds summary by status (GROUP + ORDER)
SELECT status, COUNT(*) AS cnt, SUM(amount) AS total_amount
FROM refunds
GROUP BY status
ORDER BY FIELD(status,'REQUESTED','APPROVED','PAID','DECLINED');

-- 9) Upcoming maintenance schedule per bus (ORDER)
SELECT b.plate_number, m.start_time, m.end_time, m.reason, m.status
FROM bus_maintenance m
         JOIN buses b ON b.id = m.bus_id
WHERE m.start_time >= CURRENT_DATE()
ORDER BY m.start_time;

-- 10) Ticket verifications per user/day (DATE + GROUP BY)
SELECT v.verified_by_user_id,
    DATE(v.verified_at) AS day,
    COUNT(*) AS verified_count
FROM ticket_verifications v
GROUP BY v.verified_by_user_id, DATE(v.verified_at)
ORDER BY day DESC, verified_count DESC;
