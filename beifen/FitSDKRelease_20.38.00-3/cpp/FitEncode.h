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


#if !defined(FIT_ENCODE_H)
#define FIT_ENCODE_H

#include <vector>
#include "fit.hpp"
#include "fit_mesg.hpp"
#include "fit_mesg_definition.hpp"

@interface FitEncode : NSObject
- (id)initWithVersion:(fit::ProtocolVersion)ver;
- (void) Open:(FILE *)file;
- (void) WriteMesgDef:(const fit::MesgDefinition &) mesgDef;
- (void) WriteMesg:(const fit::Mesg &) mesg;
- (void) WriteMesgs:(const std::vector<fit::Mesg> &) mesgs;
- (FIT_BOOL) Close;
- (void) OnMesg:(fit::Mesg&) mesg;
- (void) OnMesgDefinition:(fit::MesgDefinition&) mesgDef;
@end

#endif /* FIT_ENCODE_H */
