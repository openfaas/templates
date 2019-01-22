#!/bin/bash
 
adduser --system app 
chown -R app /home/app 
 
export upstream_url="http://127.0.0.1:8081" 
export mode="http" 
export CLASSPATH="/home/app/entrypoint-1.0/lib/*"
export fprocess="java com.openfaas.entrypoint.App" 
 
exec su -s /bin/sh --preserve-environment -c fwatchdog app
