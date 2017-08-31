package com.limsi.coachsport;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;

public class HeartRate extends AppCompatActivity {
    public PointF[] pointArray;
    public List pointlist = new ArrayList();
    public CurveChartView chartView;
    private Button buttonCurve ;
    public List list;
    private ListView listView;
    private ItemArrayAdapter itemArrayAdapter;
    File file;
    private String fitpath = Environment.getExternalStorageDirectory().getAbsolutePath()
            +"/DCIM/20170830-2.fit";
    private String csvpath = fitpath + ".csv";

/*  String csvpathName = "DCIM/20170830-2.fit.csv";
    String csvpath = Environment.getExternalStorageDirectory()+"/"+ csvpathName;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        buttonCurve = (Button)findViewById(R.id.buttonCurve) ;
        chartView = (CurveChartView) findViewById(R.id.chart_view);

        //BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        //bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView = (ListView) findViewById(R.id.listView);
        itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_history_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);


        file = new File(csvpath);

        FileInputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
            System.out.println("Successful to open CSV file " + csvpath);
        } catch (Exception e) {
            System.out.println("Can not find file csv: " + csvpath);
            MonitoringReaderExample example = new MonitoringReaderExample();
            int interval = 2;
            example.Process(fitpath, interval, false);

            if (interval == MonitoringReader.DAILY_INTERVAL)
                example.Process(fitpath, interval, true);

            String csvfileName = example.outputFile;
            System.out.println( "Open CSV file " + csvfileName );
            file = new File(csvfileName);
            System.out.println("Successful to open CSV file " + csvpath);
            //throw new RuntimeException(e);
        }

        try {
            fileStream = new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println( "parse csv file: " + file );
        CSVFile csvFile = new CSVFile(fileStream);
        List hrList = csvFile.read();
        ArrayAdapter resultAdapter;
        for(Object hrData : hrList ) {
            itemArrayAdapter.fileName = "heartrate";
            itemArrayAdapter.add((String[]) hrData);
            //System.out.println("hahahaha " + itemArrayAdapter.getItem(1));
        }

        buttonCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //waveLineView = (WaveLineView) findViewById(R.id.chart_view);
                //waveLineView.setVisibility(View.VISIBLE);
                //waveLineView.startAnimation();
                chartView.setVisibility(View.VISIBLE);
/*                chartView.setData(new PointF[]{
                        new PointF(0, 23),
                        new PointF(5, 53),
                        new PointF(10, 33),
                        new PointF(15, 63),
                        new PointF(20, 43),
                        new PointF(25, 83),
                        new PointF(30, 13)
                });*/
                pointlist = itemArrayAdapter.pointlist;
                pointArray = new PointF[pointlist.size()];
                for(int i = 0; i < pointlist.size(); i++) {

                    pointArray[i] = (PointF) pointlist.get(i);
                    System.out.println("hahahaha " + pointArray[i]);
                }
                chartView.setData(pointArray);

            }
        } );

    }
    public CurveChartView getChart() {
        return chartView;
    }

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