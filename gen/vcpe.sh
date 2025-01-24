#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 vcpe-image-qemux86.lxd.tar.bz2"
    exit 1
fi

imagefile=$1
imagename="${imagefile##*/}"; imagename="${imagename%.tar.bz2}"
containername="vcpe"
profilename="${containername}"
volumename="${containername}-nvram"

lxc image delete ${imagename} 2> /dev/null

lxc image import $1 --alias ${imagename}

echo "Configuring ${containername}"

lxc delete ${containername} -f > /dev/null 2>&1

lxc profile delete ${profilename} > /dev/null 2>&1

lxc profile copy default ${profilename} > /dev/null 2>&1

cat << EOF | lxc profile edit ${profilename}
name: ${containername}
description: "${containername}"
config:
    boot.autostart: "false"
    security.privileged: "true"
    security.nesting: "true"
    limits.memory: "512MB"
    limits.cpu: "0,1"
devices:
    root:
        path: /
        pool: default
        type: disk
        size: "512MB"
EOF

# erouter0
lxc profile device add ${profilename} erouter0 nic nictype=bridged parent=wan name=erouter0

# nvram
if ! lxc storage volume show default $volumename > /dev/null 2>&1; then
    lxc storage volume create default $volumename size=4MB
fi
lxc profile device add $containername nvram disk pool=default source=$volumename path=/data/poky_nvram 1>/dev/null

lxc launch ${imagename} ${containername} -p ${profilename}
