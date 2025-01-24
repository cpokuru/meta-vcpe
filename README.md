
git clone -b kirkstone git://git.yoctoproject.org/poky poky-vcpe

cd poky-vcpe

git clone https://github.com/robvogelaar/meta-vcpe

source meta-vcpe/setup-environment
bitbake vcpe-image

ls -al tmp/deploy/images/qemux86/vcpe-image-qemux86.tar.bz2

../meta-vcpe/gen/poky-vcpe.sh  tmp/deploy/images/qemux86/vcpe-image-qemux86.tar.bz2

lxc exec poky-vcpe bash

bash-5.1#
