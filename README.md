# VCPE
A Yocto reference-distro based container image to run in LXD

## Description
* inherit poky distro
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
git clone https://github.com/robvogelaar/meta-vcpe
source meta-vcpe/setup-environment
bitbake vcpe-image
ls -al tmp/deploy/images/qemux86/vcpe-image-qemux86.lxd.tar.bz2
```

### Launch container from container image
```
./gen/vcpe.sh vcpe-image-qemux86.lxd.tar.bz2
```

### Shell into container
```
lxc exec vcpe bash
```
