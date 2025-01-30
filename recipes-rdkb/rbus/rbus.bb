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
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/rbus-init ${D}${sysconfdir}/init.d/rbus
}

FILES:${PN} += "${sysconfdir}/init.d/rbus"
