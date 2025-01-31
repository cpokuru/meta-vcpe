SUMMARY = "CCSP Component Registry"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=175792518e4ac015ab6696d16c4f607e"

require ccsp_common.inc

DEPENDS += "libcap libxml2"

PV = "REL+git${SRCPV}"

SRC_URI = "git://github.com/lgirdk/ccsp-cr.git;branch=ofw-2406.6;protocol=https"

SRC_URI += "file://0001-remove-dependencies.patch"

SRCREV ?= "${AUTOREV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

CFLAGS += " \
    -I${STAGING_INCDIR}/utapi \
    -I${STAGING_INCDIR}/utctx \
    -I${STAGING_INCDIR}/libxml2 \
"

do_install:append () {
	install -d ${D}/usr/ccsp
	install -m 644 ${S}/source/cr-ethwan-deviceprofile.xml ${D}/usr/ccsp/
	install -m 644 ${S}/config/cr-deviceprofile_embedded.xml ${D}/usr/ccsp/cr-deviceprofile.xml

	ln -sf ${bindir}/CcspCrSsp ${D}/usr/ccsp/CcspCrSsp
}

FILES:${PN} += " \
    /usr/ccsp/cr-ethwan-deviceprofile.xml \
    /usr/ccsp/cr-deviceprofile.xml \
    /usr/ccsp/CcspCrSsp \
    /usr/ccsp \
"
