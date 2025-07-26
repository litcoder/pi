SUMMARY = "Custom Weston configuration for Raspberry Pi"
DESCRIPTION = "Weston configuration that uses pixman renderer to prevent crashes"

LICENSE = "CLOSED"

SRC_URI = "file://weston.ini \
           file://weston.service \
           file://start-weston.sh"

S = "${WORKDIR}"

inherit systemd

# Runtime dependencies
RDEPENDS:${PN} = "weston"

# Conflict resolution with weston-init
RCONFLICTS:${PN} = "weston-init"
RREPLACES:${PN} = "weston-init"
RPROVIDES:${PN} = "weston-init"

SYSTEMD_SERVICE:${PN} = "weston.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_install() {
    # Install Weston configuration
    install -d ${D}${sysconfdir}/xdg/weston
    install -m 0644 ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston/weston.ini
    
    # Install systemd service
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/weston.service ${D}${systemd_system_unitdir}/weston.service
    
    # Install helper script
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-weston.sh ${D}${bindir}/start-weston
}

pkg_postinst_ontarget:${PN}() {
    # Enable weston service on first boot
    if [ -x "$(command -v systemctl)" ]; then
        systemctl daemon-reload || true
        systemctl enable weston.service || true
    fi
}

FILES:${PN} = "${sysconfdir}/xdg/weston/weston.ini \
               ${systemd_system_unitdir}/weston.service \
               ${bindir}/start-weston"
