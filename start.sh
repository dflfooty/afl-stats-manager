#!/bin/bash

if [[ $TYPE == "web"* ]]; then
  java -jar /app/afl-stats-manager-web.jar
elif  [[ $TYPE == "worker"* ]]; then
  java -jar /app/afl-stats-manager-worker.jar
elif  [[ $TYPE == "scheduler"* ]]; then
  java -jar /app/afl-stats-manager-scheduler.jar
fi