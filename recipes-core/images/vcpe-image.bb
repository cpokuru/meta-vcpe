SUMMARY = "vcpe image"
DESCRIPTION = "Custom image based on core-image-minimal"

require recipes-core/images/core-image-minimal.bb

# Add extra packages you want in your image
IMAGE_INSTALL:append = " \
    packagegroup-core-ssh-openssh \
    bash \
    \
    usp-pa \
"

# Add extra image features if needed
EXTRA_IMAGE_FEATURES += " \
    debug-tweaks \
    package-management \
"
