#!/bin/bash

# take sessionID
sID=$1
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
