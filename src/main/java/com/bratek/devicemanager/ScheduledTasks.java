package com.bratek.devicemanager;

import com.bratek.devicemanager.SSHConnector.DeviceStatistic;
import com.bratek.devicemanager.SSHConnector.SSHConnector;
import com.bratek.devicemanager.domain.Connection;
import com.bratek.devicemanager.domain.Disc;
import com.bratek.devicemanager.domain.DiscLog;
import com.bratek.devicemanager.repository.ConnectionRepository;
import com.bratek.devicemanager.repository.DiscLogRepository;
import com.bratek.devicemanager.repository.DiscRepository;
import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mateusz on 2017-05-16.
 */
@Component
public class ScheduledTasks {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private DiscRepository discRepository;

    @Autowired
    private DiscLogRepository discLogRepository;

    @Scheduled(fixedRate = 60000)
    public void gatherData(){
        List<Connection> connections = connectionRepository.findAll();
        for (Connection connection : connections) {
            List<Disc> discs;
            SSHConnector sshConnector;
            sshConnector = new SSHConnector(connection.getUserHost(), connection.getPassword());
            discs = discRepository.findAllByConnection(connection);
            List<DeviceStatistic> deviceStatistics = null;
            try {
                deviceStatistics = sshConnector.getDeviceStatistisc();
            } catch (JSchException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Disc disc : discs) {
                for (DeviceStatistic deviceStatistic : deviceStatistics) {
                    if (disc.getName().equals(deviceStatistic.getName())) {
                        DiscLog discLog = new DiscLog();
                        discLog.setDisc(disc);
                        discLog.setAvgqusz(deviceStatistic.getAvgrusz());
                        discLog.setAvgrqsz(deviceStatistic.getAvgrqsz());
                        discLog.setAwait(deviceStatistic.getAwait());
                        discLog.setSvctim(deviceStatistic.getSvctm());
                        discLog.setUtil(deviceStatistic.getUtil());
                        disc.getDiscLogs().add(discLog);
                        discLogRepository.save(discLog);
                    }
                }
            }
        }
    }
}
