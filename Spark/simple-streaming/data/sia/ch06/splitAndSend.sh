#!/bin/bash

if [ -z "$1" ]; then
        echo "Missing output folder name"
        exit 1
fi

rm -rf $1
for f in `ls order-chunk*`; do
	rm $f
done
mkdir $1

split -l 10000 orders.txt order-chunk

for f in `ls order-chunk*`; do
        if [ "$2" == "local" ]; then
                mv $f $1
        else
                hdfs dfs -copyFromLocal $f $1
                rm -f $f
        fi
        sleep 3
done
