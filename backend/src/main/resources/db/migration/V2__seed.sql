-- Seed data (≥5 valid rows per table) respecting FK order.
-- Password hash = bcrypt for 'Password@123' (same for all demo users)
-- Hash source (example): $2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G
-- (You can replace with your own hashes if desired.)

SET NAMES utf8mb4;
SET time_zone = '+00:00';
SET FOREIGN_KEY_CHECKS = 0;

-- ROLES
INSERT INTO roles (id, name, description) VALUES
                                              (1,'ADMIN','System administrator'),
                                              (2,'OPS_MANAGER','Operations Manager'),
                                              (3,'BOOKING_CLERK','Booking Clerk'),
                                              (4,'CUSTOMER_SERVICE','Customer Service Officer'),
                                              (5,'SENIOR_TICKETING','Senior Ticketing Clerk'),
                                              (6,'DRIVER','Bus Driver'),
                                              (7,'IT_TECH','IT Technician');

-- PERMISSIONS (sample; expand as needed)
INSERT INTO permissions (id, name, description) VALUES
                                                    (1,'ROUTE_MANAGE','Create/Update/Delete routes & stops'),
                                                    (2,'TRIP_MANAGE','Create/Edit/Cancel trips'),
                                                    (3,'ASSIGNMENT_MANAGE','Assign buses/drivers'),
                                                    (4,'BOOKING_MANAGE','Create bookings'),
                                                    (5,'PAYMENT_MANAGE','Collect payments'),
                                                    (6,'TICKET_VERIFY','Verify tickets'),
                                                    (7,'REFUND_MANAGE','Process refunds'),
                                                    (8,'USER_MANAGE','Create/reset/deactivate users'),
                                                    (9,'ISSUE_MANAGE','Handle driver issues'),
                                                    (10,'REPORT_VIEW','View reports');

-- USERS (staff)
INSERT INTO users (id,email,password_hash,full_name,phone,status) VALUES
                                                                      (1,'admin@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Admin User','+94 70 000 0001','ACTIVE'),
                                                                      (2,'ops@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Ops Manager','+94 70 000 0002','ACTIVE'),
                                                                      (3,'book@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Booking Clerk','+94 70 000 0003','ACTIVE'),
                                                                      (4,'cs@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Customer Service','+94 70 000 0004','ACTIVE'),
                                                                      (5,'ticket@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Senior Ticketing','+94 70 000 0005','ACTIVE'),
                                                                      (6,'driver1@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Driver One','+94 70 000 0006','ACTIVE'),
                                                                      (7,'driver2@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','Driver Two','+94 70 000 0007','ACTIVE'),
                                                                      (8,'it@bus.test','$2a$10$7Q8kqQ5EVNQeZ5mTqM5kMOGm3l0eQz0lGqz2q9y9zvXc8s4gq1b2G','IT Technician','+94 70 000 0008','ACTIVE');

-- USER_ROLES
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1,1),(1,7),
                                              (2,2),
                                              (3,3),
                                              (4,4),
                                              (5,5),
                                              (6,6),
                                              (7,6),
                                              (8,7);

-- ROLE_PERMISSIONS (subset mapping)
INSERT INTO role_permissions (role_id, permission_id) VALUES
                                                          (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),
                                                          (2,1),(2,2),(2,3),(2,9),(2,10),
                                                          (3,4),(3,5),(3,10),
                                                          (4,7),(4,10),
                                                          (5,5),(5,6),(5,10),
                                                          (7,8),(7,10);

-- ROUTES
INSERT INTO routes (id,name,origin,destination,distance_km,duration_min,status) VALUES
                                                                                    (1,'Malabe–Kollupitiya','Malabe','Kollupitiya',17.5,60,'ACTIVE'),
                                                                                    (2,'Kaduwela–Fort','Kaduwela','Colombo Fort',18.0,65,'ACTIVE'),
                                                                                    (3,'Nugegoda–Pettah','Nugegoda','Pettah',9.4,40,'ACTIVE'),
                                                                                    (4,'Dehiwala–Battaramulla','Dehiwala','Battaramulla',14.2,55,'ACTIVE'),
                                                                                    (5,'Maharagama–Bambalapitiya','Maharagama','Bambalapitiya',12.1,50,'ACTIVE');

-- STOPS
INSERT INTO stops (id,name,lat,lng) VALUES
                                        (1,'Malabe Town',6.905000,79.957000),
                                        (2,'Kollupitiya',6.911100,79.849900),
                                        (3,'Kaduwela',6.933300,79.983300),
                                        (4,'Colombo Fort',6.935500,79.842800),
                                        (5,'Nugegoda',6.872200,79.889700),
                                        (6,'Pettah',6.938300,79.857800),
                                        (7,'Dehiwala',6.840000,79.865000),
                                        (8,'Battaramulla',6.900300,79.917400),
                                        (9,'Maharagama',6.841400,79.927200),
                                        (10,'Bambalapitiya',6.900100,79.855300);

