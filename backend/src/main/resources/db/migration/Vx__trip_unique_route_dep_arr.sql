ALTER TABLE trips
    ADD CONSTRAINT uq_trip_route_dep_arr
        UNIQUE (route_id, departure_time, arrival_time);