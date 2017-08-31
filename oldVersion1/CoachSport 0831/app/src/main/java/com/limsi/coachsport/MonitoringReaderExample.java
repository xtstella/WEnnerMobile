package com.limsi.coachsport;

import com.garmin.fit.*;
import com.garmin.fit.csv.MesgCSVWriter;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Scanner;
import java.io.File;
import java.util.List;

public class MonitoringReaderExample implements MonitoringMesgListener  {
    private MesgCSVWriter mesgWriter;
    private ItemArrayAdapter itemArrayAdapter;
    public String outputFile;

    public void Process(String inputFile, int interval, boolean dailyTotals)
    {
        Decode decode;
        MonitoringReader monitoringReader;
        MesgBroadcaster mesgBroadcaster;
        FileInputStream inputStream;

        try {
            inputStream = new FileInputStream(inputFile);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error opening file " + inputFile);
        }

        decode = new Decode();
        mesgBroadcaster = new MesgBroadcaster(decode);
        monitoringReader = new MonitoringReader(interval);
        if (dailyTotals)
            monitoringReader.outputDailyTotals();
        mesgBroadcaster.addListener((MonitoringInfoMesgListener) monitoringReader);
        mesgBroadcaster.addListener((MonitoringMesgListener) monitoringReader);
        mesgBroadcaster.addListener((DeviceSettingsMesgListener) monitoringReader);
        monitoringReader.addListener(this);

        System.out.println( "Decoded FIT file " + inputFile);

        outputFile = inputFile;

        outputFile += ".csv";

        try {

            mesgWriter = new MesgCSVWriter(outputFile);
            mesgWriter.showInvalidsAsEmptyCells();

            try {
                mesgBroadcaster.run(inputStream); // Run decoder.
                System.out.println( "[haha] Write CSV file " + outputFile + "."  + "  dailyTotals :" + dailyTotals);
            } catch (FitRuntimeException a) {
                // If a file with 0 data size in it's header  has been encountered,
                // attempt to keep processing the file
                if (decode.getInvalidFileDataSize()) {
                    decode.nextFile();
                    mesgBroadcaster.run(inputStream);
                }
            }
            monitoringReader.broadcast(); // End of file so flush pending data.
            mesgWriter.close();
        } catch (FitRuntimeException e) {
            System.err.print("Exception decoding file: ");
            System.err.println(e.getMessage());
        }

    }

    public void onMesg(MonitoringMesg mesg) {
        mesgWriter.onMesg(mesg);
    }
}
