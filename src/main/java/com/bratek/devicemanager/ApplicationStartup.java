//package com.bratek.devicemanager;
//
//import com.bratek.devicemanager.SSHConnector.DeviceStatistic;
//import com.bratek.devicemanager.SSHConnector.SSHConnector;
//import com.bratek.devicemanager.domain.Connection;
//import com.bratek.devicemanager.domain.Disc;
//import com.bratek.devicemanager.domain.DiscLog;
//import com.bratek.devicemanager.repository.ConnectionRepository;
//import com.bratek.devicemanager.repository.DiscLogRepository;
//import com.bratek.devicemanager.repository.DiscRepository;
//import com.jcraft.jsch.JSchException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Mateusz on 2017-05-11.
// */
//@Component
//public class ApplicationStartup {
//
//    @Autowired
//    private ConnectionRepository connectionRepository;
//
//    @Autowired
//    private DiscRepository discRepository;
//
//    @Autowired
//    private DiscLogRepository discLogRepository;
//
//    @PostConstruct
//    public void onApplicationEvent() {
//        List<Connection> connections;
//
//
//        connections = connectionRepository.
//            findAll();
//
//
//    }
//}
