FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Append our interfaces file instead of completely replacing SRC_URI
SRC_URI:remove = "file://interfaces"
SRC_URI += "file://interfaces1"

# Override the install of interfaces file specifically
do_install:append() {
    install -d ${D}${sysconfdir}/network
    install -m 0644 ${WORKDIR}/interfaces1 ${D}${sysconfdir}/network/interfaces
}
