
git clone -b kirkstone git://git.yoctoproject.org/poky poky-vcpe

cd poky-vcpe

git clone https://github.com/robvogelaar/meta-vcpe

source meta-vcpe/setup-environment
bitbake vcpe-image