-- ROUTE_STOPS (orders)
INSERT INTO route_stops (route_id,stop_id,stop_order,arrival_offset_min) VALUES
                                                                             -- Route 1
                                                                             (1,1,1,0),(1,5,2,20),(1,10,3,45),(1,2,4,60),
                                                                             -- Route 2
                                                                             (2,3,1,0),(2,8,2,20),(2,4,3,65),
                                                                             -- Route 3
                                                                             (3,5,1,0),(3,6,2,40),
                                                                             -- Route 4
                                                                             (4,7,1,0),(4,9,2,25),(4,8,3,55),
                                                                             -- Route 5
                                                                             (5,9,1,0),(5,5,2,20),(5,10,3,50);

-- BUSES
INSERT INTO buses (id,plate_number,capacity,type,status) VALUES
                                                             (1,'WP-NA-1770',50,'AC','ACTIVE'),
                                                             (2,'WP-NB-2211',45,'NON_AC','ACTIVE'),
                                                             (3,'WP-NC-3302',40,'AC','MAINTENANCE'),
                                                             (4,'WP-ND-4423',55,'LUXURY','ACTIVE'),
                                                             (5,'WP-NE-5584',50,'AC','ACTIVE');

-- DRIVERS
INSERT INTO drivers (id,first_name,last_name,license_no,phone,status) VALUES
                                                                          (1,'Kamal','Perera','L-DRV-1001','0771000001','ACTIVE'),
                                                                          (2,'Sunil','De Silva','L-DRV-1002','0771000002','ACTIVE'),
                                                                          (3,'Ruwan','Fernando','L-DRV-1003','0771000003','ACTIVE'),
                                                                          (4,'Ajith','Jayasekara','L-DRV-1004','0771000004','SUSPENDED'),
                                                                          (5,'Nimal','Gunasekara','L-DRV-1005','0771000005','ACTIVE');

-- BUS_MAINTENANCE
INSERT INTO bus_maintenance (id,bus_id,start_time,end_time,reason,status) VALUES
                                                                              (1,3,'2025-10-01 06:00:00','2025-10-03 18:00:00','Engine Overhaul','DONE'),
                                                                              (2,2,'2025-10-05 09:00:00','2025-10-05 17:00:00','Oil Change','SCHEDULED'),
                                                                              (3,1,'2025-10-10 10:00:00','2025-10-10 12:00:00','Tyre Replacement','SCHEDULED'),
                                                                              (4,5,'2025-10-08 08:00:00','2025-10-08 11:00:00','Brake Check','SCHEDULED'),
                                                                              (5,4,'2025-10-12 13:00:00','2025-10-12 16:00:00','AC Service','SCHEDULED');

-- TRIPS (future dates for demo)
INSERT INTO trips (id,route_id,departure_time,arrival_time,status,bus_id,driver_id,reserved_seats) VALUES
                                                                                                       (1,1,'2025-10-06 07:00:00','2025-10-06 08:00:00','SCHEDULED',1,1,0),
                                                                                                       (2,1,'2025-10-06 09:00:00','2025-10-06 10:00:00','SCHEDULED',2,2,0),
                                                                                                       (3,2,'2025-10-06 07:30:00','2025-10-06 08:35:00','SCHEDULED',4,3,0),
                                                                                                       (4,3,'2025-10-06 08:00:00','2025-10-06 08:40:00','SCHEDULED',5,5,0),
                                                                                                       (5,5,'2025-10-06 18:00:00','2025-10-06 18:50:00','SCHEDULED',1,1,0);

-- TRIP_ASSIGNMENTS (history/audit)
INSERT INTO trip_assignments (id,trip_id,bus_id,driver_id,assigned_by_user_id,assigned_at) VALUES
                                                                                               (1,1,1,1,2,'2025-10-02 10:00:00'),
                                                                                               (2,2,2,2,2,'2025-10-02 10:05:00'),
                                                                                               (3,3,4,3,2,'2025-10-02 10:10:00'),
                                                                                               (4,4,5,5,2,'2025-10-02 10:15:00'),
                                                                                               (5,5,1,1,2,'2025-10-02 10:20:00');

-- PASSENGERS
INSERT INTO passengers (id,full_name,email,phone) VALUES
                                                      (1,'Chathudu Sahassara','chathudu@mail.test','0711111111'),
                                                      (2,'Iresha Fernando','iresha@mail.test','0711111112'),
                                                      (3,'Kasun Perera','kasun@mail.test','0711111113'),
                                                      (4,'Nadeesha Silva',NULL,'0711111114'),
                                                      (5,'Tharindu Jay','tharindu@mail.test','0711111115');

-- BOOKINGS
INSERT INTO bookings (id,trip_id,passenger_id,booking_time,status,seats_count,total_amount) VALUES
                                                                                                (1,1,1,'2025-10-03 09:00:00','CONFIRMED',2,600.00),
                                                                                                (2,1,2,'2025-10-03 10:00:00','CONFIRMED',1,300.00),
                                                                                                (3,3,3,'2025-10-03 11:00:00','CONFIRMED',1,350.00),
                                                                                                (4,4,4,'2025-10-03 12:00:00','CONFIRMED',3,900.00),
                                                                                                (5,2,5,'2025-10-03 13:00:00','CONFIRMED',1,300.00);

