#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 path-to-yocto-image.tar.bz2"
    exit 1
fi

export MLD="$HOME/git/meta-lxd"

imagename="${1##*/}"; imagename="${imagename%.tar.bz2}"
containername="poky-vcpe"
profilename="${containername}"
volumename="${containername}-nvram"

lxc image delete ${imagename} 2> /dev/null

creation_date=$(date +%s)
creation_stamp=$(date -r "$MLD/$1" "+%Y%m%d_%H:%M")

cat > $MLD/tmp/metadata.yaml << EOL
architecture: "i686"
creation_date: ${creation_date}
properties:
    description: "$imagename (${creation_stamp})"
    os: ""
    release: ""
    version: ""
EOL

tar czf $MLD/tmp/metadata.tar.gz -C $MLD/tmp metadata.yaml
lxc image import $MLD/tmp/metadata.tar.gz $MLD/$1 --alias ${imagename}

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
