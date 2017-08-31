////////////////////////////////////////////////////////////////////////////////
// The following FIT Protocol software provided may be used with FIT protocol
// devices only and remains the copyrighted property of Dynastream Innovations Inc.
// The software is being provided on an "as-is" basis and as an accommodation,
// and therefore all warranties, representations, or guarantees of any kind
// (whether express, implied or statutory) including, without limitation,
// warranties of merchantability, non-infringement, or fitness for a particular
// purpose, are specifically disclaimed.
//
// Copyright 2017 Dynastream Innovations Inc.
////////////////////////////////////////////////////////////////////////////////
// ****WARNING****  This file is auto-generated!  Do NOT edit this file.
// Profile Version = 20.38Release
// Tag = production/akw/20.38.00-0-geccbce3
////////////////////////////////////////////////////////////////////////////////


package com.garmin.fit;
import java.math.BigInteger;


public class SpeedZoneMesg extends Mesg {

   
   public static final int MessageIndexFieldNum = 254;
   
   public static final int HighValueFieldNum = 0;
   
   public static final int NameFieldNum = 1;
   

   protected static final  Mesg speedZoneMesg;
   static {
      // speed_zone
      speedZoneMesg = new Mesg("speed_zone", MesgNum.SPEED_ZONE);
      speedZoneMesg.addField(new Field("message_index", MessageIndexFieldNum, 132, 1, 0, "", false, Profile.Type.MESSAGE_INDEX));
      
      speedZoneMesg.addField(new Field("high_value", HighValueFieldNum, 132, 1000, 0, "m/s", false, Profile.Type.UINT16));
      
      speedZoneMesg.addField(new Field("name", NameFieldNum, 7, 1, 0, "", false, Profile.Type.STRING));
      
   }

   public SpeedZoneMesg() {
      super(Factory.createMesg(MesgNum.SPEED_ZONE));
   }

   public SpeedZoneMesg(final Mesg mesg) {
      super(mesg);
   }


   /**
    * Get message_index field
    *
    * @return message_index
    */
   public Integer getMessageIndex() {
      return getFieldIntegerValue(254, 0, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

   /**
    * Set message_index field
    *
    * @param messageIndex
    */
   public void setMessageIndex(Integer messageIndex) {
      setFieldValue(254, 0, messageIndex, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

   /**
    * Get high_value field
    * Units: m/s
    *
    * @return high_value
    */
   public Float getHighValue() {
      return getFieldFloatValue(0, 0, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

   /**
    * Set high_value field
    * Units: m/s
    *
    * @param highValue
    */
   public void setHighValue(Float highValue) {
      setFieldValue(0, 0, highValue, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

   /**
    * Get name field
    *
    * @return name
    */
   public String getName() {
      return getFieldStringValue(1, 0, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

   /**
    * Set name field
    *
    * @param name
    */
   public void setName(String name) {
      setFieldValue(1, 0, name, Fit.SUBFIELD_INDEX_MAIN_FIELD);
   }

}
