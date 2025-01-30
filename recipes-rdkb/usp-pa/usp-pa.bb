#
# Yocto recipe to install obuspa open source project
#
SUMMARY = "USP Pa component"
DESCRIPTION = "Agent for USP protocol"
DEPENDS = "openssl sqlite3 curl zlib mosquitto libwebsockets rbus"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a3a4606e52c16f583aefb8b47a9db31a"

# OBUSPA is the reference USP agent codebase
SRC_URI = " \
    git://github.com/BroadbandForum/obuspa;protocol=http;branch=master;rev=8355c200afb6430e3fc55ebabe8a9a709ae5d04f;name=obuspa;destsuffix=obuspa \
    file://0001-add-lcm-datamodel.patch \
    file://usp-pa-init \
    file://conf/usp_truststore.pem \
    file://conf/oktopus-websockets-obuspa.conf \
    file://conf/axiros-websockets-obuspa.conf \
    "

# Make sure our source directory (for the build) matches the directory structure in the tarball
S = "${WORKDIR}/obuspa"

# Specify the rules to use to build and install this package
inherit autotools pkgconfig systemd update-rc.d

LDFLAGS += "-lrbus"
CFLAGS  += "-isystem${STAGING_INCDIR}/rbus"
TARGET_CFLAGS  += "-DINCLUDE_LCM_DATAMODEL"

# Init script settings
INITSCRIPT_NAME = "usp-pa"
INITSCRIPT_PARAMS = "defaults 90 10"

# Copy files to staging area
do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/usp-pa
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${sysconfdir}/init.d
    
    install -m 0777 ${B}/obuspa ${D}${bindir}/UspPa
    install -m 0644 ${WORKDIR}/conf/usp_truststore.pem ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/oktopus-websockets-obuspa.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/axiros-websockets-obuspa.conf ${D}${sysconfdir}/usp-pa
    install -m 0755 ${WORKDIR}/usp-pa-init ${D}${sysconfdir}/init.d/usp-pa
}

# Files in staging area to copy to system image
FILES:${PN} += " \
    ${bindir}/UspPa \
    ${sysconfdir}/usp-pa/usp_truststore.pem \
    ${sysconfdir}/usp-pa/oktopus-websockets-obuspa.conf \
    ${sysconfdir}/usp-pa/axiros-websockets-obuspa.conf \
    ${sysconfdir}/init.d/usp-pa \
    "
