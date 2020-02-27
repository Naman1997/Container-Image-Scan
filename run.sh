#!/bin/sh
#Required by zap to store result.json
mkdir /zap/wrk/

/zap/zap-baseline.py $@ -J report.json

python3 /zap/convert.py