-- BOOKING_SEATS (unique per trip_id+seat_number)
INSERT INTO booking_seats (id,booking_id,trip_id,seat_number) VALUES
                                                                  (1,1,1,5),(2,1,1,6),
                                                                  (3,2,1,10),
                                                                  (4,3,3,12),
                                                                  (5,4,4,1),(6,4,4,2),(7,4,4,3),
                                                                  (8,5,2,7);

-- PAYMENTS
INSERT INTO payments (id,booking_id,amount,method,status,paid_at,txn_ref) VALUES
                                                                              (1,1,600.00,'CARD','PAID','2025-10-03 09:10:00','TXN-10001'),
                                                                              (2,2,300.00,'CARD','PAID','2025-10-03 10:10:00','TXN-10002'),
                                                                              (3,3,350.00,'CASH','PAID','2025-10-03 11:10:00','CASH-10003'),
                                                                              (4,4,900.00,'CARD','PAID','2025-10-03 12:10:00','TXN-10004'),
                                                                              (5,5,300.00,'CARD','PAID','2025-10-03 13:10:00','TXN-10005');

-- TICKETS
INSERT INTO tickets (id,booking_id,ticket_code,issued_at,status) VALUES
                                                                     (1,1,'TKT-ABCD-0001','2025-10-03 09:11:00','ISSUED'),
                                                                     (2,2,'TKT-ABCD-0002','2025-10-03 10:11:00','ISSUED'),
                                                                     (3,3,'TKT-ABCD-0003','2025-10-03 11:11:00','ISSUED'),
                                                                     (4,4,'TKT-ABCD-0004','2025-10-03 12:11:00','ISSUED'),
                                                                     (5,5,'TKT-ABCD-0005','2025-10-03 13:11:00','ISSUED');

-- REFUNDS (include one of each state)
INSERT INTO refunds (id,booking_id,payment_id,amount,reason,status,processed_at) VALUES
                                                                                     (1,2,2,300.00,'Passenger request','PAID','2025-10-04 15:00:00'),
                                                                                     (2,3,3,50.00,'Partial goodwill','APPROVED',NULL),
                                                                                     (3,1,1,0.00,'No show (no refund)','DECLINED','2025-10-04 16:00:00'),
                                                                                     (4,4,4,100.00,'Schedule change','REQUESTED',NULL),
                                                                                     (5,5,5,300.00,'Trip cancelled','APPROVED',NULL);

-- TICKET_VERIFICATIONS
INSERT INTO ticket_verifications (id,ticket_id,verified_by_user_id,verified_at,location) VALUES
                                                                                             (1,1,5,'2025-10-06 06:45:00','Malabe Town'),
                                                                                             (2,2,5,'2025-10-06 06:50:00','Malabe Town'),
                                                                                             (3,3,5,'2025-10-06 07:20:00','Kaduwela'),
                                                                                             (4,4,5,'2025-10-06 07:55:00','Nugegoda'),
                                                                                             (5,5,5,'2025-10-06 09:05:00','Battaramulla');

-- DRIVER_ISSUE_REPORTS
INSERT INTO driver_issue_reports (id,driver_id,trip_id,issue_type,description,reported_at,status) VALUES
                                                                                                      (1,1,1,'MECHANICAL','Low tyre pressure detected','2025-10-06 06:30:00','ACK'),
                                                                                                      (2,2,2,'DELAY','Traffic congestion at Malabe','2025-10-06 08:40:00','OPEN'),
                                                                                                      (3,3,3,'INCIDENT','Minor altercation at stop','2025-10-06 07:45:00','RESOLVED'),
                                                                                                      (4,5,4,'DELAY','Heavy rain slowing traffic','2025-10-06 07:55:00','OPEN'),
                                                                                                      (5,1,5,'MECHANICAL','Strange engine noise','2025-10-06 17:40:00','OPEN');

-- NOTIFICATIONS
INSERT INTO notifications (id,type,to_contact,subject,body,related_booking_id,sent_at,status) VALUES
                                                                                                  (1,'EMAIL','chathudu@mail.test','Booking Confirmed','Your booking #1 confirmed',1,'2025-10-03 09:12:00','SENT'),
                                                                                                  (2,'SMS','0711111112','Booking Confirmed','Your booking #2 confirmed',2,'2025-10-03 10:12:00','SENT'),
                                                                                                  (3,'EMAIL','kasun@mail.test','Booking Confirmed','Your booking #3 confirmed',3,'2025-10-03 11:12:00','SENT'),
                                                                                                  (4,'EMAIL','support@ops.test','Issue Reported','Driver issue #2 requires attention',NULL,'2025-10-06 08:45:00','SENT'),
                                                                                                  (5,'EMAIL','iresha@mail.test','Refund Processed','Refund for booking #2 paid',2,'2025-10-04 15:05:00','SENT');

SET FOREIGN_KEY_CHECKS = 1;
