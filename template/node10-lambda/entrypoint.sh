#!/bin/sh

# start shim (similar to watchdog)
./lambda-shim &

# start bootstrapper
node bootstrap.js &

# wait for exit
wait
