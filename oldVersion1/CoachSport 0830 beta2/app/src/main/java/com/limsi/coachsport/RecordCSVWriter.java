package com.limsi.coachsport;

import com.garmin.fit.*;
import com.garmin.fit.csv.CSVWriter;

public class RecordCSVWriter implements MesgWithEventListener, RecordMesgListener {
    private CSVWriter csv;

    public RecordCSVWriter(String fileName) {
        csv = new CSVWriter(fileName);
    }

    public void close() {
        csv.close();
    }

    public void onMesg(MesgWithEvent mesg) {
        if (mesg.getTimestamp() != null)
            csv.set("Timestamp", mesg.getTimestamp().toString());

        if (mesg.getEvent() != null) {
            csv.set("Event", mesg.getEvent().toString());
        } else {
            csv.set("Event", "");
        }

        if (mesg.getEventType() != null) {
            csv.set("Event Type", mesg.getEventType().toString());
        } else {
            csv.set("Event Type", "");
        }

        if (mesg.getEventGroup() != null) {
            csv.set("Event Group", mesg.getEventGroup().toString());
        } else {
            csv.set("Event Group", "");
        }

        if (mesg instanceof EventMesg) {
            EventMesg eventMesg = (EventMesg)mesg;

            if (eventMesg.getData() != null) {
                switch (eventMesg.getEvent()) {
                    case TIMER:
                        csv.set("Event Data", "Trigger: " + eventMesg.getTimerTrigger().toString());
                        break;

                    default:
                        csv.set("Event Data", "");
                        break;
                }
            } else {
                csv.set("Event Data", "");
            }
        }

        csv.writeln();
    }

    public void onMesg(RecordMesg mesg) {
        // Clear event.
        csv.set("Event", "");
        csv.set("Event Type", "");
        csv.set("Event Data", "");
        csv.set("Event Group", "");

        for (int i=0; i<mesg.getNumSpeed1s(); i++) {
            csv.set("Timestamp", (new DateTime(mesg.getTimestamp().getTimestamp() - mesg.getNumSpeed1s() + i + 1)).toString());
            csv.set("1s Speed [m/s]", mesg.getSpeed1s(i));

            if (i < (mesg.getNumSpeed1s() - 1))
                csv.writeln();
        }

        if (mesg.getTimestamp() != null)
            csv.set("Timestamp", mesg.getTimestamp().toString());

        if (mesg.getHeartRate() != null)
            csv.set("Heart Rate [bpm]", mesg.getHeartRate().toString());

        if (mesg.getCadence() != null)
            csv.set("Cadence [rpm]", mesg.getCadence().toString());

        if (mesg.getSpeed() != null)
            csv.set("Speed [m/s]", mesg.getSpeed().toString());

        if (mesg.getDistance() != null)
            csv.set("Distance [m]", mesg.getDistance().toString());

        if (mesg.getPower() != null)
            csv.set("Power [watts]", mesg.getPower().toString());

        if (mesg.getAccumulatedPower() != null)
            csv.set("Accumulated Power [watts]", mesg.getAccumulatedPower().toString());

        if (mesg.getLeftRightBalance() != null)
        {
            csv.set("Left Right Balance [%]", new Integer(mesg.getLeftRightBalance() & LeftRightBalance.MASK).toString());
            csv.set("Is Right Balance", new Boolean((mesg.getLeftRightBalance() & LeftRightBalance.RIGHT) == LeftRightBalance.RIGHT).toString());
        }

        if (mesg.getPositionLat() != null)
            csv.set("Latitude [semicircles]", mesg.getPositionLat().toString());

        if (mesg.getPositionLong() != null)
            csv.set("Longitude [semicircles]", mesg.getPositionLong().toString());

        if (mesg.getAltitude() != null)
            csv.set("Altitude [m]", mesg.getAltitude().toString());

        if (mesg.getGrade() != null)
            csv.set("Grade [%]", mesg.getGrade().toString());

        if (mesg.getResistance() != null)
            csv.set("Resistance", mesg.getResistance().toString());

        if (mesg.getCycleLength() != null)
            csv.set("Cycle Length [m]", mesg.getCycleLength().toString());

        if (mesg.getTemperature() != null)
            csv.set("Temperature [C]", mesg.getTemperature().toString());

        csv.writeln();
    }
}