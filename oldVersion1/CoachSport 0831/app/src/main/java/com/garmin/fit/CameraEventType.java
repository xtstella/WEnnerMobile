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


public enum CameraEventType {
   VIDEO_START((short)0),
   VIDEO_SPLIT((short)1),
   VIDEO_END((short)2),
   PHOTO_TAKEN((short)3),
   VIDEO_SECOND_STREAM_START((short)4),
   VIDEO_SECOND_STREAM_SPLIT((short)5),
   VIDEO_SECOND_STREAM_END((short)6),
   VIDEO_SPLIT_START((short)7),
   VIDEO_SECOND_STREAM_SPLIT_START((short)8),
   VIDEO_PAUSE((short)11),
   VIDEO_SECOND_STREAM_PAUSE((short)12),
   VIDEO_RESUME((short)13),
   VIDEO_SECOND_STREAM_RESUME((short)14),
    INVALID((short)255);

    protected short value;

    private CameraEventType(short value) {
        this.value = value;
    }

   public static CameraEventType getByValue(final Short value) {
      for (final CameraEventType type : CameraEventType.values()) {
         if (value == type.value)
            return type;
      }

      return CameraEventType.INVALID;
   }

    /**
     * Retrieves the String Representation of the Value
     * @return The string representation of the value
     */
   public static String getStringFromValue( CameraEventType value ) {
       return value.name();
   }

   public short getValue() {
      return value;
   }


}
