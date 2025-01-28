# VCPE
A Yocto reference-distro based container image to run in LXD

## Description
* inherit poky distro
* add meta-openembedded
* add meta-vcpe
* do not build kernel
* remove undesired image types
* add lxd image class
* launch-scripts for vcpe, wan-bridge, and bng

### Dependencies
* Yocto 4.0
* LXD 6.1

### Building the image
```
git clone -b kirkstone git://git.yoctoproject.org/poky vcpe
cd vcpe
git clone git://git.openembedded.org/meta-openembedded -b kirkstone
git clone https://github.com/robvogelaar/meta-vcpe
source meta-vcpe/setup-environment
bitbake vcpe-image
ls -al tmp/deploy/images/qemux86/vcpe-image-qemux86.lxd.tar.bz2
```

### Launch container from container image
```
./gen/vcpe.sh vcpe-image-qemux86.lxd.tar.bz2
or via scp
./gen/vcpe.sh rev@rev140:/home/rev/yocto/poky-vcpe/build-vcpe/tmp/deploy/images/qemux86/vcpe-image-qemux86-20250127233011.rootfs.lxd.tar.bz2

```

### Shell into container
```
lxc exec vcpe bash
bash-5.1# 
```

# End-to-End environment


### Add meta-vcpe to system path

### Create the bridges
```
bridges.sh
```

### Create the bng-7 container
```
bng.sh 7
```

### Get erouter0 ip from bng-7 in vcpe
```
lxc exec vcpe bash
bash-5.1# udhcpc -i erouter0
```

### Run usp agent
```
lxc exec vcpe bash

bash-5.1# rm -rf /nvram/usp-pa.db
bash-5.1# /usr/bin/UspPa -v3 --resetfile /etc/usp-pa/oktopus-websockets-obuspa.txt --truststore /etc/usp-pa/usp_truststore.pem --interface erouter0 --dbfile /nvram/usp-pa.db &
```
