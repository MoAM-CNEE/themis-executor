package org.k8loud.executor.moam.statemanager.response;

import lombok.Getter;
import lombok.Setter;
import org.k8loud.executor.moam.statemanager.dto.EntityDTO;

import java.util.List;

@Setter
@Getter
public class ReadEntityActionRS {
    public List<EntityDTO> entities;
}
