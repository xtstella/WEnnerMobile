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



#include <iostream>
#include <sstream>
#include "fit_decode.hpp"
#include "fit_crc.hpp"
#include "fit_factory.hpp"
#include "fit_mesg_listener.hpp"
#include "fit_developer_data_id_mesg.hpp"
#include "fit_developer_field.hpp"

namespace fit
{

const FIT_UINT8 Decode::DevFieldNumOffset = 0;
const FIT_UINT8 Decode::DevFieldSizeOffset = 1;
const FIT_UINT8 Decode::DevFieldIndexOffset = 2;

Decode::Decode()
    : mesgListener(NULL)
    , mesgDefinitionListener(NULL)
{
    for (int i=0; i<FIT_MAX_LOCAL_MESGS; i++)
    {
        localMesgDefs[i] = MesgDefinition();
        localMesgDefs[i].SetLocalNum((FIT_UINT8) i);
    }

    headerException = "";
    streamIsComplete = FIT_TRUE;
    skipHeader = FIT_FALSE;
    invalidDataSize = FIT_FALSE;
    file = NULL;
    currentByteOffset = 0;
    bytesRead = 0;
    currentByteIndex = 0;
}

FIT_BOOL Decode::IsFIT(std::istream &file)
{
    FIT_BOOL returnValue = FIT_FALSE;

    InitRead(file);

    try
    {
        do
        {
            if ( currentByteIndex == 0 )
            {
                file.read(buffer, BufferSize);
                bytesRead = (FIT_UINT32)file.gcount();
            }
            for ( ; currentByteIndex < bytesRead; currentByteIndex++ )
            {
                if (ReadByte((FIT_UINT8)buffer[currentByteIndex]) != RETURN_CONTINUE)
                {
                    returnValue = FIT_FALSE; // Error processing file header (not FIT).
                    break;
                }
                if (state != STATE_FILE_HDR)
                {
                    returnValue = FIT_TRUE; // File header processed successfully.
                    break;
                }
            }
            currentByteIndex = 0;
        } while ( file.good() && ( state == STATE_FILE_HDR ) );
    }
    catch (RuntimeException e)
    {
         // Reset buffer state.
        bytesRead = 0;
        currentByteIndex = 0;
    }

    InitRead(file);

    return returnValue; // Error processing file header (not FIT).
}

FIT_BOOL Decode::CheckIntegrity(std::istream &file)
{
    FIT_BOOL status = FIT_TRUE;

    InitRead(file);

    try
    {
        do
        {
            if ( currentByteIndex == 0 )
            {
                file.read(buffer, BufferSize);
                bytesRead = (FIT_UINT32)file.gcount();
            }

            for ( ; currentByteIndex < bytesRead; currentByteIndex++ )
            {

                switch (ReadByte((FIT_UINT8)buffer[currentByteIndex])) {
                    case RETURN_CONTINUE:
                    case RETURN_MESG:
                    case RETURN_MESG_DEF:
                        break;

                    case RETURN_END_OF_FILE:
                        status = FIT_TRUE;
                        InitRead(file, FIT_FALSE);
                        break;

                    default:
                        status = FIT_FALSE;
                        break;
                }
            }
            currentByteIndex = 0;
        } while ( file.good() && (status == FIT_TRUE) );
    }
    catch (RuntimeException e)
    {
        // Fall through and return failure.
        status = FIT_FALSE;
         // Reset buffer state.
        bytesRead = 0;
        currentByteIndex = 0;
    }

    InitRead(file);

    return status;
}

void Decode::SkipHeader()
{
    // Do not allow changing the settings after Read has started.
    if (file != NULL)
    {
        throw RuntimeException("Can't set skipHeader option after Decode started!");
    }
    // Skip header decode
    state = STATE_RECORD;
    // Decode until we hit EOF, don't consider CRC.
    skipHeader = FIT_TRUE;

}

void Decode::IncompleteStream()
{
    // Do not allow changing the settings after Read has started.
    if (file != NULL)
    {
        throw RuntimeException("Can't set incompleteStream option after Decode started!");
    }
    // Don't raise an error if eof is encountered during decode,
    // caller may try to resume if more bytes arrive.
    streamIsComplete = FIT_FALSE;
}

FIT_BOOL Decode::Read(std::istream* file)
{
    FIT_BOOL status = FIT_TRUE;
    FIT_UINT32 fileSize = 0;

    this->file = file;
    currentByteOffset = 0;
    descriptions.clear();
    developers.clear();

    // Read out the size of the file
    file->seekg(0, file->end);
    fileSize = (FIT_UINT32)file->tellg();
    // Ensure the read starts at the beginning of the file
    file->seekg(0, file->beg);

    while ( ( currentByteOffset < fileSize ) && ( status == FIT_TRUE ) )
    {
        InitRead(*file, FIT_FALSE);
        status = Resume();
    }

    return status;
}

FIT_BOOL Decode::Read
    (
    std::istream* file,
    MesgListener* mesgListener,
    MesgDefinitionListener* definitionListener,
    DeveloperFieldDescriptionListener* descriptionListener
    )
{
    this->mesgListener = mesgListener;
    this->mesgDefinitionListener = definitionListener;
    this->descriptionListener = descriptionListener;

    return Read(file);
}

FIT_BOOL Decode::Read(std::istream &file, MesgListener& mesgListener)
{
    return Read(&file, &mesgListener, nullptr, nullptr);
}

FIT_BOOL Decode::Read(std::istream &file, MesgListener& mesgListener, MesgDefinitionListener& mesgDefinitionListener)
{
    return Read(&file, &mesgListener, &mesgDefinitionListener, nullptr);
}

void Decode::Pause(void)
{
    pause = FIT_TRUE;
}

FIT_BOOL Decode::Resume(void)
{
    pause = FIT_FALSE;
    RETURN decodeReturn = RETURN_CONTINUE;

    do
    {
        if ( currentByteIndex == 0 )
        {
            file->read(buffer, BufferSize);
            bytesRead = (FIT_UINT32)file->gcount();
        }

        for (; currentByteIndex < bytesRead; currentByteIndex++)
        {
            if (pause)
                return FIT_FALSE;

            decodeReturn = ReadByte((FIT_UINT8)buffer[currentByteIndex]);

            switch (decodeReturn) {
                case RETURN_CONTINUE:
                    break;

                case RETURN_MESG:
                    if (mesg.GetNum() == FIT_MESG_NUM_DEVELOPER_DATA_ID)
                    {
                        DeveloperDataIdMesg devIdMesg(mesg);
                        FIT_UINT8 index = devIdMesg.GetDeveloperDataIndex();
                        developers[index] = devIdMesg;
                        descriptions[index] = std::unordered_map<FIT_UINT8, FieldDescriptionMesg>();
                    }
                    else if (mesg.GetNum() == FIT_MESG_NUM_FIELD_DESCRIPTION)
                    {
                        FieldDescriptionMesg descMesg(mesg);
                        FIT_UINT8 index = descMesg.GetDeveloperDataIndex();
                        FIT_UINT8 fldNum = descMesg.GetFieldDefinitionNumber();

                        try
                        {
                            descriptions.at(index)[fldNum] = descMesg;

                            if ( descriptionListener )
                                descriptionListener->OnDeveloperFieldDescription
                                    (
                                    DeveloperFieldDescription( descMesg, developers[index] )
                                    );
                        }
                        catch (std::out_of_range)
                        {
                            // Description without a Developer Data Id Message
                        }
                    }

                    if (mesgListener)
                        mesgListener->OnMesg(mesg);
                    break;

                case RETURN_MESG_DEF:
                    if (mesgDefinitionListener)
                        mesgDefinitionListener->OnMesgDefinition(localMesgDefs[localMesgIndex]);
                    break;

                case RETURN_END_OF_FILE:
                     // Increment so we do not read the same byte twice in the case of a chained file
                    currentByteIndex++;
                    currentByteOffset++;
                    return FIT_TRUE;

                default:
                    currentByteOffset++;
                    return FIT_TRUE;
            }
            currentByteOffset++;
        }
        currentByteIndex = 0;
    } while ( file->good() );

    if ((streamIsComplete == FIT_TRUE) && (skipHeader == FIT_FALSE))
    {
        // When decoding a complete file we should exit via RETURN_END_OF_FILE state only.
      std::ostringstream message;
      message << "FIT decode error: Unexpected end of input stream at byte: " << currentByteOffset;
        throw RuntimeException(message.str());
    }
    if (streamIsComplete == FIT_FALSE)
    {
        // If stream is not yet complete caller can resume() when there is more data
        // or decide there was an error.
        if ((decodeReturn == RETURN_MESG) || (decodeReturn == RETURN_MESG_DEF))
        {
            // Our stream ended on a complete message, maybe we are done decoding.
            return FIT_TRUE;
        }
        else
        {
            // EOF was encountered mid message. Caller may want to resume once
            // more bytes are available.
            return FIT_FALSE;
        }
    }
        // if Decoding Records section only, file should end on a complete message
        // (unless incomplete stream option above was also used)
    else
    {
        if ((decodeReturn == RETURN_MESG) || (decodeReturn == RETURN_MESG_DEF))
        {
            // Our stream ended on a complete message, we are done decoding.
            return FIT_TRUE;
        }
        else
        {
            if (invalidDataSize == FIT_FALSE)
            {
                std::ostringstream message;
                message << "FIT decode error: Unexpected end of input stream at byte: " << currentByteOffset;
                throw RuntimeException(message.str());
            }
            else
            {
                return FIT_TRUE;
            }
        }
    }
}

FIT_BOOL Decode::getInvalidDataSize(void)
{
    return invalidDataSize;
}

void Decode::setInvalidDataSize(FIT_BOOL value)
{
    invalidDataSize = value;
}

void Decode::InitRead(std::istream &file)
{
    InitRead(file, FIT_TRUE);
}

void Decode::InitRead(std::istream &file, FIT_BOOL startOfFile)
{
    fileBytesLeft = 3; // Header byte + CRC.
    fileHdrOffset = 0;
    crc = 0;
    headerException = "";
    // Only reset to header state if we do not want
    // to skip the header.
    if (skipHeader == FIT_FALSE)
        state = STATE_FILE_HDR;
    lastTimeOffset = 0;

    // Reset to the beginning of the file
    if ( startOfFile == FIT_TRUE)
    {
        file.seekg(0, file.beg);
    }

    file.clear(); // workaround libc++ issue
}

void Decode::UpdateEndianness(FIT_UINT8 type, FIT_UINT8 size)
{
    FIT_UINT8 typeSize = baseTypeSizes[type & FIT_BASE_TYPE_NUM_MASK];

    if (((type & FIT_BASE_TYPE_ENDIAN_FLAG) != 0) &&
        ((archs[localMesgIndex] & FIT_ARCH_ENDIAN_MASK) != FIT_ARCH_ENDIAN_LITTLE))
    {
        // Swap the bytes for each element.
        for (int element = 0; element < size; element++)
        {
            for (int i = 0; i < (typeSize / 2); i++)
            {
                FIT_UINT8 tmp = fieldData[element * typeSize + i];
                fieldData[element * typeSize + i] = fieldData[element * typeSize + typeSize - i - 1];
                fieldData[element * typeSize + typeSize - i - 1] = tmp;
            }
        }
    }
}

Decode::RETURN Decode::ReadByte(FIT_UINT8 data)
{
    if ((fileBytesLeft > 0) && (skipHeader == FIT_FALSE))
    {
        crc = CRC::Get16(crc, data);

        fileBytesLeft--;

        if (fileBytesLeft == 1) // CRC low byte.
        {
            if (state != STATE_RECORD)
            {
                std::ostringstream message;
                message << "FIT decode error: Decoder not in correct state after last data byte in file. Check message definitions. Error at byte: " << currentByteOffset;
                throw(RuntimeException(message.str()));
            }

            return RETURN_CONTINUE; // Next byte.
        }

        if (fileBytesLeft == 0) // CRC high byte.
        {
            if (crc != 0)
            {
                std::ostringstream message;
                message << "FIT decode error: File CRC failed. Error at byte: " << currentByteOffset;
                throw(RuntimeException(message.str()));
            }

            return RETURN_END_OF_FILE;
        }
    }

    switch (state) {
        case STATE_FILE_HDR:
            switch (fileHdrOffset++)
            {
                case 0:
                    fileHdrSize = data;
                    fileBytesLeft = fileHdrSize + 2;
                    break;
                case 1:
                    if ((data & FIT_PROTOCOL_VERSION_MAJOR_MASK) > (FIT_PROTOCOL_VERSION_MAJOR << FIT_PROTOCOL_VERSION_MAJOR_SHIFT))
                    {
                        std::ostringstream message;
                        message << "FIT decode error: Protocol version " << (data & FIT_PROTOCOL_VERSION_MAJOR_MASK) << FIT_PROTOCOL_VERSION_MAJOR_SHIFT << "." << (data & FIT_PROTOCOL_VERSION_MINOR_MASK) << " not supported.  Must be " << FIT_PROTOCOL_VERSION_MAJOR << ".15 or earlier.";
                        headerException = message.str();
                    }
                    break;
                case 4:
                    fileDataSize = data & 0xFF;
                    break;
                case 5:
                    fileDataSize |= (FIT_UINT32) (data & 0xFF) << 8;
                    break;
                case 6:
                    fileDataSize |= (FIT_UINT32) (data & 0xFF) << 16;
                    break;
                case 7:
                    fileDataSize |= (FIT_UINT32) (data & 0xFF) << 24;
                    if ( (fileDataSize == 0) && (invalidDataSize == FIT_FALSE) )
                    {
                        invalidDataSize = FIT_TRUE;
                        std::ostringstream message;
                        message << "FIT decode error: File Size is 0. Error at byte : " << currentByteOffset;
                        headerException = message.str();
                    }
                    break;
                case 8:
                    if (data != '.')
                    {
                        std::ostringstream message;
                        message << "FIT decode error: File header signature mismatch.  File is not FIT. Error at byte : " << currentByteOffset;
                        headerException = message.str();
                    }
                    break;
                case 9:
                    if (data != 'F')
                    {
                        std::ostringstream message;
                        message << "FIT decode error: File header signature mismatch.  File is not FIT. Error at byte : " << currentByteOffset;
                        headerException = message.str();
                    }
                    break;
                case 10:
                    if (data != 'I')
                    {
                        std::ostringstream message;
                        message << "FIT decode error: File header signature mismatch.  File is not FIT. Error at byte : " << currentByteOffset;
                        headerException = message.str();
                    }
                    break;
                case 11:
                    if (data != 'T')
                    {
                        std::ostringstream message;
                        message << "FIT decode error: File header signature mismatch.  File is not FIT. Error at byte : " << currentByteOffset;
                        headerException = message.str();
                    }

                    if (headerException != "")
                    {
                        throw(RuntimeException(headerException));
                    }
                    break;
                default:
                    break;
            }

            if (fileHdrOffset == fileHdrSize)
            {
                fileBytesLeft = fileDataSize + 2; // include crc
                state = STATE_RECORD;

                // We don't care about the CRC when the file size is invalid
                if (invalidDataSize)
                {
                    skipHeader = FIT_TRUE;
                }
            }
            break;

        case STATE_RECORD:
            fieldIndex = 0;
            fieldBytesLeft = 0;

            if (fileBytesLeft > 1) {
                if ((data & FIT_HDR_TIME_REC_BIT) != 0) {
                    Field timestampField = Field(Profile::MESG_RECORD, Profile::RECORD_MESG_TIMESTAMP);
                    FIT_UINT8 timeOffset = data & FIT_HDR_TIME_OFFSET_MASK;

                    timestamp += (timeOffset - lastTimeOffset) & FIT_HDR_TIME_OFFSET_MASK;
                    lastTimeOffset = timeOffset;
                    timestampField.SetUINT32Value(timestamp);

                    localMesgIndex = (data & FIT_HDR_TIME_TYPE_MASK) >> FIT_HDR_TIME_TYPE_SHIFT;

                    if (localMesgDefs[localMesgIndex].GetNum() == FIT_MESG_NUM_INVALID)
                    {
                        std::ostringstream message;
                        message << "FIT decode error: Missing FIT message definition for local message number " << ((int)localMesgIndex) << ". Error at byte: " << currentByteOffset;
                        throw(RuntimeException(message.str()));
                    }

                    mesg = Mesg(localMesgDefs[localMesgIndex].GetNum());
                    mesg.SetLocalNum(localMesgIndex);
                    mesg.AddField(timestampField);

                    if (localMesgDefs[localMesgIndex].GetFields().size() == 0)
                        return RETURN_MESG;

                    state = STATE_FIELD_DATA;
                }
                else
                {
                    localMesgIndex = data & FIT_HDR_TYPE_MASK;

                    if ((data & FIT_HDR_TYPE_DEF_BIT) != 0)
                    {
                        hasDevData = ((data & FIT_HDR_DEV_FIELD_BIT) != 0);
                        state = STATE_RESERVED1;
                    }
                    else
                    {
                        if (localMesgDefs[localMesgIndex].GetNum() == FIT_MESG_NUM_INVALID)
                        {
                            std::ostringstream message;
                            message << "FIT decode error: Missing FIT message definition for local message number " << ((int)localMesgIndex) << ". Error at byte: " << currentByteOffset;
                            throw(RuntimeException(message.str()));
                        }

                        mesg = Mesg(localMesgDefs[localMesgIndex].GetNum());
                        mesg.SetLocalNum(localMesgIndex);

                        if (localMesgDefs[localMesgIndex].GetFields().size() != 0)
                        {
                            state = STATE_FIELD_DATA;
                        }
                        else if (localMesgDefs[localMesgIndex].GetDevFields().size() != 0)
                        {
                            state = STATE_DEV_FIELD_DATA;
                        }
                        else
                        {
                            return RETURN_MESG;
                        }
                    }
                }
            }
            else
            {
                // We just got the low byte of the crc.
                state = STATE_FILE_CRC_HIGH;
            }
            break;

        case STATE_RESERVED1:
            localMesgDefs[localMesgIndex].ClearFields();
            state = STATE_ARCH;
            break;

        case STATE_ARCH:
            archs[localMesgIndex] = data;
            state = STATE_MESG_NUM_0;
            break;

        case STATE_MESG_NUM_0:
            // Read the global message number bytes in as if they are in little endian format.
            localMesgDefs[localMesgIndex].SetNum((FIT_UINT16)data);
            state = STATE_MESG_NUM_1;
            break;

        case STATE_MESG_NUM_1:
            localMesgDefs[localMesgIndex].SetNum(localMesgDefs[localMesgIndex].GetNum() | ((FIT_UINT16)data << 8));

            // We have to check for endianness.
            if (archs[localMesgIndex] == FIT_ARCH_ENDIAN_BIG) {
                localMesgDefs[localMesgIndex].SetNum((localMesgDefs[localMesgIndex].GetNum() >> 8) | ((localMesgDefs[localMesgIndex].GetNum() & 0xFF) << 8));
            }
            else if (archs[localMesgIndex] != FIT_ARCH_ENDIAN_LITTLE)
            {
                std::ostringstream message;
                message << "FIT decode error: Architecture " << archs[localMesgIndex] << " not supported. Error at byte: " << currentByteOffset;
                throw(RuntimeException(message.str()));
            }

            state = STATE_NUM_FIELDS;
            break;

        case STATE_NUM_FIELDS:
            numFields = data;
            fieldIndex = 0;

            if (numFields == 0)
            {
                if (hasDevData)
                {
                    state = STATE_NUM_DEV_FIELDS;
                }
                else
                {
                    state = STATE_RECORD;
                    return RETURN_MESG_DEF;
                }
            }
            else
            {
                state = STATE_FIELD_NUM;
            }
            break;

        case STATE_FIELD_NUM:
            localMesgDefs[localMesgIndex].AddField(FieldDefinition());
            localMesgDefs[localMesgIndex].GetFieldByIndex(fieldIndex)->SetNum(data);
            state = STATE_FIELD_SIZE;
            break;

        case STATE_FIELD_SIZE:
            if ( data == 0 )
            {
                // Bad Size
                std::ostringstream message;
                message << "FIT decode error: Invalid Field Size " << data << ". Error at byte: " << currentByteOffset;
                throw(RuntimeException(message.str()));
            }

            localMesgDefs[localMesgIndex].GetFieldByIndex(fieldIndex)->SetSize(data);
            state = STATE_FIELD_TYPE;
            break;

        case STATE_FIELD_TYPE:
            localMesgDefs[localMesgIndex].GetFieldByIndex(fieldIndex)->SetType(data);

            if (++fieldIndex >= numFields)
            {
                if (hasDevData)
                {
                    state = STATE_NUM_DEV_FIELDS;
                }
                else
                {
                    state = STATE_RECORD;
                    return RETURN_MESG_DEF;
                }
            }
            else
            {
                state = STATE_FIELD_NUM;
            }
            break;

        case STATE_NUM_DEV_FIELDS:
            numFields = data;
            fieldIndex = 0;

            if (numFields == 0)
            {
                state = STATE_RECORD;
                return RETURN_MESG_DEF;
            }

            state = STATE_DEV_FIELD_NUM;
            break;

        case STATE_DEV_FIELD_NUM:
            fieldData[DevFieldNumOffset] = data;
            state = STATE_DEV_FIELD_SIZE;
            break;

        case STATE_DEV_FIELD_SIZE:
            if ( data == 0 )
            {
                // Bad Size
                std::ostringstream message;
                message << "FIT decode error: Invalid Developer Field Size " << data << ". Error at byte: " << currentByteOffset;
                throw(RuntimeException(message.str()));
            }

            fieldData[DevFieldSizeOffset] = data;
            state = STATE_DEV_FIELD_INDEX;
            break;

        case STATE_DEV_FIELD_INDEX:
            fieldData[DevFieldIndexOffset] = data;

            try
            {
                const FieldDescriptionMesg& desc = descriptions
                    .at(fieldData[DevFieldIndexOffset])
                    .at(fieldData[DevFieldNumOffset]);
                const DeveloperDataIdMesg& developer = developers
                    .at( fieldData[DevFieldIndexOffset] );

                localMesgDefs[localMesgIndex]
                    .AddDevField(DeveloperFieldDefinition(desc, developer, fieldData[DevFieldSizeOffset]));
            }
            catch (std::out_of_range)
            {
                // No Matching Description Message add a Generic Definition
                localMesgDefs[localMesgIndex]
                    .AddDevField(DeveloperFieldDefinition(
                        fieldData[DevFieldNumOffset],
                        fieldData[DevFieldSizeOffset],
                        fieldData[DevFieldIndexOffset]));
            }

            if (++fieldIndex >= numFields)
            {
                state = STATE_RECORD;
                return RETURN_MESG_DEF;
            }

            state = STATE_DEV_FIELD_NUM;
            break;

        case STATE_FIELD_DATA:
            if (fieldBytesLeft == 0)
            {
                fieldDataIndex = 0;
                fieldBytesLeft = localMesgDefs[localMesgIndex].GetFieldByIndex(fieldIndex)->GetSize();

                if (fieldBytesLeft == 0)
                {
                    fieldBytesLeft = localMesgDefs[localMesgIndex].GetFieldByIndex(++fieldIndex)->GetSize();
                }
            }

            fieldData[fieldDataIndex++] = data;
            fieldBytesLeft--;

            if (fieldBytesLeft == 0)
            {
                MesgDefinition defn = localMesgDefs[localMesgIndex];
                FieldDefinition* fldDefn = defn.GetFieldByIndex(fieldIndex);
                FIT_UINT8 baseType = fldDefn->GetType() & FIT_BASE_TYPE_NUM_MASK;
                FIT_UINT8 typeSize = baseTypeSizes[baseType];
                FIT_BOOL read = FIT_TRUE;

                if (baseType < FIT_BASE_TYPES) // Ignore field if base type not supported.
                {
                    UpdateEndianness(fldDefn->GetType(), fldDefn->GetSize());

                    Field field(mesg.GetNum(), fldDefn->GetNum());
                    if (field.IsValid()) // If known field type.
                    {
                        if ( field.GetType() != fldDefn->GetType() )
                        {
                            FIT_UINT8 profileSize = fit::baseTypeSizes[( field.GetType() & FIT_BASE_TYPE_NUM_MASK )];
                            if ( typeSize < profileSize )
                            {
                                field.SetBaseType( fldDefn->GetType() );
                            }
                            else if ( typeSize != profileSize )
                            {
                                // Demotion is hard. Don't read the field if the
                                // sizes are different. Use the profile type if the
                                // signedness of the field has changed.
                                read = FIT_FALSE;
                            }
                        }

                        if ( read )
                        {
                            field.Read(&fieldData, defn.GetFieldByIndex(fieldIndex)->GetSize());
                        }

                        // The special case time record.
                        if (defn.GetFieldByIndex(fieldIndex)->GetNum() == FIT_FIELD_NUM_TIMESTAMP)
                        {
                            timestamp = field.GetUINT32Value();
                            lastTimeOffset = (FIT_UINT8)(timestamp & FIT_HDR_TIME_OFFSET_MASK);
                        }

                        //Allows messages containing the accumulated field to set the accumulated value
                        if ( field.GetIsAccumulated() )
                        {
                            FIT_UINT8 i;
                            for (i = 0; i < field.GetNumValues(); i++)
                            {
                                FIT_FLOAT64 value = field.GetRawValue(i);
                                FIT_UINT16 j;
                                for (j = 0; j < mesg.GetNumFields(); j++)
                                {
                                    FIT_UINT16 k;
                                    Field* containingField = mesg.GetFieldByIndex(j);
                                    FIT_UINT16 numComponents = containingField->GetNumComponents();

                                    for (k = 0; k < numComponents; k++)
                                    {
                                        const Profile::FIELD_COMPONENT* fc = containingField->GetComponent(k);
                                        if ( ( fc->num == field.GetNum() ) && ( fc->accumulate ) )
                                        {
                                            value = ((((value / field.GetScale()) - field.GetOffset()) + fc->offset) * fc->scale);
                                        }
                                    }
                                }
                                accumulator.Set(mesg.GetNum(), field.GetNum(), (FIT_UINT32)value);
                            }
                        }

                        if (field.GetNumValues() > 0)
                        {
                            mesg.AddField(field);
                        }
                    }
                }
                fieldIndex++;
            }

            if (fieldIndex >= localMesgDefs[localMesgIndex].GetFields().size())
            {
                // Now that the entire message is decoded we may evaluate subfields and expand components
                for (FIT_UINT16 i=0; i<mesg.GetNumFields(); i++)
                {
                    FIT_UINT16 activeSubField = mesg.GetActiveSubFieldIndexByFieldIndex(i);

                    if (activeSubField == FIT_SUBFIELD_INDEX_MAIN_FIELD)
                    {
                        if (mesg.GetFieldByIndex(i)->GetNumComponents() > 0)
                        {
                            ExpandComponents(mesg.GetFieldByIndex(i), mesg.GetFieldByIndex(i)->GetComponent(0), mesg.GetFieldByIndex(i)->GetNumComponents());
                        }
                    }
                    else
                    {
                        if (mesg.GetFieldByIndex(i)->GetSubField(activeSubField)->numComponents > 0)
                        {
                            ExpandComponents(mesg.GetFieldByIndex(i), mesg.GetFieldByIndex(i)->GetSubField(activeSubField)->components, mesg.GetFieldByIndex(i)->GetSubField(activeSubField)->numComponents);
                        }
                    }
                }

                if (localMesgDefs[localMesgIndex].GetDevFields().size() != 0)
                {
                    fieldIndex = 0;
                    fieldBytesLeft = 0;
                    state = STATE_DEV_FIELD_DATA;
                }
                else
                {
                    state = STATE_RECORD;
                    return RETURN_MESG;
                }
            }
            break;

        case STATE_DEV_FIELD_DATA: {
             MesgDefinition& localMesgDef = localMesgDefs[localMesgIndex];
             DeveloperFieldDefinition* fieldDef = localMesgDef.GetDevFieldByIndex(fieldIndex);

             if (fieldBytesLeft == 0)
             {
                 fieldDataIndex = 0;
                 fieldBytesLeft = fieldDef->GetSize();

                 if (fieldBytesLeft == 0)
                 {
                     fieldBytesLeft = localMesgDefs->
                         GetDevFieldByIndex(++fieldIndex)->GetSize();
                 }
             }

            fieldData[fieldDataIndex++] = data;
            fieldBytesLeft--;

             if (fieldBytesLeft == 0)
             {
                 MesgDefinition defn = localMesgDefs[localMesgIndex];
                 DeveloperFieldDefinition* fldDefn = defn.GetDevFieldByIndex(fieldIndex);
                 FIT_UINT8 baseType = fldDefn->GetType() & FIT_BASE_TYPE_NUM_MASK;

                 if (baseType < FIT_BASE_TYPES) // Ignore field if base type not supported.
                 {
                     DeveloperField field(*fldDefn);

                     UpdateEndianness(fldDefn->GetType(), fldDefn->GetSize());
                     field.Read(&fieldData, fldDefn->GetSize());
                     mesg.AddDeveloperField(field);
                 }

                 fieldIndex++;

                 if (fieldIndex >= localMesgDef.GetDevFields().size()) {
                     // Mesg decode complete
                     state = STATE_RECORD;
                     return RETURN_MESG;
                 }
             }
            break;
        }

        default:
            break;
    }

    return RETURN_CONTINUE;
}

void Decode::ExpandComponents(Field* containingField, const Profile::FIELD_COMPONENT* components, FIT_UINT16 numComponents)
{
    FIT_UINT16 offset = 0;
    FIT_UINT16 i;

    for (i = 0; i < numComponents; i++)
    {
        const Profile::FIELD_COMPONENT* component = &components[i];

        if (component->num != FIT_FIELD_NUM_INVALID)
        {
            Field componentField(mesg.GetNum(), component->num);
            FIT_UINT16 subfieldIndex = mesg.GetActiveSubFieldIndex( componentField.GetNum() );
            FIT_FLOAT64 value;
            FIT_UINT32 bitsValue = FIT_UINT32_INVALID;
            FIT_SINT32 signedBitsValue = FIT_SINT32_INVALID;

            if (componentField.IsSignedInteger())
            {
                signedBitsValue = containingField->GetBitsSignedValue(offset, component->bits);

                if (signedBitsValue == FIT_SINT32_INVALID)
                    break; // No more data for components.

                if (component->accumulate)
                    bitsValue = accumulator.Accumulate(mesg.GetNum(), component->num, signedBitsValue, component->bits);
            }
            else
            {
                bitsValue = containingField->GetBitsValue(offset, component->bits);

                if (bitsValue == FIT_UINT32_INVALID)
                    break; // No more data for components.

                if (component->accumulate)
                    bitsValue = accumulator.Accumulate(mesg.GetNum(), component->num, bitsValue, component->bits);
            }

            // If the component field itself has *one* component apply the scale and offset of the componentField's
            // (nested) component
            if (componentField.GetNumComponents() == 1)
            {
                if(componentField.IsSignedInteger())
                    value = (((signedBitsValue / (FIT_FLOAT64)component->scale) - component->offset) + componentField.GetComponent(0)->offset) * componentField.GetComponent(0)->scale;
                else
                    value = (((bitsValue / (FIT_FLOAT64)component->scale) - component->offset) + componentField.GetComponent(0)->offset) * componentField.GetComponent(0)->scale;
                if (mesg.HasField(componentField.GetNum()))
                {
                    fit::Field *currentField = mesg.GetField(componentField.GetNum());
                    currentField->AddRawValue(value, currentField->GetNumValues());
                }
                else
                {
                    componentField.AddRawValue(value, componentField.GetNumValues());
                    mesg.AddField(componentField);
                }
            }
            // The component field is itself a composite field (more than one component).  Don't use scale/offset, containing
            // field data must already be encoded.  Add elements to it until we have added bitsvalue
            else if (componentField.GetNumComponents() > 1)
            {
                int bitsAdded = 0;
                long mask;

                while (bitsAdded < component->bits)
                {
                    mask = ((long)1 << baseTypeSizes[componentField.GetType() & FIT_BASE_TYPE_NUM_MASK]) - 1;
                    if (mesg.HasField(componentField.GetNum()))
                    {
                        Field* field = mesg.GetField( componentField.GetNum() );
                        field->AddValue( bitsValue & mask, field->GetNumValues() );
                    }
                    else
                    {
                        componentField.AddValue(bitsValue & mask, componentField.GetNumValues());
                        mesg.AddField(componentField);
                    }
                    bitsValue >>= baseTypeSizes[componentField.GetType() & FIT_BASE_TYPE_NUM_MASK];
                    bitsAdded += baseTypeSizes[componentField.GetType() & FIT_BASE_TYPE_NUM_MASK];
                }
            }
            // componentField is an ordinary field, apply scale and offset as usual
            else
            {
                if(componentField.IsSignedInteger())
                    value = (((signedBitsValue / (FIT_FLOAT64)component->scale) - component->offset) + componentField.GetOffset(subfieldIndex)) * componentField.GetScale(subfieldIndex);
                else
                    value = (((bitsValue / (FIT_FLOAT64)component->scale) - component->offset) + componentField.GetOffset(subfieldIndex)) * componentField.GetScale(subfieldIndex);
                if (mesg.HasField(componentField.GetNum()))
                {
                    fit::Field *currentField = mesg.GetField(componentField.GetNum());
                    currentField->AddRawValue(value, currentField->GetNumValues());
                }
                else
                {
                    componentField.AddRawValue(value, componentField.GetNumValues());
                    mesg.AddField(componentField);
                }
            }
        }
        offset += component->bits;
    }
}

} // namespace fit
