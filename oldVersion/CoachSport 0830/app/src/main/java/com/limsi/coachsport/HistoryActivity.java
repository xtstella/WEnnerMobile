package com.limsi.coachsport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Environment;

import com.garmin.fit.*;

import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

import com.garmin.fit.Decode;
import com.garmin.fit.FitRuntimeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private String inpath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/activities.csv";
    private String outpath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/new";

    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;

    private String filePath = "Activity.fit";
    private File fitFile;
    private FileInputStream fitFileInputStream;
    private Decode fitFileDecode;

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


        String fileName = "DCIM/activities.csv";
        String path = Environment.getExternalStorageDirectory()+"/"+fileName;
        File file = new File(path);

        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        CSVFile csvFile = new CSVFile(fileStream);
        List scoreList = csvFile.read();

        System.out.println(scoreList);

        for(Object scoreData : scoreList ) {
            itemArrayAdapter.fileName = "activities";
           itemArrayAdapter.add((String[]) scoreData);
        }
/*
        Intent fileExploreIntent = new Intent(
                com.limsi.coachsport.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                null,
                activityForButton,
                com.limsi.coachsport.FileBrowserActivity.class
        );
//  fileExploreIntent.putExtra(
//  	ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter,
//      "/sdcard"
//  );//Here you can add optional start directory parameter, and file browser will start from that directory.
        startActivityForResult(
                fileExploreIntent,
                REQUEST_CODE_PICK_FILE_TO_SAVE_INTERNAL
        );
*/
 /*

        Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/activities.csv";
        System.out.println(path);
        File fitFile = new File(path, filePath);
        try {
            System.out.println("haha");
            fitFileInputStream = new FileInputStream(fitFile);
            System.out.println("hahaha");
*/
//            if (!fitFile.canRead()) {
//                throw new Exception("Unable to read the input file: " + filePath);
//            }
            //if (!Decode.isFit(fitFileInputStream))
            //  throw new Exception("The following file is not a FIT file: " + filePath);
/*if (!Decode.checkIntegrity(fitFileInputStream)) throw new Exception("There are integrity issues with the following FIT file: " + filePath);*/
/*            System.out.println("hahahahahahaha" );
            fitFileDecode = new Decode();
            System.out.println("hahahahahahaha" + fitFileDecode.read(fitFileInputStream));
        } catch (FileNotFoundException fnfe) {
            System.out.println("FileNotFoundException: " + fnfe.getMessage());
        } catch (FitRuntimeException fre) {
            System.out.println("FitRuntimeException: " + fre.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }*/

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent home = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(home);
                    break;
                case R.id.navigation_profile:
                    Intent profile = new Intent(HistoryActivity.this, HeartRate.class);
                    startActivity(profile);
                    break;
                case R.id.navigation_history:
                    return true;
            }
            return false;
        }

    };

    public void Decode( ) {
        String [] args = new String[2];
        args[0] = "Activity.fit";

        Decode decode = new Decode();
        //decode.skipHeader();        // Use on streams with no header and footer (stream contains FIT defn and data messages only)
        //decode.incompleteStream();  // This suppresses exceptions with unexpected eof (also incorrect crc)
        MesgBroadcaster mesgBroadcaster = new MesgBroadcaster( decode );
        Listener listener = new Listener();
        FileInputStream in;

        System.out.printf( "FIT Decode Example Application - Protocol %d.%d Profile %.2f %s\n", Fit.PROTOCOL_VERSION_MAJOR, Fit.PROTOCOL_VERSION_MINOR, Fit.PROFILE_VERSION / 100.0, Fit.PROFILE_TYPE );

        try {
            in = new FileInputStream( args[0] );
        } catch ( java.io.IOException e ) {
            throw new RuntimeException( "Error opening file " + args[0] + " [1]" );
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
            in = new FileInputStream( args[0] );
        } catch ( java.io.IOException e ) {
            throw new RuntimeException( "Error opening file " + args[0] + " [2]" );
        }

        mesgBroadcaster.addListener( (FileIdMesgListener) listener );
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

        System.out.println( "Decoded FIT file " + args[0] + "." );
    }



    private static class Listener implements FileIdMesgListener, UserProfileMesgListener, DeviceInfoMesgListener, MonitoringMesgListener, RecordMesgListener, DeveloperFieldDescriptionListener {

        @Override
        public void onMesg( FileIdMesg mesg ) {
            System.out.println( "File ID:" );

            if ( mesg.getType() != null ) {
                System.out.print( "   Type: " );
                System.out.println( mesg.getType().getValue() );
            }

            if ( mesg.getManufacturer() != null ) {
                System.out.print( "   Manufacturer: " );
                System.out.println( mesg.getManufacturer() );
            }

            if ( mesg.getProduct() != null ) {
                System.out.print( "   Product: " );
                System.out.println( mesg.getProduct() );
            }

            if ( mesg.getSerialNumber() != null ) {
                System.out.print( "   Serial Number: " );
                System.out.println( mesg.getSerialNumber() );
            }

            if ( mesg.getNumber() != null ) {
                System.out.print( "   Number: " );
                System.out.println( mesg.getNumber() );
            }
        }

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

            if ( mesg.getBatteryStatus() != null ) {
                System.out.print( "   Battery status: " );

                switch ( mesg.getBatteryStatus() ) {

                    case BatteryStatus.CRITICAL:
                        System.out.println( "Critical" );
                        break;
                    case BatteryStatus.GOOD:
                        System.out.println( "Good" );
                        break;
                    case BatteryStatus.LOW:
                        System.out.println( "Low" );
                        break;
                    case BatteryStatus.NEW:
                        System.out.println( "New" );
                        break;
                    case BatteryStatus.OK:
                        System.out.println( "OK" );
                        break;
                    default:
                        System.out.println( "Invalid" );
                }
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