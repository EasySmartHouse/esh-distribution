package net.easysmarthouse.distribution.shared;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class HubEvent implements Serializable {

    @Id
    private Long hubId;
    private HubEventType hubEventType;

    public HubEvent() {
    }

    public HubEvent(Long hubId, HubEventType hubEventType) {
        this.hubId = hubId;
        this.hubEventType = hubEventType;
    }

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public HubEventType getHubEventType() {
        return hubEventType;
    }

    public void setHubEventType(HubEventType hubEventType) {
        this.hubEventType = hubEventType;
    }
}
