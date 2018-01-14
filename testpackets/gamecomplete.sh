#!/bin/bash

# take sessionID
sID="cumming yobbo hitler"
pName="testusername"

# send session connect
echo SESSIONCONNECT
echo PNAME: $pName
echo SESSIONID: $sID
echo END
# send gamecomplete
echo GAMECOMPLETE
echo PNAME: $pName
echo SESSIONID: $sID
echo GSCORE: 500
echo END
