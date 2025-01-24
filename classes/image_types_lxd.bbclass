inherit image_types
TYPES_EXTRA:append = " lxd"

# Function to create the LXD image
IMAGE_CMD:lxd() {
    # Create temporary directory structure
    mkdir -p ${WORKDIR}/lxd-image/rootfs

    # Create metadata.yaml with creation_date as Unix timestamp
    cat > ${WORKDIR}/lxd-image/metadata.yaml << EOF
architecture: "i686"
creation_date: $(date +%s)
properties:
  architecture: "i686"
  description: "${@d.getVar("IMAGE_NAME", True)}"
  os: "linux"
  release: "2.1"
EOF

    # Copy rootfs contents
    cp -a ${IMAGE_ROOTFS}/. ${WORKDIR}/lxd-image/rootfs/

    # Create unified tarball with xz compression
    # Use fixed timestamp to avoid warnings (Dec 21, 2023)
    tar --sort=name --owner=root:0 --group=root:0 \
         \
        --numeric-owner -C ${WORKDIR}/lxd-image \
        -cjf ${IMGDEPLOYDIR}/${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.lxd.tar.bz2 .

    # Create symlink without timestamp
    cd ${IMGDEPLOYDIR}
    ln -sf ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.lxd.tar.bz2 \
        ${IMAGE_LINK_NAME}.lxd.tar.bz2

    # Cleanup
    rm -rf ${WORKDIR}/lxd-image
}

# Define dependencies
do_image_lxd[depends] += "${@bb.utils.contains('IMAGE_FSTYPES', 'tar', '${PN}:do_image_tar', '', d)}"
