//package com.example.Stars.write_model;
//
//import com.example.Stars.apis.api.MarkNotificationsSeenCommand;
//import com.example.Stars.apis.api.MessageCommand;
//import com.example.Stars.apis.api.MessageStatusChangeCommand;
//import com.example.Stars.apis.api.NotificationsSeenEvent;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.axonframework.config.ProcessingGroup;
//import org.axonframework.modelling.saga.SagaEventHandler;
//import org.axonframework.modelling.saga.SagaLifecycle;
//import org.axonframework.modelling.saga.StartSaga;
//import org.axonframework.spring.stereotype.Saga;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Profile;
//
//import java.util.UUID;
//
//@Saga
//@Profile("write_notification_saga")
//@ProcessingGroup("notificationSagaProcessor")
//public class NotificationSaga {
//
//    @Autowired
//    private transient CommandGateway commandGateway;
//
//    @StartSaga
//    @SagaEventHandler(associationProperty = "userId")
//    public void on(NotificationsSeenEvent event) {
//        for (UUID notificationId : event.getNotificationIds()) {
//            //commandGateway.send(new MessageStatusChangeCommand(notificationId));
//        }
//        SagaLifecycle.end();
//    }
//
//}
