#region Copyright
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

#endregion

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.IO;
using System.Linq;

namespace Dynastream.Fit
{
    /// <summary>
    /// Implements the ObdiiData profile message.
    /// </summary>
    public class ObdiiDataMesg : Mesg
    {
        #region Fields
        #endregion

        /// <summary>
        /// Field Numbers for <see cref="ObdiiDataMesg"/>
        /// </summary>
        public sealed class FieldDefNum
        {
            public const byte Timestamp = 253;
            public const byte TimestampMs = 0;
            public const byte TimeOffset = 1;
            public const byte Pid = 2;
            public const byte RawData = 3;
            public const byte PidDataSize = 4;
            public const byte SystemTime = 5;
            public const byte StartTimestamp = 6;
            public const byte StartTimestampMs = 7;
            public const byte Invalid = Fit.FieldNumInvalid;
        }

        #region Constructors
        public ObdiiDataMesg() : base(Profile.GetMesg(MesgNum.ObdiiData))
        {
        }

        public ObdiiDataMesg(Mesg mesg) : base(mesg)
        {
        }
        #endregion // Constructors

        #region Methods
        ///<summary>
        /// Retrieves the Timestamp field
        /// Units: s
        /// Comment: Timestamp message was output</summary>
        /// <returns>Returns DateTime representing the Timestamp field</returns>
        public DateTime GetTimestamp()
        {
            Object val = GetFieldValue(253, 0, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return TimestampToDateTime(Convert.ToUInt32(val));
            
        }

        /// <summary>
        /// Set Timestamp field
        /// Units: s
        /// Comment: Timestamp message was output</summary>
        /// <param name="timestamp_">Nullable field value to be set</param>
        public void SetTimestamp(DateTime timestamp_)
        {
            SetFieldValue(253, 0, timestamp_.GetTimeStamp(), Fit.SubfieldIndexMainField);
        }
        
        ///<summary>
        /// Retrieves the TimestampMs field
        /// Units: ms
        /// Comment: Fractional part of timestamp, added to timestamp</summary>
        /// <returns>Returns nullable ushort representing the TimestampMs field</returns>
        public ushort? GetTimestampMs()
        {
            Object val = GetFieldValue(0, 0, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToUInt16(val));
            
        }

        /// <summary>
        /// Set TimestampMs field
        /// Units: ms
        /// Comment: Fractional part of timestamp, added to timestamp</summary>
        /// <param name="timestampMs_">Nullable field value to be set</param>
        public void SetTimestampMs(ushort? timestampMs_)
        {
            SetFieldValue(0, 0, timestampMs_, Fit.SubfieldIndexMainField);
        }
        
        
        /// <summary>
        ///
        /// </summary>
        /// <returns>returns number of elements in field TimeOffset</returns>
        public int GetNumTimeOffset()
        {
            return GetNumFieldValues(1, Fit.SubfieldIndexMainField);
        }

        ///<summary>
        /// Retrieves the TimeOffset field
        /// Units: ms
        /// Comment: Offset of PID reading [i] from start_timestamp+start_timestamp_ms. Readings may span accross seconds.</summary>
        /// <param name="index">0 based index of TimeOffset element to retrieve</param>
        /// <returns>Returns nullable ushort representing the TimeOffset field</returns>
        public ushort? GetTimeOffset(int index)
        {
            Object val = GetFieldValue(1, index, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToUInt16(val));
            
        }

        /// <summary>
        /// Set TimeOffset field
        /// Units: ms
        /// Comment: Offset of PID reading [i] from start_timestamp+start_timestamp_ms. Readings may span accross seconds.</summary>
        /// <param name="index">0 based index of time_offset</param>
        /// <param name="timeOffset_">Nullable field value to be set</param>
        public void SetTimeOffset(int index, ushort? timeOffset_)
        {
            SetFieldValue(1, index, timeOffset_, Fit.SubfieldIndexMainField);
        }
        
        ///<summary>
        /// Retrieves the Pid field
        /// Comment: Parameter ID</summary>
        /// <returns>Returns nullable byte representing the Pid field</returns>
        public byte? GetPid()
        {
            Object val = GetFieldValue(2, 0, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToByte(val));
            
        }

        /// <summary>
        /// Set Pid field
        /// Comment: Parameter ID</summary>
        /// <param name="pid_">Nullable field value to be set</param>
        public void SetPid(byte? pid_)
        {
            SetFieldValue(2, 0, pid_, Fit.SubfieldIndexMainField);
        }
        
        
        /// <summary>
        ///
        /// </summary>
        /// <returns>returns number of elements in field RawData</returns>
        public int GetNumRawData()
        {
            return GetNumFieldValues(3, Fit.SubfieldIndexMainField);
        }

        ///<summary>
        /// Retrieves the RawData field
        /// Comment: Raw parameter data</summary>
        /// <param name="index">0 based index of RawData element to retrieve</param>
        /// <returns>Returns nullable byte representing the RawData field</returns>
        public byte? GetRawData(int index)
        {
            Object val = GetFieldValue(3, index, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToByte(val));
            
        }

        /// <summary>
        /// Set RawData field
        /// Comment: Raw parameter data</summary>
        /// <param name="index">0 based index of raw_data</param>
        /// <param name="rawData_">Nullable field value to be set</param>
        public void SetRawData(int index, byte? rawData_)
        {
            SetFieldValue(3, index, rawData_, Fit.SubfieldIndexMainField);
        }
        
        
        /// <summary>
        ///
        /// </summary>
        /// <returns>returns number of elements in field PidDataSize</returns>
        public int GetNumPidDataSize()
        {
            return GetNumFieldValues(4, Fit.SubfieldIndexMainField);
        }

        ///<summary>
        /// Retrieves the PidDataSize field
        /// Comment: Optional, data size of PID[i].  If not specified refer to SAE J1979.</summary>
        /// <param name="index">0 based index of PidDataSize element to retrieve</param>
        /// <returns>Returns nullable byte representing the PidDataSize field</returns>
        public byte? GetPidDataSize(int index)
        {
            Object val = GetFieldValue(4, index, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToByte(val));
            
        }

        /// <summary>
        /// Set PidDataSize field
        /// Comment: Optional, data size of PID[i].  If not specified refer to SAE J1979.</summary>
        /// <param name="index">0 based index of pid_data_size</param>
        /// <param name="pidDataSize_">Nullable field value to be set</param>
        public void SetPidDataSize(int index, byte? pidDataSize_)
        {
            SetFieldValue(4, index, pidDataSize_, Fit.SubfieldIndexMainField);
        }
        
        
        /// <summary>
        ///
        /// </summary>
        /// <returns>returns number of elements in field SystemTime</returns>
        public int GetNumSystemTime()
        {
            return GetNumFieldValues(5, Fit.SubfieldIndexMainField);
        }

        ///<summary>
        /// Retrieves the SystemTime field
        /// Comment: System time associated with sample expressed in ms, can be used instead of time_offset.  There will be a system_time value for each raw_data element.  For multibyte pids the system_time is repeated.</summary>
        /// <param name="index">0 based index of SystemTime element to retrieve</param>
        /// <returns>Returns nullable uint representing the SystemTime field</returns>
        public uint? GetSystemTime(int index)
        {
            Object val = GetFieldValue(5, index, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToUInt32(val));
            
        }

        /// <summary>
        /// Set SystemTime field
        /// Comment: System time associated with sample expressed in ms, can be used instead of time_offset.  There will be a system_time value for each raw_data element.  For multibyte pids the system_time is repeated.</summary>
        /// <param name="index">0 based index of system_time</param>
        /// <param name="systemTime_">Nullable field value to be set</param>
        public void SetSystemTime(int index, uint? systemTime_)
        {
            SetFieldValue(5, index, systemTime_, Fit.SubfieldIndexMainField);
        }
        
        ///<summary>
        /// Retrieves the StartTimestamp field
        /// Comment: Timestamp of first sample recorded in the message.  Used with time_offset to generate time of each sample</summary>
        /// <returns>Returns DateTime representing the StartTimestamp field</returns>
        public DateTime GetStartTimestamp()
        {
            Object val = GetFieldValue(6, 0, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return TimestampToDateTime(Convert.ToUInt32(val));
            
        }

        /// <summary>
        /// Set StartTimestamp field
        /// Comment: Timestamp of first sample recorded in the message.  Used with time_offset to generate time of each sample</summary>
        /// <param name="startTimestamp_">Nullable field value to be set</param>
        public void SetStartTimestamp(DateTime startTimestamp_)
        {
            SetFieldValue(6, 0, startTimestamp_.GetTimeStamp(), Fit.SubfieldIndexMainField);
        }
        
        ///<summary>
        /// Retrieves the StartTimestampMs field
        /// Units: ms
        /// Comment: Fractional part of start_timestamp</summary>
        /// <returns>Returns nullable ushort representing the StartTimestampMs field</returns>
        public ushort? GetStartTimestampMs()
        {
            Object val = GetFieldValue(7, 0, Fit.SubfieldIndexMainField);
            if(val == null)
            {
                return null;
            }

            return (Convert.ToUInt16(val));
            
        }

        /// <summary>
        /// Set StartTimestampMs field
        /// Units: ms
        /// Comment: Fractional part of start_timestamp</summary>
        /// <param name="startTimestampMs_">Nullable field value to be set</param>
        public void SetStartTimestampMs(ushort? startTimestampMs_)
        {
            SetFieldValue(7, 0, startTimestampMs_, Fit.SubfieldIndexMainField);
        }
        
        #endregion // Methods
    } // Class
} // namespace
