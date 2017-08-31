package com.limsi.coachsport;

import android.content.Intent;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garmin.fit.Decode;
import com.garmin.fit.DeveloperField;
import com.garmin.fit.DeveloperFieldDescription;
import com.garmin.fit.DeveloperFieldDescriptionListener;
import com.garmin.fit.DeviceInfoMesg;
import com.garmin.fit.DeviceInfoMesgListener;
import com.garmin.fit.Factory;
import com.garmin.fit.Field;
import com.garmin.fit.FieldBase;
import com.garmin.fit.FileIdMesg;
import com.garmin.fit.FileIdMesgListener;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.Gender;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgBroadcaster;
import com.garmin.fit.MesgDefinitionListener;
import com.garmin.fit.MesgListener;
import com.garmin.fit.MonitoringMesg;
import com.garmin.fit.MonitoringMesgListener;
import com.garmin.fit.MonitoringReader;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.RecordMesgListener;
import com.garmin.fit.UserProfileMesg;
import com.garmin.fit.UserProfileMesgListener;
import com.garmin.fit.test.Tests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HeartRate extends AppCompatActivity {

    public List list;
    TextView date1;
    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;
    File file;
    private String in = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/20170818.fit";
    private String out = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);

        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.listView);
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_history_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        String fileName = "DCIM/20170818.fit-f-i2.csv";
        String path = Environment.getExternalStorageDirectory()+"/"+fileName;
        file = new File(path);
