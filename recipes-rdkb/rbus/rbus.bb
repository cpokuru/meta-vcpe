SUMMARY = "rbus library component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ed63516ecab9f06e324238dd2b259549"

SRC_URI = "git://github.com/rdkcentral/rbus.git;branch=rbus-2.0;protocol=https \
           file://rbus-init \
          "

SRCREV = "v2.0.13"
SRCREV_FORMAT = "base"
PV = "REL+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig update-rc.d

DEPENDS = "cjson msgpack-c linenoise"

#RDK Specific Enablements
EXTRA_OECMAKE += " -DCMAKE_BUILD_TYPE=Release "
EXTRA_OECMAKE += " -DMSG_ROUNDTRIP_TIME=ON -DENABLE_RDKLOGGER=OFF"

CFLAGS:append = " -Wno-format-truncation -Wno-stringop-truncation -Wno-unused-result"
CXXFLAGS:append = " -Wno-format-truncation -Wno-stringop-truncation -Wno-unused-result"

INITSCRIPT_NAME = "rbus"
INITSCRIPT_PARAMS = "defaults 30 70"

do_install:append() {

    # Fix include paths within rbus headers to match path in sysroot
    for f in ${D}${includedir}/rbus/*.h ${D}${includedir}/rtmessage/*.h ; do
        sed -e 's|^#include <rbus|#include <rbus/rbus|' \
            -e 's|^#include <rtAdvisory.h>|#include <rtmessage/rtAdvisory.h>|' \
            -e 's|^#include <rtConnection.h>|#include <rtmessage/rtConnection.h>|' \
            -e 's|^#include <rtError.h>|#include <rtmessage/rtError.h>|' \
            -e 's|^#include <rtLog.h>|#include <rtmessage/rtLog.h>|' \
            -e 's|^#include <rtMemory.h>|#include <rtmessage/rtMemory.h>|' \
            -e 's|^#include <rtMessageHeader.h>|#include <rtmessage/rtMessageHeader.h>|' \
            -e 's|^#include <rtRetainable.h>|#include <rtmessage/rtRetainable.h>|' \
            -e 's|^#include <rtVector.h>|#include <rtmessage/rtVector.h>|' \
            -e 's|^#include <rtm_discovery_api.h>|#include <rtmessage/rtm_discovery_api.h>|' \
            -i $f
    done

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/rbus-init ${D}${sysconfdir}/init.d/rbus
}

FILES:${PN} += "${sysconfdir}/init.d/rbus"
