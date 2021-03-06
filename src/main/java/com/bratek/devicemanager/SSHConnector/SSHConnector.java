package com.bratek.devicemanager.SSHConnector;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mateusz on 2017-05-09.
 */
public class SSHConnector {

    private String user;
    private String host;
    private String password;
    private final String COMMAND = "iostat -d -x -t -m";


    public SSHConnector(String usernameHost, String password){
        String[] userHost = usernameHost.split("@");
        this.user = userHost[0];
        this.host = userHost[1];
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getDiscNames() throws JSchException, IOException {
        List<String> devices = new ArrayList<>();

        JSch jSch = new JSch();

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        Session session = jSch.getSession(user,host,22);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(COMMAND);
        channel.setXForwarding(true);
        channel.setInputStream(System.in);

        InputStream inputStream = channel.getInputStream();

        channel.connect();

        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStream, stringWriter);

        String result []= StringUtils.substringAfter(stringWriter.toString(), "%util\n").split("\\r?\\n");

        List<String> names = Arrays.asList(result);

        names.forEach(System.out::println);


        devices.addAll(names.stream().map(tmp -> StringUtils.substringBefore(tmp, " ")).collect(Collectors.toList()));

        devices.forEach(System.out::println);

        channel.disconnect();
        session.disconnect();

        return devices;
    }

    public static List<DeviceStatistic> getDeviceStatistisc () throws JSchException, IOException {
        List<DeviceStatistic> statistics = new ArrayList<>();

        final String host = "taurus.fis.agh.edu.pl";
        final String userName = "3bratek";
        final String password = "lesserseways";
        final String command = "iostat -d -x -t -m";

        JSch jSch = new JSch();

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        Session session = jSch.getSession(userName,host,22);
        session.setPassword(password);
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("exec");
        ((ChannelExec) channel).setCommand(command);
        channel.setXForwarding(true);
        channel.setInputStream(System.in);

        InputStream inputStream = channel.getInputStream();

        channel.connect();

        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStream, stringWriter);

        String result []= StringUtils.substringAfter(stringWriter.toString(), "%util\n").split("\\r?\\n");

        List<String> names = Arrays.asList(result);


        for (String tmp: names) {
            DeviceStatistic deviceStatistic = new DeviceStatistic(tmp);
            statistics.add(deviceStatistic);
        }


        System.out.println(new Date());

        channel.disconnect();
        session.disconnect();

        return statistics;
    }
}
