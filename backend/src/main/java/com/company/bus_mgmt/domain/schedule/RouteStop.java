package com.company.bus_mgmt.domain.schedule;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(
        name = "route_stops",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_route_stop_order", columnNames = {"route_id", "stop_order"})
        },
        indexes = {
                @Index(name = "ix_route_stop_route", columnList = "route_id"),
                @Index(name = "ix_route_stop_stop", columnList = "stop_id")
        }
)
public class RouteStop {

    // Getters / setters
    @EmbeddedId
    private RouteStopId id;

    /** Tie the FK to the composite PK component route_id. */
    @MapsId("routeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    /** Tie the FK to the composite PK component stop_id. */
    @MapsId("stopId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stop_id", nullable = false)
    private Stop stop;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(name = "arrival_offset_min", nullable = false)
    private Integer arrivalOffsetMin;

}
