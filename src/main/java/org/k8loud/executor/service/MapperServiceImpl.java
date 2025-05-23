package org.k8loud.executor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.k8loud.executor.actions.Action;
import org.k8loud.executor.moam.statemanager.StateManagerService;
import org.k8loud.executor.util.ActionHelper;
import org.k8loud.executor.cnapp.db.DBService;
import org.k8loud.executor.cnapp.db.MongoService;
import org.k8loud.executor.cnapp.db.MySQLService;
import org.k8loud.executor.cnapp.http.HTTPService;
import org.k8loud.executor.cnapp.mail.MailService;
import org.k8loud.executor.cnapp.sockshop.SockShopService;
import org.k8loud.executor.command.CommandExecutionService;
import org.k8loud.executor.datastorage.DataStorageService;
import org.k8loud.executor.exception.ActionException;
import org.k8loud.executor.exception.MapperException;
import org.k8loud.executor.kubernetes.KubernetesService;
import org.k8loud.executor.model.ExecutionRQ;
import org.k8loud.executor.model.Params;
import org.k8loud.executor.openstack.OpenstackService;
import org.k8loud.executor.util.ClassHelper;
import org.k8loud.executor.util.ClassParameter;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.k8loud.executor.exception.code.MapperExceptionCode.INVALID_CONSTRUCTOR;
import static org.k8loud.executor.exception.code.MapperExceptionCode.NEW_INSTANCE_FAILURE;

@RequiredArgsConstructor
@Slf4j
@Service
public class MapperServiceImpl implements MapperService {
    private final ActionHelper actionHelper = new ActionHelper();
    private final OpenstackService openstackService;
    private final CommandExecutionService commandExecutionService;
    private final KubernetesService kubernetesService;
    private final SockShopService sockShopService;
    private final MailService mailService;
    private final DataStorageService dataStorageService;
    private final HTTPService httpService;
    private final StateManagerService stateManagerService;
    private final DBService mySQLService = new MySQLService();
    private final DBService mongoService = new MongoService();

    @NotNull
    @Override
    public Action map(@NotNull ExecutionRQ executionRQ) throws MapperException, ActionException {
        String collectionName = executionRQ.getCollectionName();
        String actionName = executionRQ.getActionName();
        Params params = executionRQ.getParams();

        Class<?> actionClass = actionHelper.getActionClass(collectionName, actionName);
        List<ClassParameter> classParameters = new ArrayList<>(List.of(new ClassParameter(Params.class, params)));
        try {
            switch (collectionName) {
                case "openstack" -> classParameters.add(new ClassParameter(OpenstackService.class, openstackService));
                case "command" ->
                        classParameters.add(new ClassParameter(CommandExecutionService.class, commandExecutionService));
                case "kubernetes" ->
                        classParameters.add(new ClassParameter(KubernetesService.class, kubernetesService));
                case "cnapp.sockshop" ->
                        classParameters.add(new ClassParameter(SockShopService.class, sockShopService));
                case "cnapp.db.mysql" -> classParameters.add(new ClassParameter(DBService.class, mySQLService));
                case "cnapp.db.mongo" -> classParameters.add(new ClassParameter(DBService.class, mongoService));
                case "cnapp.mail" -> classParameters.add(new ClassParameter(MailService.class, mailService));
                case "cnapp.themis.datastorage" ->
                        classParameters.add(new ClassParameter(DataStorageService.class, dataStorageService));
                case "cnapp.http" -> classParameters.add(new ClassParameter(HTTPService.class, httpService));
                case "moam.statemanager" ->
                        classParameters.add(new ClassParameter(StateManagerService.class, stateManagerService));
            }
            return (Action) ClassHelper.getInstance(actionClass, classParameters.toArray(ClassParameter[]::new));
        } catch (NoSuchMethodException e) {
            throw new MapperException(e, INVALID_CONSTRUCTOR);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new MapperException(e, NEW_INSTANCE_FAILURE);
        }
    }
}