/*
//        if ( file == null ) {
            MonitoringReaderExample example = new MonitoringReaderExample();
            int interval = 2;
            example.Process(in, interval, false);

            if (interval == MonitoringReader.DAILY_INTERVAL)
                example.Process(in, interval, true);


            String csvfileName = example.outputFile;
            System.out.println( "[haha] open CSV file " + csvfileName );
            file = new File(csvfileName);
  //      }
*/
        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CSVFile csvFile = new CSVFile(fileStream);
        List hrList = csvFile.read();
        ArrayAdapter resultAdapter;
        for(Object hrData : hrList ) {
            itemArrayAdapter.fileName = "heartrate";
            itemArrayAdapter.add((String[]) hrData);
        }

 /*
       try {
            if (!Decode.checkIntegrity((InputStream) new FileInputStream(in)))
                throw new RuntimeException("FIT file integrity failure.");
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }


        Tests tests = new Tests();
        System.out.println("Running FIT verification tests...");
        if (tests.run(in))
            System.out.println("Passed FIT verification.");
        else
            System.out.println("Failed FIT verification.");


        try {
            Decode decode = new Decode();
            MesgBroadcaster broadcaster = new MesgBroadcaster(decode);
            MesgToTrainingDataAdapter mesgToData = new MesgToTrainingDataAdapter();
            RecordCSVWriter recordWriter = new RecordCSVWriter(out + "_records.csv");

            decode.addListener((MesgDefinitionListener) mesgToData);
            decode.addListener((MesgListener) mesgToData);
            broadcaster.addListener((RecordMesgListener)recordWriter);
            //broadcaster.addListener((MesgWithEventListener)recordWriter);

            broadcaster.run((InputStream) new FileInputStream(in));


            recordWriter.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
*/

    }

  /*
        Decode decode = new Decode();
        //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
        //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)
        MesgBroadcaster mesgBroadcaster = new MesgBroadcaster( decode );
        Listener listener = new Listener();
        FileInputStream in;

        try {
            in = new FileInputStream( path );
        } catch ( java.io.IOException e ) {
            throw new RuntimeException( "Error opening file " + path + " [1]" );
        }

        try {
            if ( !decode.checkFileIntegrity( (InputStream) in ) ) {
                throw new RuntimeException( "FIT file integrity failed." );
            }
        } catch ( RuntimeException e ) {
            System.err.print( "Exception Checking File Integrity: " );
            System.err.println( e.getMessage() );
            System.err.println( "Trying to continue..." );
        } finally {
            try {
                in.close();
            } catch ( java.io.IOException e ) {
                throw new RuntimeException( e );
            }
        }


        try {
            in = new FileInputStream( path );
        } catch ( java.io.IOException e ) {
            throw new RuntimeException( "Error opening file " + path + " [2]" );
        }

        //mesgBroadcaster.addListener( (FileIdMesgListener) listener );
        mesgBroadcaster.addListener( (UserProfileMesgListener) listener );
        mesgBroadcaster.addListener( (DeviceInfoMesgListener) listener );
        mesgBroadcaster.addListener( (MonitoringMesgListener) listener );
        mesgBroadcaster.addListener( (RecordMesgListener) listener );

        decode.addListener( (DeveloperFieldDescriptionListener) listener );

        try {
            decode.read( in, mesgBroadcaster, mesgBroadcaster );
        } catch ( FitRuntimeException e ) {
            // If a file with 0 data size in it's header  has been encountered,
            // attempt to keep processing the file
            if ( decode.getInvalidFileDataSize() ) {
                decode.nextFile();
                decode.read( in, mesgBroadcaster, mesgBroadcaster );
            } else {
                System.err.print( "Exception decoding file: " );
                System.err.println( e.getMessage() );

                try {
                    in.close();
                } catch ( java.io.IOException f ) {
                    throw new RuntimeException( f );
                }

                return;
            }
        }

        try {
            in.close();
        } catch ( java.io.IOException e ) {
            throw new RuntimeException( e );
        }

        System.out.println( "Decoded FIT file " + path + "." );

        for(Object scoreData : list ) {
          itemArrayAdapter.add((String[]) scoreData);
        }


    }
*/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home = new Intent(HeartRate.this, MainActivity.class);
                    startActivity(home);
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_history:
                    Intent questionaryActivity = new Intent(HeartRate.this, QuestionaryActivity.class);
                    startActivity(questionaryActivity);
                    break;
            }
            return false;
        }

    };


    private static class Listener implements UserProfileMesgListener, DeviceInfoMesgListener,
                    MonitoringMesgListener, RecordMesgListener, DeveloperFieldDescriptionListener {

        public List list;

        @Override
        public void onMesg( UserProfileMesg mesg ) {
            System.out.println( "User profile:" );

            if ( ( mesg.getFriendlyName() != null ) ) {
                System.out.print( "   Friendly Name: " );
                System.out.println( mesg.getFriendlyName() );
            }

            if ( mesg.getGender() != null ) {
                if ( mesg.getGender() == Gender.MALE ) {
                    System.out.println( "   Gender: Male" );
                } else if ( mesg.getGender() == Gender.FEMALE ) {
                    System.out.println( "   Gender: Female" );
                }
            }

            if ( mesg.getAge() != null ) {
                System.out.print( "   Age [years]: " );
                System.out.println( mesg.getAge() );
            }

            if ( mesg.getWeight() != null ) {
                System.out.print( "   Weight [kg]: " );
                System.out.println( mesg.getWeight() );
            }
        }

        @Override
        public void onMesg( DeviceInfoMesg mesg ) {
            System.out.println( "Device info:" );

            if ( mesg.getTimestamp() != null ) {
                System.out.print( "   Timestamp: " );
                System.out.println( mesg.getTimestamp() );
            }

        }

        @Override
        public void onMesg( MonitoringMesg mesg ) {
            System.out.println( "Monitoring:" );

            if ( mesg.getTimestamp() != null ) {
                System.out.print( "   Timestamp: " );
                System.out.println( mesg.getTimestamp() );

            }

            if ( mesg.getActivityType() != null ) {
                System.out.print( "   Activity Type: " );
                System.out.println( mesg.getActivityType() );
            }

            // Depending on the ActivityType, there may be Steps, Strokes, or Cycles present in the file
            if ( mesg.getSteps() != null ) {
                System.out.print( "   Steps: " );
                System.out.println( mesg.getSteps() );
            } else if ( mesg.getStrokes() != null ) {
                System.out.print( "   Strokes: " );
                System.out.println( mesg.getStrokes() );
            } else if ( mesg.getCycles() != null ) {
                System.out.print( "   Cycles: " );
                System.out.println( mesg.getCycles() );
            }

            printDeveloperData( mesg );
        }

        @Override
        public void onMesg( RecordMesg mesg ) {
            System.out.println( "Record:" );

            printValues( mesg, RecordMesg.HeartRateFieldNum );
            printValues( mesg, RecordMesg.CadenceFieldNum );
            printValues( mesg, RecordMesg.DistanceFieldNum );
            printValues( mesg, RecordMesg.SpeedFieldNum );

            printDeveloperData( mesg );
        }

        private void printDeveloperData( Mesg mesg ) {
            for ( DeveloperField field : mesg.getDeveloperFields() ) {
                if ( field.getNumValues() < 1 ) {
                    continue;
                }

                if ( field.isDefined() ) {
                    System.out.print( "   " + field.getName() );

                    if ( field.getUnits() != null ) {
                        System.out.print( " [" + field.getUnits() + "]" );
                    }

                    System.out.print( ": " );
                } else {
                    System.out.print( "   Undefined Field: " );
                }

                System.out.print( field.getValue( 0 ) );
                for ( int i = 1; i < field.getNumValues(); i++ ) {
                    System.out.print( "," + field.getValue( i ) );
                }

                System.out.println();
            }
        }

        @Override
        public void onDescription( DeveloperFieldDescription desc ) {
            System.out.println( "New Developer Field Description" );
            System.out.println( "   App Id: " + desc.getApplicationId() );
            System.out.println( "   App Version: " + desc.getApplicationVersion() );
            System.out.println( "   Field Num: " + desc.getFieldDefinitionNumber() );
        }

        private void printValues( Mesg mesg, int fieldNum ) {
            Iterable<FieldBase> fields = mesg.getOverrideField( (short) fieldNum );
            Field profileField = Factory.createField( mesg.getNum(), fieldNum );
            boolean namePrinted = false;

            if ( profileField == null ) {
                return;
            }

            for ( FieldBase field : fields ) {
                if ( !namePrinted ) {
                    System.out.println( "   " + profileField.getName() + ":" );
                    namePrinted = true;
                }

                if ( field instanceof Field ) {
                    System.out.println( "      native: " + field.getValue() );
                } else {
                    System.out.println( "      override: " + field.getValue() );
                }
            }
        }
    }


}