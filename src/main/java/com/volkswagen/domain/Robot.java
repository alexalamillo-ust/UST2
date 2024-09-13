package com.volkswagen.domain;

import com.volkswagen.application.exception.ObstacleException;
import com.volkswagen.application.port.in.RobotCommand;

import java.time.LocalDate;
import java.util.Objects;

import static com.volkswagen.domain.RobotOrientation.*;

public class Robot {

    private static Long nextId = 1L;

    private final Long id;

    private final RobotPosition position;

    private final Workplace workplace;

    private Robot(Workplace workplace, RobotPosition position) {

        if (workplace == null) throw new IllegalArgumentException("Workplace cannot be null");
        if (position == null) throw new IllegalArgumentException("Position cannot be null");

        this.workplace = workplace;
        this.position = position;

        if (workplace.isOutside(position)) {
            throw new IllegalArgumentException("Invalid coordinates");
        }

        this.id = nextId++;
    }

    public static Robot from(Workplace workplace, RobotPosition robotPosition) {
        //logica controllar errores ...
        return new Robot(workplace, robotPosition);
    }

    public Robot turnLeft() {

        switch (this.position.orientation()) {
            case EAST -> this.position.orientation(NORTH);
            case WEST -> this.position.orientation(SOUTH);
            case NORTH -> this.position.orientation(WEST);
            case SOUTH -> this.position.orientation(EAST);
        }

        return this;
    }

    public Robot turnRight() {

        switch (this.position.orientation()) {
            case EAST -> this.position.orientation(SOUTH);
            case WEST -> this.position.orientation(NORTH);
            case NORTH -> this.position.orientation(EAST);
            case SOUTH -> this.position.orientation(WEST);
        }

        return  this;
    }

    public Robot moveForward() {


       RobotPosition nextPosition = this.position;
        switch (this.position.orientation()) {
            case EAST ->nextPosition.x(this.position.x()+1);
            case WEST -> nextPosition.x(this.position.x()-1);
            case NORTH -> nextPosition.y(this.position.y()+1);
            case SOUTH -> nextPosition.y(this.position.y()-1);
        }
        if(workplace.isOutside(nextPosition.x(),nextPosition.y())){
            throw new IllegalArgumentException("Robot is outside the workplace");
        }
        if(workplace.hasObstacleIn(nextPosition.x(),nextPosition.y())){
            throw new IllegalArgumentException("Robot has found an obstacle");
        }
        this.position.x(nextPosition.x());
        this.position.y(nextPosition.y());

        return this;
    }

    public Robot executeCommand(RobotCommand command) {
        switch (command) {
            case M -> this.moveForward();
            case L -> this.turnLeft();
            case R -> this.turnRight();
        }
        return this;
    }

    public RobotPosition position() {
        return position;
    }

    public Long id() {
        return id;
    }

    @Override
    public String toString() {
        return this.position.x() + " " + this.position.y() + " " + this.position.orientation().getRawValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return Objects.equals(id, robot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

