SUMMARY = "vcpe image"
DESCRIPTION = "Custom image based on core-image-minimal"

require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL:append = " \
    init-ifupdown \
    procps \
    bash \
    packagegroup-core-ssh-openssh \
    curl \
    lsof \
    strace \
    tcpdump \
    \
    rbus \
    usp-pa \
    \
    ccsp-cr \
"

# Add extra image features if needed
EXTRA_IMAGE_FEATURES += " \
    debug-tweaks \
    package-management \
"
