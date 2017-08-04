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


#if !defined(FIT_SPORT_MESG_HPP)
#define FIT_SPORT_MESG_HPP

#include "fit_mesg.hpp"

namespace fit
{

class SportMesg : public Mesg
{
public:
    class FieldDefNum final
    {
    public:
       static const FIT_UINT8 Sport = 0;
       static const FIT_UINT8 SubSport = 1;
       static const FIT_UINT8 Name = 3;
       static const FIT_UINT8 Invalid = FIT_FIELD_NUM_INVALID;
    };

    SportMesg(void) : Mesg(Profile::MESG_SPORT)
    {
    }

    SportMesg(const Mesg &mesg) : Mesg(mesg)
    {
    }

    ///////////////////////////////////////////////////////////////////////
    // Checks the validity of sport field
    // Returns FIT_TRUE if field is valid
    ///////////////////////////////////////////////////////////////////////
    FIT_BOOL IsSportValid() const
    {
        const Field* field = GetField(0);
        if( FIT_NULL == field )
        {
            return FIT_FALSE;
        }

        return field->IsValueValid();
    }

    ///////////////////////////////////////////////////////////////////////
    // Returns sport field
    ///////////////////////////////////////////////////////////////////////
    FIT_SPORT GetSport(void) const
    {
        return GetFieldENUMValue(0, 0, FIT_SUBFIELD_INDEX_MAIN_FIELD);
    }

    ///////////////////////////////////////////////////////////////////////
    // Set sport field
    ///////////////////////////////////////////////////////////////////////
    void SetSport(FIT_SPORT sport)
    {
        SetFieldENUMValue(0, sport, 0, FIT_SUBFIELD_INDEX_MAIN_FIELD);
    }

    ///////////////////////////////////////////////////////////////////////
    // Checks the validity of sub_sport field
    // Returns FIT_TRUE if field is valid
    ///////////////////////////////////////////////////////////////////////
    FIT_BOOL IsSubSportValid() const
    {
        const Field* field = GetField(1);
        if( FIT_NULL == field )
        {
            return FIT_FALSE;
        }

        return field->IsValueValid();
    }

    ///////////////////////////////////////////////////////////////////////
    // Returns sub_sport field
    ///////////////////////////////////////////////////////////////////////
    FIT_SUB_SPORT GetSubSport(void) const
    {
        return GetFieldENUMValue(1, 0, FIT_SUBFIELD_INDEX_MAIN_FIELD);
    }

    ///////////////////////////////////////////////////////////////////////
    // Set sub_sport field
    ///////////////////////////////////////////////////////////////////////
    void SetSubSport(FIT_SUB_SPORT subSport)
    {
        SetFieldENUMValue(1, subSport, 0, FIT_SUBFIELD_INDEX_MAIN_FIELD);
    }

    ///////////////////////////////////////////////////////////////////////
    // Checks the validity of name field
    // Returns FIT_TRUE if field is valid
    ///////////////////////////////////////////////////////////////////////
    FIT_BOOL IsNameValid() const
    {
        const Field* field = GetField(3);
        if( FIT_NULL == field )
        {
            return FIT_FALSE;
        }

        return field->IsValueValid();
    }

    ///////////////////////////////////////////////////////////////////////
    // Returns name field
    ///////////////////////////////////////////////////////////////////////
    FIT_WSTRING GetName(void) const
    {
        return GetFieldSTRINGValue(3, 0, FIT_SUBFIELD_INDEX_MAIN_FIELD);
    }

    ///////////////////////////////////////////////////////////////////////
    // Set name field
    ///////////////////////////////////////////////////////////////////////
    void SetName(FIT_WSTRING name)
    {
        SetFieldSTRINGValue(3, name, 0);
    }

};

} // namespace fit

#endif // !defined(FIT_SPORT_MESG_HPP)
