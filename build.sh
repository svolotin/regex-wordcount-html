#!/bin/bash
# Sami

mvn clean package
if [ $? -ne 0 ];then
    echo "Unit tests of build is failed"
    exit 1
fi
docker-compose build

if [ $? -ne 0 ];then
    echo "docker-compose failed"
    exit 1
fi
