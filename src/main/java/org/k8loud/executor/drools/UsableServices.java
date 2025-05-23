package org.k8loud.executor.drools;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.k8loud.executor.cnapp.db.MongoService;
import org.k8loud.executor.cnapp.db.MySQLService;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.cnapp.mail.MailService;
import org.k8loud.executor.cnapp.sockshop.SockShopService;
import org.k8loud.executor.command.CommandExecutionService;
import org.k8loud.executor.kubernetes.KubernetesService;
import org.k8loud.executor.openstack.OpenstackService;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class UsableServices {
    private final SockShopService sockShopService;
    private final CommandExecutionService commandExecutionService;
    private final KubernetesService kubernetesService;
    private final OpenstackService openstackService;
    private final MySQLService mySQLService;
    private final MongoService mongoService;
    private final HTTPService httpService;
    private final MailService mailService;
    private final StateManagerService stateManagerService;
}
