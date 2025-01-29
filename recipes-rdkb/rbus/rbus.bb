SUMMARY = "rbus library component"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ed63516ecab9f06e324238dd2b259549"

SRC_URI = "git://github.com/rdkcentral/rbus.git;branch=rbus-2.0;protocol=https"

SRCREV = "v2.0.13"
SRCREV_FORMAT = "base"
PV = "REL+git${SRCPV}"
S = "${WORKDIR}/git"

inherit cmake pkgconfig
DEPENDS = "cjson msgpack-c linenoise"


#RDK Specific Enablements
EXTRA_OECMAKE += " -DCMAKE_BUILD_TYPE=Release "
EXTRA_OECMAKE += " -DMSG_ROUNDTRIP_TIME=ON -DENABLE_RDKLOGGER=OFF"

CFLAGS:append = " -Wno-format-truncation -Wno-stringop-truncation -Wno-unused-result"
CXXFLAGS:append = " -Wno-format-truncation -Wno-stringop-truncation -Wno-unused-result"
