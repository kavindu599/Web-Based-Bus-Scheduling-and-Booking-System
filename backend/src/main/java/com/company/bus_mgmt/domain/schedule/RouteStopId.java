package com.company.bus_mgmt.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;


@Setter
@Getter
@Embeddable
public class RouteStopId implements Serializable {
    @Column(name = "route_id", nullable = false)
    private Long routeId;

    @Column(name = "stop_id", nullable = false)
    private Long stopId;

    public RouteStopId() {}
    public RouteStopId(Long routeId, Long stopId) {
        this.routeId = routeId;
        this.stopId = stopId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RouteStopId that)) return false;
        return Objects.equals(routeId, that.routeId)
                && Objects.equals(stopId, that.stopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, stopId);
    }
}
