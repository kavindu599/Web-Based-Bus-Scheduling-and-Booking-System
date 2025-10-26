package com.company.bus_mgmt.repository.schedule;

import com.company.bus_mgmt.domain.schedule.Route;
import com.company.bus_mgmt.domain.schedule.RouteStop;
import com.company.bus_mgmt.domain.schedule.RouteStopId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteStopRepository extends JpaRepository<RouteStop,RouteStopId> {
    List<RouteStop> findByRouteOrderByStopOrderAsc(Route route);
    long countByStop_Id(Long stopId);

    boolean existsByRoute_IdAndStopOrder(Long routeId, Integer stopOrder);

    @Modifying
    @Query("update RouteStop rs set rs.stopOrder = rs.stopOrder + 1 " +
            "where rs.route.id = :routeId and rs.stopOrder >= :fromOrder")
    int shiftDown(@Param("routeId") Long routeId, @Param("fromOrder") int fromOrder);

}
