#!/usr/bin/env python

import socket

print("RUNNING")

HOST = '127.0.0.1'
PORT = 6666
BUFFERSIZE = 1024
SID = "SESSIONID:"

# connect
socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.connect((HOST, PORT))

while True:
    response = socket.recv(BUFFERSIZE)
    rdec = response.decode('utf-8').strip('\n')
    rls = rdec.split(" ")

    if rls[0] == SID:
        print("TRIGGER STRING GOT")
