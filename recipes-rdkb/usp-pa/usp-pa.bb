#
# Yocto recipe to install obuspa open source project
#

SUMMARY = "USP Pa component"
DESCRIPTION = "Agent for USP protocol"
DEPENDS = "openssl sqlite3 curl zlib mosquitto libwebsockets rbus"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a3a4606e52c16f583aefb8b47a9db31a"

# OBUSPA is the reference USP agent codebase
SRC_URI += "git://github.com/BroadbandForum/obuspa;protocol=http;branch=master;rev=8355c200afb6430e3fc55ebabe8a9a709ae5d04f;name=obuspa;destsuffix=obuspa"

SRC_URI += "file://0001-add-lcm-datamodel.patch"

# Configuration files for target
SRC_URI += "file://conf/usp_factory_reset.conf"
SRC_URI += "file://conf/usp_dm_comps.conf"
SRC_URI += "file://conf/usp_dm_objs.conf"
SRC_URI += "file://conf/usp_dm_params.conf"
SRC_URI += "file://conf/usp_truststore.pem"
SRC_URI += "file://conf/oktopus-websockets-obuspa.conf"
SRC_URI += "file://conf/axiros-websockets-obuspa.conf"

# Make sure our source directory (for the build) matches the directory structure in the tarball
S = "${WORKDIR}/obuspa"

# Specify the rules to use to build and install this package
inherit autotools pkgconfig systemd

LDFLAGS += "-lrbus"
CFLAGS  += "-isystem${STAGING_INCDIR}/rbus"
TARGET_CFLAGS  += "-DINCLUDE_LCM_DATAMODEL"

# Copy files to staging area
do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/usp-pa
    install -d ${D}${systemd_system_unitdir}

    install -m 0777 ${B}/obuspa ${D}${bindir}/UspPa
    install -m 0644 ${WORKDIR}/conf/usp_factory_reset.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_comps.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_objs.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_dm_params.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/usp_truststore.pem ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/oktopus-websockets-obuspa.conf ${D}${sysconfdir}/usp-pa
    install -m 0644 ${WORKDIR}/conf/axiros-websockets-obuspa.conf ${D}${sysconfdir}/usp-pa
}

# Files in staging area to copy to system image
FILES_${PN} += "${bindir}/UspPa"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_factory_reset.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_dm_comps.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_dm_objs.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_dm_params.conf"
FILES_${PN} += "${sysconfdir}/usp-pa/usp_truststore.pem"
