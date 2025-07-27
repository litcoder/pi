SUMMARY = "Reaction game for Raspberry Pi"
DESCRIPTION = "A basic Qt6 application demonstrating GPIO control"

LICENSE = "CLOSED"

SRC_URI = "file://mole-1.0.tar.gz"

S = "${WORKDIR}"

DEPENDS = "qtbase qtbase-native spdlog"
RDEPENDS:${PN} = "spdlog"

inherit qt6-cmake

EXTRA_OECMAKE += " \
    -DCMAKE_BUILD_TYPE=Release \
"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${B}/mole ${D}${bindir}/
}

FILES:${PN} = "${bindir}/mole"