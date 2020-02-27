# OWASP ZAP
OWASP Zed Attack Proxy

## Docker Setup

```
docker build -t <imagename> .
docker run -t <imagename> run.sh -t <target address> -m <Minutes the spider runs>
```
## Warning
The Target Address should have the protocol mentioned -> http:// https:// \ 
If it is not present, the tools fails

## Example 
```
docker build -t MyImage
docker run -t MyImage run.sh -t https://kaiburr.com -m 2
```

The default spider run time is 1 minute.
## Parameters
```
    -b target image         target image name ex:alpine:3
    -t target         target URL including the protocol, eg https://www.example.com

    -h                print this help message
    -c config_file    config file to use to INFO, IGNORE or FAIL warnings
    -u config_url     URL of config file to use to INFO, IGNORE or FAIL warnings
    -g gen_file       generate default config file (all rules set to WARN)
    -m mins           the number of minutes to spider for (default 1)
    -a                include the alpha passive scan rules as well
    -d                show debug messages
    -P                specify listen port
    -D                delay in seconds to wait for passive scanning 
    -i                default rules not in the config file to INFO
    -I                do not return failure on warning
    -j                use the Ajax spider in addition to the traditional one
    -l level          minimum level to show: PASS, IGNORE, INFO, WARN or FAIL, use with -s to hide example URLs
    -n context_file   context file which will be loaded prior to spidering the target
    -p progress_file  progress file which specifies issues that are being addressed
    -s                short output format - dont show PASSes or example URLs
    -T                max time in minutes to wait for ZAP to start and the passive scan to run
    -z zap_options    ZAP command line options e.g. -z "-config aaa=bbb -config ccc=ddd"
    --hook            path to python file that define your custom hooks

```

## Output
The output JSON is stored in `/zap/result.json`.
