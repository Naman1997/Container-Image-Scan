# OWASP ZAP
OWASP Zed Attack Proxy

## Docker Setup
Database password=password
You can also use your own database, just point the clair server to the correct db in the docker-compose file.

```
git clone https://github.com/Naman1997/Kaiburr.git
cd Kaiburr #Rename the folder if its named something else

docker-compose up -d postgres

# Input data from the sql file, make sure the network for the continer above is named "kaiburr_default" or else you can change the network name for the command below
#Also, the sql data can be downloaded by clair by default, however, you will have to wait 30 minutes for that, this is faster.
docker run -it \
    -v $(pwd):/sql/ \
    --network "kaiburr_default" \
    --link clair_postgres:clair_postgres \
    postgres:latest \
        bash -c "PGPASSWORD=password psql -h clair_postgres -U postgres < /sql/clair.sql"
        
#Start the clair server
docker-compose up -d clair

#Build our app
sudo docker build -t zap .
```
## Warning
The Target Address should have the protocol mentioned -> http:// https:// \ 
If it is not present, the tools fails

## Example for scaning images
Once the image zap is built, we can use it to scan images.
The image scanning is handled by claircli by a function named printer in zap-baseline.py
It's a one line instruction and can be changed for your liking. Learn more [HERE](https://github.com/joelee2012/claircli)
Make sure that all 3 containers are on the same network. Below I have again used "kaiburr_default". If your network is named something else change it for the command below.

```
#Here I'm using alpine:3 as the image to scan
docker run --network kaiburr_default -t zap run.sh -b alpine:3

```

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
