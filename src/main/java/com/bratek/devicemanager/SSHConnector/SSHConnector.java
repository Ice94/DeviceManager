package com.bratek.devicemanager.SSHConnector;

import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
//        session.setX11Host(xhost);
//        session.setX11Port(xport);
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


        for (String tmp: names) {
            devices.add(StringUtils.substringBefore(tmp," "));
        }

        devices.forEach(System.out::println);

        channel.disconnect();
        session.disconnect();

        return devices;
    }
}
