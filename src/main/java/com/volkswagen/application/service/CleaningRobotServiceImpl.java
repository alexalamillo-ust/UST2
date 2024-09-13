package com.volkswagen.application.service;

import com.volkswagen.application.port.in.CleaningRobotService;
import com.volkswagen.application.port.in.RobotsDataCommand;
import com.volkswagen.application.port.out.RobotsResultPort;
import com.volkswagen.domain.Robot;
import com.volkswagen.domain.Workplace;

import java.util.ArrayList;
import java.util.List;

import static com.volkswagen.domain.Robot.from;

public class CleaningRobotServiceImpl implements CleaningRobotService {

    private final RobotsResultPort robotsResultPort;

    public CleaningRobotServiceImpl(RobotsResultPort robotsResultPort) {
        this.robotsResultPort = robotsResultPort;
    }

    @Override
    public List<Robot> controlRobots(RobotsDataCommand robotsDataCommand) {

        Workplace workplace = robotsDataCommand.workplace();
        List<Robot> listaRobots = new ArrayList<>();
        for (int i = 0; i < robotsDataCommand.configurations().size(); i++) {

           var configuration = robotsDataCommand.configurations().get(i);
           var initialPosition = configuration.robotPosition();
            var currentRobot =Robot.from(workplace,initialPosition);


                for (int j = 0; j < configuration.commands().size(); j++) {
                    var command = configuration.commands().get(j);
                        currentRobot.executeCommand(command);
                }

            listaRobots.add(currentRobot);
            workplace.addFinishedRobot(currentRobot);
        }

        robotsResultPort.processRobotsResult(listaRobots);
        return listaRobots;
    }
}
