# VCPE
A Yocto reference-distro based container image to run in LXD

## Description
* inherit poky distro
* add meta-openembedded
* add meta-vcpe
* do not build kernel
* remove undesired image types
* add lxd image class
* create end-to-end environment
* launch vcpe container in the end-to-end environment
* test rbus and usp agent in vcpe container

### Dependencies
* Yocto Project dependencies
* LXD 6.1

## Build vcpe container image
```
git clone -b kirkstone git://git.yoctoproject.org/poky vcpe
cd vcpe
git clone git://git.openembedded.org/meta-openembedded -b kirkstone
git clone https://github.com/robvogelaar/meta-vcpe
source meta-vcpe/setup-environment
bitbake vcpe-image
ls -al tmp/deploy/images/qemux86/vcpe-image-qemux86.lxd.tar.bz2
```

## Create End-to-End environment

### Add meta-vcpe to system path
```
vi ~/.bashrc
```

### Create wan / lxdbr1 bridges
```
bridges.sh
```

### Create bng-7 container
```
bng.sh 7
```

### Launch vcpe container from vcpe container image
```
./gen/vcpe.sh user@host:/home/rev/yocto/poky-vcpe/build-vcpe/tmp/deploy/images/qemux86/vcpe-image-qemux86-20250127233011.rootfs.lxd.tar.bz2
```

### Shell into container
```
lxc exec vcpe bash
bash-5.1# 
```

### Verify rbus broker (rtrouted) and usp agent (UspPa)
```
bash-5.1# ps ax
    PID TTY      STAT   TIME COMMAND
      1 ?        Ss     0:00 init [5]
     48 ?        Ss     0:00 /sbin/udevd -d
    205 ?        Ss     0:00 udhcpc -R -b -p /var/run/udhcpc.erouter0.pid -i erouter0
    240 ?        Ss     0:00 sshd: /usr/sbin/sshd [listener] 0 of 10-100 startups
    248 ?        S      0:00 /sbin/syslogd -n -O /var/log/messages
    251 ?        S      0:00 /sbin/klogd -n
    254 ?        Ss     0:00 /usr/bin/rtrouted
    563 ?        Ss     0:00 bash
    573 ?        Sl     0:01 UspPa -v 4 -l syslog -p -r /etc/usp-pa/axiros-websockets-obuspa.conf
   1400 ?        R+     0:00 ps ax
```

### Verify usp controller api (note USER:PASSWORD)
```
bash-5.1# curl -X 'POST' \
>   'http://usp52.picode.it/live/AXAPI/Portal/DeviceManagement/RPC/USPJob' \
>   -H 'accept: application/json' \
>   -H 'Content-Type: application/json' \
>   -u 'USER:PASSWORD' \
>   -d '{
>   "cpe": "vcpe-ws",
>   "job_parameters": {
>     "paths": ["Device.DeviceInfo.SoftwareVersion"],
>     "max_depth": 2
>   },
>   "job_path": "JobDefinitions.USPJobs.Get",
>   "persistent": false,
>   "timeout": 10
> }'
{
    "code": "0",
    "data": {
        "post": {
            "result": {
                "Response": {
                    "GetResp": {
                        "req_path_results": [
                            {
                                "err_code": 0,
                                "err_msg": "",
                                "requested_path": "Device.DeviceInfo.SoftwareVersion",
                                "resolved_path_results": [
                                    {
                                        "resolved_path": "Device.DeviceInfo.",
                                        "result_params": {
                                            "SoftwareVersion": "MySoftwareVersion"
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                }
            },
            "status": "success"
        }
    },
    "status": "success",
    "status_code": 200
}
```
